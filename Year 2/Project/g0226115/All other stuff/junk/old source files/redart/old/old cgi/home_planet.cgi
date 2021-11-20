#!/usr/bin/php
<?
	include "include/header.cgi";
	include "include/dbconnect.cgi";

	$starnumber	  = $HTTP_COOKIE_VARS["starID"];
	$playernumber = $HTTP_COOKIE_VARS["TraderID"];

	/*** ALL ARRAYS ***/
	$name	= array("food","water","fuel","gold","titanium","industrial","mining","farming");
	$Qty	= array(0,0,0,0,0,0,0,0);
	$forsale= array(0,0,0,0,0,0,0,0);
	$cost	= array(0,0,0,0,0,0,0,0);

	/*** Updates Forsale array using GOODS table***/
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
	<script language = Javascript>
	<!--
	function validateTrade(form){
		if(	form.sale0.value > ($Qty[0]-$forsale[0]) || form.sale1.value > ($Qty[1]-$forsale[1]) ||
			form.sale2.value > ($Qty[2]-$forsale[2]) || form.sale3.value > ($Qty[3]-$forsale[3]) ||
			form.sale4.value > ($Qty[4]-$forsale[4]) || form.sale5.value > ($Qty[5]-$forsale[5]) ||
			form.sale6.value > ($Qty[6]-$forsale[6]) || form.sale7.value > ($Qty[7]-$forsale[7])){
			alert(\"You have entered incorrect values\");
			return false;
		}
		return true;
	} -->
	</script>

	Player number: $playernumber <br>
	Star (Planet) number: $starnumber

	<FORM METHOD=POST ACTION=update.cgi name=\"my_form\" onSubmit=\"return(validateTrade(this))\">
	<!-- Header Row -->
		<table>
		  <tr>
			<td><B> Resource	</B></td>
			<td><B> Quantity	</B></td>
			<td> </td>
			<td><B> For Sale	</B></td>
			<td><B> Trade		</B></td>
			<td><B> Not For Sale</B></td>
			<td><B> Unit Cost	</B></td>
			<td><B> Change Cost	</B></td>
		  </tr>	";

	/*** Print each row of table ***/
	for($i=0; $i<8; $i++){
	print "
		<tr>
			<!--Resource -->	<td> $name[$i]								</td>
			<!--Quantity-->		<td> $Qty[$i]								</td>
			<!--Bar graph-->	<td> <img src=images/blue_dot.gif height=16 width=" .($Qty[$i]/$sum_Qty)*100 ."> </td>
			<!--For Sale-->		<td> $forsale[$i] </td>
			<!--Trade-->		<td> <input type=text size=2 name=sale$i>	</td>
			<!--Not For Sale-->	<td> ".($Qty[$i]-$forsale[$i])."			</td>
			<!--Cost-->			<td> $cost[$i] </td>
			<!--Change Cost-->	<td> <input type=text size=2 name=cost$i>	</td>
		</tr> ";
	}

	print "
		</table>
	<!-- Buttons -->
		<br>

		<!-- 	onClick=\"javascript:validateTrade(my_form);\"	 -->


		<input type=submit name=Update value=PutOnMarket> <br><br>
		<input type=submit name=Update value=TakeOffMarket> <br><br>
		<input type=submit name=Update value=UpdateCost> <br><br>
		<input type=reset	value = \"Clear Values\">
	</FORM> ";

	include "include/dbdisconnect.cgi";
	include "include/footer.cgi";
?>