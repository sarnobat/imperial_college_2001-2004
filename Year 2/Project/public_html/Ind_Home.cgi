<?
print "
	<FORM METHOD=POST ACTION=IndCreate.cgi>

	<br><br><h2>Manufaturing information</h2><table><tr><td><p align=left> An industrial planet can create industrial tools, as well as agricultural and mining tools,
	which it can trade with other planets <br> </font></p></td></tr></table><br>";

	/*************************************************************************
	****************** Code pasted from Frontpage for Industrial table *******
	**************************************************************************/
	print "
		<table class=data width=80%>
		  <tr class=tableheading>
			<td width=20%>Tool to create</td>
			<td width=33%>Materials needed to create new tool</td>
			<td width=33%>Existing tools needed to create new tool<BR>('Lifetime' of tool)</td>
		  </tr>
		  <tr>
			<td>
			  Farming Tool(x4)</p>
			</td>
			<td width=589>
			  Food </b>6 <b>Water</b>
			  3 <b>Fuel</b> 2 <b>Gold</b> 0 <b>Titanium</b>  2</p>
			</td>
			<td>
			  Industrial Tool</b> 0.3</p>
			</td>
		  </tr>
		  <tr>
			<td>
			  Industrial Tool(x2)</p>
			</td>
			<td width=589>
			  Food </b>4 <b>Water</b> 1 <b>Fuel</b>
			  4 <b>Gold</b> 1 <b>Titanium</b>  1</p>
			</td>
			<td>
			  Industrial Tool</b> 0.5</p>
			</td>
		  </tr>
		  <tr>
			<td>
			  Mining Tool(x3)</p>
			</td>
			<td width=589>
			  Food </b>5 <b>Water</b>
			  2 <b>Fuel</b> 3 <b>Gold</b> 1 <b>Titanium</b>  1</p>
			</td>
			<td>
			  Industrial Tool</b> 0.1</p>
			</td>
		  </tr>
		</table>
		";

	/*************************************************************************
	*********************** End of pasted table code *************************
	**************************************************************************/
	print "
	<p align=left> <I>Using your exisiting resouces, you can create the following tools</I> </font></p>	";

	/******************************************************************************************
	**** Functions to check which tools u can create using resources on home planet ***********
	**** $createAgrTool[$i], $createIndTool[$i], $createMinTool[$i] defined in gamehome.cgi ***
	*******************************************************************************************/

	function canTool($toolsReq, $notforsale){
	/* Can I create the relevant tool? */
		$can = True;
		for($i=0; $i<8; $i++){
			if($notforsale[$i] < $toolsReq[$i]){
				$can = False;
			}
		}
		return $can;
	}

	function printButtons($createAgrTool, $createIndTool, $createMinTool, $notforsale){
		print "
		<table border=0 width=50%>
		<TR>
			<TD> ";
				if(canTool($createAgrTool, $notforsale)){
					echo "<input type=submit name=Create value=\"Create Agricultural Tool\"> <br> <br>";
				}
				else {
					echo "<B>Insufficient</B> resources to create <B>Agricultural Tool</B> <br> <br>";
				}
		print "
			</TD>
		</TR>
		<TR>
			<TD> ";
				if(canTool($createIndTool, $notforsale)){
					echo "<input type=submit name=Create value=\"Create Industrial Tool\"> <br> <br>";
				}
				else {
					echo "<B>Insufficient</B> resources to create <B>Industrial Tool</B> <br> <br>";
				}
		print "
			</TD>
		</TR>
		<TR>
			<TD> ";
				if(canTool($createMinTool, $notforsale)){
					echo "<input type=submit name=Create value=\"Create Mining Tool\"> <br> <br>";
				}
				else {
					echo "<B>Insufficient</B> resources to create <B>Mining Tool</B>";
				}
		print "
			</TD>
		</TR>
		</TABLE> ";
	}
	
	printButtons($createAgrTool, $createIndTool, $createMinTool, $notforsale);

	echo "</FORM> ";
?>