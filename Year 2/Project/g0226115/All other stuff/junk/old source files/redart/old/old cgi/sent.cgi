<?
	include "include/dbconnect.cgi";

	print "
		<table width=100%>
			<tr>
				<td align=left><B>SENT</B></td>
				<td align=right><a href=sent.cgi>REFRESH</a></td>
			</tr>
		</table>
	";

	$playernum = $HTTP_COOKIE_VARS["TraderID"];

	$sql = "SELECT * FROM messages WHERE sender = '$playernum' ORDER BY msgnum DESC;";
	$resultset = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	
	$rows = pg_num_rows($resultset);

	for($i=0; $i<$rows; $i++)
	{
		$row = pg_fetch_object($resultset, $i);

		$msgnum		= $row->msgnum;
		$date		= $row->date;
		$message	= $row->message;
		$subject	= $row->subject;

		$sql		= "SELECT * FROM mailbox WHERE msgnum = $msgnum;";
		$resultset2 = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row2		= pg_fetch_object($resultset2, 0);
		$dest		= $row2->dest;

		$sql		= "SELECT playername FROM players WHERE playernum = $dest;";
		$resultset2 = pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
		$row2		= pg_fetch_object($resultset2, 0);
		$destname	= $row2->playername;

		echo "<TABLE border=2 width=500px>";
			echo "<TR><TD><B>To: </B>" .$destname ."</TD><TD><B>Sent: </B>" .$date ."</TD></TR>";
			echo "<TR><TD colspan=2><B>Subject: </B>" .$subject ."</TD></TR>";
			echo "<TR><TD colspan=2><B>Message: </B>" .$message ."</TD></TR>";
		echo "</TABLE>";

		echo "<TABLE width=500px>";
			echo "<TR><TD align=right><a href=data/deletemsg.cgi?msgnum=" .$msgnum ."&url=sent.cgi>Delete</a></TD></TR>";
		echo "</TABLE><BR>";

	}

	include "dbdisconnect.cgi";
?>