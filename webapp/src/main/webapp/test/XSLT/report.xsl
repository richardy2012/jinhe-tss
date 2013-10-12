<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
	xmlns:myfn="http://unmi.cc/fn"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl myfn">

<xsl:template match="/">
	<HTML>
		<HEAD><TITLE>成绩单</TITLE></HEAD>
		<BODY>
			<xsl:apply-templates select="document"/>
		</BODY>
	</HTML>
</xsl:template>

<xsl:template match="document">
	<TABLE border="1" cellspacing="0">
		<xsl:apply-templates select="@*"/>
		<TH>姓名</TH><TH>英语</TH><TH>数学</TH><TH>化学</TH>
		<xsl:apply-templates select="grade"/>
	</TABLE>
</xsl:template>

<xsl:template match="grade">
	<TR><xsl:apply-templates select="@*"/>
		<TD><xsl:apply-templates select="name"/></TD>
		<TD><xsl:apply-templates select="english"/></TD>
		<TD><xsl:apply-templates select="math"/></TD>
		<TD><xsl:apply-templates select="chymest"/></TD>
	</TR>
</xsl:template>

<xsl:template match="@*">
	<xsl:if test="name() = 'style'">
		<xsl:copy><xsl:value-of select="."/></xsl:copy>
	</xsl:if>
	<xsl:value-of select="name(..)"/>
	<xsl:value-of select="myfn:test('JK')"/>
</xsl:template>


<xsl:template match="name">
	<xsl:apply-templates select="@*"/>
	<xsl:value-of select="."/>
</xsl:template>


<xsl:template match="english|math|chymest">
	<xsl:choose>
		<xsl:when test=". &gt; 85">优秀</xsl:when>
		<xsl:when test=". &gt; 70">一般</xsl:when>
		<xsl:when test=". &gt; 60">起格</xsl:when>
		<xsl:otherwise>不起格</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<msxsl:script implements-prefix="myfn" language="JavaScript">
	var baseurl  = "www.google.com";
	function test(name) {
		return name;
	}
</msxsl:script>

</xsl:stylesheet>


