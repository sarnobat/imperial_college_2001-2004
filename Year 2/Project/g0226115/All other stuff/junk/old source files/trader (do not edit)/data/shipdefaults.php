<?
	/* This file is inserted in registerplanet.php */
	
	$shipname = $_POST["shipname"];
	$shipCapacity = 100;
	$fuelcontent = 100;
	
	/* Updating UNIVERSE*/

	$sql = "SELECT x, y, z FROM universe WHERE star = '$planetchosen' ";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_array($resultset,NULL,PGSQL_ASSOC);
	$x = $row['x'];
	$y = $row['y'];
	$z = $row['z'];
	// Set cookies to show player's co-ordinates (added by ss401) - PERHAPS THIS IS BETTER OFF IN ANOTHER FILE?
	setcookie("x",$x);
	setcookie("y",$y);
	setcookie("z",$z);

	/* Updating SHIPSTATE*/
	$sql = "INSERT INTO shipstate (shipnum, owner, x,y,z, fuelcontent, mileage)
			VALUES ('$playernum', '$playernum', '$x', '$y', '$z', '$shipCapacity', '$fuelcontent')";
			
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	/* Updating SHIPS*/	
	$sql = "INSERT INTO ships (shipnum, name, food, water, fuel, gold, titanium, industrial, mining, farming)
	VALUES ('$playernum', '$shipname', 0, 0, 0, 0, 0, 0, 0, 0)";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	/* Updating SHIPSTORAGE*/	
	$sql = "INSERT INTO shipstorage (shipnum, volumeused, freespace)
	VALUES ('$playernum', 0, 1000)";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
?>