#!/usr/bin/php
<?
	ob_start();
	include "preheader.cgi";
	include "dbconnect.cgi";

	/* Data imported from query strings, cookies etc. */

	$planetchosen = $_POST["star"];
	$playernum = $HTTP_COOKIE_VARS["TraderID"];

	/* Determine the starnumber, given the star name */
	
	$sql = "SELECT star,starnum FROM universe WHERE star = '$planetchosen';";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_array($resultset,NULL,PGSQL_ASSOC);
	$starnumber = $row['starnum'];
	$star = $row['star'];
	setcookie("starID",$starnumber);
	setcookie("starName",$star);

	/* Set the star chosen as being owned by the player currently logged in */

	$sql = "UPDATE starstate SET owner = '$playernum' WHERE starnum = '$starnumber';"; 
	$status = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	include "planetdefaults.cgi";
	include "playerdefaults.cgi";
	include "shipdefaults.cgi";
	include "goodsdefaults.cgi";
	
	/* Foward player to Game Home Page */
	echo "<meta http-equiv=Refresh content=\"1; URL= gamehome.cgi \">";
	
	include "dbdisconnect.cgi";
	ob_end_flush();
?>