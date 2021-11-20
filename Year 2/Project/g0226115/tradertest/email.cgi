#!/usr/bin/php
<?
	/*	This file emails a player its password	*/

	include "dbconnect.cgi";
	include "preheader.cgi";

	$email = $_POST["email"];
	
	//Determine the password corresponding to the given username
	$username = getonlycellvalue ($connection, "SELECT * FROM players WHERE email = '$email';","playername");
	$password = getonlycellvalue ($connection, "SELECT * FROM players WHERE email = '$email';","password");
	

	function getonlycellvalue($conn,$query,$columnname){
		// This function fetches the first value of the field 'columname' in the result of the query supplied
		$resultset = pg_query($conn,$query)  or die("Error in query: $sql." .pg_last_error($conn));
		$numrows = pg_num_rows($resultset);
		if ($numrows > 0){
			$row = pg_fetch_object($resultset,0);
			return $row->$columnname;
		}
		else{
			return "False";
		}
	}

	mail ($strMailTo, $strSubject, $strBody, $strXHeaders); 

	$sent = mail($email, "Your Space Trader Password", "username: $username\npassword: $password", ""); 

	echo "<br><br><br><br><br><br><br><h2>";
	if ($sent == TRUE && $password != "False"){
		echo "Password emailed successfully";
	}
	else{
		echo "Password not sent";
	}

	echo "</h2><br><i>Returning to homepage...</i>";

	include "dbconnect.cgi";

	echo "<meta http-equiv=Refresh content=\"0; URL=index.cgi \"> ";

	include "footer.cgi";
?>