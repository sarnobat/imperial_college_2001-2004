<?xml version="1.0" encoding="utf-8"?>
<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"> 
	<xsl:template match="report/title">
		<h1>
			<xsl:value-of select="text()"/>
		</h1>
	</xsl:tem ate>
	<xsl:template match="report/chapter/ch_title">
		<h2>
			<xsl:value-of select="text()"/>
		</h2>
	</xsl:template>
	<xsl:template match="report/chapter/text/list">
<ol><li>
<xsl:apply-templates/></li></ol>
</xsl:template>
</xsl:transform>
