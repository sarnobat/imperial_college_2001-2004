<?xml version="1.0" encoding="utf-8"?>
<!--<?xml version="1.0" encoding="ISO-8859-1"?>-->

<html xsl:version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/TR/xhtml1/strict">
<h1><xsl:value-of select="report/title"/></h1>

<xsl:value-of select="report/name"/>
<br/>
<xsl:value-of select="report/email"/>
<br/>
Supervised by: <xsl:value-of select="report/supervisor"/>
<br/>
<xsl:value-of select="report/university"/>
<br/>
<br/>
<xsl:value-of select="report/date"/>
<br/>
<br/>




<b>ABSTRACT</b>
<br/>
<xsl:value-of select="report/abstract"/>
<ol>
<xsl:for-each select="report/chapter">
<li><h2><xsl:value-of select="ch_title"/></h2>
<xsl:value-of select="text">
</xsl:value-of>

<!--<xsl:template match="text/list"><b><xsl:apply-templates/></b></xsl:template>-->
</li>
<br/><br/>
</xsl:for-each>
</ol>
</html>


