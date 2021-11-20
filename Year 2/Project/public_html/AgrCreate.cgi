#!/usr/bin/php
<?
	include "dbconnect.cgi";
	include "preheader.cgi";

	$starNum = $HTTP_COOKIE_VARS["starID"];
	$player = $HTTP_COOKIE_VARS["TraderID"];

	$choice = $_POST["Create"];

	switch($choice){
		case "Grow Food":
			$passBack = 0;
			$increase = 30;
			echo "Growing Food ... <br>";
			Update("food", 0.2, $starNum, $player, $connection, $increase);
			$flashimage = "food";
			break;

		case "Collect Water":
			$passBack = 1;
			$increase = 25;
			echo "Collecting Water ... <br>";
			Update("water", 0.25, $starNum, $player, $connection, $increase);
			$flashimage = "water";
			break;
	}

	function Update($Item, $value, $starNum, $player, $conn, $add){
	/**	Increase $Item by $add in
			- Quantity in Planets
			- NotForSale in Goods ***

		Decrease Farming Tools by $value in
			- Quantity in Planets
			- NotForSale in Goods ***/

		$sql = "UPDATE planets SET $Item=$Item+$add WHERE starnum=$starNum;";
		$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));

		$sql = "UPDATE goods SET notforsale=notforsale+$add WHERE playernum=$player AND resourcename='$Item';";
		$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));

		$sql = "UPDATE planets SET farming=farming-$value WHERE starnum=$starNum;";
		$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));

		$sql = "UPDATE goods SET notforsale=notforsale-$value WHERE playernum=$player AND resourcename='farming';";
		$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));
	}

	echo "<br><object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0\" width=600 height=371>
			<param name=movie value=flash/$flashimage.swf>
			<param name=quality value=high>
			<param name=WMODE value=transparent>
			<embed src=flash/$flashimage.swf quality=high pluginspage=http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash type=application/x-shockwave-flash width=600 height=371>
			</embed></object>";	
	echo "<meta http-equiv=Refresh content=\"5; URL=gamehome.cgi?mychoice=$passBack&inc=$increase \"> ";


	include "footer.cgi";
?>