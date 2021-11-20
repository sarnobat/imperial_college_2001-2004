#!/usr/bin/php
<?
	include "dbconnect.cgi";
	include "preheader.cgi";

	$starNum = $HTTP_COOKIE_VARS["starID"];
	$player = $HTTP_COOKIE_VARS["TraderID"];

	$choice = $_POST["Extract"];

	switch($choice){
		case "Titanium":
			$passBack = 4;	// Value of titanium in db
			$increase = 6;
			echo "Extracting Titanium ... <br>";
			Update("titanium", 0.3, $starNum, $player, $connection, $increase);
			break;

		case "Fuel":
			$passBack = 2;	// Value of fuel in db
			$increase = 35;
			echo "Extracting Fuel ... <br>";
			Update("fuel", 0.4, $starNum, $player, $connection, $increase);
			break;

		case "Gold":
			$passBack = 3; 	// Value of Gold in db
			$increase = 2;
			echo "Extracting Gold ... <br>";
			Update("gold", 0.1, $starNum, $player, $connection, $increase);
			break;
	}

	function Update($Mineral, $value, $starNum, $player, $conn, $add){
	/**	Increase $Mineral by $add in
			- Quantity in Planets
			- NotForSale in Goods ***

		Decrease Mining Tools by $value in
			- Quantity in Planets
			- NotForSale in Goods ***/

		$sql = "UPDATE planets SET $Mineral=$Mineral+$add WHERE starnum=$starNum;";
		$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));

		$sql = "UPDATE goods SET notforsale=notforsale+$add WHERE playernum=$player AND resourcename='$Mineral';";
		$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));

		$sql = "UPDATE planets SET mining=mining-$value WHERE starnum=$starNum;";
		$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));

		$sql = "UPDATE goods SET notforsale=notforsale-$value WHERE playernum=$player AND resourcename='mining';";
		$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));
	}

	echo "<br><object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0\" width=600 height=371>
			<param name=movie value=flash/mining.swf>
			<param name=quality value=high>
			<param name=WMODE value=transparent>
			<embed src=flash/mining.swf quality=high pluginspage=http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash type=application/x-shockwave-flash width=600 height=371>
			</embed></object>";

	echo "<meta http-equiv=Refresh content=\"5; URL=gamehome.cgi?mychoice=$passBack&inc=$increase \"> ";

	include "footer.cgi";
?>