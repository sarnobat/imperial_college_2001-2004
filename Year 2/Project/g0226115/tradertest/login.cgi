#!/usr/bin/php
<? 
	ob_start();
	include "dbconnect.cgi";

	$name = $_POST["username"];
	$password = $_POST["password"];

	$sql = "SELECT * FROM players WHERE playername = '$name' AND password = '$password'";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." . pg_last_error($connection));
	$numEntries = pg_num_rows($resultset);

	/* Visitor hasn't registered */
	if($numEntries == 0){

		$output = "Login failed. Either your password is wrong or you have not registered <br>
					<i>Returning to registering page..</i>";

		// This line redirects you to the Logging in Page to try again
		$output = $output ."<meta http-equiv=Refresh content=\"3; URL= index.cgi \"> ";
	}

	/* Successful login */
	else{
		/* Set playing to True (and drop some temporary tables */
		$sql = "UPDATE players SET playing = 't' WHERE playername = '$name';";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." . pg_last_error($connection));

		/*	The next block of code sets			*
		 *	the cookie "TraderID" equal to the playernum	*
		 *	of the player that has just logged in		*/

		$sql = "SELECT playernum,playername FROM players WHERE playername = '$name' AND password = '$password'";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$playernum = $row->playernum;
		$playername = $row->playername;
		setcookie("TraderID",$playernum);
		setcookie("TraderName",$playername);
		setcookie("ShipID",$playernum);
		
		$shipCapacity = 1000;
		setcookie("ShipCapacity",$shipCapacity);

		$sql = "SELECT starnum FROM starstate WHERE owner = $playernum";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$starnum = $row->starnum;
		setcookie("starID",$starnum);
		
		//Determine the name and co-ordinates of your home planet, and set the cookies
		$sql = "SELECT star FROM universe WHERE starnum = $starnum";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$star = $row->star;
		$home_x = $row->x;
		$home_y = $row->y;
		$home_z = $row->z;
		setcookie("starName",$star);
		setcookie("home_x",$home_x);
		setcookie("home_y",$home_y);
		setcookie("home_z",$home_z);

		//Determine the co-ordinates of your ship, and set the cookie
		$sql = "SELECT * FROM shipstate WHERE owner = $playernum";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$ship_x = $row->x;
		$ship_y = $row->y;
		$ship_z = $row->z;
		setcookie("ship_x",$ship_x);
		setcookie("ship_y",$ship_y);
		setcookie("ship_z",$ship_z);

		$output = "<meta http-equiv=Refresh content=\"0; URL= gamehome.cgi \">";
	}
	include "preheader.cgi";

	echo $output;

	include "dbdisconnect.cgi";
	include "footer.cgi";
	ob_end_flush();
?>