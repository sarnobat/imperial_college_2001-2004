#!/usr/bin/php
<?
	include "../include/dbconnect.cgi";
		
	$messnum = $_GET["msgnum"];

J sql = "DELETE FROM messages WHERE msgnum = $messnum;
			DELETE FROM mailbox WHERE msgnum = $messnum;";
	$resultset = pg_query($connection,$sql) or die("");

	include "../include/dbdisconnect.cgi";

	echo "<meta http-equiv=Refresh content=\"0.0001 URL=../" .$url ."\" >";
?>