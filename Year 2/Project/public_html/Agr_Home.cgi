<?
print "
	<FORM METHOD=POST ACTION=AgrCreate.cgi>

	<br><br><h2>Manufacturing information</h2><table><tr><td><p align=left> On an Agricultural planet, you can: <br>
	* Grow Food <br>
	* Collect Water <br> </font></p></td><tr></table><br><br><br>";

	/*************************************************************************
	****************** Code pasted from Frontpage for Agricultural table *****
	**************************************************************************/
	print "
		<table class=data width=60%>
		  <tr class=tableheading>
			<td><b>Action</b></td>
			<td><b>Units</b></td>
			<td>Tools needed</td><td>Tool deterioration extent</td>
		  </tr>
		  <tr>
			<td>Grow Food<td>30</td>
			<td><b>Farming Tool</b></td><td>0.2</td>
		  </tr>
		  <tr>
			<td>Collect water</td><td>25</td>
			<td><b>Farming Tool</b></td><td>0.25</td>
		  </tr>
		</table><br><br><br>";

	/*************************************************************************
	*********************** End of pasted table code *************************
	**************************************************************************/
	print "
	<table><tr><td><p align=left> <I>Using your exisiting resouces, you can:</I> </font></p></td></tr></table><br>";

	/*** DRAW BUTTONS ***/

	// $notforsale[7] = 'Farming Tool'

	if ($notforsale[7] >= 0.2){
			echo "<input type=submit name=Create value=\"Grow Food\"> <br> <br>";
	}
	else {
		echo "<B>Insufficient</B> resources to <B>Grow Food</B> <br> <br>";
	}

	if($notforsale[7] >= 0.25){
		echo "<input type=submit name=Create value=\"Collect Water\"> <br> <br>";
	}
	else {
		echo "<B>Insufficient</B> resources to <B>Collect Water</B> <br> <br>";
	}

	echo "</FORM> ";
?>