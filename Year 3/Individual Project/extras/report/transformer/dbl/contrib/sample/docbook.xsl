<?xml version='1.0'?>
<!--############################################################################# 
 |	$Id: docbook.xsl,v 1.1 2004/01/26 11:12:40 j-devenish Exp $
 |- #############################################################################
 |	$Author: j-devenish $
 |	PURPOSE: This customisation layer defines some useful habits for all contrib
 |	sample files.
 + ############################################################################## -->
<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:doc="http://nwalsh.com/xsl/documentation/1.0"
	exclude-result-prefixes="doc" version='1.0'>

	<xsl:import href="../../xsl/sample/docbook.xsl"/>

	<xsl:param name="admon.graphics.path">../../xsl/figures</xsl:param>

</xsl:stylesheet>

