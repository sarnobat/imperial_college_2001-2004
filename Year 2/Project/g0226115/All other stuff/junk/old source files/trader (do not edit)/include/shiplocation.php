<?
//	include "../dbconnect.php";
		
	/************************************************************************	
	 *		We use this file to determine the number of the player at		*
	 *		whose planet our ship is										*
	 ************************************************************************/
	

	// Determine your your ship's co-ordinates
	$myplayernum = $_COOKIE["TraderID"];
//	echo $thisplayer;
	$sql = "SELECT * FROM shipstate WHERE owner = '$myplayernum';";
//	echo $sql;
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset,0);

	$shipx = $row->x;
	$shipy = $row->y;
	$shipz = $row->z;
	
	// Find out which star resides at these co-ordinates
	$sql = "CREATE TABLE table1 AS (SELECT * FROM universe WHERE x=$shipx AND y=$shipy AND z=$shipz);
			CREATE TABLE table2 AS (SELECT table1.starnum FROM table1 INNER JOIN starstate ON table1.starnum = starstate.starnum);
			CREATE TABLE table3 AS (SELECT owner FROM table2 INNER JOIN	starstate ON table2.starnum = starstate.starnum);
			SELECT * FROM table3;";
	$resultset = pg_query($connection,$sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset,0);
	$starlordnum = $row->owner;
	
	$sql = "SELECT * FROM players WHERE playernum = $starlordnum;";
	$resultset = pg_query($connection,$sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset,0);
	$starlordname = $row->playername;

	$sql = "DROP TABLE table1; DROP TABLE table2; DROP TABLE table3;";
	$resultset = pg_query($connection,$sql) or die("Error in query: $sql." .pg_last_error($connection));
	

//	include "../dbdisconnect.php";

?>
