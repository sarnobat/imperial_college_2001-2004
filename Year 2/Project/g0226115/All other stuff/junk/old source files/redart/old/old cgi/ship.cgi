#!/usr/bin/php
<?
	echo "<script language = Javascript src = include/displayvalues.js></script>";

	include "include/header.cgi";
	include "include/dbconnect.cgi";
	
	$playernum = $HTTP_COOKIE_VARS["TraderID"];
	$sql = "SELECT * FROM shipstate WHERE owner = '$playernum';";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	/* $i below could just be 0, if we decide to have only one ship and thus get rid of line 10-12,14 */
	$rows = pg_num_rows($resultset);
	for($i=0; $i<$rows; $i++)
	{
		$row = pg_fetch_object($resultset, $i);
	}

	$tankcapacity	= 500;
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
	<table >
		<TR>
			<TD><B>Ship Name</B></TD>
			<TD><B>$shipname</B></TD>
		</TR>
		<TR>
			<TD><B>Location</B></TD>
			<TD><B>$planetlocation</B></TD>
		</TR>
		<TR>
			<TD><B>Capacity</B></TD>
			<TD><B>$capacity</B></TD>
		</TR>
		<TR>
			<TD><B>Volume Used</B></TD>
			<TD><B>$volumeused</B></TD>
		</TR>
		<TR>
			<TD><B>Mileage</B></TD>
			<TD><B>$mileage</B></TD>
		</TR>
	</table>
	";

	echo "<H2>Cargo Hold:</H2>";
	echo "<BR><table width=" .$capacity .">";
	print"
		<TR>
			<TD>
			";
			for ($i=0;$i<8;$i++){
				$amount = $amounts[$i];
				if($amount>0){
					$j = $i + 1;
					echo "<img src=images/marble" .$j .".gif height=30 width=" .$amount .">";
				}
			}
			if($freespace>0){
				$j = $i + 1;
				echo "<img src=images/marble" .$j .".gif height=30 width=" .$freespace .">";
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
				echo "<TD width=60px>" .$name[$i] ."</TD>";
			}
			echo "</TR>";
			echo "<TR>";
			for ($i=0;$i<8;$i++){
				echo "<TD width=60px>" .$amounts[$i] ."</TD>";
			}
	print"
			</TR>
		</TABLE>
	";

	echo "<H2>Fuel Tank:</H2>";
	echo "<P><table width=" .$tankcapacity .">";
	print"
		<TR>
			<TD>
			";
				echo "<img src=images/marble3.gif height=30 width=" .$fuelcontent .">";
				echo "<img src=images/fuelempty.gif height=30 width=" .$fuelempty .">";
	print "
			</TD>
		</TR>
	</table>
	";

	print"
		<TABLE>
			<TR>
				<TD>Fuel Content</TD>
				<TD>Fuel Empty</TD>
			</TR>
			<TR>
		";
				echo "<TD width=100px>" .$fuelcontent ."</TD>";
				echo "<TD width=100px>" .$fuelempty ."</TD>";
	print"
			</TR>
		</TABLE>
	";

	include "include/dbdisconnect.cgi";
	include "include/footer.cgi";
?>