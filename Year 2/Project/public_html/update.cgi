#!/usr/bin/php
<?
	include "dbconnect.cgi";
	
	$player = $HTTP_COOKIE_VARS["TraderID"];
	$starnumber = $HTTP_COOKIE_VARS["starID"];
	$name = array("food","water","fuel","gold","titanium","industrial","mining","farming");

	$sell = array();		// PutOnMarket
	$takeOff = array();		// TakeOffMarket
	$cost = array();		// Update Cost
	$curforsale = array();
	$newNotForSale = array();

	/**************************************************************************************
	* The values that are read into the arrays have all been validated in home_planet.cgi *
	* Therefore no need for validation in this page ***************************************
	***************************************************************************************/

	$choice = $_POST["Update"];

	/* Update ALL arrays using values passed */
	for($i=0; $i<8; $i++){
		$sell[$i] = $_POST["sale".$i];
		$takeOff[$i] = $_POST["takeoff".$i];
		$cost[$i] = $_POST["cost".$i];
	}
	
	/*** Read in QUANTITY and FORSALE array using PLANETS table ****************
	**** This array will be used for both "PutOnMarket" and "TakeOffMarket" ****/

	for($i=0; $i<8; $i++){
		/******* QUANTITY *****/
		$sql = "SELECT $name[$i] FROM planets WHERE starnum=$starnumber";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$Qty[$i] = $row->$name[$i];

		/******* FORSALE ******/
		$sql = "SELECT forsale FROM goods WHERE playernum=$player AND resourcename='$name[$i]';";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		// fetch 0th row of $resultset
		$row = pg_fetch_object($resultset,0);
		$curforsale[$i] = $row->forsale;
	}

	/* Calculate NEW-FOR-SALE array according to which button was clicked */
	if($choice == "PutOnMarket"){
		for($i=0; $i<8; $i++){
			$newForSale[$i] = $curforsale[$i] + $sell[$i];
		}
	}
	else if($choice == "TakeOffMarket"){
		for($i=0; $i<8; $i++){
			$newForSale[$i] = $curforsale[$i] - $takeOff[$i];
		}
	}

	else if($choice == "UpdateCost"){
		for($i=0; $i<8; $i++){
			if($cost[$i] != ""){	/* To check that Cost field is not accidentally set to 0*/
				$sql = "UPDATE goods SET price='$cost[$i]' WHERE playernum=$player AND resourcename='$name[$i]';";
				$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
			}
		}
	}

	/****** Update FOR-SALE and NOT-FORSALE ******
	****** NO CHANGE TO QUANTITY ****************/
	if($choice == "PutOnMarket" || $choice == "TakeOffMarket"){
		for($i=0; $i<8; $i++){
			$sql = "UPDATE goods SET forsale=$newForSale[$i] WHERE playernum=$player AND resourcename='$name[$i]';";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));		

			$newNotForSale[$i] = $Qty[$i] - $newForSale[$i];
			$sql = "UPDATE goods SET notforsale=$newNotForSale[$i] WHERE playernum=$player AND resourcename='$name[$i]';";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		}
	}

	echo "<meta http-equiv=Refresh content=\"0; URL=home_planet.cgi \"> ";
	include "dbdisconnect.cgi";
?>