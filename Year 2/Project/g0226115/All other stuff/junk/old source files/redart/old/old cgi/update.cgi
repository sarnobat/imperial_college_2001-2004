#!/usr/bin/php
<?
	include "include/header.cgi";
	include "include/dbconnect.cgi";
	
	$player = $HTTP_COOKIE_VARS["TraderID"];
	$name = array("food","water","fuel","gold","titanium","industrial","mining","farming");
	$sell = array(0,0,0,0,0,0,0,0);
	$cost = array(0,0,0,0,0,0,0,0);
	$forsale= array(0,0,0,0,0,0,0,0);

	/****************************/

	$choice = $_POST["Update"];

	/* Read Sale update values into array */
	for($i=0; $i<8; $i++){
		$sell[$i] = $_POST["sale".$i];
		$cost[$i] = $_POST["cost".$i];
	}

	if($choice == "PutOnMarket"){
		for($i=0; $i<8; $i++){
			$sql = "SELECT forsale FROM goods WHERE playernum=$player AND resourcename='$name[$i]';";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
			// fetch 0th row of $resultset
			$row = pg_fetch_object($resultset,0);
			
			$forsale[$i] = $row->forsale;
			$new = $forsale[$i]+$sell[$i];
			$sql = "UPDATE goods SET forsale=$new WHERE playernum=$player AND resourcename='$name[$i]';";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		}
	}

	else if($choice == "TakeOffMarket"){
		for($i=0; $i<8; $i++){
			$sql = "SELECT forsale FROM goods WHERE playernum=$player AND resourcename='$name[$i]';";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
			$row = pg_fetch_object($resultset,0);

			$forsale[$i] = $row->forsale;
			echo "$forsale[$i]";
			$new = $forsale[$i]-$sell[$i];
			echo $new;
			$sql = "UPDATE goods SET forsale=$new WHERE playernum=$player AND resourcename='$name[$i]';";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		}
	}

	else if($choice == "UpdateCost"){
		for($i=0; $i<8; $i++){
			$sql = "UPDATE goods SET price='$cost[$i]' WHERE playernum=$player AND resourcename='$name[$i]';";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		}
	}

	echo "<meta http-equiv=Refresh content=\"0.00000000001; URL=home_planet.cgi \"> ";

	include "include/dbdisconnect.cgi";
	include "include/footer.cgi";
?>