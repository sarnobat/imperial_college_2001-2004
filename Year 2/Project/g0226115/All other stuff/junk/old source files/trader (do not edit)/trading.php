<?
	include "header.php";
	include "dbconnect.php";
	include "include/prices.php";
	include "include/shiplocation.php";

	/*************************************************************************
	 *	Gets the user's requested display configuration						 *
	 *************************************************************************/?>
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




	/************************************************************************
	 *	Create a new SQL table which holds everything the user ultimately	*
	 *	wants to see														*
	 ************************************************************************/
	$sql = "CREATE TABLE TEMPtrading 
			AS (
				SELECT playernum,resourcename,playername,price,volume,forsale,playing
				FROM	(
							SELECT goods.playernum,playername,resourcename,price,volume,forsale,playing 
							FROM goods INNER JOIN players 
							ON players.playernum = goods.playernum) 
							AS table1 
						);
			ALTER TABLE TEMPtrading ADD COLUMN distance DECIMAL(3);";
	$resultsetmain =	pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	
	
	
	/*************************************************************************
	 *	The block of SQL simply creates a table called TEMPplayerdata from   *
	 *	which we later on determine any other player's distance, and insert  *
	 *	it in our TEMPtrading table											 *
	 *************************************************************************/
	$sql =
		"CREATE TABLE TEMPplayernumstarnum AS(SELECT starnum,owner FROM starstate);
		CREATE TABLE TEMPplayernumplayername AS (SELECT playernum,playername FROM players);
		CREATE TABLE TEMPplayernamestarnum AS (SELECT playernum,playername,starnum FROM TEMPplayernumplayername INNER JOIN 
		TEMPplayernumstarnum ON TEMPplayernumplayername.playernum = TEMPplayernumstarnum.owner);
		CREATE TABLE TEMPplayerdata AS (SELECT playernum,playername,universe.starnum,star,x,y,z FROM TEMPplayernamestarnum INNER JOIN universe ON universe.starnum = TEMPplayernamestarnum.starnum);
		DROP TABLE TEMPplayernumstarnum;
		DROP TABLE TEMPplayernumplayername;
		DROP TABLE TEMPplayernamestarnum;";

	$resultset = pg_query($connection,$sql);

	
	
	/*************************************************************************
	 *	Determine how many players there are								 *
	 *************************************************************************/
	$sql = "SELECT playername FROM players";
	$resultsetplayers =	pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$numplayers = pg_num_rows($resultsetplayers);
//	echo $numplayers;

	/*************************************************************************
	 *	For each player, determine its distance from you, and put it in		 *
	 *	the table TEMPtrading												 *
 	 *************************************************************************/
	/* Retreive your own coordinates */
	$myx = $_COOKIE["x"];
	$myy = $_COOKIE["y"];
	$myz = $_COOKIE["z"];
	for($i=0;$i<$numplayers;$i++){
		// For each player...
//			echo $i;
			// Determine this player's co-ordinates
			$row = pg_fetch_object($resultsetplayers,$i);
			$sql = "SELECT * FROM TEMPplayerdata WHERE playername = '$row->playername';";
//			echo "<br>" .$sql;
//			echo $sql ."<br>";
			$resultset = pg_query($connection,$sql);
			$row2 = pg_fetch_object($resultset,0);

			// Evaluate this player's distance from you
			$x = $row2->x;
			$y = $row2->y;
			$z = $row2->z;
			$distance = sqrt (($x-$myx)*($x-$myx)+($y-$myy)*($y-$myy)+($z-$myz)*($z-$myz));

			// Add it to our results table
			$sql = "UPDATE TEMPtrading SET distance = $distance WHERE playername = '$row->playername'; ";
			$resultset = pg_query($connection,$sql) or die("Error in query: $sql." .pg_last_error($connection));
	}



	/*************************************************************************
	 *	Display the data contained in TEMPtrading							 *
	 *																		 *
 	 *************************************************************************/
	$myname = $_COOKIE["TraderName"];
// PUT THE PROPER QUERY BACK IN LATER
//	$sql = "SELECT * FROM temptrading;";
		$sql = "SELECT * FROM temptrading WHERE playername != '$myname' ORDER BY $sort $type;";
	$resultset = pg_query($connection,$sql) or die("Error in query: $sql." .pg_last_error($connection));
		
	echo "<table>";
		echo "<tr>";
			echo "<td class=tableheading>&nbsp;</td>";
			echo "<td class=tableheading>Resource</td>";
			echo "<td class=tableheading>Player</td>";
			echo "<td class=tableheading>Planet Distance</td>";
			echo "<td class=tableheading>Value</td>";
			echo "<td class=tableheading>Volume</td>";
		echo "</tr>";
	for($i=0;$i<pg_num_rows($resultset);$i++)
	{
		$row = pg_fetch_object($resultset, $i);
	

		/* The next 2 if statements detect whether to put an empty row in */
		if($i>1 && $sort == "playername" && $lastplayer != $row->playername )
		{
			echo "<tr><td>&nbsp;</td></tr>";
		}
		if($i>1 && $sort == "resourcename" && $lastresource != $row->resourcename)
		{
			echo "<tr><td>&nbsp;</td></tr>";
		}

		echo "<tr>";
			echo "<td>P" //.<img src=images/" .$row->resourcename.".jpg width=30>
					."</td>";
			echo "<td>" .$row->resourcename ."</td>";
			echo "<td>" .$row->playername	."</td>";
			echo "<td>" .$row->distance	."</td>";
			echo "<td>" .$row->price		."</td>";
			echo "<td>" .$row->volume		."</td>";

			if ($starlordname == $row->playername && $row->playing == "t" ){//MAKE SURE THE PLAYER IS ACTUALLY PLAYING
				echo "<td>PLAYING</td>";
				echo "	<td>
							<form action=data/purchase.php method=post>
								Buy <input type = text name=qty size=2>
									<input type=hidden name=playername value=$row->playername><!-- <h2>$row->playernum</h2> -->
									<input type=hidden name=playernum value=$row->playernum>
									<input type=hidden name=volume value=$row->volume>
									<input type=hidden name=resourcename value=$row->resourcename> units
									<input type=submit value=Confirm>
							</form>
						</td>";
			}
			else
			{
				echo "<td>NOT PLAYING</td>";
			}
		echo "</tr>";

		$lastplayer = $row->playername;
		$lastresource = $row->resourcename;
	}
	echo "</table>";



	$sql =	"DROP TABLE TEMPplayerdata;".
			"DROP TABLE TEMPtrading;";
	$resultset = pg_query($connection,$sql);

	include "dbdisconnect.php";
	include "footer.php";


?>