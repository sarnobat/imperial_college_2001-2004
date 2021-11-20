#!/usr/bin/php
<?
	include "header.cgi";
	include "dbconnect.cgi";

	print " <h1>Your Planet</h1>
			<h3>Resources you own and goods you can create <br>
			</h3>";

	/* Accessing cookies on client */
	$starNum	= $HTTP_COOKIE_VARS["starID"];
	$starName	= $HTTP_COOKIE_VARS["starName"];
	$playerNum	= $HTTP_COOKIE_VARS["TraderID"];
	$playerName = $HTTP_COOKIE_VARS["TraderName"];

	/* Resources needed to create tools */
	$createAgrTool = array(6, 3, 2, 0, 2, 0.3, 0, 0);
	$createIndTool = array(4, 1, 4, 1, 1, 0.5, 0, 0);
	$createMinTool = array(5, 2, 3, 1, 1, 0.1, 0, 0);

	/* ARRAYS */
	$nameDisp	= array("Food","Water","Fuel","Gold","Titanium","Industrial Tools","Mining Tools","Farming Tools");
	$name		= array("food","water","fuel","gold","titanium","industrial","mining","farming");

	/* CIVILISATION */
	$sql = "SELECT civilisation FROM starstate WHERE starnum = $starNum;";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset);
	$starType = $row->civilisation;

	/* BALANCE */
	$sql = "SELECT * FROM players WHERE playernum = $playerNum;";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset);
	$balance = $row->balance;

	/* Value PASSED BACK (from createIndTools, ), saying which tool was created */
	$mychoice;
	if($mychoice != ""){
		print "	<div ALIGN=left><font COLOR=#FF0000><B> YOU HAVE CREATED $inc units of $nameDisp[$mychoice] </B> </font></div>
				<br>";
	}

	/**** Read NOT-FOR-SALE column for user ****/
	for($i=0; $i<8; $i++){
		$sql = "SELECT notforsale FROM goods WHERE playernum=$playerNum AND resourcename='$name[$i]';";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);

		$notforsale[$i] = $row->notforsale;
	}

	$Choice = $name[$mychoice];

	print "<h2>Your Resource Levels</h2>
	<!-- Header Row -->
	<table class=data>
		<tr class=tableheading><td><B> Resource </B></td> ";

	/*** Print Resource Headings ***/
	for($i=0; $i<8; $i++){
		if($name[$i] == $Choice){
			echo  "<td width=60> <FONT COLOR=#FF0000> <B>$nameDisp[$i]</B> </FONT> </td>";
		}
		else{
			echo  "<td width=60> $nameDisp[$i] </td>";
		}
	}
	print "
	</tr>
	<tr><td class=tableheading><B> Quantity availiable to use </B></td> ";

	/*** Print NOT-FOR-SALE values ***/
	for($i=0; $i<8; $i++){
		if($name[$i] == $Choice){
			echo  "<td> <FONT COLOR=#FF0000> <B>$notforsale[$i]</B> </FONT> </td>";
		}
		else{
			echo  "<td> $notforsale[$i] </td>";
		}
	}
	print "
	</tr>
	</table> ";

	switch($starType){
		case "industrial": include "Ind_Home.cgi"; break;
		case "agriculture": include "Agr_Home.cgi"; break;
		case "mine": include "Min_Home.cgi"; break;
	}

	include "dbdisconnect.cgi";
	include "footer.cgi";
?>