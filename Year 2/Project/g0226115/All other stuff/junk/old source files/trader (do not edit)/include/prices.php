<?
//	include "../dbconnect.php";

	$resources = array("food","water","fuel","gold","titanium","industrial","mining","farming");
	$num_resources = count($resources);
	echo "<p align = center><marquee scrollamount = 5 width=50%>Latest Average Prices:&nbsp;&nbsp;&nbsp;";
	for($i=0;$i<$num_resources;$i++){
		$sql = "SELECT * FROM goods WHERE resourcename = '$resources[$i]';";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$num_rows = pg_num_rows($resultset);

		$acc = 0;
		for($j=0;$j<$num_rows;$j++){
			$row = pg_fetch_object($resultset,$j);
			$acc = $acc + $row->price;
		}

		echo "<b>"  .$resources[$i] ."&nbsp;&nbsp;&nbsp;</b>" .($acc % $num_resources)
			."&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	}
//	include "../dbdisconnect.php";
	echo "</marquee></p>";
?>