<?
	/*		-------------  goodsdefaults.php -------------------------------
	 *		This file inserts the prices of the goods a customer is selling
	 *		which, in our implentation of the game, can be changed by the individual
	 */

	$resourcename = array("food", "water","fuel","gold","titanium","industrial","mining","farming");
	//echo	resourcename[0] ."<br>";

	//	prices
	$foodprice		= 5;	
	$waterprice		= 1;
	$fuelprice		= 10;
	$goldprice		= 50;
	$titaniumprice	= 20;
	$industryprice	= 200;
	$miningprice	= 400;
	$farmingprice	= 100;

	$price	= array($foodprice,$waterprice,$fuelprice,$goldprice,$titaniumprice,$industryprice,$miningprice,$farmingprice);
	
	// volumes
	$foodvol		= 5;	
	$watervol		= 10;
	$fuelvol		= 10;
	$goldvol		= 1;
	$titaniumvol	= 5;
	$industryvol	= 100;
	$miningvol		= 20;
	$farmingvol		= 50;

	$volume	= array($foodvol,$watervol,$fuelvol,$goldvol,$titaniumvol,$industryvol,$miningvol,$farmingvol);
	
	for ($i=0;$i<8;$i++)
	{
		$sql = "INSERT INTO goods (playernum,resourcename,price,volume,forsale)
				VALUES ('$playernum',
						'$resourcename[$i]',
						$price[$i],
						$volume[$i],
						0					);";
		$status = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	}

?>