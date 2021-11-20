#!/usr/bin/php
<?
	include "header.cgi";
	include "dbconnect.cgi";
	include "shiplocation.cgi";

	echo "<h1>Trade Goods</h1><h3>Here you can buy goods from the planet your ship is currently on.</h3>";

	include "prices.cgi";

	/*************************************************************************
	 *	Gets the user's requested display configuration						 *
	 *************************************************************************/?>
	<!-- The form for selecting your display options -->
	<form action=trading.cgi method=post>
		Show goods for sale, sorted by
		<select name=sortingfield>
			<option value=resourcename>Resource
			<option value=playername>Retailer name
			<option value=distance>Distance of retailer from my ship
			<option value=price>Price of goods
			<option value=volume>Volume
			<option value=playing>Playing
		</select>
		in
		<select name=sortingtype>
			<option value=ASC>Ascending
			<option value=DESC>Descending
		</select>
		order.
		<input type=submit value=Re-sort>
	</form>
	<script language=JavaScript src=isNumeric.js></script>
<?
	$sort = $_POST["sortingfield"];
	$type = $_POST["sortingtype"];
	/* If it's the user's first time, default the configuration to Resource Name, Ascending */
	if ($sort == null)
	{
		$sort = "resourcename";
	}
	if ($type == null)
	{
		$type = "ASC";
	}




	/************************************************************************
	 *	Create a new SQL table which holds everything the user ultimately	*
	 *	wants to see														*
	 ************************************************************************/
	$sql = "DROP TABLE TEMPplayerdata; DROP TABLE TEMPtrading;
	
			CREATE TABLE TEMPtrading 
			AS (
				SELECT playernum,resourcename,playername,price,volume,forsale,playing
				FROM	(
							SELECT goods.playernum,playername,resourcename,price,volume,forsale,playing 
							FROM goods INNER JOIN players 
							ON players.playernum = goods.playernum) 
							AS table1 
						);
			ALTER TABLE TEMPtrading ADD COLUMN distance DECIMAL(3);";
	$resultsetmain =	pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	
	
	
	/*************************************************************************
	 *	This block of SQL simply creates a table called TEMPplayerdata from   *
	 *	which we later on determine any other player's distance, and insert  *
	 *	it in our TEMPtrading table											 *
	 *************************************************************************/
	$sql =
		"CREATE TABLE TEMPplayernumstarnum AS(SELECT starnum,owner FROM starstate);
		CREATE TABLE TEMPplayernumplayername AS (SELECT playernum,playername FROM players);
		CREATE TABLE TEMPplayernamestarnum AS (SELECT playernum,playername,starnum FROM TEMPplayernumplayername INNER JOIN 
		TEMPplayernumstarnum ON TEMPplayernumplayername.playernum = TEMPplayernumstarnum.owner);
		CREATE TABLE TEMPplayerdata AS (SELECT playernum,playername,universe.starnum,star,x,y,z FROM TEMPplayernamestarnum INNER JOIN universe ON universe.starnum = TEMPplayernamestarnum.starnum);
		DROP TABLE TEMPplayernumstarnum;
		DROP TABLE TEMPplayernumplayername;
		DROP TABLE TEMPplayernamestarnum;";

	$resultset = pg_query($connection,$sql);

	
	
	/*************************************************************************
	 *	Determine how many players there are								 *
	 *************************************************************************/
	$sql = "SELECT playername FROM players";
	$resultsetplayers =	pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$numplayers = pg_num_rows($resultsetplayers);

	/*************************************************************************
	 *	For each player, determine its distance from you, and put it in		 *
	 *	the table TEMPtrading												 *
 	 *************************************************************************/
	/* Retreive your own coordinates */
	$myx = $_COOKIE["ship_x"];
	$myy = $_COOKIE["ship_y"];
	$myz = $_COOKIE["ship_z"];
	for($i=0;$i<$numplayers;$i++){
		// For each player...
			// Determine this player's co-ordinates
			$row = pg_fetch_object($resultsetplayers,$i);
			$sql = "SELECT * FROM TEMPplayerdata WHERE playername = '$row->playername';";
			$resultset = pg_query($connection,$sql);
			$rows = pg_num_rows($resultset);
			if($rows > 0)
			$row2 = pg_fetch_object($resultset,0);

			// Evaluate this player's distance from your ship
			$x = $row2->x;
			$y = $row2->y;
			$z = $row2->z;
			$distance = sqrt (($x-$myx)*($x-$myx)+($y-$myy)*($y-$myy)+($z-$myz)*($z-$myz));

			// Add it to our results table
			$sql = "UPDATE TEMPtrading SET distance = $distance WHERE playername = '$row->playername'; ";
			$resultset = pg_query($connection,$sql) or die("Error in query: $sql." .pg_last_error($connection));
	}



	/*************************************************************************
	 *	Display the data contained in TEMPtrading							 *
	 *																		 *
 	 *************************************************************************/
	$myname = $_COOKIE["TraderName"];
	$sql = "SELECT * FROM temptrading WHERE playername != '$myname' ORDER BY $sort $type;";
	$resultset = pg_query($connection,$sql) or die("Error in query: $sql." .pg_last_error($connection));
		
	echo "<table class=data>";
		echo "<tr class=tableheading>";
			echo "<td>Resource</td>";
			echo "<td>Player</td>";
			echo "<td>Planet Distance</td>";
			echo "<td>Price</td>";
			echo "<td>Volume</td>";
			echo "<td>For Sale</td>";
			echo "<td>Playing</td>";
		echo"<td>Purchase</td></tr>";
	for($i=0;$i<pg_num_rows($resultset);$i++)
	{
		$row = pg_fetch_object($resultset, $i);
	

		/* The next 2 if statements detect whether to put an empty row in */
		if($i>1 && $sort == "playername" && $lastplayer != $row->playername )
		{
			echo "<tr><td>&nbsp;</td></tr>";
		}
		if($i>1 && $sort == "resourcename" && $lastresource != $row->resourcename)
		{
			echo "<tr><td>&nbsp;</td></tr>";
		}

		$resource = $row->resourcename;

		print"
			<tr>
			 <td><font class= $resource> $resource			</font></td>
			 <td><font class= $resource> $row->playername	</font></td>
			 <td><font class= $resource> $row->distance		</font></td>
			 <td><font class= $resource> £ $row->price		</font></td>
			 <td><font class= $resource> $row->volume		</font></td>
			 <td><font class= $resource> $row->forsale		</font></td>
		";

			// $starlordname has been initialized in shiplocation
			if ($row->playing == "t" ){
				echo "<td><img src=xtick1.gif width=15 height=15></td>";

				
			}
			else
			{
				echo "<td><img src=xross1.gif></td>";
			}
			if ($starlordname == $row->playername && $row->forsale > 0){
					echo "	<td>
								<form action=purchase.cgi method=post onSubmit= \" return checkIsFilled(qty); \">
									Buy <input type = text name=qty size=2 
											onblur = \" validateSell(qty, $row->forsale, 0);
														checkIsNumeric(qty); \">
										<input type=hidden name=playername value=$row->playername>
										<input type=hidden name=playernum value=$row->playernum>
										<input type=hidden name=volume value=$row->volume>
										<input type=hidden name=resourcename value=$row->resourcename>units
										<input type=hidden name=unitprice value=$row->price>
										<input type=submit value=Confirm>
								</form>
							</td>";
				}
		echo "</tr>";

		$lastplayer = $row->playername;
		$lastresource = $row->resourcename;
	}
	echo "</table>";


/*
	$sql =	"DROP TABLE TEMPplayerdata;".
			"DROP TABLE TEMPtrading;";
	$resultset = pg_query($connection,$sql);
*/
	include "dbdisconnect.cgi";
	include "footer.cgi";


?>
<meta http-equiv=Refresh content=\"20; URL= ship.cgi \">