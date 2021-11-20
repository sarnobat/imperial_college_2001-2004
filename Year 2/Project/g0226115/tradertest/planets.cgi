#!/usr/bin/php
<?
	ob_start();

	include "header.cgi";
	include "dbconnect.cgi";
	
	echo "<h1>Universe Map</h1><h3>Locations of all planets. Click on any planet to travel to it.</h3>";

	$resourcename = array("food", "water","fuel","gold","titanium","industrial","mining","farming");
	$fuelconsumptionrate = 0.3;

	// Retreive your ship coordinates
	$ship_x = $_COOKIE["ship_x"];
	$ship_y = $_COOKIE["ship_y"];
	$ship_z = $_COOKIE["ship_z"];



	echo "<body>
			<DIV ID=sbWin STYLE=\"POSITION: absolute; Z-INDEX: 1\"></DIV>
			<SCRIPT LANGUAGE=JavaScript src=popup.js></SCRIPT>";


	for($i=0;$i<99;$i++){

		for($j=0;$j<99;$j++){
			$space[$i][$j] = "";
		}
	}

	$sql = "SELECT * FROM universe ORDER BY starnum;";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	// Iterate through the list of stars that exist in 'universe'
	for($i=0;$i<pg_num_rows($resultset);$i++)
	{
		$starnum = $i+1;
		$row = pg_fetch_object($resultset,$i);
	
		//Determine whether this planet has an owner
		$sql3 = "SELECT * FROM starstate WHERE starnum = $starnum;";
		$resultset3 = pg_query($connection, $sql3) or die("Error in query: $sql." .pg_last_error($connection));
		$row3 = pg_fetch_object($resultset3,0);
		$owner = $row3->owner;


		// Insert an image tag (or maybe a flash tag) into the corresponding array cell
		if ($owner == ""){
			// No one currently resides at this star
			$space [$row->x] [$row->y] = "<img src=images/inactive.gif width=5 height=5>";
		}
		else{
			//There is a player currently residing at this star

			//Determine the star's name
			$sql4 = "SELECT * FROM universe WHERE starnum = $starnum;";
			$resultset4 = pg_query($connection, $sql4) or die("Error in query: $sql." .pg_last_error($connection));
			$row4 = pg_fetch_object($resultset4,0);
			$starname = $row4->star;

			//Determine the planet type
			$sql2 = "SELECT * FROM starstate WHERE starnum = $starnum;";
			$resultset2 = pg_query($connection, $sql2) or die("Error in query: $sql." .pg_last_error($connection));
			$row2 = pg_fetch_object($resultset2,0);
			$planettype = $row2->civilisation;

			//Determine the owner's number
			$sql5 = "SELECT * FROM starstate WHERE starnum = $starnum;";
			$resultset5 = pg_query($connection, $sql5) or die("Error in query: $sql." .pg_last_error($connection));
			$row5 = pg_fetch_object($resultset5,0);
			$ownernum = $row5->owner;

			//Determine the owner's name
			$sql6 = "SELECT * FROM players WHERE playernum = $ownernum;";
			$resultset6 = pg_query($connection, $sql6) or die("Error in query: $sql." .pg_last_error($connection));
			$row6 = pg_fetch_object($resultset6,0);
			$owner = $row6->playername;

			/*//Determine the owner's resource counts
			$sql7 = "SELECT * FROM planets WHERE starnum = $starnum;";
			$resultset7 = pg_query($connection, $sql7) or die("Error in query: $sql." .pg_last_error($connection));
			$row7 = pg_fetch_object($resultset7,0);
			for($j=0;$j<8;$j++)
			{
					$resourcecount[$j] = $row7->$resourcename[$j];
			}*/

			for($j=0;$j<8;$j++){
				$sql7 = "SELECT * FROM goods WHERE playernum = $ownernum AND resourcename='" .$resourcename[$j] ."';";
				$resultset7 = pg_query($connection, $sql7) or die("Error in query: $sql." .pg_last_error($connection));
				$row7 = pg_fetch_object($resultset7,0);
				$resourcecount[$j] = $row7->forsale;
			}

			//Determine the distance of this planet from your ship
			//Retrieve the other person's co-ordinates
			$sql8 = "SELECT * FROM universe WHERE starnum = $starnum;";
			$resultset8 = pg_query($connection, $sql8) or die("Error in query: $sql." .pg_last_error($connection));
			$row8 = pg_fetch_object($resultset8,0);
			// Evaluate this player's distance from you
			$x = $row8->x;
			$y = $row8->y;
			$z = $row8->z;

			$distance = floor(sqrt (($x-$ship_x)*($x-$ship_x)+($y-$ship_y)*($y-$ship_y)+($z-$ship_z)*($z-$ship_z)));

			$popupcontent =	"<table class=popup><tr><td class=leftalign>DISTANCE FROM SHIP</td><td class=rightalign>$distance</td></tr><tr><td class=leftalign>STAR NAME</td><td class=rightalign>$starname</td><tr><td class=leftalign>OWNER NAME</td><td class=rightalign>$owner</td></tr><tr></tr>";
			for($j=0;$j<8;$j++){
				$popupcontent = $popupcontent ."<tr><td class=leftalign><font class=" .$resourcename[$j] .">" .$resourcename[$j] ."</td><td class=rightalign><font class=" .$resourcename[$j] .">" .(floor($resourcecount[$j])) ."</td></tr>";
			}
			$popupcontent = $popupcontent ."</table>";
			
			if ($distance > 0){
				$href = "moveship.cgi?x=$x&y=$y&z=$z";
			}
			else{
				$href = "#";
			}

				$pre = "<a	href = $href
							onmouseout =\"h(); return true;\" 
							onmouseover =\"wr('$popupcontent',168); return true;\"
							onclick = \"return confirm('Do you want to travel to the planet at $starname?')\"
							>";
				$post = "</a>";
			
			$space [$row->x] [$row->y] = $pre ."<img src=images/$planettype.gif width=35 height=35 border=0>" .$post;

			if($ownernum == $_COOKIE["TraderID"]){
				$space [$row->x] [$row->y] = $space [$row->x] [$row->y] ."<br>My home planet";
				$home_x = $row->x;
				$home_y = $row->y;
				$home_z = $row->z;
			}

//			echo "Planet co-ordinates: .$x .$y .$z <br>";
//			echo "Ship location: .$ship_x .$ship_y .$ship_z<br><br>";

			if($x == $ship_x && floor($y) == floor($ship_y)){
				$space [$row->x] [$row->y] =  "My ship's location<br>" .($space [$row->x] [$row->y]);
			}
			
		}

	}


	//print it all out

	echo "<table class=map border=0 cellpadding=0 width=100% height=50%>";
	for($i=0;$i<100;$i++){
		
		echo "<tr>";
		
		for($j=0;$j<100;$j++){
			echo "<td align=center valign=center >";
			echo $space[$i][$j];
			echo "</td>";
		}

		echo "</tr>\n\n";
	}
	echo "</table>";


/*	THESE DON'T WORK, SO I'VE SET THEM ABOVE IN A DIFFERENT WAY
	// Retreive your home planet coordinates
	$home_x = $_COOKIE["home_x"];
	$home_y = $_COOKIE["home_y"];
	$home_z = $_COOKIE["home_z"];
*/
	echo "<a href=moveship.cgi?x=$home_x&y=$home_y&z=$home_z><img src=images/return.gif border=0></a><br><br>
		Fuel Consumption rate: $fuelconsumptionrate litres per light year<br><br>The figures shown in the popups are the goods quantities for sale,
				but the total quantities contained on the planet.
				";

	include "footer.cgi";
	include "dbdisconnect.cgi";
	ob_end_flush();
?>