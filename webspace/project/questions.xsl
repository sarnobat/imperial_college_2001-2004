<?xml version="1.0" encoding="utf-8"?>
<html xsl:version="1.0" xmlns:xsl="http://www.w3.org/1 9/XSL/Transform" xmlns="http://www.w3.org/TR/xhtml1/strict">
<body>
				<u>Questions</u>
		<br/>
		<br/><ol>
			<xsl:for-each select="question_list/question">
				<li>
					<xsl:value-of select="."/>
				</li>
			</xsl:for-each>
		</ol>
</body>
</html>