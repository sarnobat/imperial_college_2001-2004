<?
	$sql		= "SELECT * FROM mailbox WHERE msgnum = $messnum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$rows		= pg_num_rows($resultset);

	$sql		= "SELECT * FROM messages WHERE msgnum = $messnum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row		= pg_fetch_object($resultset,0);
	$sent		= $row->sent;

	if($rows==0 && $sent=='f')
	{
		$sql = "DELETE FROM messages WHERE msgnum = $messnum;";
		$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	}
?>