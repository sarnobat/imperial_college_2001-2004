#!/usr/bin/php
<body>
<?
	include "dbconnect.cgi";
	include "preheader.cgi";

	print "

		<table width=500px>
			<tr>
				<td class=leftalign><font size=4 color=#AADEE1><B>INBOX</B></font></td>
				<td class=rightalign><a href=inbox.cgi><font color=white>REFRESH</font></a></td>
			</tr>
		</table>
	";

	$playernum	= $HTTP_COOKIE_VARS["TraderID"];

	$sql		= "SELECT msgnum FROM mailbox WHERE dest = '$playernum' ORDER BY msgnum DESC;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	
	$rows		= pg_num_rows($resultset);

	for($i=0; $i<$rows; $i++)
	{
		$row	= pg_fetch_object($resultset, $i);

		$msgnum = $row->msgnum;

		$sql		= "SELECT * FROM messages WHERE msgnum = $msgnum;";
		$resultset2 = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

		$row2 = pg_fetch_object($resultset2, 0);
		$sender		= $row2->sender;
		$date		= $row2->date;
		$message	= $row2->message;
		$subject	= $row2->subject;

		$sql		= "SELECT playername FROM players WHERE playernum = $sender;";
		$resultset2 = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

		$row2		= pg_fetch_object($resultset2, 0);
		$sendername	= $row2->playername;

		echo "<TABLE border=2 width=500px>";
			echo "<TR><TD class=leftalign><B>From: </B>" .$sendername ."</TD><TD class=leftalign><B>Sent: </B>" .$date ."</TD></TR>";
			echo "<TR><TD class=leftalign colspan=2><B>Subject: </B>" .$subject ."</TD></TR>";
			echo "<TR><TD class=leftalign colspan=2><B>Message: </B>" .$message ."</TD></TR>";
		echo "</TABLE>";

		echo "<TABLE width=500px>";
			echo "<TR><TD class=rightalign><a href=inboxdelete.cgi?msgnum=" .$msgnum ."&url=inbox.cgi><font color=white>Delete</font></a></TD></TR>";
		echo "</TABLE><BR>";

	}

	echo "<meta http-equiv=Refresh content=\"30 URL=inbox.cgi\" >";


	include "dbdisconnect.cgi";
?>