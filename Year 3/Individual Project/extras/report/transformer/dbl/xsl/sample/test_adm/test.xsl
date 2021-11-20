<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>
<!--############################################################################# 
 |	$Id: test.xsl,v 1.2 2003/04/25 08:38:06 j-devenish Exp $
 |- #############################################################################
 |	$Author: j-devenish $												
 |														
 + ############################################################################## -->

<xsl:import href="../../docbook.xsl"/>
<xsl:output method="text" encoding="ISO-8859-1" indent="yes"/>
<xsl:variable name="latex.use.babel">0</xsl:variable>
<xsl:variable name="latex.use.isolatin1">0</xsl:variable>
<xsl:variable name="latex.use.hyperref">0</xsl:variable>
<xsl:variable name="latex.use.fancyvrb">0</xsl:variable>
<xsl:variable name="latex.use.fancybox">1</xsl:variable>
<xsl:variable name="latex.use.fancyhdr">0</xsl:variable>
<xsl:variable name="latex.use.subfigure">1</xsl:variable>
<xsl:variable name="latex.use.rotating">1</xsl:variable>
<xsl:variable name="latex.use.makeidx">1</xsl:variable>
<xsl:variable name="latex.pdf.support">1</xsl:variable>
<xsl:variable name="latex.math.support">0</xsl:variable>
<xsl:variable name="latex.document.font">palatino</xsl:variable>
<xsl:variable name="latex.hyperref.param.pdftex">pdfstartview=FitH</xsl:variable>
<xsl:variable name="latex.admonition.path">../../figures</xsl:variable>

<xsl:variable name="latex.book.preamblestart">
<xsl:text>% --------------------------------------------	&#10;</xsl:text>
<xsl:text>% This is an example 					&#10;</xsl:text>
<xsl:text>% Adding a customization Layer			&#10;</xsl:text>
<xsl:text>% Autogenerated LaTeX file for books 			&#10;</xsl:text>
<xsl:text>% --------------------------------------------	&#10;</xsl:text>
<xsl:text>\documentclass[english,a4paper,10pt,final]{report}&#10;</xsl:text>
</xsl:variable>


<xsl:variable name="latex.article.preamblestart">
<xsl:text>% --------------------------------------------	&#10;</xsl:text>
<xsl:text>% This is an example 					&#10;</xsl:text>
<xsl:text>% Adding a customization Layer			&#10;</xsl:text>
<xsl:text>% Autogenerated LaTeX file for articles 		&#10;</xsl:text>
<xsl:text>% --------------------------------------------	&#10;</xsl:text>
<xsl:text>\documentclass[english,a4paper,10pt]{article}		&#10;</xsl:text>
</xsl:variable>


<xsl:variable name="latex.biblio.output">cited</xsl:variable>

</xsl:stylesheet>
