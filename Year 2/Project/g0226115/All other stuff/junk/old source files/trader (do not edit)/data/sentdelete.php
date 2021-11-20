<?
	include "../dbconnect.php";

	$messnum	= $_GET["msgnum"];

	$sql		= "SELECT * FROM messages WHERE msgnum = $messnum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row		= pg_fetch_object($resultset, 0);

	$sent		= $row->sent;

	$sql		= "UPDATE messages SET sent='f' WHERE msgnum = $messnum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	include "deletemsg.php";
	include "../dbdisconnect.php";

	echo "<meta http-equiv=Refresh content=\"3 URL=../" .$url ."\" >";
?>