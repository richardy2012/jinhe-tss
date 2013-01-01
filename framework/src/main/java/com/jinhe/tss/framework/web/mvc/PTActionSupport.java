package com.jinhe.tss.framework.web.mvc;

import java.util.ArrayList;
import java.util.List;

import com.jinhe.tss.framework.web.dispaly.SuccessMessageEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xmlhttp.XmlHttpEncoder;
 
public abstract class PTActionSupport extends BaseActionSupport {
    
    private static final String DEFAULT_SUCCESS_MSG = "操作成功！";
    
    private static final Integer TRUE = new Integer(1);
    
    protected Integer isNew; // 是否新建
    
    protected boolean isCreateNew() {
        return TRUE.equals(isNew);
    }
    
    /**
     * 在action调用service进行保存操作后执行
     * @param isNew 
     *            是否新增节点。如果是新增，则返回新增的树形式节点。    
     * @param returnObj
     * @param treeName
     */
    protected String doAfterSave(boolean isNew, Object returnObj, String treeName){        
        return doAfterSave(isNew, returnObj, treeName, DEFAULT_SUCCESS_MSG);
    }
    
    protected String doAfterSave(boolean isNew, Object returnObj, String treeName, String successMsg){        
        if(isNew){
            List<Object> list = new ArrayList<Object>();
            list.add(returnObj);       
            TreeEncoder encoder = new TreeEncoder(list);
            encoder.setNeedRootNode(false);
            return print(treeName, encoder);
        }
        return printSuccessMessage(successMsg);
    }
    
    /**
     * 向客户端输出信息
     * @param returnDataName
     * @param value
     */
    protected String print(String returnDataName, Object value){
        XmlHttpEncoder xmlHttpEncoder = new XmlHttpEncoder();
        xmlHttpEncoder.put(returnDataName, value);
        xmlHttpEncoder.print(getWriter());
        
        return XML;
    }
    
    /**
     * 向客户端输出信息
     * @param returnDataNames
     * @param values
     */
    protected String print(String[] returnDataNames, Object[] values){
        XmlHttpEncoder xmlHttpEncoder = new XmlHttpEncoder();
        for(int i = 0; i < returnDataNames.length; i++){
            xmlHttpEncoder.put(returnDataNames[i], values[i]);
        }       
        xmlHttpEncoder.print(getWriter());
        
        return XML;
    }
    
    /**
     * 向客户端输出一条成功信息
     */
    protected String printSuccessMessage(){
        return printSuccessMessage(DEFAULT_SUCCESS_MSG);
    }
    
    /**
     * 向客户端输出一条成功信息
     */
    protected String printSuccessMessage(String str){
        new SuccessMessageEncoder(str).print(getWriter());
        return XML;
    }
    
    /**
     * 生成分页信息
     */
    protected String generatePageInfo(int totalRows, int page, int pagesize, int currentPageRows) {
        int totalPages = totalRows / pagesize;
        if(totalRows % pagesize != 0){
            totalPages ++;
        }       
        
        StringBuffer sb = new StringBuffer("<pagelist totalpages=\"").append(totalPages).append("\" totalrecords=\"");
        sb.append(totalRows).append("\" currentpage=\"").append(page).append("\" pagesize=\"").append(pagesize);
        sb.append("\" currentpagerows=\"").append(currentPageRows).append("\" />");
        return sb.toString();
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }
}

