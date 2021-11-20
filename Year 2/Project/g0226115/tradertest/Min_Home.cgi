<?
print "
	<FORM METHOD=POST ACTION=MinCreate.cgi>

	<br><br><br><h2>Manufaturing information</h2><table><tr><td><p align=left> On a <I><B>Mining planet</B></I>, you can extract: <br>
	* Titanium <br>
	* Fuel <br>
	* Gold</font></p></td></tr></table><br><br><br>";

	/*************************************************************************
	****************** Code pasted from Frontpage for Mining table *****
	**************************************************************************/
	print "
		<table class=data width=44%>
		  <tr class=tableheading class=tableheading>
			<td><b>Extract</b></td>
			<td><b>Units</b></td>
			<td>Tools needed</td>
			<td>Tool deterioration extent</td>
		  </tr>
		  <tr>
			<td> Titanium</td>
			<td>6</td>
			<td><b>Mining Tool</b></td>
			<td>0.3</td>
		  </tr>
          <tr>
			<td> Fuel</td>
			<td>35</td>
			<td><b>Mining Tool</b></td>
			<td>0.4</td>
          </tr>
		  <tr>
			<td> Gold</td>
			<td>2</td>
			<td><b>Mining Tool</b></td>
			<td>0.1</td>
		  </tr>
		</table> ";

	/*************************************************************************
	*********************** End of pasted table code *************************
	**************************************************************************/
	print "
	<br><br><br><table><tr><td><p align=left> <I>Using your exisiting resouces, you can extract:</I> </font></p></td></tr></table><br>";

	/*** DRAW BUTTONS ***/

	// $notforsale[6] = 'Mining Tool'

	if ($notforsale[6] >= 0.3){
			echo "<input type=submit name=Extract value=Titanium> <br> <br>";
	}
	else {
		echo "<B>Insufficient</B> resources to <B>Extract Titanium</B> <br> <br>";
	}

	if($notforsale[6] >= 0.4){
		echo "<input type=submit name=Extract value=Fuel> <br> <br>";
	}
	else {
		echo "<B>Insufficient</B> resources to <B>Extract Fuel</B> <br> <br>";
	}
	if($notforsale[6] >= 0.1){
		echo "<input type=submit name=Extract value=Gold> <br> <br>";
	}
	else {
		echo "<B>Insufficient</B> resources to <B>Extract Gold</B> <br> <br>";
	}

	echo "</FORM> ";
?>