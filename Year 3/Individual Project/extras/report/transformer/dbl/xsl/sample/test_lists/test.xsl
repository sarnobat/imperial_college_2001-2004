<?xml version='1.0'?>
<!--############################################################################
|	$Id: test.xsl,v 1.1 2004/01/31 11:58:03 j-devenish Exp $
|- #########################################################= ##################
+ ############################################################################## -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

	<xsl:import href="../docbook.xsl"/>

	<!-- single-column! -->
	<xsl:param name="latex.documentclass.article">a4paper,12pt</xsl:param>
	<xsl:param name="latex.use.longtable">1</xsl:param>

</xsl:stylesheet>
