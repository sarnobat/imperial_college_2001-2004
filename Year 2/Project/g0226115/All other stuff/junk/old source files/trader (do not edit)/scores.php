<?
	include "header.php";
	include "dbconnect.php";

	$sql = "CREATE TABLE scores AS (SELECT * FROM players);	SELECT * FROM scores ORDER BY balance DESC;";
	$resultset = pg_query($connection,$sql) or die("");

	echo <table>";
	echo "<tr><td>Player</td><td>Score</td></tr>";
	for($i=0;$i<pg_num_rows($resultset);$i++)
	{
		$row = pg_fetch_object($resultset,$i);
		echo "<tr><td>" .$row->playername ."</td><td>"	.$row->balance ."</td></tr>";
	}
	echo "</table>";

	$sql = "DROP TABLE scores;";
	$resultset = pg_query($connection,$sql) or die("");


	include "dbdisconnect.php";
	include "footer.php";


?>