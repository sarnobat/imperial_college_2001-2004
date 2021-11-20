#!/usr/bin/php
<?
	include "dbconnect.cgi";

	$playernumber		= $HTTP_COOKIE_VARS["TraderID"];
	$shipnumber			= $_POST["shipnumber"];
	$mileageRequested	= $_POST["mileageRequested"];
	$cost				= $_POST["cost"];
	
	// Determine if the trader has this amount
	$sql = "SELECT balance FROM players WHERE playernum = $playernumber;";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset,0);
	$buyerbalance = $row->balance;
	
	// If not, inform the user of the error
	if ($buyerbalance < $cost)
	{
			echo "You are " .($cost-$buyerbalance) ." units short of money to complete this re-service.  ";
			echo "<i>Returning to ship page...</i>";

			echo "<meta http-equiv=Refresh content=\"1; URL= ship.cgi \">";
	}
	else
	{
		$sql			= "UPDATE players SET balance = balance - $cost WHERE playernum = $playernumber;";
		$resultset		= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

		$sql			= "UPDATE shipstate SET mileage = mileage + $mileageRequested WHERE shipnum=$shipnumber;";
		$resultset		= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

		echo "<meta http-equiv=Refresh content=\"1; URL=ship.cgi \"> ";
	}

	include "dbdisconnect.cgi";
?>