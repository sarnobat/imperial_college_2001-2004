#!/usr/bin/php
<?
	include "include/dbconnect.cgi";

	print "
		<B>PLAYERS</B><P>
	";

	$playernum = $HTTP_COOKIE_VARS["TraderID"];

	$sql = "SELECT playernum,playername,playing FROM players ORDER BY playing DESC;";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	$rows = pg_num_rows($resultset);
	
	print "
		<SCRIPT languange=javascript src=include/checking.js></SCRIPT>
		<FORM name=users method=POST action=compose.cgi target=sent onSubmit=uncheckAll(all,document.users.playername)>
		<INPUT TYPE=checkbox NAME=all onClick=checkfield(this,document.users.playername)><BR>
	";
	for($i=0; $i<$rows; $i++)
	{
		$row = pg_fetch_object($resultset, $i);

		$playernum	= $row->playernum;
		$playername	= $row->playername;
		$playing	= $row->playing;

		if($playing == "t")
			echo "<INPUT TYPE=checkbox NAME=playername VALUE=" .$playername ."><FONT color=blue><B>" .$playername ."</B></FONT><BR>";
		else
			echo "<INPUT TYPE=checkbox NAME=playername VALUE=" .$playername ."><FONT color=red><B>" .$playername ."</B></FONT><BR>";
	}
	print "
		<BR><INPUT TYPE=submit VALUE=Compose></FORM>
		<P><a href=users.cgi>REFRESH</a>
	";

	include "include/dbdisconnect.cgi";
?>