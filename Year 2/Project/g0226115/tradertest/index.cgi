#!/usr/bin/php
<? include "preheader.cgi"; ?>
<TABLE>
<tr>
<TD>
<h1>Welcome To </h1>
</TD>
<td>
	<object	classid=clsid:D27CDB6E-AE6D-11cf-96B8-444553540000
						codebase=http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0
						width=120 height=120>
				<param name=movie value=flash/logo.swf>
				<param name=quality value=high>
				<param name=WMODE value=transparent>
                <embed src=flash/logo.swf quality=high pluginspage=http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash type=application/x-shockwave-flash
							width=100 height=100>
                      </embed></object>
</td>
</tr>
</table>
<a href="#" onClick="window.open('gamedesc.cgi', 'newWindow','scrollbars=0,resizable=1,height=650,width=250')">Game Description</a>

<?
	include "login.html";
	include "footer.cgi";
?>
