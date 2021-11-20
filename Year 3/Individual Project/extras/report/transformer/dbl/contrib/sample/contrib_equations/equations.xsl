<?xml version='1.0'?>
<!--############################################################################# 
|	$Id: equations.xsl,v 1.1 2004/01/26 11:43:32 j-devenish Exp $
|- #############################################################################
|	$Author: j-devenish $
+ ############################################################################## -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>
	<!--
	<xsl:import href="http://db2latex.sourceforge.net/xsl/docbook.xsl"/>
	-->
	<xsl:import href="../docbook.xsl"/>

	<xsl:param name="latex.document.font" select="'courier,helvet,mathptm'"/>

	<xsl:param name="my.hyperref.param.pdftex.extra">,pdftitle={contrib-equations}</xsl:param>
	<xsl:param name="tex.math.in.alt"></xsl:param>
	<xsl:param name="latex.alt.is.preferred">0</xsl:param>

	<xsl:param name="latex.article.preamble.post">
		<xsl:text><![CDATA[
\makeatletter
\renewcommand\thesection{\Roman{section}}
\renewcommand\thesubsection{\thesection\alph{subsection}}
\renewcommand\@seccntformat[1]{\csname the#1\endcsname. }
%\def\fs@ruled\fs@plain
%\def\fs@boxed\fs@plain
\makeatother
\raggedbottom
		]]></xsl:text>
	</xsl:param>

</xsl:stylesheet>
