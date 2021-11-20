#!/usr/bin/php
<script language = Javascript src = validateform.js></script>

<?
	include "preheader.cgi";
	include "dbconnect.cgi";


	echo "<h1>Register</h1><h3>Choose your ship name and planet type</h3>";



	echo "<form action = registerplanet.cgi method=post onSubmit = \"return (validateShipName(this) && validateplanet(this));\">";
	echo	"Please choose a name for your Spaceship:<BR>
			<input type = text name=shipname><br><br>";

	$sql = "CREATE TABLE tempplanets AS (SELECT civilisation, star FROM 
				(SELECT * FROM starstate 
				INNER JOIN universe ON universe.starnum = starstate.starnum)
				AS table1 WHERE owner = null ORDER BY star);";
	$resultset = pg_query($connection,$sql) or die("Error in query: $sql." . pg_last_error($connection));

	$planettypes = array("agriculture","industrial","mine");
	
	echo "<table>
			<tr>
			<td valign=top><h2>Agricultural Planet</h2>
					can:	<br><br>
					<div class=planetcapabilities><li>Grow food				<br>
					<li>Collect water</div></td>
			<td valign=top><h2>Industrial Planet</h2>
					can create:<br><br>
					<div class=planetcapabilities><li>Industrial Tools</li>		<br>
					<li>Agricultural Tools</li>	<br>
					<li>Mining Tools</li>			<br> <br></td></div>
			<td valign=top><h2>Mining Planet</h2>
					can extract: <br><br>
					<div class=planetcapabilities><li>Titanium</li>				<br>
					<li>Fuel</li>					<br>
					<li>Gold</li></div>	</td>
					

			</tr>
			
			<tr>";
	for($i=0;$i<3;$i++){
		$sql = "SELECT * FROM tempplanets WHERE civilisation = '" .$planettypes[$i] ."';";
		$resultset = pg_query($connection,$sql) or die("Error in query: $sql." .pg_last_error($connection));
		$numstars = pg_num_rows($resultset);
		
		echo "<td class=planetlist>";
		for($j=0;$j<$numstars;$j++){
			$row = pg_fetch_object($resultset,$j);
			echo "<input type=radio name=star value=\"" .$row->star ."\">\n";
			echo $row->star;
			echo "<br>";
			echo "";
		}
		echo "</td>";
	}
	echo "</tr></table>";


	print "
	<BR>
	<BR><input type=submit value=\"Join Game\"></form>";

	// Embed flash object
	echo "<object	classid=clsid:D27CDB6E-AE6D-11cf-96B8-444553540000
						codebase=http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0
						width=170 height=170>
				<param name=movie value=flash/orbit.swf>
				<param name=quality value=high>
				<param name=WMODE value=transparent>
                <embed src=flash/orbit.swf quality=high pluginspage=http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash type=application/x-shockwave-flash
							width=170 height=170>
                      </embed></object>";

	$sql = "DROP TABLE tempplanets;";
	$resultset = pg_query($connection,$sql) or die("Error in query: $sql." . pg_last_error($connection));

	
	include "dbdisconnect.cgi";
	include "footer.cgi";
?>