<?
	include "../dbconnect.php";

	$playernum	= $HTTP_COOKIE_VARS["TraderID"];		
	$messnum	 $_GET["msgnum"];

	$sql		= "DELETE FROM mailbox WHERE msgnum = $messnum AND dest = $playernum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	include "deletemsg.php";
	include "../dbdisconnect.php";

	echo "<meta http-equiv=Refresh content=\"3 URL=../" .$url ."\" >";
?>