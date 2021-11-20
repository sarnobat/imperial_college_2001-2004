<?
	$playernum	= $HTTP_COOKIE_VARS["TraderID"];
	$messnum = $_GET["msgnum"];

	$sql = "SELECT * FROM messages WHERE msgnum = $msgnum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset, 0);
	$sent = $row->sent;

	$sql = "SELECT * FROM mailbox WHERE msgnum = $msgnum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$rows = pg_num_rows($resultset);
	
	if($rows==0 && $sent=='f'){
		$sql = "DELETE FROM messages WHERE msgnum = $messnum;DELETE FROM mailbox WHERE msgnum = $messnum;";
		$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	}

	echo "<meta http-equiv=Refresh content=\"0 URL=" .$url ."\" >";
?>