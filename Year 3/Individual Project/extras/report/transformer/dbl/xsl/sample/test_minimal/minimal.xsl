<?xml version='1.0'?>
<!--############################################################################# 
|	$Id: minimal.xsl,v 1.2 2003/04/21 09:36:22 j-devenish Exp $
|- #############################################################################
|	$Author: j-devenish $												
|														
+ ############################################################################## -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>
<xsl:import href="../../docbook.xsl"/>
<xsl:output method="text" encoding="ISO-8859-1" indent="yes"/>

<xsl:variable name="latex.use.hyperref">0</xsl:variable>

<xsl:variable name="latex.override">
<xsl:text>\documentclass[a4paper,12pt]{article}&#10;</xsl:text>
<xsl:text>\pdfcompresslevel=9&#10;</xsl:text>
</xsl:variable>

</xsl:stylesheet>
