<?
	function printFlashTag($page){
		echo "<object	classid=clsid:D27CDB6E-AE6D-11cf-96B8-444553540000
						codebase=http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0
						width=30
						height=30>
				<param name=movie value=flash/buttons/" .$page .".swf>
				<param name=quality value=high>
				<param name=WMODE value=transparent>
                <embed src=flash/buttons/" .$page ." quality=high pluginspage=http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash type=application/x-shockwave-flash
							width=30 height=30>
                      </embed></object>";
	}
?>
	<object	classid=clsid:D27CDB6E-AE6D-11cf-96B8-444553540000
						codebase=http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0
						width=120 height=120>
				<param name=movie value=flash/logo.swf>
				<param name=quality value=high>
				<param name=WMODE value=transparent>
                <embed src=flash/logo.swf quality=high pluginspage=http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash type=application/x-shockwave-flash
							width=100 height=100>
                      </embed></object>
<br>

<table>
<tr><td align=right><? printFlashTag("gamehome");?></td><td align=left>
<a href="gamehome.cgi">Game Homepage</a>
</td><tr>
<tr><td align=right><? printFlashTag("planets");?></td><td align=left>
<a href="planets.cgi">Universe Map</a>
</td><tr>
<tr><td align=right><? printFlashTag("home_planet");?></td><td align=left>
<a href="home_planet.cgi">Your Warehouse</a>
</td><tr>
<tr><td align=right><? printFlashTag("ship");?></td><td align=left>
<a href="ship.cgi">Your Ship</a>
</td><tr>
<tr><td align=right><? printFlashTag("trading");?></td><td align=left>
<a href="trading.cgi">Trade goods</a>
</td><tr>
<tr><td align=right><? printFlashTag("messages");?></td><td align=left>
<a href="#" onClick="window.open('messages.cgi', 'newWindow','scrollbars=0,resizable=1,height=800,width=750')">Messages</a>
</td><tr>

<tr><td><hr></td><td><hr></td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td align=right><? printFlashTag("scores");?></td><td align=left>
<a href="scores.cgi">Scoreboard</a>
</td><tr>
<tr><td align=right><? printFlashTag("playing");?></td><td align=left>
<a href="#" onclick="window.open('playing.cgi', 'newWindow','scrollbars=1,resizable=1,height=800,width=750')">How to Play</a>
</td><tr>
<tr><td align=right><? printFlashTag("logout");?></td><td align=left>
<a href="logout.cgi">Log Out</a>
</td><tr>
<tr><td><hr></td><td><hr></td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td align=right><? printFlashTag("delete");?></td><td align=left>
<a href="delete.cgi" onclick="return confirm('Are you sure you want to delete your account?\nThis action cannot be undone.')">Delete Account</a>
</td>
</tr>
</table>
<br>
<hr  class=menurule>

<? include "playerdetails.cgi"; ?>
