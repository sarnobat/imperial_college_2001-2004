#!/usr/bin/php
<?
	echo "<script language = Javascript src = displayvalues.js></script>";

	include "header.cgi";
	include "dbconnect.cgi";

	echo "<h1>Your Ship Details</h1><h3>Details concerning your ship such as mileage, fuel level.<br> You may refuel or reservice it at any location provided you have the necessary funds.</h3>";
	
	$playernum = $HTTP_COOKIE_VARS["TraderID"];
	$sql = "SELECT * FROM shipstate WHERE owner = '$playernum';";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	/* $i below could just be 0, if we decide to have only one ship and thus get rid of line 10-12,14 */
	$rows = pg_num_rows($resultset);
	for($i=0; $i<$rows; $i++)
	{
		$row = pg_fetch_object($resultset, $i);
	}

	$costpermile	= 2;
	$tankcapacity	= 1000;
	$shipnumber		= $row->shipnum;
	$fuelcontent	= $row->fuelcontent;
	$mileage		= $row->mileage;
	$x				= $row->x;
	$y				= $row->y;
	$z				= $row->z;
	$fuelempty		= $tankcapacity - $fuelcontent;

	$sql = "SELECT star FROM universe WHERE (x=$x AND y=$y AND z=$z);";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	
	/* $i below could just be 0, if we decide to have only one ship and thus get rid of line 27-29,31 */
	$rows = pg_num_rows($resultset);
	for($i=0; $i<$rows; $i++)
	{
		$row = pg_fetch_object($resultset, $i);
	}

	$planetlocation = $row->star;
	$homeplanet = $HTTP_COOKIE_VARS["starName"];
	$homeplanetnum = $HTTP_COOKIE_VARS["starID"];

	$sql = "SELECT * FROM ships WHERE shipnum = '$shipnumber';";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	/* $i below could just be 0, if we decide to have only one ship and thus get rid of line 37-39,41 */
	$rows = pg_num_rows($resultset);
	for($i=0; $i<$rows; $i++)
	{
		$row = pg_fetch_object($resultset, $i);
	}

	$shipname	= $row->name;
	$name		= array("food","water","fuel","gold","titanium","industrial","mining","farming");
	$nameDisp	= array("Food","Water","Fuel","Gold","Titanium","Industrial Tools","Mining Tools","Farming Tools");
	$amounts	= array($row->food,$row->water,$row->fuel,$row->gold,$row->titanium,$row->industrial,$row->mining,$row->farming);
		
	$sql = "SELECT * FROM shipstorage WHERE shipnum = '$shipnumber';";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	/* $i below could just be 0, if we decide to have only one ship and thus get rid of line 46-49,51 */
	$rows = pg_num_rows($resultset);
	for($i=0; $i<$rows; $i++)
	{
		$row = pg_fetch_object($resultset, $i);
	}

	$volumeused	= $row->volumeused;
	$freespace	= $row->freespace;
	$capacity	= $volumeused+$freespace;

	echo "<H2>Ship Info:</H2>";
	print "
	<P><table>
		<TR>
			<TD></TD>
			<TD><B>Ship Name</B></TD>
			<TD><B>$shipname</B></TD>
		</TR>
		<TR>
			<TD></TD>
	";

	if($planetlocation == $homeplanet)
		echo"<TD><B>Current Location <BR><font color=green>(Home Planet)</font></B></TD>";
	else
		echo"<TD><B>Current Location <BR><font color=green>(Away From Home PLanet)</font></B></TD>";
	
	print"
			<TD><B>$planetlocation</B></TD>
		</TR>
		<TR>
			<TD></TD>
			<TD><B>Capacity</B></TD>
			<TD><B>$capacity</B></TD>
		</TR>
		<TR>
			<TD></TD>
			<TD><B>Volume Used</B></TD>
			<TD><B>$volumeused</B></TD>
		</TR>
		<TR>
	";

	$mileagelimit = 450;

	if($mileage < $mileagelimit){
		print"
			<TD><img src=exclamation_mark.gif height=25></TD>
			<TD><font color=#00ff11><B>Mileage</B></font></TD>
			<TD><B>$mileage light years</B></TD>
			<TD><img src=exclamation_mark.gif height=25></TD>
		 </TR>
		</TABLE>
		<B><font color=yellow> Warning: Low Mileage!!!</font></B><BR>
		";
	}
	else{
		print"
			<TD></TD>
			<TD><B>Mileage</B></TD>
			<TD><B>$mileage light years</B></TD>
		 </TR>
		</TABLE><BR>
		";
	}

	print"
	<h2>Reservicing</h2>
	<FORM method=post action=reservice.cgi>
		<TABLE class=data>
			<tr class=tableheading>
				<td>
					Enter Mileage Upgrade Amount
				</td>
				<td>
					Calculate Cost <br> (@ £$costpermile per light-year)
				</td>				
				<td>
					Proceed with reservicing
				</td>
			</tr>
			<tr>
				<td>
					<INPUT TYPE=text size=6 name=mileageRequested  onBlur=checkIsNumeric(mileageRequested);> light years
				</td>
				<td>
					<BR>Cost Of Service
					<INPUT TYPE=text size=6 name=cost>
				</td>
				<td>
					<INPUT TYPE=submit VALUE=\"Re-service Ship\" onClick= \"calculateCost(mileageRequested, cost, $costpermile);\">
				</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<INPUT TYPE=button VALUE=\"Calculate Cost\" onClick= \"calculateCost(mileageRequested, cost, $costpermile);\">
				</td>
				<td></td>
			</tr>
		</TABLE>
		<INPUT type=hidden name=shipnumber value=$shipnumber>
	</FORM><BR><BR>
	";



	echo "<H2>Cargo Hold:</H2>";
	echo "<P><table width=" .$capacity .">";
	print"
		<TR>
			<TD>
			";
			for ($i=0;$i<8;$i++){
				$amount = $amounts[$i];
				if($amount>0){
					$j = $i + 1;

					// Determine the volume of the resource
					$sql = "SELECT * FROM goods WHERE resourcename = '" .$name[$i] ."';";
					$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
					$row = pg_fetch_object($resultset,0);
					$volume = $row->volume;
					echo "<img src=images/marble" .$j .".gif height=30 width=" .($amount*$volume) ." alt=" .$nameDisp[$i] .">";
				}
			}
			if($freespace>0){
				$j = $i + 1;
				echo "<img src=images/marble" .$j .".gif height=30 width=" .$freespace ." alt=Freespace>";
			}	
	print "
			</TD>
		</TR>
	</table>
	";
	
	print"
		<TABLE>
			<TR>
			";
			for ($i=0;$i<8;$i++){
				echo "<TD width=60px><font class= $name[$i]>" .$nameDisp[$i] ."</TD>";
			}
			echo "</TR>";
			echo "<TR>";
			for ($i=0;$i<8;$i++){
				echo "<TD width=60px><font class= $name[$i]>" .$amounts[$i] ."</TD>";
			}
	print"
			</TR>
		</TABLE>
	";

	if($planetlocation == $homeplanet && $volumeused>0){
		print"
			<FORM method=post action=unloadship.cgi>
				<TABLE border=3 bordercolor=red>
					<TR>
						<TD><INPUT TYPE=submit VALUE=\"Unload Cargo\"></TD>
					</TR>
				</TABLE>
			</FORM><BR><BR>
		";
	}
	else
	{
		echo "<BR><BR>";
	}

	$fuellimit = 50;

	if($fuelcontent < $fuellimit){
		print"
			<table>
				<tr>
				<td><img src=exclamation_mark.gif height=25></td>
				<td><BR><H2> FUEL TANK:</H2></td>
				<td> <img src=exclamation_mark.gif height=25></td>
		";
	}
	else{
		print"
			<H2>Fuel Tank:</H2>
		";
	}
	echo "<P><table width=" .$tankcapacity .">";
	print"
		<TR>
			<TD>
			";
				echo "<img src=images/marble3.gif height=30 width=" .$fuelcontent ." alt=\"Fuel Content\">";
				echo "<img src=images/fuelempty.gif height=30 width=" .$fuelempty ." alt=\"Fuel Empty\">";
	print "
			</TD>
		</TR>
	</table>
	";

	print"
		<TABLE class=data>
			<TR class=tableheading>
	";
	if($fuelcontent < $fuellimit){
		echo "<TD><B><font color=yellow>Warning:<BR>Low Fuel</font></B></TD>";
	}
	else{
		echo "<TD>Fuel Content</TD>";
	}
	print"
			<TD>Tank Capacity</TD>
			</TR>
			<TR>
	";
	if($fuelcontent < $fuellimit){
		echo "<TD width=100px><B><font color=yellow>" .$fuelcontent ."</font></B></TD>";
	}
	else{
		echo "<TD width=100px>" .$fuelcontent ."</TD>";
	}
	print"
			<TD width=100px>" .$tankcapacity ."</TD>
			</TR>
		</TABLE>
		<br>
		<br>
	";

	print"
		<FORM method=POST action=refuel.cgi onSubmit= \"return checkIsFilled(fuelRequested);\">
			<TABLE class=data>
				<TR class=tableheading>
					<TD><B>Fuel Available On Home Planet</B></TD>
	";
	if($planetlocation != $homeplanet){
				echo "<TD><B>Fuel Available On Current Planet</B></TD>";
	}
	else{
				echo "<TD></TD>";
	}
	
	print"
					<TD></TD><TD><B>Max Refuel: $fuelempty</B></TD><TD></TD>
				</TR>
				<TR>
	";

	$sql		= "SELECT notforsale,price FROM goods WHERE playernum = $playernum AND resourcename = 'fuel';";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row		= pg_fetch_object($resultset,0);
	$fuel		= $row->notforsale;
	$price		= $row->price;
	echo"			<TD><B>$fuel litres @ £$price </B>per litre</TD>";

	if($planetlocation != $homeplanet){
		$sql		= "SELECT starnum FROM universe WHERE star = '$planetlocation';";
		$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row		= pg_fetch_object($resultset,0);
		$starnum	= $row->starnum;

		$sql				= "SELECT owner FROM starstate WHERE starnum = $starnum;";
		$resultset			= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row				= pg_fetch_object($resultset,0);
		$currentstarowner	= $row->owner;

		$sql		= "SELECT forsale,price FROM goods WHERE playernum = $currentstarowner AND resourcename = 'fuel';";
		$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row		= pg_fetch_object($resultset,0);
		$fuel		= $row->forsale;
		$price		= $row->price;

		echo"		<TD><B>$fuel litres @ £$price </B>per litre</TD>";
	}
	else{
		echo"		<TD></TD>";
		$currentstarowner	= $playernum;
		$starnum			= $homeplanetnum;
	}

	print"
					<TD><INPUT type=submit value=\"Take all $fuel litres\" onClick=\"fillTankValue(fuelRequested,$fuelempty, $fuel);
																			validateSell(fuelRequested, $fuelempty, 0);
																			  checkIsNumeric(fuelRequested);\"></TD>
					<TD>Refuel by <INPUT type=text size=4 name=fuelRequested onBlur=\"validateSell(fuelRequested, $fuelempty, 0);
																			 validateSell(fuelRequested, $fuel, 0);
																			  checkIsNumeric(fuelRequested);\"> litres</TD>
					<TD><INPUT type=submit value=\"Confirm\"></TD>
				</TR>
			</TABLE>
			<INPUT type=hidden name=shipnumber value=$shipnumber>
			<INPUT type=hidden name=starnum value=$starnum>
			<INPUT type=hidden name=currentstarowner value=$currentstarowner>
		</FORM>
		<SCRIPT language=JavaScript src=isNumeric.js></SCRIPT>
		<br><br><br><br>
	";

	include "dbdisconnect.cgi";
	include "footer.cgi";
?>