#!/usr/bin/php
<html>
	<head>
		<script language='JavaScript' type='text/javascript'>
			<!--
			function uncheckAll(){
							top.frames['users'].document.users.elements[0].checked=false;
							for (i = 1; i < top.frames['users'].document.users.elements.length; i++)
								top.frames['users'].document.users.elements[i].checked = false ;
			}
			// -->
		</script>
	</head>
<body onLoad='uncheckAll();'>
<?
	include "dbconnect.cgi";
	include "preheader.cgi";

	if ($player_name){
		print "
			<table width=100%>
				<tr>
					<td class=leftalign><font size=4 color=#AADEE1><B>COMPOSE</B></font></td>
				</tr>
			</table>
		";

		$i=0;
        foreach($player_name as $key => $playername){
			if($playername != "all"){
				$sql		= "SELECT * FROM players WHERE playername = '$playername'";
                $resultset	= pg_query($connection, $sql) or die("Error in SQL : $sql" . pg_last_error($connection));
				
				$row[$i]	= pg_fetch_object($resultset,0);
				$i++;
			}
		}
		
		print"
			<FORM method=POST action=insertmessage.cgi>
				<TABLE>
					<TR>
						<TD class=leftalign>TO:</TD>
						<TD class=leftalign>
		";
							echo "<INPUT TYPE=hidden NAME=numdest VALUE=$i>";
							for($j=0; $j<$i ; $j++){
								$name[$j] = $row[$j]->playername;
								echo "<INPUT TYPE=hidden NAME=hidden$j VALUE=" .$name[$j] .">";
								echo "<B>" .$name[$j].";</B> ";
							}
		print"
						</TD>
					</TR>
					<TR>
						<TD class=leftalign>SUBJECT:</TD>
						<TD class=leftalign><INPUT TYPE=text size=50 NAME=subject></TD>
					</TR>
					<TR>
						<TD class=leftalign>MESSAGE:</TD>
						<TD class=leftalign><TEXTAREA COLS=50 ROWS=10 NAME=message WRAP=virtual></TEXTAREA></TD>
					</TR>
					<TR>
						<TD></TD>
						<TD><INPUT TYPE=submit VALUE=Send Message>&nbsp;&nbsp;&nbsp;<INPUT TYPE=reset VALUE=Clear Messsage></TD>
					</TR>
				</TABLE>
			</FORM>
		";
	}
	else{
		echo "Please choose recipient/s!!!";
		echo "<meta http-equiv=Refresh content=\"2 URL=sent.cgi\" >";
	}

	include "dbdisconnect.cgi";
?>
</body>
</html>