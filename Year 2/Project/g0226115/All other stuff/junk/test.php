<?
include ("dbconnect.php");

$query = "SELECT * FROM universe";
$query = pg_query($query);
while($row = pg_fetch_array($query,NULL,PGSQL_ASSOC))
{
// print_r($row);
// Uncomment the preceding line to see the enti  array.
echo $row['star']."<br/>";
}

?>