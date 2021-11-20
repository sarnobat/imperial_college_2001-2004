<?
	include "header.php";
	include "dbconnect.php";

	/*	Include the file which shows the average prices of the goods.
	 *	Yet to be implemented
	 *	Don't forget to make this optional */
	include "include/prices.php";

	/*	Temporary tables get created so that handling the results is easier. Therefore old results
	 *	must be deleted
	 */

?>
	<!-- The form for selecting your display options -->
	<form action=trading.php method=post>
		Show goods for sale, sorted by
		<select name=sortingfield> <!-- DON'T KNOW HOW TO SET A DEFAULT VALUE -->
			<option value=resourcename value=resourcename>Resource
			<option value=playername>Retailer name
			<option value=distance>Distance of retailer from my planet
			<option value=price>Price of goods
			<option value=volume>Volume
		</select>
		in
		<select name=sortingtype>
			<option value=ASC>ascending
			<option value=DESC>descending
		</select>
		order.
		<input type=submit>

	</form>


<?
	/*	Gets the user's requested display configuration	 */
	$sort = $_POST["sortingfield"];
	$type = $_POST["sortingtype"];
	/* If it's the user's first time, default the configuration to Resource Name, Ascending */
	if ($sort == null)
	{
		$sort = "resourcename";
	}
	if ($type == null)
	{
		$type = "ASC";
	}

//	echo "<br>Currently sorted by " .$sort;

	/* This is similar to what unltimately gets displayed */
	/* MORE THOUGHTFUL VERSION TAKES THIS ONE'S PLACE
							$sql = "SELECT * FROM ( SELECT * 
							FROM goods INNER JOIN players 
							ON players.playernum = goods.playernum) AS table1
							ORDER BY $sort $type;";*/
	$sql = "CREATE TABLE TEMPtrading AS (SELECT resourcename,playername,price,volume,forsale FROM (SELECT * FROM goods INNER JOIN players ON players.playernum = goods.playernum) AS table1 ORDER BY $sort $type); ALTER TABLE TEMPtrading ADD COLUMN distance DECIMAL(3);";
	$resultset =	pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	$sql = "SELECT * FROM TEMPtrading;";
	$resultsettrading =	pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	
	/* Find out how many entries there are so we know the loop control variables's limit */
	$num_goods = pg_num_rows($resultset);


	echo "<table>";
		echo "<tr>";
		echo "<td>Resource</td>";
		echo "<td>Player</td>";
		echo "<td>Planet Distance</td>";
		echo "<td>Value</td>";
		echo "<td>Volume</td>";
	echo "</tr>";

		/* The block of SQL simply creates a table called TEMPplayerdata from which we later on determine any other
		 * player's distance, and insert it in our TEMPtrading table
		 */
		$sql =
		"CREATE TABLE TEMPplayernumstarnum AS(SELECT starnum,owner FROM starstate);
		CREATE TABLE TEMPplayernumplayername AS (SELECT playernum,playername FROM players);
		CREATE TABLE TEMPplayernamestarnum AS (SELECT playernum,playername,starnum FROM TEMPplayernumplayername INNER JOIN 
		TEMPplayernumstarnum ON TEMPplayernumplayername.playernum = TEMPplayernumstarnum.owner);
		CREATE TABLE TEMPplayerdata AS (SELECT playernum,playername,universe.starnum,star,x,y,z FROM TEMPplayernamestarnum INNER JOIN universe ON universe.starnum = TEMPplayernamestarnum.starnum);";
//		echo $sql;
		$resultset = pg_query($connection,$sql);



		$sql = "SELECT playername FROM players";
		$resultsetallplayernames = pg_query($connection,$sql);
		$num_players = pg_num_rows($resultsetallplayernames);


	for($i=0;$i<2;$i++){    //<$num_players;$i++){
//			echo $i;
			// Get the co-ordinates of the current player so that we can determine its distance from you
			$sql = "SELECT * FROM TEMPplayerdata;";// WHERE table2.playername = '$row->playername';";
			$resultsettrading = pg_query($connection,$sql);
			$row2 = pg_fetch_object($resultset2,$i);
			$x = $row2->x;
			$y = $row2->y;
			$z = $row2->z;
// 			echo "Your coordinates: $x,$y,$z";
			$myx = $_COOKIE["x"];
			$myy = $_COOKIE["y"];
			$myz = $_COOKIE["z"];
//			echo "My Coordinates: " ."$myx,$myy,$myz";
			$distance = sqrt (($x-$myx)*($x-$myx)+($y-$myy)*($y-$myy)+($z-$myz)*($z-$myz));
//			echo "Distance is " .$distance;
			// Add the information to the SQL database
			$sql = "UPDATE TEMPtrading SET distance = $distance WHERE playername = '$row->playername'; ";
//			echo $sql;
			$resultset2 = pg_query($connection,$sql);
	}

	$myname = $_COOKIE["TraderName"];
			
	for($i=0;$i<$num_goods;$i++)
	{// live variables: $resultset,$row

			$lastplayer = $row->playername;
			$lastresource = $row->resourcename;

			$row = pg_fetch_object($resultset, $i);

			if($i>1 && $sort == "playername" && $lastplayer != $row->playername )
			{
				echo "<tr><td>&nbsp;</td></tr>";
			}

			if($i>1 && $sort == "resourcename" && $lastresource != $row->resourcename)
			{
				echo "<tr><td>&nbsp;</td></tr>";
			}

	
			
			if($row->playername != $myname)
			{
				echo "<tr>";
					echo "<td>" .$row->resourcename ."</td>";
					echo "<td>" .$row->playername	."</td>";
					echo "<td>" .round($distance,1)			."</td>";
					echo "<td>" .$row->price		."</td>";
					echo "<td>" .$row->volume		."</td>";
				echo "</tr>";
			}
	}
	echo "</table>";
	
	$sql = 
			"DROP TABLE TEMPplayernumstarnum;".
			"DROP TABLE TEMPplayernumplayername;".
			"DROP TABLE TEMPplayernamestarnum;".
			"DROP TABLE TEMPplayerdata;".
			"DROP TABLE TEMPtrading;";
	$resultset = pg_query($connection,$sql);



	include "dbdisconnect.php";
	include "footer.php";
?>