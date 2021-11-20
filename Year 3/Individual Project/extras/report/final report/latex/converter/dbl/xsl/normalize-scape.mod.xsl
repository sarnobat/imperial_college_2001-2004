<?xml version='1.0'?>
<!--############################################################################# 
|	$Id: normalize-scape.mod.xsl,v 1.10 2003/04/07 11:21:57 j-devenish Exp $
|- #############################################################################
|	$Author: j-devenish $
|														
|   PURPOSE:
|	Escape LaTeX and normalize-space templates.
|    < > # $ % & ~ _ ^ \ { }
+ ############################################################################## -->

<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:doc="http://nwalsh.com/xsl/documentation/1.0"
    exclude-result-prefixes="doc" version='1.0'>


    <!--############################################################################# -->
    <!-- DOCUMENTATION                                                                -->
    <doc:reference id="normalize-scape" xmlns="">
	<referenceinfo>
	    <releaseinfo role="meta">
		$Id: normalize-scape.mod.xsl,v 1.10 2003/04/07 11:21:57 j-devenish Exp $
	    </releaseinfo>
	<authorgroup>
	    <author> <firstname>Ramon</firstname> <surname>Casellas</surname> </author>
	    <author> <firstname>James</firstname> <surname>Devenish</surname> </author>
	</authorgroup>
	    <copyright><year>2000</year><year>2001</year><year>2002</year><year>2003</year>
		<holder>Ramon Casellas</holder>
	    </copyright>
	</referenceinfo>
	<title>Normalize and Scape strings.</title>
	<partintro>
	    <section><title>Introduction</title>
		<para></para>
	    </section>
	</partintro>
    </doc:reference>
    <!--############################################################################# -->

    <!--############################################################################# -->
    <!-- DOCUMENTATION                                                                -->
    <doc:template name="normalize-scape" xmlns="">
	<refpurpose> Abstract XSL template.  </refpurpose>
	<refdescription>
	</refdescription>
    </doc:template>
    <!--############################################################################# -->

    <xsl:template match="text()">
	<xsl:call-template name="scape">
	    <xsl:with-param name="string" select="."/>
	</xsl:call-template>
    </xsl:template>

    <xsl:template match="text()" mode="xref.text">
	<xsl:call-template name="scape">
	    <xsl:with-param name="string" select="."/>
	</xsl:call-template>
    </xsl:template>

    <xsl:template match="text()" mode="xref-to">
	<xsl:call-template name="scape">
	    <xsl:with-param name="string" select="."/>
	</xsl:call-template>
    </xsl:template>

    <xsl:template match="text()" mode="latex.verbatim">
	<xsl:value-of select="."/> 
    </xsl:template>

	<!-- this template is noly used by xref.mod.xsl and only when
	     $latex.hyphenation.tttricks != 1. -->
    <xsl:template match="text()" mode="slash.hyphen">
		<xsl:call-template name="scape.slash.hyphen">
			<xsl:with-param name="string" select="." />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="scape.slash.hyphen">
	<xsl:param name="string" />
	<xsl:choose>
	    <xsl:when test="contains($string,'://')">
		<xsl:call-template name="string-replace">
			<xsl:with-param name="to">/\-</xsl:with-param>
			<xsl:with-param name="from">/</xsl:with-param>
		    <xsl:with-param name="string">
			<xsl:value-of select="substring-before($string,'://')"/>
			<xsl:value-of select="'://'"/>
			<xsl:call-template name="scape">
			    <xsl:with-param name="string" select="substring-after($string,'://')"/>
		    </xsl:call-template></xsl:with-param>
		</xsl:call-template>
	    </xsl:when>
	    <xsl:otherwise>
		<xsl:call-template name="string-replace">
			<xsl:with-param name="to">/\-</xsl:with-param>
			<xsl:with-param name="from">/</xsl:with-param>
		    <xsl:with-param name="string">
			<xsl:call-template name="scape">
			    <xsl:with-param name="string" select="$string"/>
		    </xsl:call-template></xsl:with-param>
		</xsl:call-template>
	    </xsl:otherwise>
	</xsl:choose>
    </xsl:template>

    <xsl:template name="normalize-scape" >
	<xsl:param name="string"/>
	<xsl:variable name="result">
	    <xsl:call-template name="scape"><xsl:with-param name="string" select="$string"/></xsl:call-template>
	</xsl:variable>
	<xsl:value-of select="normalize-space($result)"/>
    </xsl:template>


    <xsl:template name="scape" >
	<xsl:param name="string"/>
		<xsl:call-template name="string-replace">
		    <xsl:with-param name="to">\textless{}</xsl:with-param>
		    <xsl:with-param name="from">&lt;</xsl:with-param>
		    <xsl:with-param name="string">
			<xsl:call-template name="string-replace">
			    <xsl:with-param name="to">\textgreater{}</xsl:with-param>
			    <xsl:with-param name="from">&gt;</xsl:with-param>
			    <xsl:with-param name="string">
				<xsl:call-template name="string-replace">
				    <xsl:with-param name="to">\{</xsl:with-param>
				    <xsl:with-param name="from">{</xsl:with-param>
				    <xsl:with-param name="string">
					<xsl:call-template name="string-replace">
					    <xsl:with-param name="to">\}</xsl:with-param>
					    <xsl:with-param name="from">}</xsl:with-param>
					    <xsl:with-param name="string">
						<xsl:call-template name="string-replace">
						    <xsl:with-param name="to">\&amp;</xsl:with-param>
						    <xsl:with-param name="from">&amp;</xsl:with-param>
						    <xsl:with-param name="string">
							<xsl:call-template name="string-replace">
							    <xsl:with-param name="to">\#</xsl:with-param>
							    <xsl:with-param name="from">#</xsl:with-param>
							    <xsl:with-param name="string">
								<xsl:call-template name="string-replace">
								    <xsl:with-param name="to">\_</xsl:with-param>
								    <xsl:with-param name="from">_</xsl:with-param>
								    <xsl:with-param name="string">
									<xsl:call-template name="string-replace">
									    <xsl:with-param name="to">\$</xsl:with-param>
									    <xsl:with-param name="from">$</xsl:with-param>
									    <xsl:with-param name="string">
										<xsl:call-template name="string-replace">
										    <xsl:with-param name="to">\%</xsl:with-param>
										    <xsl:with-param name="from">%</xsl:with-param>
										    <xsl:with-param name="string">
											<xsl:call-template name="string-replace">
												<xsl:with-param name="to">\^</xsl:with-param>
												<xsl:with-param name="from">^</xsl:with-param>
												<xsl:with-param name="string">
												<xsl:call-template name="string-replace">
													<xsl:with-param name="to">\textasciitilde{}</xsl:with-param>
													<xsl:with-param name="from">~</xsl:with-param>
													<xsl:with-param name="string">
													<xsl:call-template name="string-replace">
														<xsl:with-param name="to">\textbackslash{}</xsl:with-param>
														<xsl:with-param name="from">\</xsl:with-param>
														<xsl:with-param name="string" select="$string"></xsl:with-param>
													</xsl:call-template>
												</xsl:with-param>
											</xsl:call-template></xsl:with-param>
										</xsl:call-template></xsl:with-param>
								    </xsl:call-template></xsl:with-param>
							    </xsl:call-template></xsl:with-param>
						    </xsl:call-template></xsl:with-param>
					    </xsl:call-template></xsl:with-param>
				    </xsl:call-template></xsl:with-param>
			    </xsl:call-template></xsl:with-param>
		    </xsl:call-template></xsl:with-param>
	    </xsl:call-template></xsl:with-param>
	</xsl:call-template>
    </xsl:template>




    <xsl:template name="string-replace">
	<xsl:param name="string"/>
	<xsl:param name="from"/>
	<xsl:param name="to"/>

	<xsl:choose>
	    <xsl:when test="contains($string, $from)">

		<xsl:variable name="before" select="substring-before($string, $from)"/>
		<xsl:variable name="after" select="substring-after($string, $from)"/>
		<xsl:variable name="prefix" select="concat($before, $to)"/>

		<xsl:value-of select="$before"/>
		<xsl:value-of select="$to"/>
		<xsl:call-template name="string-replace">
		    <xsl:with-param name="string" select="$after"/>
		    <xsl:with-param name="from" select="$from"/>
		    <xsl:with-param name="to" select="$to"/>
		</xsl:call-template>
	    </xsl:when> 
	    <xsl:otherwise>
		<xsl:value-of select="$string"/>  
	    </xsl:otherwise>
	</xsl:choose>            
    </xsl:template>




    <!--  
    (c) David Carlisle
    replace all occurences of the character(s) `from'
    by the string `to' in the string `string'.
    <xsl:template name="string-replace" >
	<xsl:param name="string"/>
	<xsl:param name="from"/>
	<xsl:param name="to"/>
	<xsl:choose>
	    <xsl:when test="contains($string,$from)">
		<xsl:value-of select="substring-before($string,$from)"/>
		<xsl:value-of select="$to"/>
		<xsl:call-template name="string-replace">
		    <xsl:with-param name="string" select="substring-after($string,$from)"/>
		    <xsl:with-param name="from" select="$from"/>
		    <xsl:with-param name="to" select="$to"/>
		</xsl:call-template>
	    </xsl:when>
	    <xsl:otherwise>
		<xsl:value-of select="$string"/>
	    </xsl:otherwise>
	</xsl:choose>
    </xsl:template>
    -->

</xsl:stylesheet>
