<?xml version='1.0'?>
<!--############################################################################# 
|	$Id: article.xsl,v 1.2 2003/04/03 10:54:29 rcasellas Exp $
|- #############################################################################
|	$Author: rcasellas $												
|														
+ ############################################################################## -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

<xsl:import href="../../docbook.xsl"/>

<xsl:output method="text" encoding="ISO-8859-1" indent="yes"/>

<xsl:variable name="latex.override">
<xsl:text>\documentclass[pdftex,english,a4paper,10pt,twocolumn]{infocom}&#10;</xsl:text>
<xsl:text>\usepackage[pdftex,bookmarksnumbered,colorlinks,backref, bookmarks, breaklinks, linktocpage]{hyperref}&#10;</xsl:text>
<xsl:text>\usepackage[pdftex]{graphicx}&#10;</xsl:text>
<xsl:text>\usepackage{isolatin1}&#10;</xsl:text>
<xsl:text>\usepackage{anysize}&#10;</xsl:text>
<xsl:text>\usepackage{varioref}&#10;</xsl:text>
<xsl:text>\usepackage{latexsym}&#10;</xsl:text>
<xsl:text>\usepackage{enumerate}&#10;</xsl:text>
<xsl:text>\usepackage{float}&#10;</xsl:text>
<xsl:text>\usepackage{fancyvrb}&#10;</xsl:text>
<xsl:text>\usepackage{fancybox}&#10;</xsl:text>
<xsl:text>\usepackage{amsmath,amsthm, amsfonts, amssymb, amsxtra,amsopn}&#10;</xsl:text>
<xsl:text>\usepackage{rotating}&#10;</xsl:text>
<xsl:text>\usepackage{subfigure}&#10;</xsl:text>
<xsl:text>\usepackage{tabularx}&#10;</xsl:text>
<xsl:text>\usepackage{url}&#10;</xsl:text>
<xsl:text>\usepackage{times}&#10;</xsl:text>
<xsl:text>\pdfcompresslevel=9&#10;</xsl:text>
</xsl:variable>

</xsl:stylesheet>
