#!/usr/bin/php
<? 
	include "../include/dbconnect.cgi";

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
		$output = $output ."<meta http-equiv=Refresh content=\"4; URL= login.html \"> ";
	}

	/* Successful login */
	else{
		/* Set playing to True */

		$sql = "UPDATE players SET playing = 't' WHERE playername = '$name'";
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

		$sql = "SELECT starnum FROM starstate WHERE owner = $playernum";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$starnum = $row->starnum;
		setcookie("starID",$starnum);
		
		$sql = "SELECT star FROM universe WHERE starnum = $starnum";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$star = $row->star;
		setcookie("starName",$star);

		$output = "<meta http-equiv=Refresh content=\"0.0001; URL= ../gamehome.cgi \">";
	}
	include "../include/header.cgi";

	echo $output;

	include "../include/dbdisconnect.cgi";
	include "../include/footer.cgi";
?>