<?
	include "dbconnect.php";

	print "
		<B>PLAYERS</B><P>
	";

	$playernum	= $HTTP_COOKIE_VARS["TraderID"];

	$sql		= "SELECT playernum,playername,playing FROM players ORDER BY playing DESC;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	$rows		= pg_num_rows($resultset);
	
	print "
		<SCRIPT languange=javascript>
			<!--
			function checkAll(){
				for (i = 1; i < document.users.elements.length; i++)
				document.users.elements[i].checked = true ;
			}

			function uncheckAll(){
				document.users.elements[0].checked=false;
				for (i = 1; i < document.users.elements.length; i++)
				document.users.elements[i].checked = false ;
			}

			function checkfield(){
				if(!document.users.elements[0].checked)
					uncheckAll();
				else
					checkAll();
			}
			// -->
		</SCRIPT>
		<FORM name=users method=POST action=compose.php target=sent>
		<INPUT TYPE=checkbox NAME=player_name[] VALUE=all onClick=checkfield()>Select All Users<BR>
	";

	for($i=0; $i<$rows; $i++)
	{
		$row		= pg_fetch_object($resultset, $i);

		$playernum	= $row->playernum;
		$playername	= $row->playername;
		$playing	= $row->playing;

		if($playing == "t")
			echo "<INPUT TYPE=checkbox NAME=player_name[] VALUE=" .$playername ."><FONT color=blue><B>" .$playername ."</B></FONT><BR>";
		else
			echo "<INPUT TYPE=checkbox NAME=player_name[] VALUE=" .$playername ."><FONT color=red><B>" .$playername ."</B></FONT><BR>";
	}
	print "
		<BR><INPUT TYPE=submit VALUE=Compose></FORM>
		<P><a href=users.php>REFRESH</a>
	";

	include "dbdisconnect.php";
?>