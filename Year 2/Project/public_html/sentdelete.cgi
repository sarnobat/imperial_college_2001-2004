#!/usr/bin/php
<?
	include "dbconnect.cgi";
	include "preheader.cgi";

	$messnum	= $_GET["msgn "];

	$sql		= "SELECT * FROM messages WHERE msgnum = $messnum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row		= pg_fetch_object($resultset, 0);

	$sent		= $row->sent;

	$sql		= "UPDATE messages SET sent='f' WHERE msgnum = $messnum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	include "deletemsg.cgi";
	include "dbdisconnect.cgi";
?>