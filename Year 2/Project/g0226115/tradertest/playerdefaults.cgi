
<?
	$defaultplayerbalance = 2500;	// The default amount of money a player starts wi 
	
	$sql = "UPDATE players SET balance = '$defaultplayerbalance' WHERE playernum = '$playernum';";
	$status = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
?>