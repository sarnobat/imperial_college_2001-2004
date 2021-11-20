<?xml version='1.0'?>
<!--############################################################################# 
 |	$Id: test.xsl,v 1.2 2003/03/24 11:12:53 j-devenish Exp $
 |- #############################################################################
 |	$Author: j-devenish $												
 |														
 + ############################################################################## -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

<xsl:import href="../../docbook.xsl"/>
<xsl:output method="text" encoding="ISO-8859-1" indent="yes"/>

<xsl:variable name="latex.use.babel">0</xsl:variable>
<xsl:variable name="latex.use.isolatin1">0</xsl:variable>
<xsl:variable name="latex.use.hyperref">0</xsl:variable>
<xsl:variable name="latex.use.fancyhdr">0</xsl:variable>
<xsl:variable name="latex.use.subfigure">0</xsl:variable>
<xsl:variable name="latex.fancyvrb.tabsize">4</xsl:variable>

</xsl:stylesheet>
