<?xml version='1.0'?>
<!--############################################################################# 
|	$Id: test.xsl,v 1.1 2003/03/23 11:40:02 j-devenish Exp $
|- #############################################################################
|	$Author: j-devenish $												
|														
+ ############################################################################## -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>
<xsl:import href="../../docbook.xsl"/>
<xsl:output method="text" encoding="ISO-8859-1" indent="yes"/>

<xsl:variable name="latex.use.hyperref">0</xsl:variable>

<xsl:variable name="latex.override">
<xsl:text>\documentclass[a4paper,10pt]{article}&#10;</xsl:text>
<xsl:text>\usepackage{parskip}&#10;</xsl:text>
<xsl:text>\pdfcompresslevel=9&#10;</xsl:text>
<xsl:text>\newcommand{\hypertarget}[2]{#2}&#10;</xsl:text>
</xsl:variable>

</xsl:stylesheet>
