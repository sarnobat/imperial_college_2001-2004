#!/usr/bin/php
<?
	include "dbconnect.cgi";
	include "header.cgi";

	$name = array("food","water","fuel","gold","titanium","industrial","mining","farming");

	echo "<div align=center><table><tr>";

	for($i=0; $i<8  $i++){
			print"	
				<td align=center><font class=$name[$i]>$name[$i]</font></td>
			";
	}

	echo "</tr><tr>";

	for($i=1; $i<9 ; $i++){
			print"	
				<td align=center><img src=images/marble" .$i .".gif height=30 width=50></td>
			";
	}

	echo "</tr></table></div>";

	include "dbdisconnect.cgi";
?>