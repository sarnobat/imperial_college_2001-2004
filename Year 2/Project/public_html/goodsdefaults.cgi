
<?
	/*		-------------  goodsdefaults.cgi -------------------------------
	 *		This file inserts the prices of the goods a customer is selling
	 *		which, in our implentation of the game, can be changed by the individual
	 */

	$resourcename = array("food", "water","fuel","gold","titanium","industrial","mining","farming");
	//echo	resourcename[0] ."<br>";

	//	prices
	$foodprice		= 15;	
	$waterprice		= 10;
	$fuelprice		= 20;
	$goldprice		= 40;
	$titaniumprice	= 25;
	$industryprice	= 115;
	$miningprice	= 75;
	$farmingprice	= 30;

	$price	= array($foodprice,$waterprice,$fuelprice,$goldprice,$titaniumprice,$industryprice,$miningprice,$farmingprice);
	
	// volumes
	$foodvol		= 10;	
	$watervol		= 20;
	$fuelvol		= 20;
	$goldvol		= 4;
	$titaniumvol	= 8;
	$industryvol	= 100;
	$miningvol		= 30;
	$farmingvol		= 50;

	$volume	= array($foodvol,$watervol,$fuelvol,$goldvol,$titaniumvol,$industryvol,$miningvol,$farmingvol);

	for ($i=0;$i<8;$i++){
		$sql = "SELECT $resourcename[$i] FROM planets WHERE starnum=$starnumber";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$Qty[$i] = $row->$resourcename[$i];

		$sql = "INSERT INTO goods VALUES ('$playernum', '$resourcename[$i]', $price[$i], $volume[$i],0,$Qty[$i]);";
		$status = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	}
?>