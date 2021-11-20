<?
	include "dbconnect.cgi";
	
	// The destination co-ordinates passed should be extracted
	$x = $_GET['x'];
	$y = $_GET['y'];
	$z = $_GET['z'];
	// Number of units of fuel used per unit distance travelled
	$fuelconsumptionrate = 0.1; 
	$shipnum = $_COOKIE["ShipID"];
	
	// A distance calculation must be performed in an external file, so we must pass the co-ordinates
	$x_source = $_COOKIE["x"]; $y_source = $_COOKIE["y"]; $y_source = $_COOKIE["y"]; 
	$x_dest = $x; $y_dest = $y;	$z_dest = $z;	
	include "determinedistance.cgi";
	// $distance will be the result of the this file's calculation

	// Determine the ship's current mileage and fuel count
	$sql = "SELECT * FROM shipstate WHERE shipnum = $shipnum;";
	$resultset = pg_query($connection,$sql) or die("");
	$row = pg_fetch_object($resultset,0);
	$mileage = $row->mileage;
	$fuelcontent = $row->fuelcontent;

	// Determine what these two values would be should the journey succeed
	$newmilage = mileage - $distance;
	$newfuelcontent = $fuelcontent - $distance * $fuelconsumptionrate
	
	if (newmileage <contcontcontcontcontcontcontconthip's mileage will be exceed and that a new ship must be bought;
	}
	else if($newfuelcontent < 0){
		// alert user that the ship doesn't have enough fuel to get to the destination
	}
	else{
		//Decrease mileage and fuel count; Change your ship's co-ordinates (not the cookies, they show
		//your home planet's co-ordinates		
		$sql = "UPDATE shipstate SET x=$x,y=$y,z=$z WHERE shipnum=$shipnum;
				UPDATE shipstate SET fuelcontent = $newfuelcontent,mileage = $newmileage
					WHERE shipnum = $shipnum;";
		$resultset = pg_query($connection,$sql) or die("");
	}
		
	
	include "dbdisconnect.cgi";
?>