#!/usr/bin/php
<?
	include "include/dbconnect.cgi";
	
	echo "<form action=fly.cgi method=post>
	Go to<br>
		x<input type=text name=x><br>
		y<input type=text name=y><br>
		z<input type=text name=z><br>
		<input type=submi 
	</form>";

	echo $_COOKIE["TraderID"];

	$x = $_POST["x"];
	$y = $_POST["y"];
	$z = $_POST["z"];
	
	echo $sql = " UPDATE shipstate SET x=$x,y=$y,z=$z WHERE owner=" .$_COOKIE["TraderID"] .";";

	if ($x != null && $y != null && $z != null)
	{
		$resultset = pg_query($connection,$sql) or die("");
	}

	include "include/dbdisconnect.cgi";


?>