#!/usr/bin/php
<?
	include "dbconnect.cgi";
	include "preheader.cgi";
	
	$shipnum = $_COOKIE["ShipID"];
	$starnum = $_COOKIE["starID"];
	$playernum = $_COOKIE["TraderID"];
	$shipcapacity = $_COOKIE["ShipCapacity"];
	
	$resources = array("food","water","fuel","gold","titanium","industrial","mining","farming");

	// ---------------------MAKE SURE YOU ARE ON YOUR HOME PLANET-----------------------

	// For each resource...
	$sql = "UPDATE planets SET";
	for($i=0;$i<8;$i++){
		//determine its quantity on the ship; 
		$sql2 = "SELECT * FROM ships WHERE shipnum = $shipnum;";
		$resultset2 = pg_query($connection,$sql2) or die("");
		$row = pg_fetch_object($resultset2,0);
		$resourcename = $resources[$i];
		$qty = $row->$resourcename;
		
		//then set it to zero
		$sql2 = "UPDATE ships SET $resourcename = 0 WHERE shipnum = $shipnum;";
		$resultset2 = pg_query($connection,$sql2) or die("");

		//increase the planet's quantity of these resources
		$sql = $sql ." $resourcename = $resourcename+$qty ";
		if($i == 7){
			$sql = $sql ."WHERE starnum=$starnum;";
		}
		else{
			$sql = $sql .",";
		}

		$sql3 = "UPDATE goods SET notforsale =  notforsale + $qty WHERE playernum = $playernum AND resourcename='$resources[$i]';";
		$resultset3 = pg_query($connection,$sql3) or die("");
	}
	
	$resultset = pg_query($connection,$sql) or die("");
	
	//Increase ship's storage attributes again
	$sql = "UPDATE shipstorage SET volumeused = 0,freespace = $shipcapacity WHERE shipnum = $shipnum;";
	$resultset = pg_query($connection,$sql) or die("");

	echo "<meta http-equiv=Refresh content=\"0; URL=home_planet.cgi \">";

	include "dbdisconnect.cgi";
?>