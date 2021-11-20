<?xml version="1.0" encoding="utf-8"?>
<html xsl:version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/TR/xhtml1/strict">

<head>
<title>Translating between XML and Relational Databases</title>
</head>









	<body style="font-family:Arial,helvetica,sans-serif;  background-color:#EEEEE">
		<h1>
			<xsl:value-of select="page/title"/>
		</h1>
		<b>
			<xsl:value-of select="page/project"/>
		</b>
		<br/>
		<br/>
		<xsl:value-of select="page/name"/>
		<br/>
		<xsl:value-of select="page/email"/>
		<br/>
		<!--<a href="mailto:
		<xsl:value-of select="page/email"/>
		">sddsafa
		</a>-->
		<br/>
		<br/>





		<h2>Application</h2>
		<a href="src/">Source Code</a><br/>
		<a href="xtractor.zip">Application</a>

		<h2>Documentation</h2>
		<a href="report/">Report Chapters</a><br/>
		<a href="slides.ppt">Presentation slides</a><br/>
		<a href="javadoc/">Javadoc</a>


		
		<h2>References</h2>
		<a href="http://www.cse.ucsc.edu/classes/cmps290s/Spring03/fk.pdf">Storing and Querying XML Data using an RDBMS</a><br/>
		<a href="http://www.cs.cornell.edu/people/jai/papers/GeneralXMLQuerying.pdf">A General Technique for Querying XML Documents using a Relational Database System</a><br/>
		Representing the XPath model in Relational Databases<br/>
		<a href="http://nike.psu.edu/publications/book03.pdf">Schema Conversion Methods between XML and Relational Models</a><br/>
		<a href="http://citeseer.ist.psu.edu/cache/papers/cs/22090/http:zSzzSzwww.cobase.cs.ucla.eduzSztech-docszSzdongwonzSzcpi-dke.pdf/lee01cpi.pdf">CPI: Constraints-Preserving Inlining Algorithm for Mapping XML DTD to Relational Schema</a><br/>
		<a href="http://www.idealliance.org/papers/dx_xml03/papers/06-01-01/06-01-01.pdf">XER - Extensible Entity Relationship Modeling</a><br/>
		Conceptual Modeling of XML Schemas
		<!--<a href="http://delivery.acm.org/10.1145/960000/956722/p102-loscio.pdf?key1=956722&key2=1979835701&coll=GUIDE&dl=ACM&CFID=16121099&CFTOKEN=76508334">Conceptual Modeling of XML Schemas</a><br/>
-->

		<h2>Latest</h2>
		<table>
			<xsl:for-each select="page/update_list/update">
				<tr>
					<td>
						<b>
							<xsl:value-of select="date"/>
						</b>
					</td>
					<td>
					
					</td>
					<td>
						<xsl:value-of select="description"/>
					</td>
				</tr>
			</xsl:for-each>
		</table>
		<br/>
		<br/>
		<h2>Meetings</h2>
			<xsl:for-each select="page/meeting_list/meeting">
				<xsl:value-of select="."/><br/>
			</xsl:for-each>
		
		<br/><br/>


	</body>
</html>
