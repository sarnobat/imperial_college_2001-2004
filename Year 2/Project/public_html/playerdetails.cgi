<?
	include "dbconnect.cgi";

		/* Accessing cookies on client */
	$starNum	= $HTTP_COOKIE_VARS["starID"];
	$starName	= $HTTP_COOKIE_VARS["starName"];
	$playerNum	= $HTTP_COOKIE_VARS["TraderID"];
	$playerName = $HTTP_COOKIE_VARS["TraderName"];

	/* Resources needed to create tools */
	$createAgrTool = array(6, 3, 2, 0, 2, 0.3, 0, 0);
	$createIndTool = array(4, 1, 4, 1, 1, 0.5, 0, 0);
	$createMinTool = array(5, 2, 3, 1, 1, 0.1, 0, 0);



	/* ARRAYS */
	$nameDisp	= array("Food","Water","Fuel","Gold","Titanium","Industrial Tools","Mining Tools","Farming Tools");
	$name		= array("food","water","fuel","gold","titanium","industrial","mining","farming");

	/* CIVILISATION */
	$sql = "SELECT civilisation FROM starstate WHERE starnum = $starNum;";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset);
	$starType = $row->civilisation;

	/* BALANCE */
	$sql = "SELECT * FROM players WHERE playernum = $playerNum;";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset);
	$balance = $row->balance;

	/*	Ship details */
	$sql =  "SELECT * FROM shipstate;";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset);
	$mileage = $row->mileage;
	$fuel	=	$row->fuelcontent;

	echo "You have logged in as:	<br><div class=menudata>$playerName </div>";
	echo "Home Planet Name:		<br><div class=menudata>$starName</div>";
	echo "Home Planet Type:		<br><div class=menudata>$starType</div>";
	echo "Your account balance:	<br><div class=menudata>£ $balance</div>	";
	echo "<hr class=menurule>";
	echo "Ship mileage:	<br><div class=menudata>$mileage </div>";
	echo "Ship fuel:	<br><div class=menudata>$fuel</div>	";


	include "dbdisconnect.cgi";
?>