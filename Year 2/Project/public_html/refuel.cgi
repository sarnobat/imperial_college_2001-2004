#!/usr/bin/php
<?
	include "dbconnect.cgi";

	$starnumber		= $HTTP_COOKIE_VARS["starID"];
	$playernumber	= $HTTP_COOKIE_VARS["TraderID"];
	$shipnumber		= $_POST["shipnumber"];
	$fuelRequested	= $_POST["fuelRequested"];
	
	$starnum			= $_POST["starnum"];
	$currentstarowner	= $_POST["currentstarowner"];

	$sql		= "SELECT price FROM goods WHERE playernum = $currentstarowner AND resourcename = 'fuel';";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row		= pg_fetch_object($resultset,0);
	$price		= $row->price;

	$cost		= $fuelRequested * $price;
	
	// Determine if the trader has this amount
	$sql = "SELECT balance FROM players WHERE playernum = $playernumber;";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset,0);
	$buyerbalance = $row->balance;
	
	// If not enuf money and away from home planet, inform the user
	if ($buyerbalance < $cost && $starnumber != $starnum)
	{
			echo "You are " .($cost-$buyerbalance) ." units short of money to complete this fueling.  ";
			echo "<i>Returning to ship page...</i>";
	}
	else{	
		$sql			= "UPDATE shipstate SET fuelcontent = fuelcontent + $fuelRequested WHERE shipnum = $shipnumber;";
		$resultset		= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

		//away from home planet but got enuf money
		if($starnumber != $starnum){
			$sql		= "UPDATE players SET balance = balance + $cost WHERE playernum=$currentstarowner;";
			$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
			$sql		= "UPDATE players SET balance = balance - $cost WHERE playernum=$playernumber;";
			$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

			$sql		= "UPDATE goods SET forsale = forsale - $fuelRequested WHERE playernum=$currentstarowner AND resourcename='fuel';";
			$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		}
		//on home planet (dont care about money)
		else{
			$sql		= "UPDATE goods SET notforsale = notforsale - $fuelRequested WHERE playernum=$playernumber AND resourcename='fuel';";
			$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		}

		$sql			= "UPDATE planets SET fuel = fuel - $fuelRequested WHERE starnum=$starnum;";
		$resultset		= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	}

	echo "<meta http-equiv=Refresh content=\"1; URL=ship.cgi \"> ";

	include "dbdisconnect.cgi";
?>