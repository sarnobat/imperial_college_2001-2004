<?
	/* Determine how many stars there are */	
	$sql = "SELECT * FROM universe";
	$resultset =  _query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$numstars = pg_num_rows($resultset);


	for($i=0;$i<=$numstars;$i++){
		
		$x = floor(rand(0,100));
		$y = floor(rand(0,100));
		$z = floor(rand(0,100));
		$sql = "UPDATE universe SET x = $x,y=$y,z=$z WHERE starnum=$i;";
		$status = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		//echo $status;
	}

?>