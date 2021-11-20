<?xml version='1.0'?>
<!--############################################################################# 
 |	$Id: test.xsl,v 1.1 2004/01/09 12:37:30 j-devenish Exp $
 |- #############################################################################
 |	$Author: j-devenish $												
 + ############################################################################## -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

	<xsl:import href="../docbook.xsl"/>

	<xsl:param name="latex.document.font">mathptmx,courier</xsl:param>
	<xsl:param name="latex.documentclass.article">a4paper,10pt</xsl:param>
	<xsl:param name="my.hyperref.param.pdftex.extra">,pdftitle={test-callout}</xsl:param>

	<xsl:template match="highlights">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="articleinfo/legalnotice/*[position()=last()]">
		<xsl:apply-imports/>
		<xsl:text>\par\hrulefill\par&#10;</xsl:text>
	</xsl:template>

	<xsl:template match="para[local-name(preceding-sibling::*[1])='mediaobjectco']">
		<xsl:text>\noindent </xsl:text>
		<xsl:apply-imports/>
	</xsl:template>

	<!-- make the calloutlist as an unnumbered subsection -->
	<xsl:param name="latex.list.title.style">\subsection*</xsl:param>

	<!-- make symbols instead of numbers -->
	<xsl:template match="callout" mode="generate.callout.areasymbol">
		<xsl:param name="callout"/>
		<xsl:variable name="position">
			<xsl:number count="callout" format="1"/>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$position=1">
				<xsl:text>\textcircled{$\diamond$}</xsl:text>
			</xsl:when>
			<xsl:when test="$position=2">
				<xsl:text>\textcircled{$\bullet$}</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$position"/>
				<xsl:text>.</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- print "Line 4" instead of just "4"	-->
	<xsl:template match="area" mode="generate.arearef.linerange">
		<xsl:variable name="string">
			<xsl:apply-imports/>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="contains($string, '--')">
				<xsl:text>Lines </xsl:text>
				<xsl:value-of select="$string"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>Line </xsl:text>
				<xsl:value-of select="$string"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- draw the symbols larger on the image than in the calloutlist -->
	<xsl:template match="area" mode="generate.areamark.calspair">
		<xsl:text>\Huge </xsl:text>
		<xsl:apply-imports/>
	</xsl:template>

</xsl:stylesheet>
