#!/usr/bin/php
<script>this.focus();</script>

<?
	include "dbconnect.cgi";

	print "

		<FRAMESET cols=\"25%, 75%\" border=0>
		  <FRAME src=users.cgi noresize name=users>
		  <FRAMESET rows=\"50%, 50%\" 
			  <FRAME src=inbox.cgi name=inbox>
			  <FRAME src=sent.cgi name=sent>
		  </FRAMESET>
		</FRAMESET>

	";
	
	include "dbdisconnect.cgi";
?>