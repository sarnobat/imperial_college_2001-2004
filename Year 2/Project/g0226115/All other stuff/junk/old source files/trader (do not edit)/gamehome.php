<?
	include "header.php";
	include "dbconnect.php";
	$starNum  = $HTTP_COOKIE_VARS["starID"];
	$starName = $HTTP_COOKIE_VARS["starName"];

	$sql = "SELECT civilisation FROM starstate WHERE starnum = $starNum; 
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset,NULL,PGSQL_ASSOC);
	$starType = $row->civilisation;

	print "	Star (Planet) number: $starNum <br>
			Star (Planet) Name: $starName <br>
			<B>Planet Type: $starType</B> ";

	include "dbdisconnect.php";
	include "footer.php";
?>