<script language = Javascript src = include/validateform.js></script>

<?
	include "dbconnect.php";

/*	Complicated query, but essentially matching the Star name 
 *	with it's associated planet type (e.g. Agricultural etc.)
 */

$sql = "SELECT civilisation,star FROM (SELECT * FROM starstate INNER JOIN universe ON universe.starnum = starstate.starnum) AS table1 WHERE owner = null;";
$resultset = pg_query($connection,$sql) or die("Error in query: $account." . pg_last_error($connection));

echo "<form action = registerplanet.php method = post onSubmit = \"return validateShipName(this)\"> <table>";


/* 	The rest of the code is concerned with creating a form with
 *	a radio element corresponding to each star that is available
 *	for the new player
 */
 
 echo "Please choose a planet:<BR>";

$row = pg_fetch_array($resultset,NULL,PGSQL_ASSOC);
	echo "<tr>";
	echo ("<td><input type=radio name=star value=\"" .$row['star'] ."\" checked></td>");
	echo ("<td>".$row['star']."</td>");
	echo ("<td>".$row['civilisation'] ."</td>");
	echo "</tr>";


while( $row = pg_fetch_array($resultset,NULL,PGSQL_ASSOC)  )
{
	echo "\r<tr>";
	echo ("<td><input type=radio name=star value=\"" .$row['star'] ."\">\t</td>");
	echo ("<td>".$row['star']."</td>");
	echo ("<td>".$row['civilisation'] ."</td>");
	echo "</tr>";
}
echo "</table><P>";

echo "Please choose a name for your ship:";
echo "<BR><input type = text name = shipname > <BR>";

echo "<BR><input type=submit value=\"Join Game\"></form>";

include "dbdisconnect.php";
?>