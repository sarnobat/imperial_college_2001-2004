<?
	/* This file is inserted in registerplanet.cgi */
	
	$shipname = $_POST["shipname"];

	$shipCapacity = 1000;
	setcookie("ShipCapacity",$shipCapacity);

	$fuelcontent = 200;
	setcookie("DefaultFuelContent",$fuelcontent);

	$mileage = 500;
	setcookie("DefaultMileage",$mileage);

	
	/* Updating UNIVERSE*/

	$sql = "SELECT x, y, z FROM universe WHERE star = '$planetchosen' ";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_array($resultset,NULL,PGSQL_ASSOC);
	$x = $row['x'];
	$y = $row['y'];
	$z = $row['z'];
	// Set cookies to show the co-ordinates of the player's home planet and their ship)
	setcookie("home_x",$x);
	setcookie("home_y",$y);
	setcookie("home_z",$z);
	// Set the cookies for the ship's co-ordinates
	setcookie("ship_x",$x);
	setcookie("ship_y",$y);
	setcookie("ship_z",$z);

	/* Updating SHIPSTATE*/
	$sql = "INSERT INTO shipstate (shipnum, owner, x,y,z, fuelcontent, mileage)
			VALUES ('$playernum', '$playernum', '$x', '$y', '$z', '$fuelcontent', '$mileage')";
			
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	/* Updating SHIPS*/	
	$sql = "INSERT INTO ships (shipnum, name, food, water, fuel, gold, titanium, industrial, mining, farming)
	VALUES ('$playernum', '$shipname', 0, 0, 0, 0, 0, 0, 0, 0)";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	/* Updating SHIPSTORAGE*/	
	$sql = "INSERT INTO shipstorage (shipnum, volumeused, freespace)
	VALUES ('$playernum', 0, $shipCapacity)";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
?>