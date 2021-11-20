#!/usr/bin/php
<?
	include "include/header.cgi";
	include "include/dbconnect.cgi";

	$starnumber	  = $HTTP_COOKIE_VARS["starID"];
	$starname	  = $HTTP_COOKIE_VARS["starName"];
	$playernumber = $HTTP_COOKIE_VARS["TraderID"];

	/*** ALL ARRAYS ***/
	$name	= array("food","water","fuel","gold","titanium","industrial","mining","farming");
	$n      

	/*** Updates Forsale array using GOODS table ***/
	for($i=0; $i<8; $i++){
		$sql = "SELECT forsale FROM goods WHERE playernum = $playernumber AND resourcename = '$name[$i]';";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$forsale[$i] = $row->forsale;
	}

	// Total number of resources on planet
	$sum_Qty=0;

	/*** Updates Quantity array using PLANETS table ***/
	for($i=0; $i<8; $i++){
		$sql = "SELECT $name[$i] FROM planets WHERE starnum=$starnumber";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$Qty[$i] = $row->$name[$i];
		$sum_Qty=$sum_Qty+$Qty[$i];
	}

	/*** Updates Cost array using GOODS table ***/
	for($i=0; $i<8; $i++){
		$sql = "SELECT price FROM goods WHERE playernum = $playernumber AND resourcename = '$name[$i]';";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$cost[$i] = $row->price;
	}

	print "
	Player number: $playernumber <br>
	Star name: $starName <br>
	Star (Planet) number: $starnumber

	<FORM METHOD=POST ACTION=data/update.cgi>
	<!-- Header Row -->
		<table>
		  <tr>
			<td><B> Resource	</B></td>
			<td><B> Quantity	</B></td>
			<td> </td>
			<td><B> For Sale	</B></td>
			<td><B> Put For Sale</B></td>
			<td><B> Not For Sale</B></td>
			<td><B> Take Off Sale</B></td>
			<td><B> Unit Cost	</B></td>
			<td><B> Change Cost	</B></td>
		  </tr>	";

	/*** Print each row of table ***/
	for($i=0; $i<8; $i++){
	print "
		<tr>
			<!--Resource -->	<td> $nameDisp[$i]							</td>
			<!--Quantity-->		<td> $Qty[$i]								</td>
			<!--Bar graph-->	<td> <img src=images/blue_dot.gif height=16 width=" .($Qty[$i]/$sum_Qty)*100 ."> </td>
			<!--For Sale-->		<td> $forsale[$i] </td>
			<!--Put For Sale-->	<td> <input type=text size=2 name=sale$i
									  onblur = \" validateSell(sale$i, $Qty[$i], $forsale[$i]);
												  checkIsNumeric(sale$i);		\">	</td>

			<!--Not For Sale-->	<td> ".($Qty[$i]-$forsale[$i])."					</td>
			<!--Take Off Sale--><td> <input type=text size=2 name=takeoff$i
									  onblur = \" validateTakeOff(takeoff$i, $forsale[$i]);
												  checkIsNumeric(takeoff$i);	\">	</td>
			<!--Cost-->			<td> $cost[$i] </td>
			<!--Change Cost-->	<td> <input type=text size=2 name=cost$i
									  onblur = \" validateCost(cost$i);
												  checkIsNumeric(cost$i);		\">	</td>
		</tr> ";
	}

	print "
		</table>
	<!-- Buttons -->
		<br>

		<input type=submit name=Update value=PutOnMarket> <br><br>
		<input type=submit name=Update value=TakeOffMarket> <br><br>
		<input type=submit name=Update value=UpdateCost> <br><br>
	</FORM> ";

	include "include/dbdisconnect.cgi";
	include "include/footer.cgi";
?>

<script>
	function validateSell(sale, qty, forsale){
		if (sale.value < 0){
			alert("Cannot add negative values,\nPlease re-enter value");
			sale.value = "";		// Clear the field
			sale.focus();
		}		
		if (sale.value > (qty - forsale)){
			alert("Incorrect trade value");
			sale.value = "";
			sale.focus();
		}
	}

	function validateTakeOff(takeOff, forsale){
		if (takeOff.value < 0){
			alert("Cannot remove negative values,\nPlease re-enter value");
			takeOff.value = "";
			takeOff.focus();
			return false;
		}

		// Can only take off sale as many items as have been put on
		if (takeOff.value > forsale){
			alert("Incorrect trade value");
			takeOff.value = "";
			takeOff.focus();
			return false;
		}
	}

	function validateCost(cost){
		if (cost.value < 0){
			alert("Cannot set negative price,\nPlease re-enter value");
			cost.value = "";
			cost.focus();
			return false;
		}
	}
	
	function checkIsNumeric(string){
		if(!IsNumeric(string.value)){
			alert("Please enter an integer");
			string.value = "";
			string.focus();
			return false;
		}
	}

	function IsNumeric(strValue){
		var ValidChars = "0123456789";
		var IsNumber=true;
		var Char;

		for (i=0; i < strValue.length && IsNumber == true; i++){
			Char = strValue.charAt(i); 
			if (ValidChars.indexOf(Char) == -1){
				IsNumber = false;
			}
		}
		return IsNumber;
	}
</script>