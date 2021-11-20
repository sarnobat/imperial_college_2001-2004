#!/usr/bin/php
<?
	ob_start();
	include "dbconnect.cgi";
	include "preheader.cgi";

	$name = $_POST["username"];
	$password = $_POST["password"];
	$email = $_POST["email"];
	


	/* If no players have registered, generate the coordinates of the stars as a one-off */
	$sql = "SELECT * FROM players ORDER BY playernum DESC;";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$numplayers = pg_num_rows($resultset);
		
	if($numplayers == 0)
	{
		include "setcoords.cgi";
		$highestid = 1;
	}
	else
	{
		$row = pg_fetch_object($resultset,0);
		$highestid = $row->playernum;
	}

	$sql = "SELECT * FROM players";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	$originalName = True;

	$rows = pg_num_rows($resultset);

	if($rows > 0)
	{
		/* Check through the table to see if the requested username has already been chosen */
		for($i=0; $i<$rows; $i++)
		{
			$row = pg_fetch_object($resultset, $i);
			if($row->playername == $name)
			{
				$originalName = False;
			}
		}
	}

	if($originalName == False)
	{/* Specified username is already in use */
		include "preheader.cgi";
		echo "Username <B>\"$name\"</B> already chosen. Please choose something else.<br><i>Returning to registering page..</i>";
		// This line redirects you to the Logging in Page
		echo "<meta http-equiv=Refresh content=\"1; URL= index.cgi \">" ;
		include "footer.cgi";
	}
	else
	{/* Specified username is acceptable for registration */

		$playernum = pg_num_rows($resultset) + 1;
		$playernum = $highestid + 1;
		$sql = "INSERT INTO players  (playernum, playername, playing, password,email) VALUES ($playernum, '$name', 't','$password','$email')";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." . pg_last_error($connection));
		setcookie("TraderID",$playernum);
		setcookie("TraderName",$name);
		setcookie("ShipID",$playernum);
		echo "<meta http-equiv=Refresh content=\"1; URL= selectplanet.cgi \">";
	}

	include "dbdisconnect.cgi";
	ob_end_flush();

?>