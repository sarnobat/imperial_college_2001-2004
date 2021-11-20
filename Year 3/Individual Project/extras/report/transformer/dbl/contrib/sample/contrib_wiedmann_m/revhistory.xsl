<?xml version='1.0'?>
<!--############################################################################
|	$Id: revhistory.xsl,v 1.1 2004/01/26 10:55:08 j-devenish Exp $
+ ############################################################################## -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

	<xsl:import href="../docbook.xsl"/>

	<xsl:import href="../../xsl/contrib_wiedmann_m/db2latex-mw-revhistory.xsl"/>
	<xsl:variable name="latex.mapping.xml" select="document('../../xsl/contrib_wiedmann_m/db2latex-mw-revhistory.xml')"/>

	<xsl:variable name="latex.use.hyperref">0</xsl:variable>

	<xsl:template match="articleinfo" mode="standalone.article">
		<xsl:apply-imports/>
		<xsl:apply-templates select="revhistory"/>
	</xsl:template>

</xsl:stylesheet>
