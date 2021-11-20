#!/usr/bin/php
<?
	include "dbconnect.cgi";
	include "preheader.cgi";

	$playernum	= $HTTP_COOKIE_VARS["TraderID"];
	$starnum	= $HTTP_COOKIE_VARS["starID"];
	$shipnum	= $playernum;

	$sql		= "DELETE FROM goods WHERE playernum = $playernum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	
	$sql		= "DELETE FROM mailbox WHERE dest = $playernum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));
	
	$sql		= "DELETE FROM messages WHERE sender = $playernum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	$sql		= "DELETE FROM planets WHERE starnum = $starnum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	$sql		= "DELETE FROM players WHERE playernum = $playernum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	$sql		= "DELETE FROM ships WHERE shipnum = $shipnum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	$sql		= "DELETE FROM shipstate WHERE shipnum = $shipnum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	$sql		= "DELETE FROM shipstorage WHERE shipnum = $shipnum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	$sql		= "UPDATE starstate SET owner=NULL WHERE starnum = $starnum;";
	$resultset	= pg_query($connection, $sql) or die("Error in query: $sql." .pg_last_error($connection));

	print"<I>Thank You For Playing <font color=red><B>SOLAR IMPACT</font></I></b><br>";

	echo "<br><object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0\" width=550 height=400>
			<param name=movie value=flash/delete.swf>
			<param name=quality value=high>
			<param name=WMODE value=transparent>
			<embed src=flash/delete.swf quality=high pluginspage=http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash type=application/x-shockwave-flash width=550 height=400>
			</embed></object>";	

	echo "<meta http-equiv=Refresh content=\"6; URL= index.cgi \">"; // Do not reduce the time here

	include "footer.cgi";
	include "dbdisconnect.cgi";
?>