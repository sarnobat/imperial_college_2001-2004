#!/usr/bin/php
<?
	include "header.cgi";
	include "dbconnect.cgi";

	echo "<h1>Your Warehouse</h1><h3>A summary of the quantities of each resource you own. <br> Put more or less of them for sale, or change the price per unit</h3>";

	$starnumber	  = $HTTP_COOKIE_VARS["starID"];
	$starname	  = $HTTP_COOKIE_VARS["starName"];
	$playernumber = $HTTP_COOKIE_VARS["TraderID"];
	$playername   = $HTTP_COOKIE_VARS["TraderName"];

	/*** ALL ARRAYS ***/
	$name	= array("food","water","fuel","gold","titanium","industrial","mining","farming");
	$nameDisp = array("Food","Water","Fuel","Gold","Titanium","Industrial Tool","Mining Tool","Farming Tool");
	$Qty	= array();
	$forsale= array();
	$notforsale = array();
	$cost	= array();

	/*** Updates Forsale array using GOODS table ***/
	for($i=0; $i<8; $i++){
		$sql = "SELECT forsale FROM goods WHERE playernum = $playernumber AND resourcename = '$name[$i]';";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$forsale[$i] = floor($row->forsale);
	}

	/*** Updates NotForsale array using GOODS table ***/
	for($i=0; $i<8; $i++){
		$sql = "SELECT notforsale FROM goods WHERE playernum = $playernumber AND resourcename = '$name[$i]';";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$notforsale[$i] = floor($row->notforsale);
	}

	// Total number of resources on planet
	$sum_Qty=0;

	/*** Updates Quantity array using PLANETS table ***/
	for($i=0; $i<8; $i++){
		$sql = "SELECT $name[$i] FROM planets WHERE starnum=$starnumber";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$Qty[$i] = floor($row->$name[$i]);
		$sum_Qty=$sum_Qty+$Qty[$i];
	}

	if($sum_Qty==0){
		$sum_Qty = 1;
	}
	/*** Updates Cost array using GOODS table ***/
	for($i=0; $i<8; $i++){
		$sql = "SELECT price FROM goods WHERE playernum = $playernumber AND resourcename = '$name[$i]';";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$cost[$i] = $row->price;
	}

	print "

	<FORM METHOD=POST ACTION=update.cgi>
	<!-- Header Row -->
		<table class=data>
		  <tr class=tableheading>
			<td><B> Resource	</B></td>
			<td><B> Quantity	</B></td>
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
			<!--Bar graph-->	<td> <P align=left><img src=images/blue_dot.gif align=left height=16 width=" .($Qty[$i]/$sum_Qty)*100 ."><B>$Qty[$i]</B></P></td>
			<!--For Sale-->		<td> $forsale[$i] </td>
			<!--Put For Sale-->	<td> <input type=text size=2 name=sale$i
									  onblur = \" validateSell(sale$i, $Qty[$i], $forsale[$i]);
												  checkIsNumeric(sale$i);		\">	</td>

			<!--Not For Sale-->	<td> $notforsale[$i] </td>
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
		<!-- Buttons -->
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td> <input type=submit name=Update value=PutOnMarket>	</td>
			<td></td>
			<td> <input type=submit name=Update value=TakeOffMarket>	</td>
			<td></td>
			<td> <input type=submit name=Update value=UpdateCost>	</td>
		</tr>
		</table>
	</FORM> ";

	print "<I>	The 'Quantity' and 'Not For Sale' values of Tools may differ from the values in 'Game Home' <br>
				because only an integer number of tools (i.e. brand new goods) can be put for sale</I>";

	include "dbdisconnect.cgi";
	include "footer.cgi";
?>

<script>
	function validateTakeOff(takeOff, forsale){
		if (takeOff.value < 0){
			alert("Cannot use negative values,\nPlease re-enter value");
			takeOff.value = "";
			takeOff.focus();
			return false;
		}

		/**** Can only take off sale as many items as have been put on ****/
		if (takeOff.value > forsale){
			alert("Incorrect trade value (Too Large)");
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
</script>
<script language=JavaScript src=isNumeric.js></script>