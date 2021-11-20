#!/usr/bin/php
<?
	include "dbconnect.cgi";
	include "preheader.cgi";

	$resourcename = $_POST["resourcename"];
	$playername = $_POST["playername"];
	$sellernum= $_POST["playernum"];
	$qty = $_POST["qty"];
	$volumesingle = $_POST["volume"];
	$price = $_POST["unitprice"];
	
	$thisplayernum = $_COOKIE["TraderID"];
	$thisplayername = $_COOKIE["TraderName"];
	$thisshipnum = $_COOKIE["ShipID"];

	/*if($qty == NULL)
		$qty = 0;*/

	// Determine how much money should change hands
	$subtotal = $price * $qty;

	// Determine if the buyer has this amount
	$sql = "SELECT balance FROM players WHERE playernum = $thisplayernum;";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset,0);
	$buyerbalance = $row->balance;
	
	// If not, inform the user of the error
	if ($buyerbalance < $subtotal)
	{
			echo "<h3>You are £" .($subtotal-$buyerbalance) ." short of money to complete this transaction.</h2>";
			echo "<i>Returning to trading page...</i>";

			echo "<meta http-equiv=Refresh content=\"5; URL= trading.cgi \">";
	}
	// Otherwise...
	else
	{
		// Check how much space is required for the goods you are trying to purchase		
		$totalvolume = $volumesingle * $qty;

		// Check there is enough space on your ship for this
		$sql = "SELECT freespace FROM shipstorage WHERE shipnum = $thisshipnum;";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$freespace = $row->freespace;

		
		if($freespace < $totalvolume)
		{
			echo "<h2>There isn't enough free space on your ship.</h3>
					You are trying to buy $totalvolume cubic metres of goods, but only have $freespace
					cubic metres of free space in your ship";
			echo "<br><br><i>Returning to trading page...</i>";

			echo "<meta http-equiv=Refresh content=\"5; URL= trading.cgi \">";
		}
		else
		{

			// Increase the SPACESHIP'S resource quantity from the buyer and decrease its balance
			$sql = "UPDATE players SET balance = balance - $subtotal WHERE playernum = $thisplayernum;";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

			//Load the ship with the resources
			$sql = "UPDATE ships SET $resourcename = $resourcename + $qty WHERE shipnum = $thisshipnum;";
			
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

			// Determine the star number of the seller
			$sql = "SELECT starnum FROM starstate WHERE owner = $sellernum;";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
			$row = pg_fetch_object($resultset,0);
			$sellerstarnum = $row->starnum;

			// Determine how much of the resource the seller currently has
			$sql = "SELECT $resourcename FROM planets WHERE starnum = $sellerstarnum;";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
			$row = pg_fetch_object($resultset,0);
			$currentresourcecount = $row->$resource;

			// Determine how much money the seller currently has
			$sql = "SELECT balance FROM players WHERE playernum = $sellernum;";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
			$row = pg_fetch_object($resultset,0);
			$sellerbalance = $row->balance;
		
			// Subtract the resource quantity from the owner's PLANET and increase its balance 
			$sql = "UPDATE planets SET $resourcename = $resourcename - $qty WHERE starnum = $sellerstarnum;
					UPDATE goods SET forsale = forsale - $qty WHERE playernum = $sellernum AND resourcename = '$resourcename';
					UPDATE players SET balance = balance + $subtotal WHERE playernum = $sellernum;";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

			// Update the ship's storage space details
			$sql = "UPDATE shipstorage SET volumeused = volumeused + $totalvolume, freespace = freespace - $totalvolume WHERE shipnum = $thisshipnum;";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
			
			// Generate a breakdown of this to print to the user on screen
			Report($resourcename, $qty);

			echo "<meta http-equiv=Refresh content=\"2; URL= ship.cgi \">";
		}
	}

	function Report($resName, $qty){

		if($qty>1){
			$units = "units";
		}
		else{
			$units = "unit";
		}
		switch($resName){
			case "industrial": $resName = "industrial tool(s)"; break;
			case "mining": $resName = "mining tool(s)"; break;
			case "farming": $resName = "farming tool(s)"; break;
		}

		echo "Success.  <br>$qty $units of $resName transferred.";
	}
	include "dbdisconnect.cgi";
?>