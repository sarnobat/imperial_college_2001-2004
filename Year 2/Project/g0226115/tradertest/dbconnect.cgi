<?
	$host = "db";
	$user = "g0226115";
	$pass = "madgreysuc";
	$db = "g0226115db";

	$co ection = pg_connect ("host=$host dbname=$db user=$user password=$pass");

	if (!$connection)
	{
		die("Could not open connection to database server");
	}
?>