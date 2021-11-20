#!/usr/bin/php
<?
	include "header.cgi";
	include "dbconnect.cgi";

	echo "<h1>Scoreboard</h1><h3>All players ranked by balance</h3>";

	$sql = "CREATE TABLE scores AS (SELECT * FROM players);	SELECT * FROM scores ORDER BY balance DESC;";
	$resultset = pg_query($connection,$sql) or die("");

	echo "<table width=60%>";
	echo "<tr class=tableheading><td>Rank</td><td>Player</td><td>Score</td></tr>";
	for($i=0;$i<pg_num_rows($resultset);$i++)
	{
		$row = pg_fetch_object($resultset,$i);
		echo "<tr><td>" .($i+1) ."</td><td><b>" .$row->playername ."</b></td><td>£ "	.$row->balance ."</td></tr>";
	}
	echo "</table>";

	$sql = "DROP TABLE scores;";
	$resultset = pg_query($connection,$sql) or die("");


	include "dbdisconnect.cgi";
	include "footer.cgi";


?>