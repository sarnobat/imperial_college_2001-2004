<?
	
	include "dbconnect.php";

	/* Data imported from query strings, cookies etc. */

	$planetchosen = $_POST["star"];
	$playernum = $HTTP_COOKIE_VARS["TraderID"];

	/* Determine the starnumber, given the star name */
	
	$sql = "SELECT star,starnum FROM universe WHERE star = '$planetchosen';";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_array($resultset,NULL,PGSQL_ASSOC);
	$starnumber = $row['starnum'];
	$star = $row['star'];
	setcookie("starID",$starnumber);
	setcookie("starName",$star);

	/* Set the star chosen as being owned by the player currently logged in */

	$sql = "UPDATE starstate SET owner = '$playernum' WHERE starnum = '$starnumber';"; 
	$status = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	include "data/planetdefaults.php";
	include "data/playerdefaults.php";
	include "data/shipdefaults.php";
	include "data/goodsdefaults.php";
	
	/* Foward player to Game Home Page */
	echo "<meta http-equiv=Refresh content=\".00001; URL= gamehome.php \">";
	
	include "dbdisconnect.php";
?>