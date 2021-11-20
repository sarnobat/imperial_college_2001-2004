<?
	include "dbconnect.php";

	print "
		<table width=100%>
			<tr>
				<td align=left><B>INBOX</B></td>
				<td align=right><a href=inbox.php>REFRESH</a></td>
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
			echo "<TR><TD><B>From: </B>" .$sendername ."</TD><TD><B>Sent: </B>" .$date ."</TD></TR>";
			echo "<TR><TD colspan=2><B>Subject: </B>" .$subject ."</TD></TR>";
			echo "<TR><TD colspan=2><B>Message: </B>" .$message ."</TD></TR>";
		echo "</TABLE>";

		echo "<TABLE width=500px>";
			echo "<TR><TD align=right><a href=data/inboxdelete.php?msgnum=" .$msgnum ."&url=inbox.php>Delete</a></TD></TR>";
		echo "</TABLE><BR>";

	}

	include "dbdisconnect.php";
?>