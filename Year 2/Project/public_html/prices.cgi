<?


	$resources = array("food","water","fuel","gold","titanium","industrial","mining","farming");
	$num_resources = count($resources);
	echo "<p align = center><marquee scrollamount = 5 width=50%>Latest Average Prices courtesy of the Galactica 100 index:&nbsp;&nbsp;&nbsp;";
	for($i=0;$i<$num_resources;$i++){
		$sql = "SELECT * FROM goods WHERE resourcename = '$resources[$i]';";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$num_rows = pg_num_rows($resultset);

		$acc = 0;
		for($j=0;$j<$num_rows;$j++){
			$row = pg_fetch_object($resultset,$j);
			$acc = $acc + $row->price;
		}

		echo "<b><font class=$resources[$i]>"  .$resources[$i] ."&nbsp;&nbsp;&nbsp;</b>" .($acc / $num_rows)
			."</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	}
	echo "</marquee></p>";
?>