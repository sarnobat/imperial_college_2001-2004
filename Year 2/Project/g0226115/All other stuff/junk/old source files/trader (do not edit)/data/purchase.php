<?
	include "../dbconnect.php";

	$resourcename = $_POST["resourcename"];
	$playername = $_POST["playername"];
	$sellernum= $_POST["playernum"];
	$qty = $_POST["qty"];
	$volumesingle = $_POST["volume"];
	
	$thisplayernum = $_COOKIE["TraderID"];
	


	
	
	// Determine how much money should change hands
	$sql = "SELECT price FROM goods WHERE playernum = $sellernum AND resourcename = '$resourcename';";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset,0);
	$price = $row->price;
	$subtotal = $price * $qty;

	// Determine if the buyer has this amount
	$sql = "SELECT balance FROM players WHERE playernum = $thisplayernum;";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$row = pg_fetch_object($resultset,0);
	$buyerbalance = $row->balance;
	
	// If not, inform the user of the error
	if ($buyerbalance < $price)
	{
			echo "You are " .($price-$buyerbalance) ." units short of money to complete this transaction.";
			echo "<i>Returning to trading page...</i>";
	}
	// Otherwise...
	else
	{
		// Determine your ship number
		$sql =	"SELECT shipnum FROM shipstate WHERE owner = $thisplayernum;";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$shipnumber = $row->shipnum;

		// Check there is enough space on your ship
		$sql = "SELECT freespace FROM shipstorage WHERE shipnum = $shipnumber;";
		$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row = pg_fetch_object($resultset,0);
		$freespace = $row->freespace;

		$totalvolume = $volumesingle * $qty;
		
		if($freespace < $totalvolume)
		{
			echo "There isn't enough free space on your ship.<br>
					You are trying to buy $totalvolume, but only have $freespace
					units of freespace in your ship";
		}
		else
		{

			// Increase the SPACESHIP'S resource quantity from the buyer and decrease its balance
			$sql = "UPDATE shipstorage SET freespace = $freespace - $totalvolume WHERE shipnum = $shipnumber;
					UPDATE players SET balance = $buyerbalance - $subtotal WHERE playernum = $thisplayernum;";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
			
			// Determine the star number of the seller
			$sql = "SELECT starnum FROM starstate WHERE owner = $sellernum;";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
			$row = pg_fetch_object($resultset,0);
			$sellerstarnum = $row->starnum;

			// Determine how much of the resource the seller currently has
			$sql = "SELECT $resourcename FROM planets WHERE starnum = $sellerstarnum;";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
			$row = pg_fetch_object($resultset,0);
			$currentresourcecount = $row->$resource;

			// Subtract the resource quantity from the owner's PLANET and increase its balance
			$sql = "UPDATE planets SET $resourcename = $currentresourcecount - $qty;";
			$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

			// Generate a breakdown of this to print to the user on screen
			echo "Success.<br>$qty of $resourcename transferred.";
			
		}
	
	}

	include "../dbdisconnect.php";

?>