<?xml version='1.0'?>
<!--############################################################################# 
|	$Id: article.xsl,v 1.3 2003/04/03 10:54:31 rcasellas Exp $
|- #############################################################################
|	$Author: rcasellas $												
|														
+ ############################################################################## -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

<xsl:import href="../../docbook.xsl"/>

<xsl:output method="text" encoding="ISO-8859-1" indent="yes"/>

<xsl:variable name="latex.override">
<xsl:text>\documentclass[pdftex,english,a4paper,10pt]{infocom}&#10;</xsl:text>
<xsl:text>\usepackage[pdftex,bookmarksnumbered,colorlinks,backref, bookmarks, breaklinks, linktocpage]{hyperref}&#10;</xsl:text>
<xsl:text>\usepackage[pdftex]{graphicx}&#10;</xsl:text>
<xsl:text>\usepackage{isolatin1}&#10;</xsl:text>
<xsl:text>\usepackage{fancyvrb}&#10;</xsl:text>
<xsl:text>\pdfcompresslevel=9&#10;</xsl:text>
</xsl:variable>

<xsl:variable name="latex.use.subfigure">0</xsl:variable>

</xsl:stylesheet>
