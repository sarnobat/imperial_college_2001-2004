<?xml version='1.0'?>
<!--############################################################################# 
 |	$Id: vimxml.xsl,v 1.3 2004/01/11 09:05:26 j-devenish Exp $
 |- #############################################################################
 |	$Author: j-devenish $
 + ############################################################################## -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:exsl="http://exslt.org/common"
				version='1.0'>

	<xsl:import href="../docbook.xsl"/>

	<!-- suppress the document's HTMLOnly elements -->
	<xsl:template match="*[@role='HTMLOnly']"/>

	<!-- hide the inner-side portion of each header  -->
	<xsl:param name="latex.fancyhdr.truncation.style">io</xsl:param>
	<xsl:param name="latex.fancyhdr.truncation.partition">0</xsl:param>

	<!-- to make the document suitable for printing, we do not colour
	hyperlinks: instead, they are shown with an on-screen border -->
	<xsl:param name="my.hyperref.param.pdftex.extra">,pdftitle={test-pinkjuice},colorlinks=false,pdfborder=0 0 0</xsl:param>

	<!-- document lacks a lang/xml:lang attribute -->
	<xsl:param name="l10n.gentext.default.language" select="'en'"/>

	<!-- this is only needed because of our ../docbook.xsl -->
	<xsl:param name="latex.document.font">default</xsl:param>

	<!-- this is only needed because of our ../docbook.xsl -->
	<xsl:param name="latex.use.parskip">1</xsl:param>

	<!-- LaTeX things for parity with the FO version -->
	<xsl:param name="latex.titlepage.file">titlepage</xsl:param>
	<xsl:param name="latex.book.preamble.post">
		<xsl:text>\renewcommand{\rmdefault}{phv}&#10;</xsl:text>
		<!-- draw in commands from ../docbook.xsl -->
		<xsl:value-of select="$my.common.preamble"/>
	</xsl:param>
	<xsl:param name="ulink.show">0</xsl:param>
	<xsl:param name="ulink.footnotes">1</xsl:param>
	<xsl:param name="latex.graphics.formats">PNG,PDF</xsl:param>

	<!-- file paths for db2latex/xsl/sample/test_pinkjuice/pics -->
	<xsl:template match="imagedata">
		<xsl:param name="is.imageobjectco" select="false()"/>
		<xsl:call-template name="imagedata">
			<xsl:with-param name="is.imageobjectco" select="$is.imageobjectco"/>
			<xsl:with-param name="filename">
				<xsl:call-template name="string-replace">
					<xsl:with-param name="from" select="'../pics/'"/>
					<xsl:with-param name="to" select="'pics/'"/>
					<xsl:with-param name="string" select="@fileref"/>
				</xsl:call-template>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- For parity with the FO version -->
	<xsl:template match="keysym|keycombo[@action='simul']">
		<xsl:text>[</xsl:text>
		<xsl:apply-imports/>
		<xsl:text>]</xsl:text>
	</xsl:template>

	<!-- generate some handy index links -->
	<xsl:template match="command">
		<!-- this is a pretty crazy method, but it works :) -->
		<xsl:apply-imports/>
		<xsl:variable name="indexterm">
			<indexterm>
				<primary>commands</primary>
				<secondary><xsl:value-of select="."/></secondary>
			</indexterm>
		</xsl:variable>
		<xsl:apply-templates select="exsl:node-set($indexterm)/*"/>
	</xsl:template>

</xsl:stylesheet>
