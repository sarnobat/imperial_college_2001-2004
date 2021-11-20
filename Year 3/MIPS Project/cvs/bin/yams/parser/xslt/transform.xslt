<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="Instructions">
	class MIPSParser extends Parser;
	
	Goal 		: StmtList &lt;EOF&gt; ;
	StmtList		: Stat (StmtList)? ;
	Stat			: Label Instruction
				| Label Data
				| Instruction
				| Data
				| SymDef
				| Directive
				;
	OpCode		: <xsl:apply-templates select="Instruction"/>
				;
	</xsl:template>
	<xsl:template match="Instruction">
				| <xsl:value-of select="OpCode"/>
	</xsl:template>
</xsl:stylesheet>
