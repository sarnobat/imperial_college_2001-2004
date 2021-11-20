#!/usr/bin/php
<?
	ob_start();
	include "dbconnect.cgi";
	include "preheader.cgi";
		
	// The destination co-ordinates passed should be extracted
	$x = $_GET['x'];
	$y = $_GET['y'];
	$z = $_GET['z'];
	// Number of units of fuel used per unit distance travelled
	$fuelconsumptionrate = 0.3;
	$shipnum = $_COOKIE["ShipID"];
	
	echo "<h3>Flight in progress...</h3>";

	// A distance calculation must be performed in an external file, so we must pass the co-ordinates
	$x_source = $_COOKIE["ship_x"]; $y_source = $_COOKIE["ship_y"]; $z_source = $_COOKIE["ship_z"]; 
	$x_dest = $x; $y_dest = $y; $z_dest = $z;	
	include "determinedistance.cgi";
	// $distance will be the result of the this file's calculation
//	echo $distance;
	$distance = floor ($distance);

	// Determine the ship's current mileage and fuel count
	$sql = "SELECT * FROM shipstate WHERE shipnum = $shipnum;";
	$resultset = pg_query($connection,$sql) or die("");
	$row = pg_fetch_object($resultset,0);
	$mileage = $row->mileage;
	$fuelcontent = $row->fuelcontent;


	// Determine what these two values would be should the journey succeed
	$newmileage = $mileage - $distance;
	$newfuelcontent = $fuelcontent - $distance * $fuelconsumptionrate;
	//echo "New Mileage: $newmileage\n";echo "<BR>";
	//echo "New Fuel Content: $newfuelcontent";

	if ($newmileage < 0){
		//ship's mileage will be exceed and that a new ship must be bought;
		echo "Not enough mileage left in your ship to complete journey";
		echo "Mileage: $mileage<br>";
		echo "Mileage required: $distance<br>";
		echo "<meta http-equiv=Refresh content=\"10; URL=ship.cgi\"> ";
	}
	else if($newfuelcontent < 0){
		// alert user that the ship doesn't have enough fuel to get to the destination
		echo "<h2>Not enough fuel to complete journey</h2>";
		echo "Fuel Content: $fuelcontent<br><br>";	
		echo "Fuel requried: " .(floor($distance * $fuelconsumptionrate)) ."<br><br>";
		echo "<meta http-equiv=Refresh content=\"10; URL=ship.cgi\"> ";
	}
	else{
		//Embed flash object
		echo "<object	classid=clsid:D27CDB6E-AE6D-11cf-96B8-444553540000
						codebase=http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0
						width=600 height=371>
				<param name=movie value=flash/rocket.swf>
				<param name=quality value=high>
				<param name=WMODE value=transparent>
                <embed src=flash/rocket.swf quality=high pluginspage=http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash type=application/x-shockwave-flash
							width=600 height=371>
                      </embed></object>";


		//Decrease mileage and fuel count; Change your ship's co-ordinates (not the cookies, they show
		//your home planet's co-ordinates		
		$sql = "UPDATE shipstate SET x=$x,y=$y,z=$z WHERE shipnum=$shipnum;
				UPDATE shipstate SET fuelcontent = $newfuelcontent,mileage = mileage-$distance WHERE shipnum = $shipnum;";
		$resultset = pg_query($connection,$sql) or die("");
		// Set the cookies which tell you where your ship is
		setcookie("ship_x",$x_dest);
		setcookie("ship_y",$y_dest);
		setcookie("ship_z",$z_dest);
		echo "<meta http-equiv=Refresh content=\"3; URL=trading.cgi\"> ";
	}
		
	include "footer.cgi";
	include "dbdisconnect.cgi";
	ob_end_flush();
?>