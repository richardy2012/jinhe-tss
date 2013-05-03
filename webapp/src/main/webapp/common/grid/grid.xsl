<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/TR/WD-xsl" version="1.0">
    <xsl:template match="/">
        <xsl:eval>getTotalRows()</xsl:eval>
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
                                <xsl:attribute name="align"><xsl:choose><xsl:when test="@align"><xsl:value-of select="@align"/></xsl:when><xsl:otherwise><xsl:eval>getAlign()</xsl:eval></xsl:otherwise></xsl:choose></xsl:attribute>
                            </col>
                        </xsl:if>
                    </xsl:for-each>
                </colgroup>
                <thead class="header">
                    <xsl:eval>void(curHeadRow=1)</xsl:eval>
                    <xsl:eval>void(curType=true)</xsl:eval>
                    <xsl:apply-templates select="/grid/declare"/>
                </thead>
                <xsl:if test="/grid/data/sumDeclare[@top='true']">
                    <tbody class="sum">
                        <tr>
                            <xsl:attribute name="height"><xsl:eval>cellHeight</xsl:eval></xsl:attribute>
                            <xsl:if test="/grid/declare[@header!='']">
                                <td>
                                    <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cellheader")</xsl:eval>;</xsl:attribute>
                                    <nobr>&amp;nbsp;</nobr></td>
                            </xsl:if>
                            <xsl:if test="/grid/declare[(@sequence='true') or not(@sequence)]">
                                <td name="cellsequence" class="sequence">
                                    <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cellsequence")</xsl:eval>;</xsl:attribute>
                                    <nobr>合计</nobr></td>
                            </xsl:if>
                            <xsl:for-each select="/grid/declare//column[not(@display) or not(@display='none')]">
                                <xsl:if expr="getLocked()==true">
                                    <td>
                                        <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cell")</xsl:eval>;</xsl:attribute>
                                        <xsl:attribute name="name">
                                            <xsl:value-of select="@name"/>
                                        </xsl:attribute>
                                        <nobr>&amp;nbsp;</nobr></td>
                                </xsl:if>
                            </xsl:for-each>
                        </tr>
                    </tbody>
                </xsl:if>
                <tbody class="cell">
                    <xsl:for-each select="/grid/data//row">
                        <tr>
                            <xsl:apply-templates select="@*"/>
                            <xsl:attribute name="height"><xsl:eval>cellHeight</xsl:eval></xsl:attribute>
                            <xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute>
                            <xsl:eval>void(curRow=childNumber(this))</xsl:eval>
                            <xsl:if test="/grid/declare[@header!='']">
                                <td mode="cellheader" name="cellheader">
                                    <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cellheader")</xsl:eval>;</xsl:attribute>
                                    <xsl:if expr="disableRow==true">
                                        <xsl:attribute name="disabled">true</xsl:attribute>
                                    </xsl:if>
                                    <nobr><input class="selectHandle"><xsl:attribute name="name"><xsl:eval>id</xsl:eval>_header</xsl:attribute><xsl:attribute name="type"><xsl:value-of select="/grid/declare/@header"/></xsl:attribute></input></nobr></td>
                            </xsl:if>
                            <xsl:if test="/grid/declare[(@sequence='true') or not(@sequence)]">
                                <td mode="cellsequence" name="cellsequence">
                                    <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cellsequence")</xsl:eval>;</xsl:attribute>
                                    <nobr><xsl:eval>childNumber(this)+begin</xsl:eval></nobr></td>
                            </xsl:if>
                            <xsl:for-each select="/grid/declare/*[(@locked='true')]">
                                <xsl:if expr="this.tagName=='column' &amp;&amp; this.getAttribute('display')!='none'">
                                    <td>
                                        <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cell")</xsl:eval>;</xsl:attribute>
                                        <xsl:apply-templates select="@*"/>
                                        <nobr>&amp;nbsp;</nobr></td>
                                </xsl:if>
                                <xsl:if expr="this.tagName=='band'">
                                    <xsl:for-each select=".//column[not(@display) or not(@display='none')]">
                                        <td>
                                            <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cell")</xsl:eval>;</xsl:attribute>
                                            <xsl:apply-templates select="@*"/>
                                            <nobr>&amp;nbsp;</nobr></td>
                                    </xsl:for-each>
                                </xsl:if>
                            </xsl:for-each>
                        </tr>
                    </xsl:for-each>
                </tbody>
                <xsl:if test="/grid/data/sumDeclare[@top='false']">
                    <tbody class="sum">
                        <tr>
                            <xsl:attribute name="height"><xsl:eval>cellHeight</xsl:eval></xsl:attribute>
                            <xsl:if test="/grid/declare[@header!='']">
                                <td>
                                    <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cellheader")</xsl:eval>;</xsl:attribute>
                                    <nobr>&amp;nbsp;</nobr></td>
                            </xsl:if>
                            <xsl:if test="/grid/declare[(@sequence='true') or not(@sequence)]">
                                <td name="cellsequence">
                                    <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cellsequence")</xsl:eval>;</xsl:attribute>
                                    <nobr>&amp;nbsp;</nobr></td>
                            </xsl:if>
                            <xsl:for-each select="/grid/declare//column">
                                <xsl:if expr="this.getAttribute('display')!='none' &amp;&amp; getLocked()==true">
                                    <td>
                                        <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cell")</xsl:eval>;</xsl:attribute>
                                        <xsl:attribute name="name">
                                            <xsl:value-of select="@name"/>
                                        </xsl:attribute>
                                        <nobr>&amp;nbsp;</nobr></td>
                                </xsl:if>
                            </xsl:for-each>
                        </tr>
                    </tbody>
                </xsl:if>
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
                                <xsl:attribute name="align"><xsl:choose><xsl:when test="@align"><xsl:value-of select="@align"/></xsl:when><xsl:otherwise><xsl:eval>getAlign()</xsl:eval></xsl:otherwise></xsl:choose></xsl:attribute>
                            </col>
                        </xsl:if>
                    </xsl:for-each>
                </colgroup>
                <thead>
                    <xsl:eval>void(curHeadRow=1)</xsl:eval>
                    <xsl:eval>void(curType=false)</xsl:eval>
                    <xsl:apply-templates select="/grid/declare"/>
                </thead>
                <xsl:if test="/grid/data/sumDeclare[@top='true']">
                    <tbody class="sum">
                        <tr>
                            <xsl:attribute name="height"><xsl:eval>cellHeight</xsl:eval></xsl:attribute>
                            <xsl:for-each select="/grid/declare//column[not(@display) or not(@display='none')]">
                                <xsl:if expr="getLocked()==false">
                                    <td>
                                        <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cell")</xsl:eval>;</xsl:attribute>
                                        <xsl:attribute name="name">
                                            <xsl:value-of select="@name"/>
                                        </xsl:attribute>
                                        <nobr>&amp;nbsp;</nobr></td>
                                </xsl:if>
                            </xsl:for-each>
                        </tr>
                    </tbody>
                </xsl:if>
                <tbody class="cell">
                    <xsl:for-each select="/grid/data//row">
                        <tr>
                            <xsl:apply-templates select="@*"/>
                            <xsl:attribute name="height"><xsl:eval>cellHeight</xsl:eval></xsl:attribute>
                            <xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute>
                            <xsl:eval>void(curRow=childNumber(this))</xsl:eval>
                            <xsl:for-each select="/grid/declare/*[(@locked='false') or not(@locked)]">
                                <xsl:if expr="this.tagName=='column' &amp;&amp; this.getAttribute('display')!='none'">
                                    <td>
                                        <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cell")</xsl:eval>;</xsl:attribute>
                                        <xsl:apply-templates select="@*"/>
                                        <nobr>&amp;nbsp;</nobr></td>
                                </xsl:if>
                                <xsl:if expr="this.tagName=='band'">
                                    <xsl:for-each select=".//column[not(@display) or not(@display='none')]">
                                        <td>
                                            <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cell")</xsl:eval>;</xsl:attribute>
                                            <xsl:apply-templates select="@*"/>
                                            <nobr>&amp;nbsp;</nobr></td>
                                    </xsl:for-each>
                                </xsl:if>
                            </xsl:for-each>
                        </tr>
                    </xsl:for-each>
                </tbody>
                <xsl:if test="/grid/data/sumDeclare[@top='false']">
                    <tbody class="sum">
                        <tr>
                            <xsl:attribute name="height"><xsl:eval>cellHeight</xsl:eval></xsl:attribute>
                            <xsl:for-each select="/grid/declare//column">
                                <xsl:if expr="this.getAttribute('display')!='none' &amp;&amp; getLocked()==false">
                                    <td>
                                        <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("cell")</xsl:eval>;</xsl:attribute>
                                        <xsl:attribute name="name">
                                            <xsl:value-of select="@name"/>
                                        </xsl:attribute>
                                        <nobr>&amp;nbsp;</nobr></td>
                                </xsl:if>
                            </xsl:for-each>
                        </tr>
                    </tbody>
                </xsl:if>
            </table>
        </div>
    </xsl:template>
    <xsl:template match="/grid/declare">
        <tr>
            <xsl:attribute name="height"><xsl:eval>headerCellHeight</xsl:eval></xsl:attribute>
            <xsl:if test=".[@header!='']" expr="curType==true &amp;&amp; curHeadRow==1">
                <td>
                    <xsl:attribute name="class">column<xsl:eval>totalRows&gt;1?(" column"+totalRows):""</xsl:eval></xsl:attribute>
                    <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("header")</xsl:eval>;</xsl:attribute>
                    <xsl:attribute name="rowspan"><xsl:eval>totalRows</xsl:eval></xsl:attribute>
                    <nobr><xsl:if test=".[@header='checkbox']"><input type="checkbox" id="headerCheckAll"/></xsl:if><xsl:if test=".[@header='radio']">&amp;nbsp;</xsl:if></nobr></td>
            </xsl:if>
            <xsl:if test=".[(@sequence='true') or not(@sequence)]" expr="curType==true &amp;&amp; curHeadRow==1">
                <td>
                    <xsl:attribute name="class">column<xsl:eval>totalRows&gt;1?(" column"+totalRows):""</xsl:eval></xsl:attribute>
                    <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth("sequence")</xsl:eval>;</xsl:attribute>
                    <xsl:attribute name="rowspan"><xsl:eval>totalRows</xsl:eval></xsl:attribute>
                    <nobr>序号</nobr></td>
            </xsl:if>
            <xsl:for-each select=".//*">
                <xsl:if expr="(getcurHeadRow()==curHeadRow)&amp;&amp;(getLocked()==curType)">
                    <xsl:apply-templates select="."/>
                </xsl:if>
            </xsl:for-each>
        </tr>
        <xsl:if expr="curHeadRow&lt;totalRows">
            <xsl:eval>void(curHeadRow++)</xsl:eval>
            <xsl:apply-templates select="/grid/declare"/>
        </xsl:if>
    </xsl:template>
    <xsl:template match="band">
        <xsl:if expr="getAllColumn()&gt;0">
            <td class="band">
                <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth()</xsl:eval>;</xsl:attribute>
                <xsl:attribute name="colspan"><xsl:eval>getAllColumn()</xsl:eval></xsl:attribute>
                <nobr><xsl:eval>getCaption()</xsl:eval></nobr></td>
        </xsl:if>
    </xsl:template>
    <xsl:template match="column[not(@display) or not(@display='none')]">
        <xsl:eval>var tempRowSpan=calcRowSpan()</xsl:eval>
        <td>
            <xsl:attribute name="class">column<xsl:eval>tempRowSpan&gt;1?(" column"+tempRowSpan):""</xsl:eval></xsl:attribute>
            <xsl:attribute name="style">border-width:<xsl:eval>getBorderWidth()</xsl:eval>;height:<xsl:eval>getHeaderHeight()</xsl:eval>px;</xsl:attribute>
            <xsl:attribute name="rowspan"><xsl:eval>tempRowSpan</xsl:eval></xsl:attribute>
            <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
            <xsl:attribute name="canSort"><xsl:value-of select="@canSort"/></xsl:attribute>
            <nobr><xsl:eval>getCaption()</xsl:eval><xsl:if test=".[@mode='boolean']"><input type="checkbox"/></xsl:if></nobr></td>
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
var cellHeight=22;
var headerCellHeight=22;
var totalWidth=1230;
var totalHeight=206;
var begin=0;
var id='grid2';</xsl:script>
    <xsl:script>
        var totalRows=0;
        var curRow;
        var curColumn;
        var curHeadRow;
        var curType;
        var disableRow;
        var disableCells;
        var declare = this.selectSingleNode("/grid/declare");
        function getTotalRows(){
            totalRows=parseInt(declare.getAttribute("_rows"));
        }

        function getBorderWidth(s){
            var top=0;
            var right=1;
            var bottom=1;
            var left=0;

            if(s=="cell"){
                //if(this.getAttribute("name")==this.selectSingleNode("/grid/declare//column[not(@display) or not(@display='none')][0]").getAttribute("name") &amp;&amp; declare.getAttribute("header")==null &amp;&amp; declare.getAttribute("sequence")=="false"){
                //    left=1;
                //}
            }else if(s=="cellheader"){
                //left=1;
            }else if(s=="cellsequence"){
                //if(declare.getAttribute("header")==null){
                //    left=1;
                //}
            }else if(s=="header"){
                top=1;
                left=1;
            }else if(s=="sequence"){
                top=1;
                left = 1;
                //if(declare.getAttribute("header")==null){
                //    left=1;
                //}
            }else{
                top = 1;
                left = 1;
                /*
                if(depth(this)-depth(declare)==1){
                    top=1;
                }
                if(absoluteChildNumber(this)==1 &amp;&amp; declare.getAttribute("header")==null &amp;&amp; declare.getAttribute("sequence")=="false"){
                    left=1;
                    var temp=this;
                    while(depth(temp)-depth(declare)&gt;1){
                        if(absoluteChildNumber(temp.parentNode)==1){
                            temp=temp.parentNode;
                        }else{
                            left=0;
                            break;
                        }
                    }
                }*/
            }
            return top+" "+right+" "+bottom+" "+left;
        }
        function getAllColumn(){
            return this.selectNodes(".//column[not(@display) or not(@display='none')]").length;
        }
        function getHeaderHeight(){
            var row=(totalRows-(depth(this)-depth(declare))+1);
            return row*headerCellHeight;
        }
        function getcurHeadRow(){
            return depth(this)-depth(declare);
        }
        function getLocked(){
            if(this.parentNode==declare){
                return this.getAttribute("locked")=="true";
            }
			else{
                var theAncestor=this.parentNode;
                while(depth(theAncestor)-depth(declare)&gt;1){
                    theAncestor=theAncestor.parentNode;
                }	

				return theAncestor.getAttribute("locked")=="true";				
            }
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
        function calcRowSpan(){
            return totalRows - ( depth(this) - depth(declare) - 1 );
        }
    </xsl:script>
</xsl:stylesheet>
</xml>