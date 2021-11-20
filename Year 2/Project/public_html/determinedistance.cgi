<?

	//This file will have to be INCLUDED, not INVOKED because otherwise there is no way of passing the result back

/*	THESE ARE NOT NEEDED; HOWEVER, MAKE SURE THE PARENT FILE SETS THESE 6 VARIABLES BEFORE THE INCLUSION

	$x_source = $_GET['x_source'];
	$y_source = $_GET['y_source'];
	$z_source = $_GET['z_source'];
	
	$x_dest = $_GET['x_dest'];
	$y_dest = $_GET['y_dest'];
	$z_dest = $_GET['z_dest'];
*/	
	$x_diff = ($x_source-$x_dest);
	$y_diff = ($y_source-$y_dest);
	$z_diff = ($z_source-$z_dest);
	$distance = sqrt ($x_diff*$x_diff + $y_diff*$y_diff + $z_diff*$z_diff);
?>