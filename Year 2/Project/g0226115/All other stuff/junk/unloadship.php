<?
	include "dbconnect.cgi";
	
	$shipnum = $_COOKIE["ShipID"];
	$shipcapacity = 1000;
	
	$resources = ("food","water","fuel","gold","titanium","industrial","mining","farming");	
	//$qtysinship = (0,0,0,0,0,0,0,0);
	
	// For each resource...
	$sql = "UPDATE planets SET";
	for($i=0;$i<$resources.length();$i++){
		//determine its quantity on the ship; 
		$sql2 = "SELECT * FROM ships WHERE shipnum = $shipnum;";
		$resultset2 = pg_query($connection,$sql2) or die("");
		$row = pg_fetch_object($resultset,0);
		$resourcename = resources[$i];
		$qty = $row->$resourcename;
		//$qtysinship[$i] = $qty;
		
		//then set it to zero
		$sql2 = "UPDATE ships SET $resourcename = 0 WHERE shipnum = $shipnum;";
		$resultset2 = pg_query($connection,$sql2) or die("");

		//increase the planet's quantity of these resources
		$sql = $sql ." $resourcename = $qty ";
		if($i == resources.length()){
			$sql = $sql .";";
		}
		else{
			$sql = $sql ."'";
		}
		$resultset = pg_query($connection,$sql) or die("");
	}
	
	 using the array
	
	//Increase ship's storage attributes again
	$sql = "UPDATE shipstorage SET volumeused = 0,freespace = $shipcapacity WHERE shipnum = $shipnum;";
	$resultset = pg_query($connection,$sql) or die("");
	
	include "dbdisconnect.cgi";
?>