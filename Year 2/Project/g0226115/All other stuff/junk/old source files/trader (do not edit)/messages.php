<script>this.focus();</script>

<?
	include "dbconnect.php";

	print "
		<FRAMESET cols=\"25%, 75%\">
		  <FRAME src=users.php noresize name=users>
		  <FRAMESET rows=\"50%, 50%\">
			  <FRAME src=inbox.ph name=inbox>
			  <FRAME src=sent.php name=sent>
		  </FRAMESET>
		</FRAMESET>
	";

	include "dbdisconnect.php";
?>