#!/usr/bin/php
<body>
<?
	include "dbconnect.cgi";
	include "preheader.cgi";

	print "
		<div align=left><font size=4 color=#AADEE1><B>PLAYERS</B></font><P>
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
		<FORM name=users method=POST action=compose.cgi target=sent>
		<INPUT TYPE=checkbox NAME=player_name[] VALUE=all onClick=checkfield()>All Users<BR><BR>
	";

	for($i=0; $i<$rows; $i++)
	{
		$row		= pg_fetch_object($resultset, $i);

		$playernum	= $row->playernum;
		$playername	= $row->playername;
		$playing	= $row->playing;

		if($playing == "t")
			echo "<INPUT TYPE=checkbox NAME=player_name[] VALUE=" .$playername ."><FONT color=#33FFFF><B>" .$playername ."</B></FONT><BR>";
		else
			echo "<INPUT TYPE=checkbox NAME=player_name[] VALUE=" .$playername ."><FONT color=#FF99FF><B>" .$playername ."</B></FONT><BR>";
	}
	print "
		<BR><INPUT TYPE=submit VALUE=Compose></FORM>
		<P><a href=users.cgi><font color=white>REFRESH</a></P>
		<P><FONT color=#33FFFF>(online players)</font></P>
		<P><FONT color=#FF99FF>(offline players)</font></P>
	";

	echo "<meta http-equiv=Refresh content=\"30 URL=users.cgi\" >";

	include "dbdisconnect.cgi";
?>