<?xml version="1.0"?>
<!--############################################################################# 
|	$Id: qandaset.mod.xsl,v 1.6 2003/04/07 10:14:22 rcasellas Exp $
|- #############################################################################
|	$Author: rcasellas $
|														
|   PURPOSE:
|   Portions (c) Norman Walsh, official DocBook XSL stylesheets.
|                See docbook.sf.net
+ ############################################################################## -->

<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:doc="http://nwalsh.com/xsl/documentation/1.0" 
	exclude-result-prefixes="doc" version="1.0">


<!--############################################################################# -->
<!-- DOCUMENTATION                                                                -->
  <doc:reference xmlns="" id="qandaset">
    <referenceinfo>
      <releaseinfo role="meta"> $Id: qandaset.mod.xsl,v 1.6 2003/04/07 10:14:22 rcasellas Exp $ </releaseinfo>
	<authorgroup>
      	<author> <firstname>Ramon</firstname> <surname>Casellas</surname> </author>
      	<author> <firstname>James</firstname> <surname>Devenish</surname> </author>
	</authorgroup>
      <copyright>
        <year>2000</year> <year>2001</year> <year>2002</year> <year>2003</year>
        <holder>Ramon Casellas</holder>
      </copyright>
    </referenceinfo>
    <title>QandaSet <filename>qandaset.mod.xsl</filename></title>
    <partintro>
      <section>
        <title>Introduction</title>
      </section>
    </partintro>
  </doc:reference>




<!--############################################################################# 
|   qandaset
|-  #############################################################################
|														
+   ############################################################################# -->

<xsl:template match="qandaset">
		<!-- get all children that are not the following -->
		<xsl:variable name="preamble" select="*[name(.) != 'title'
									  and name(.) != 'titleabbrev'
									  and name(.) != 'qandadiv'
									  and name(.) != 'qandaentry']"/>
		<xsl:variable name="label-width"/>
		<xsl:variable name="table-summary"/>
		<xsl:variable name="cellpadding"/>
		<xsl:variable name="cellspacing"/>
		<xsl:variable name="toc"/>
		<xsl:variable name="toc.params"/>
  		<xsl:variable name="qalevel">
    		<xsl:call-template name="qanda.section.level"/>
  		</xsl:variable>
		<xsl:text>% -------------------------------------------------------------&#10;</xsl:text>
		<xsl:text>% QandASet                                                     &#10;</xsl:text>
		<xsl:text>% -------------------------------------------------------------&#10;</xsl:text>
		<xsl:choose>
      		<xsl:when test="ancestor::sect2">
	    		<xsl:text>\subsubsection*{</xsl:text>
			</xsl:when>
      		<xsl:when test="ancestor::sect1">
	    		<xsl:text>\subsection*{</xsl:text>
			</xsl:when>
      		<xsl:when test="ancestor::article | ancestor::appendix">
	    		<xsl:text>\section*{</xsl:text>
			</xsl:when>
      		<xsl:when test="ancestor::book">
	    		<xsl:text>\chapter*{</xsl:text>
			</xsl:when>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="title">
				<xsl:apply-templates select="title"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>F.A.Q.</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text>}&#10;</xsl:text>
	    <xsl:call-template name="label.id"/>
		<xsl:text>&#10;</xsl:text>

		<!-- process toc -->
		<xsl:if test="contains($toc.params, 'toc') and $toc != '0'">
			<xsl:call-template name="process.qanda.toc"/>
		</xsl:if>
		<!-- process preamble -->
		<xsl:apply-templates select="$preamble"/>
		<!-- process divs and entries -->
		<xsl:apply-templates select="qandaentry|qandadiv"/>
</xsl:template>


<xsl:template match="qandaset/title">
<xsl:apply-templates/>
</xsl:template>




<!--############################################################################# 
|   qandadiv
|-  #############################################################################
|														
+   ############################################################################# -->


<xsl:template match="qandadiv">
	<!-- get the preamble -->
  <xsl:variable name="preamble" select="*[name(.) != 'title'
                                          and name(.) != 'titleabbrev'
                                          and name(.) != 'qandadiv'
                                          and name(.) != 'qandaentry']"/>
  <xsl:variable name="qalevel">
    <xsl:call-template name="qandadiv.section.level"/>
  </xsl:variable>
	<!-- process the title if it exists -->
		<xsl:text>% -----------&#10;</xsl:text>
		<xsl:text>% QandADiv   &#10;</xsl:text>
		<xsl:text>% -----------&#10;</xsl:text>
		<xsl:choose>
      		<xsl:when test="ancestor::sect2">
	    		<xsl:text>\paragraph*{</xsl:text>
			</xsl:when>
      		<xsl:when test="ancestor::sect1">
	    		<xsl:text>\subsubsection*{</xsl:text>
			</xsl:when>
      		<xsl:when test="ancestor::article | ancestor::appendix">
	    		<xsl:text>\subsection*{</xsl:text>
			</xsl:when>
      		<xsl:when test="ancestor::book">
	    		<xsl:text>\section*{</xsl:text>
			</xsl:when>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="title">
				<xsl:apply-templates select="title"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>F.A.Q. Part</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text>}&#10;</xsl:text>
		<xsl:call-template name="label.id"/>
		<xsl:text>&#10;</xsl:text>

<!--
  <xsl:variable name="toc.params">
  </xsl:variable>
  <xsl:if test="contains($toc.params, 'toc')">
        <xsl:call-template name="process.qanda.toc"/>
  </xsl:if>
  <xsl:if test="$preamble">
        <xsl:apply-templates select="$preamble"/>
  </xsl:if>
-->
<!--
	<xsl:text>\begin{toc}&#10;</xsl:text>
	<xsl:for-each select="qandaentry">
		<xsl:text>\tocref{</xsl:text>
		<xsl:value-of select="@id"/>
		<xsl:text>}&#10;</xsl:text>
	</xsl:for-each>
	<xsl:text>\end{toc}&#10;</xsl:text>
-->

<!-- pseudo table of contents -->
	<xsl:text>\floatstyle{ruled}&#10;</xsl:text>
	<xsl:text>\newfloat{qandadivtoc}{H}{qdtoc}&#10;</xsl:text>
	<!-- Either this one : -->
	<xsl:text>\floatname{qandadivtoc}{</xsl:text>
	<xsl:call-template name="gentext">
		<xsl:with-param name="key">TableofContents</xsl:with-param>
	</xsl:call-template>
	<xsl:text>}&#10;</xsl:text>
	<!-- or this one : 
	<xsl:text>\floatname{qandadivtoc}{}&#10;</xsl:text>
	-->
	<xsl:text>\begin{qandadivtoc}&#10;</xsl:text>
	<xsl:choose>
		<xsl:when test="title">
			<xsl:text>\caption{</xsl:text>
			<xsl:apply-templates select="title"/>
			<xsl:text>}&#10;</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:text>\caption{</xsl:text>
			<xsl:text>F.A.Q. Part</xsl:text>
			<xsl:text>}&#10;</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:for-each select="qandaentry">
	<xsl:text>\noindent</xsl:text>
	<xsl:value-of select="position()"/>
	<xsl:text>.~</xsl:text>
	<xsl:apply-templates select="question"/>
	</xsl:for-each>
	<xsl:text>\end{qandadivtoc}&#10;</xsl:text>
	<xsl:text>\vspace{0.25cm}&#10;</xsl:text>

	<xsl:for-each select="qandaentry">
	<xsl:text>\noindent</xsl:text>
	<xsl:value-of select="position()"/>
	<xsl:text>.~</xsl:text>
	<xsl:apply-templates select="question|answer"/>
	</xsl:for-each>
<!--  <xsl:apply-templates select="qandadiv|qandaentry"/> -->
<!--  <xsl:apply-templates/> -->
</xsl:template>








<!--############################################################################# 
|   qandadiv/title
|-  #############################################################################
|														
+   ############################################################################# -->

<xsl:template match="qandadiv/title">
    <!-- <xsl:apply-templates select="parent::qandadiv" mode="label.markup"/> -->
    <xsl:apply-templates/>
</xsl:template>




<!--############################################################################# 
|   question
|-  #############################################################################
|														
+   ############################################################################# -->

<xsl:template match="question">
<!-- get the default label -->
<xsl:variable name="deflabel">
		<xsl:choose>
			<xsl:when test="ancestor-or-self::*[@defaultlabel]">
				<xsl:value-of select="(ancestor-or-self::*[@defaultlabel])[last()]/@defaultlabel"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="latex.qanda.defaultlabel"/>
			</xsl:otherwise>
		</xsl:choose>
</xsl:variable>
<!-- process the question itself 
<xsl:apply-templates select="." mode="label.markup"/>
<xsl:choose>
	<xsl:when test="$deflabel = 'none' and not(label)">
		<xsl:apply-templates select="*[name(.) != 'label']"/>
	</xsl:when>
	<xsl:otherwise>
		<xsl:apply-templates select="*[name(.) != 'label']"/>
	</xsl:otherwise>
</xsl:choose>
-->
<xsl:text>{\textbf {Q : }}{\em </xsl:text>
<xsl:apply-templates/>
<xsl:text>} \newline&#10;</xsl:text>
</xsl:template>








<!--############################################################################# 
|   answer 
|-  #############################################################################
|														
+   ############################################################################# -->

<xsl:template match="answer">
<xsl:text>\noindent{\textbf{A :}}</xsl:text>
<xsl:apply-templates/>
<xsl:text>\newline&#10;</xsl:text>
<xsl:text>\vspace{0.25cm}&#10;</xsl:text>
<xsl:text>&#10;&#10;</xsl:text>
</xsl:template>







<!--############################################################################# 
|   label 
|-  #############################################################################
|														
+   ############################################################################# -->

<xsl:template match="label">
  <xsl:apply-templates/>
</xsl:template>


<xsl:template name="process.qanda.toc">
    <xsl:apply-templates select="qandadiv" mode="qandatoc.mode"/>
    <xsl:apply-templates select="qandaentry" mode="qandatoc.mode"/>
</xsl:template>

<xsl:template match="qandadiv" mode="qandatoc.mode">
  <xsl:apply-templates select="title" mode="qandatoc.mode"/>
  <xsl:call-template name="process.qanda.toc"/>
</xsl:template>






<xsl:template match="qandadiv/title" mode="qandatoc.mode">
<xsl:variable name="qalevel">
    <xsl:call-template name="qandadiv.section.level"/>
</xsl:variable>
<xsl:call-template name="label.id">
	<xsl:with-param name="object" select="parent::*"/>
</xsl:call-template>
<xsl:apply-templates select="parent::qandadiv" mode="label.markup"/>
<xsl:value-of select="$autotoc.label.separator"/>
<xsl:apply-templates/>
</xsl:template>



<xsl:template match="qandaentry" mode="qandatoc.mode">
  <xsl:apply-templates mode="qandatoc.mode"/>
</xsl:template>



<xsl:template match="question" mode="qandatoc.mode">
  <xsl:variable name="firstch" select="(*[name(.)!='label'])[1]"/>
    <xsl:apply-templates select="." mode="label.markup"/>
    <xsl:text> </xsl:text>
</xsl:template>


<xsl:template match="answer|revhistory" mode="qandatoc.mode">
  <!-- nop -->
</xsl:template>




<xsl:template name="question.answer.label">
	<!-- variable: deflabel -->
  <xsl:variable name="deflabel">
  	<!-- chck whether someone has a defaultlabel attribute -->
    <xsl:choose>
		<xsl:when test="ancestor-or-self::*[@defaultlabel]">
        	<xsl:value-of select="(ancestor-or-self::*[@defaultlabel])[last()]/@defaultlabel"/>
	      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="latex.qanda.defaultlabel"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>


  <xsl:variable name="label" select="@label"/>
  <xsl:choose>
    <xsl:when test="$deflabel = 'qanda'">
      <xsl:call-template name="gentext">
        <xsl:with-param name="key">
          <xsl:choose>
            <xsl:when test="local-name(.) = 'question'">question</xsl:when>
            <xsl:when test="local-name(.) = 'answer'">answer</xsl:when>
            <xsl:when test="local-name(.) = 'qandadiv'">qandadiv</xsl:when>
            <xsl:otherwise>qandaset</xsl:otherwise>
          </xsl:choose>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:when>
    <xsl:when test="$deflabel = 'label'">
      <xsl:value-of select="$label"/>
    </xsl:when>
    <xsl:when test="$deflabel = 'number' and local-name(.) = 'question'">
      <xsl:apply-templates select="ancestor::qandaset[1]" mode="number"/>
      <xsl:choose>
        <xsl:when test="ancestor::qandadiv">
          <xsl:apply-templates select="ancestor::qandadiv[1]" mode="number"/>
          <xsl:apply-templates select="ancestor::qandaentry" mode="number"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="ancestor::qandaentry" mode="number"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:when>
    <xsl:otherwise>
      <!-- nothing -->
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

</xsl:stylesheet>
