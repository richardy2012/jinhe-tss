<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/TR/WD-xsl" version="1.0">
    <xsl:template match="/">
        <div id="locked">
            <xsl:attribute name="style">
                height:<xsl:eval>totalHeight</xsl:eval>px;
                overflow:hidden;
                position:absolute;
                display:inline;
            </xsl:attribute>
            <table border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed">
                <colgroup>
                    <xsl:if test="/grid/declare[@header!='']">
                        <col align="center"/>
                    </xsl:if>
                    <xsl:if test="/grid/declare[(@sequence='true') or not(@sequence)]">
                        <col align="center" class="sequence"/>
                    </xsl:if>
                    <xsl:for-each select=".//column[not(@display) or not(@display='none')]">
                        <xsl:if expr="getLocked()==true">
                            <col>
                                <xsl:attribute name="align">
									<xsl:choose>
										<xsl:when test="@align"><xsl:value-of select="@align"/></xsl:when>
										<xsl:otherwise><xsl:eval>getAlign()</xsl:eval></xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
                            </col>
                        </xsl:if>
                    </xsl:for-each>
                </colgroup>
				
                <thead class="header">
                    <xsl:eval>void(isLocked=true)</xsl:eval>
                    <xsl:apply-templates select="/grid/declare"/>
                </thead>
				
                <tbody class="cell">
                    <xsl:for-each select="/grid/data//row">
                        <tr>
                            <xsl:apply-templates select="@*"/>
                            <xsl:attribute name="height"><xsl:eval>cellHeight</xsl:eval></xsl:attribute>
                            <xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute>
                            <xsl:eval>void(curRow = childNumber(this))</xsl:eval>
                            <xsl:if test="/grid/declare[@header!='']">
                                <td mode="cellheader" name="cellheader">
                                    <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cellheader")</xsl:eval>;</xsl:attribute>
                                    <nobr>
										<input class="selectHandle">
											<xsl:attribute name="name">grid_header</xsl:attribute>
											<xsl:attribute name="type"><xsl:value-of select="/grid/declare/@header"/></xsl:attribute>
										</input>
									</nobr>
								</td>
                            </xsl:if>
                            <xsl:if test="/grid/declare[(@sequence='true') or not(@sequence)]">
                                <td mode="cellsequence" name="cellsequence">
                                    <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cellsequence")</xsl:eval>;</xsl:attribute>
                                    <nobr><xsl:eval>childNumber(this)</xsl:eval></nobr>
								</td>
                            </xsl:if>
                            <xsl:for-each select="/grid/declare/*[(@locked='true')]">
                                <xsl:if expr="this.getAttribute('display')!='none'">
                                    <td>
                                        <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cell")</xsl:eval>;</xsl:attribute>
                                        <xsl:apply-templates select="@*"/>
                                        <nobr>&amp;nbsp;</nobr>
									</td>
                                </xsl:if>
                                
                            </xsl:for-each>
                        </tr>
                    </xsl:for-each>
                </tbody>
            </table>
        </div>
        <div id="unlocked">
            <xsl:attribute name="style">
                height:<xsl:eval>totalHeight</xsl:eval>px;
                overflow:hidden;
                position:absolute;
                display:inline;
            </xsl:attribute>
            <table border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed">
                <colgroup>
                    <xsl:for-each select=".//column[not(@display) or not(@display='none')]">
                        <xsl:if expr="getLocked()==false">
                            <col>
                                <xsl:attribute name="align">
									<xsl:choose>
										<xsl:when test="@align"><xsl:value-of select="@align"/></xsl:when>
										<xsl:otherwise><xsl:eval>getAlign()</xsl:eval></xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
                            </col>
                        </xsl:if>
                    </xsl:for-each>
                </colgroup>
                <thead>
                    <xsl:eval>void(isLocked=false)</xsl:eval>
                    <xsl:apply-templates select="/grid/declare"/>
                </thead>
                <tbody class="cell">
                    <xsl:for-each select="/grid/data//row">
                        <tr>
                            <xsl:apply-templates select="@*"/>
                            <xsl:attribute name="height"><xsl:eval>cellHeight</xsl:eval></xsl:attribute>
                            <xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute>
                            <xsl:eval>void(curRow=childNumber(this))</xsl:eval>
                            <xsl:for-each select="/grid/declare/*[(@locked='false') or not(@locked)]">
                                <xsl:if expr="this.getAttribute('display')!='none'">
                                    <td>
                                        <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cell")</xsl:eval>;</xsl:attribute>
                                        <xsl:apply-templates select="@*"/>
                                        <nobr>&amp;nbsp;</nobr></td>
                                </xsl:if>
                            </xsl:for-each>
                        </tr>
                    </xsl:for-each>
                </tbody>
            </table>
        </div>
    </xsl:template>
	
    <xsl:template match="/grid/declare">
        <tr>
            <xsl:attribute name="height"><xsl:eval>headerCellHeight</xsl:eval></xsl:attribute>
            <xsl:if test=".[@header!='']" expr="isLocked==true">
                <td>
                    <xsl:attribute name="class">column</xsl:attribute>
                    <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("header")</xsl:eval>;</xsl:attribute>
                    <nobr>
						<xsl:if test=".[@header='checkbox']">
							<input type="checkbox" id="headerCheckAll"/>
						</xsl:if>
						<xsl:if test=".[@header='radio']">&amp;nbsp;</xsl:if>
					</nobr>
				</td>
            </xsl:if>
            <xsl:if test=".[(@sequence='true') or not(@sequence)]" expr="isLocked==true">
                <td>
                    <xsl:attribute name="class">column</xsl:attribute>
                    <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("sequence")</xsl:eval>;</xsl:attribute>
                    <nobr>序号</nobr></td>
            </xsl:if>
            <xsl:for-each select=".//*">
                <xsl:if expr="getLocked()==isLocked">
                    <xsl:apply-templates select="."/>
                </xsl:if>
            </xsl:for-each>
        </tr>
    </xsl:template>
	
    <xsl:template match="column[not(@display) or not(@display='none')]">
        <td>
            <xsl:attribute name="class">column</xsl:attribute>
            <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth()</xsl:eval>;height:<xsl:eval>headerCellHeight</xsl:eval>px;</xsl:attribute>
            <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
            <xsl:attribute name="sortable"><xsl:value-of select="@sortable"/></xsl:attribute>
            <nobr>
				<xsl:eval>getCaption()</xsl:eval>
				<xsl:if test=".[@mode='boolean']"><input type="checkbox"/></xsl:if>
			</nobr>
		</td>
    </xsl:template>
	
    <xsl:template match="@*">
        <xsl:choose>
            <xsl:when expr="this.nodeName=='highlightCol' &amp;&amp; this.nodeValue=='true'">
                <xsl:attribute name="class">highlightCol</xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy><xsl:value-of/></xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
	
    <xsl:script>
		var cellHeight = 22;
		var headerCellHeight = 22;
		var totalWidth = 1230;
		var totalHeight = 206;
	</xsl:script>
    <xsl:script>
        var curRow;
        var isLocked;
        var declare = this.selectSingleNode("/grid/declare");

        function getBorderWidth(s){
            var top = 1;
            var right = 1;
            var bottom = 1;
            var left = 1;

            return top + " " + right + " " + bottom + " " + left;
        }
		
        function getAllColumn(){
            return this.selectNodes(".//column[not(@display) or not(@display='none')]").length;
        }
				
        function getLocked(){
            return this.getAttribute("locked")=="true";
        }
		
        function getAlign(){
            switch(this.getAttribute("mode")){
                case "number":
                    return "right";
                    break;
                case "boolean":
                case "date":
                    return "center";
                    break;
                default:
                    return "left";
                    break;

            }
        }
		
        function getCaption(){
            return this.getAttribute("caption") || "&amp;nbsp;";
        }

    </xsl:script>
</xsl:stylesheet>
