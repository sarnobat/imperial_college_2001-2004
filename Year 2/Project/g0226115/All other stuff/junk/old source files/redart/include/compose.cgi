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

	if ($player_name){
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
						<TD>TO:</TD>
						<TD>
		";
							echo "<INPUT TYPE=hidden NAME=numdest VALUE=$i>";
							for($j=0; $j<$i ; $j++){
								$name[$j] = $row[$j]->playername;
								echo "<INPUT TYPE=hidden NAME=hidden$j VALUE=" .$name[$j] .">";
								echo $name[$j]."; ";
							}
		print"
						</TD>
					</TR>
					<TR>
						<TD>SUBJECT:</TD>
						<TD><INPUT TYPE=text size=50 NAME=subject></TD>
					</TR>
					<TR>
						<TD>MESSAGE:</TD>
						<TD><TEXTAREA COLS=50 ROWS=10 NAME=message WRAP=virtual></TEXTAREA></TD>
					</TR>
					<TR>
						<TD><INPUT TYPE=submit VALUE=Send Message></TD>
						<TD><INPUT TYPE=reset VALUE=Clear Messsage></TD>
					</TR>
				</TABLE>
			</FORM>
		";
	} else echo "Please choose recipient/s!!!";

	include "dbdisconnect.cgi";
?>
</body>
</html>