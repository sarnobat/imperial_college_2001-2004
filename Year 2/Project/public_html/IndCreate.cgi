#!/usr/bin/php
<?
	include "dbconnect.cgi";
	include "preheader.cgi";

	$starNum = $HTTP_COOKIE_VARS["starID"];
	$player = $HTTP_COOKIE_VARS["TraderID"];

	$name = array("food","water","fuel","gold","titanium","industrial","mining","farming");

	$choice = $_POST["Create"];

	switch($choice){
		case "Create Industrial Tool":
			$passBack = 5;
			$increase = 2;
			echo "Creating Industrial Tool <br>";
			createIndTool($name, $starNum, $player, $connection);
			break;

		case "Create Agricultural Tool":
			$passBack = 7;
			$increase = 4;
			echo "Creating Agricultural Tool <br>";
			createAgrTool($name, $starNum, $player, $connection);
			break;
		case "Create Mining Tool":
			$passBack = 6;
			$increase = 3;
			echo "Creating Mining Tool <br>";
			createMinTool($name, $starNum, $player, $connection);
			break;
	}
	
	function createIndTool($name, $starNum, $player, $conn){
	/* Resources needed to create Ind tools */
	$createIndTool = array(4, 1, 4, 1, 1, 0.5, 0, 0);

	/*	MUST INCREMENT #INDUSTRIAL TOOLS by 2
		$createIndTool[5] = 'Industrial Tools' */
	$createIndTool[5] = $createIndTool[5] - 2;

		/** - update Quantity in Planets
			- update NotForSale in Goods ***/
		for($i=0; $i<8; $i++){
			/******* GET QUANTITY *****/
			$sql = "SELECT $name[$i] FROM planets WHERE starnum=$starNum;";
			$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));
			$row = pg_fetch_object($resultset,0);
			$Qty = $row->$name[$i];
			
			$newQty = $Qty - $createIndTool[$i];
			// -- update Quantity in Planets --
			$sql = "UPDATE planets SET $name[$i]=$newQty WHERE starnum=$starNum;";
			$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));

			/******* GET NOT-FORSALE ******/
			$sql = "SELECT notforsale FROM goods WHERE playernum=$player AND resourcename='$name[$i]';";
			$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));
			// fetch 0th row of $resultset
			$row = pg_fetch_object($resultset,0);
			$curNotForSale = $row->notforsale;

			$newNotForSale = $curNotForSale - $createIndTool[$i];;
			// -- update NOT-FORSALE in Planets --
			$sql = "UPDATE goods SET notforsale=$newNotForSale WHERE playernum=$player AND resourcename='$name[$i]';";
			$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));
		}
	}

	function createAgrTool($name, $starNum, $player, $conn){
	/* Resources needed to create Agricultural tools */
	$createAgrTool = array(6, 3, 2, 0, 2, 0.3, 0, 0);

	/*	MUST INCREMENT #AGRICULTURAL TOOLS by 4
		$createIndTool[7] = 'Farming Tools' */
	$createAgrTool[7] = $createAgrTool[7] - 4;

		/** - update Quantity in Planets
			- update NotForSale in Goods ***/
		for($i=0; $i<8; $i++){
			/******* GET QUANTITY *****/
			$sql = "SELECT $name[$i] FROM planets WHERE starnum=$starNum;";
			$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));
			$row = pg_fetch_object($resultset,0);
			$Qty = $row->$name[$i];
			
			$newQty = $Qty - $createAgrTool[$i];
			// -- update Quantity in Planets --
			$sql = "UPDATE planets SET $name[$i]=$newQty WHERE starnum=$starNum;";
			$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));

			/******* GET NOT-FORSALE ******/
			$sql = "SELECT notforsale FROM goods WHERE playernum=$player AND resourcename='$name[$i]';";
			$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));
			// fetch 0th row of $resultset
			$row = pg_fetch_object($resultset,0);
			$curNotForSale = $row->notforsale;
			
			$newNotForSale = $curNotForSale - $createAgrTool[$i];
			// -- update NOT-FORSALE in Planets --
			$sql = "UPDATE goods SET notforsale=$newNotForSale WHERE playernum=$player AND resourcename='$name[$i]';";
			$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));
		}
	}

	function createMinTool($name, $starNum, $player, $conn){
	/* Resources needed to create Mining tools */
	$createMinTool = array(5, 2, 3, 1, 1, 0.1, 0, 0);

	/*	MUST INCREMENT #MINING TOOLS by 3
		$createIndTool[6] = 'Mining Tools' */
	$createMinTool[6] = $createMinTool[6] - 3;

		/** - update Quantity in Planets
			- update NotForSale in Goods ***/
		for($i=0; $i<8; $i++){
			/******* GET QUANTITY *****/
			$sql = "SELECT $name[$i] FROM planets WHERE starnum=$starNum;";
			$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));
			$row = pg_fetch_object($resultset,0);
			$Qty = $row->$name[$i];
			
			$newQty = $Qty - $createMinTool[$i];
			// -- update Quantity in Planets --
			$sql = "UPDATE planets SET $name[$i]=$newQty WHERE starnum=$starNum;";
			$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));

			/******* GET NOT-FORSALE ******/
			$sql = "SELECT notforsale FROM goods WHERE playernum=$player AND resourcename='$name[$i]';";
			$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));
			// fetch 0th row of $resultset
			$row = pg_fetch_object($resultset,0);
			$curNotForSale = $row->notforsale;
			
			$newNotForSale = $curNotForSale - $createMinTool[$i];
			// -- update NOT-FORSALE in Planets --
			$sql = "UPDATE goods SET notforsale=$newNotForSale WHERE playernum=$player AND resourcename='$name[$i]';";
			$resultset = pg_query($conn, $sql) or die("Error in query: $sql." .pg_last_error($conn));
		}
	}
	echo "<br><object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0\" width=600 height=371>
			<param name=movie value=flash/industrial.swf>
			<param name=quality value=high>
			<param name=WMODE value=transparent>
			<embed src=flash/industrial.swf quality=high pluginspage=http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash type=application/x-shockwave-flash width=600 height=371>
			</embed></object>";
	echo "<meta http-equiv=Refresh content=\"5; URL=gamehome.cgi?mychoice=$passBack&inc=$increase \"> ";

	include "footer.cgi";
?>