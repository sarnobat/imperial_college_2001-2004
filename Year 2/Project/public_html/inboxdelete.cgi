#!/usr/bin/php
<?
	include "dbconnect.cgi";
	include "preheader.cgi";

	$playernum	= $HTTP_COO E_VARS["TraderID"];		
	$messnum	= $_GET["msgnum"];

	$sql		= "DELETE FROM mailbox WHERE msgnum = $messnum AND dest = $playernum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	include "deletemsg.cgi";
	include "dbdisconnect.cgi";
?>