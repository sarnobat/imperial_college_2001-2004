#!/usr/bin/php
<?
	/*	Perhaps no need to open a new connection to the database because
		at the point when this script is called, the connection hasn't yet been closed */	
	include "dbconnect.cgi";

	$playernum = $HTTP_COOKIE_VARS["TraderID"];
	/* Set the "playing" field to false in the table "players" */
	$sql = "UPDATE players SET playing = 'f' WHERE playernum = '"  .$playernum ."'";
//	echo $sql;
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	/* Set the "playing" field to false in the table "game" */
	$sql = "UPDATE game SET playing = 'f' WHERE playernum = '" .$HTTP_COOKIE_VARS["TraderID"] ."'";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	/* Clear the "TraderID" cookie value */
	setcookie("TraderID","-1");

	/* Return player to Login Page */
	echo "<meta http-equiv=Refresh content=\"0.001; URL= index.cgi \">";

	include "dbconnect.cgi";
?>