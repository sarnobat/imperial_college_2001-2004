<?
include "dbconnect.php";

$planetchosen = $_POST["star"];

$sql = "SELECT star m FROM universe WHERE star = '$planetchosen';";
echo $sql;

$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
$row = pg_fetch_array($resultset,NULL,PGSQL_ASSOC);
echo $row['starnum'];









/*
$players = pg_query($connnection, $sql) or die("Error");
$row = pg_fetch_array($players,NULL,PGSQL_ASSOC);
echo $row['starnum'];
*/
include "dbdisconnect.php";
?>
