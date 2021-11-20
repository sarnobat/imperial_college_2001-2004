#!/usr/bin/php

<?
	include "dbconnect.cgi";
	include "style.html";

	print"
		<H1>The Game</H1>

		<TABLE>
			<TR>
				<TD>
				<DIV align=justify>
					You will join an interactive trading game where you are given a 'home' planet. 
					From your home planet you are able to trade goods with other players by putting 
					them on/off the intergalactic market and change their selling price. You are 
					also given the option of producing goods depending on the type of planet you own.
					
					<BR><BR>
					
					A spaceship is provided which you use to travel to other planets that 
					have been inhabited by other players. This allows you to purchase items from their 
					planet and load them onto your ship's cargo hold, which can later be unloaded 
					on your home planet.

					<BR><BR>

					Also provided is a communication system where you can communicate with other players and
					negotiate trade offers etc.

					<BR><BR>

					The winner of the game is the player with the largest balance.
				</DIV>
				</TD>
			</TR>
		</TABLE>
		
		<BR><BR>

		System requirements:
		
		<BR><BR>

					<LI>Enable Animations</LI>
					<LI>Enable Cookies</LI>
					<LI>Enable JavaScript</LI>
					<LI>Requires Flash Plugin</LI>
	";

	include "dbdisconnect.cgi";
?>