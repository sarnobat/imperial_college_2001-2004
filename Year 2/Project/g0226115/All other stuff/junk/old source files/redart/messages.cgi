#!/usr/bin/php
<script>this.focus();</script>

<?
	include "include/dbconnect.cgi";

	print "
		<FRAMESET cols=\"25%, 75%\">
		  <FRAME src=include/users.cgi noresize name=users>
		  <FRAMESET rows=\"50%,   %\">
			  <FRAME src=include/inbox.cgi name=inbox>
			  <FRAME src=include/sent.cgi name=sent>
		  </FRAMESET>
		</FRAMESET>
	";

	include "include/dbdisconnect.cgi";
?>