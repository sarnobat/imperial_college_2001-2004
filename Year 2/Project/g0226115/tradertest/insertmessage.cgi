#!/usr/bin/php
<?
	include "dbconnect.cgi";
	include "preheader.cgi";

	$sender		= $HTTP_COOKIE_VARS["TraderID"];
	$i			= $_POST["numdest"];
	$message	= $_POST["message"];
	$subject	= $_POST["subject"];

	$sql		= "SELECT * FROM messages ORDER BY msgnum DESC;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$nummsgs	= pg_num_rows($resultset);

	if($nummsgs == 0)
		$newmsgnum = 1;
	else
	{
		$row			= pg_fetch_object($resultset,0);
		$highestmsgnum	= $row->msgnum;
		$newmsgnum		= $highestmsgnum+1;
	}

	for($j=0; $j<$i ; $j++){
		$destname = $_POST["hidden$j"];

		$sql		= "SELECT * FROM players WHERE playername='$destname';";
		$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row		= pg_fetch_object($resultset,0);

		$destnum = $row->playernum;

		$sql		= "INSERT INTO mailbox VALUES ($newmsgnum, $destnum);";
		$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	}

	$sql		= "SELECT CURRENT_TIMESTAMP;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row		= pg_fetch_object($resultset, 0);

	$date		= $row->timestamp;

	$sql		= "INSERT INTO messages VALUES ($newmsgnum, NULL, $sender, '$date', '$message', '$subject', 't');";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	print"
		Your Message Has Been Sent Successfully!!!
		<meta http-equiv=Refresh content=\"2; URL= sent.cgi \">
	";

	include "dbdisconnect.cgi";
?>