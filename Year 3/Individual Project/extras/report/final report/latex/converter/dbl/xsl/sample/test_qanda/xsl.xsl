<?xml version='1.0'?>
<!--############################################################################# 
|	$Id: xsl.xsl,v 1.3 2003/03/24 11:48:58 j-devenish Exp $
|- #############################################################################
|	$Author: j-devenish $												
|														
+ ############################################################################## -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>
<xsl:import href="../../docbook.xsl"/>
<xsl:output method="text" encoding="ISO-8859-1" indent="yes"/>

<xsl:variable name="latex.override">
<xsl:text>\documentclass[pdftex,english,a4paper,10pt]{article}&#10;</xsl:text>
<xsl:text>\usepackage[pdftex,bookmarksnumbered,colorlinks,backref, bookmarks, breaklinks, linktocpage]{hyperref}&#10;</xsl:text>
<xsl:text>\usepackage[pdftex]{graphicx}&#10;</xsl:text>
<xsl:text>\usepackage{isolatin1}&#10;</xsl:text>
<xsl:text>\usepackage{float}    %Required for QandADiv&#10;</xsl:text>
<xsl:text>\usepackage{fancyvrb} %Required for ProgramListing&#10;</xsl:text>
<xsl:text>\pdfcompresslevel=9&#10;</xsl:text>
</xsl:variable>

</xsl:stylesheet>
