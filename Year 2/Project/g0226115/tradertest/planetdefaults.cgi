
<?
	/*
	 *	Determine the planet civilisation type
	 */
	
	$sql = "SELECT civilisation FROM starstate WHERE starnum = '$starnumber';";
	$resultset2 = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_array($resultset2,NULL,PGSQL_ASSOC);
	$civilisationtype = $row['civilisation'];


	/*
	 *	Set planet defaults accordingly
	 */
	if($civilisationtype == "agriculture"){

		$sql2 = "INSERT INTO planets VALUES
			(	'$starnumber',
				'100',
				'50',
				'26',
				'10',
				'10',
				'2',
				'3',
				'10');";
	}
	else if($civilisationtype == "industrial"){

		$sql2 = "INSERT INTO planets VALUES
			(	'$starnumber',
				'50',
				'25',
				'30',
				'7',
				'8',
				'11',
				'5',
				'7');";
	}
	else if($civilisationtype == "mine"){

		$sql2 = "INSERT INTO planets VALUES
			(	'$starnumber',
				'40',
				'30',
				'30',
				'20',
				'15',
				'2',
				'12',
				'4');";
	}
	
	$resultset3 = pg_query($connection, $sql2) or die("Error in query: $sql2." .pg_last_error($connection));

?>