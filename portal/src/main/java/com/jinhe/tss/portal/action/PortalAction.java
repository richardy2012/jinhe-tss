package com.jinhe.tss.portal.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.web.dispaly.grid.DefaultGridNode;
import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.dispaly.grid.IGridNode;
import com.jinhe.tss.framework.web.dispaly.tree.ILevelTreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.TreeNode;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.engine.FreeMarkerSupportAction;
import com.jinhe.tss.portal.engine.HTMLGenerator;
import com.jinhe.tss.portal.engine.model.PortalNode;
import com.jinhe.tss.portal.engine.releasehtml.MagicRobot;
import com.jinhe.tss.portal.engine.releasehtml.SimpleRobot;
import com.jinhe.tss.portal.entity.IssueInfo;
import com.jinhe.tss.portal.entity.Structure;
import com.jinhe.tss.portal.entity.Theme;
import com.jinhe.tss.portal.entity.permission.PortalPermissionsFull;
import com.jinhe.tss.portal.entity.permission.PortalResourceView;
import com.jinhe.tss.portal.helper.StrictLevelTreeParser;
import com.jinhe.tss.portal.service.IPortalService;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.XMLDocUtil;

@Controller
@RequestMapping("/auth/portal")
public class PortalAction extends FreeMarkerSupportAction {
    
    @Autowired private IPortalService service;

    /**
     * 预览门户
     * 
     * @param portalId
     * @param id
     * @param themeId
     * @param method  browse/view/maintain
     */
    public void previewPortal(HttpServletResponse response, Long portalId, Long themeId, String method, Long id)  {     
        PortalNode portalNode = service.getPortal(portalId, themeId, method);
        HTMLGenerator gen = new HTMLGenerator(portalNode, id, getFreemarkerParser(portalId));

        printHTML(portalId, gen.toHTML(), false);
    }
    
    /**
     * 获取页面上某个版面或者portletInstanse的xml格式内容。
     * 用以替换某块指定的区域（targetId对应的版面（或页面）中布局器的替换区域：navigatorContentIndex）。
     * 菜单点击的时候会调用到本方法。
     */
    public void getPortalXML(HttpServletResponse response, Long portalId, Long themeId, String method, Long id, Long targetId) {
        PortalNode portalNode = service.getPortal(portalId, themeId, method);
        HTMLGenerator gen = new HTMLGenerator(portalNode, id, targetId, getFreemarkerParser(portalId));
        
        StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
        sb.append("<Response><Portlet>").append(gen.toXML()).append("</Portlet></Response>");
         
        printHTML(portalId, sb.toString(), false);
    }

    /**
     * 静态发布匿名访问的门户
     */
    public void staticIssuePortal(HttpServletResponse response, int type, Long id) {
        if(type == 1){ // 发布整个站点
            MagicRobot robot = new MagicRobot(id);
            robot.start();
            String feedback = robot.getFeedback();
            
            printSuccessMessage(feedback);
            
        } else if(type == 2) { // 只发布当前页
            String visitUrl = service.getIssueInfo(id).getVisitUrl();
            new SimpleRobot(visitUrl).start();
            
            printSuccessMessage("页面静态发布门户成功！");
        }
    }
    
    /**
     * 获取所有的Portal对象（取门户结构PortalStructure）并转换成Tree相应的xml数据格式
     */
    public void getAllPortals4Tree(HttpServletResponse response) {
        List<?> data = service.getAllStructures();
        TreeEncoder encoder = new TreeEncoder(data, new StrictLevelTreeParser());
        print("SourceTree", encoder);
    }
    
    public void getOperationsByResource(HttpServletResponse response, Long resourceId) {
        PermissionHelper permissionHelper = PermissionHelper.getInstance();
        
        resourceId = resourceId == null ? PortalConstants.ROOT_ID : resourceId;
        List<String> list = permissionHelper.getOperationsByResource(resourceId, 
                PortalPermissionsFull.class.getName(), PortalResourceView.class);
        
        // 加入授予角色权限
        String portalResourceType = PortalConstants.PORTAL_RESOURCE_TYPE;
		List<?> highOperations = permissionHelper.getHighOperationsByResource( portalResourceType, resourceId, Environment.getOperatorId() );
		if( !highOperations.isEmpty() ) {
            list.add("_1");
		}
        
        print("Operation", "p1,p2," + EasyUtils.list2Str(list) );
    }
    
    /**
     * <p>
     * 获取除portlet应用外的门户结构，并转换成Tree相应的xml数据格式。
     * 应该是先取所有有新增权限的节点，再取它们可见的父节点组装成树，可见的父节点将设为不能被选择。
     * 另外还要过滤掉自身节点为不可选。
     * 
     * 移动到...，复制到...的时候将调用本方法。
     * </p>
     */
    @SuppressWarnings("unchecked")
    public void getActivePortalStructures4Tree(HttpServletResponse response, final Long id) {
        List<ILevelTreeNode> all = (List<ILevelTreeNode>) service.getAllStructures();
        List<ILevelTreeNode> targets = (List<ILevelTreeNode>) service.getTargetStructures();
        
        final List<Long> targetIds = new ArrayList<Long>();
        for( ILevelTreeNode temp : targets ){
            targetIds.add( temp.getId() );
        }
   
        List<ILevelTreeNode> composedTree = composeTargetTree(all, targets);
        TreeEncoder encoder = new TreeEncoder(composedTree, new StrictLevelTreeParser());
        
        final int type = service.getStructure(id).getType();
        encoder.setTranslator( new ITreeTranslator() { 
            
            // 门户结构树转换器：门户结构移动时根据当前节点的type值过滤其它节点是否可以选择
            public Map<String, Object> translate(Map<String, Object> attributes) {
                if( id.equals(attributes.get("id"))){
                    attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                } 
                
                Integer tempType = (Integer)attributes.get("type");
                switch(type){
                case 1: // 移动的是页面，则非门户节点的都不可选
                    if(Structure.TYPE_PORTAL != tempType) {
                        attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                    }
                    break;
                case 2:
                case 3:
                    // 移动的是版面或portlet实例，则门户节点不可选
                    if(Structure.TYPE_PORTAL == tempType) {
                        attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                    }
                    break;
                }                                    
                return attributes;
            }
        });
        encoder.setRootCanSelect(false);
        print("SourceTree", encoder);
    }
    
    /**
     * 将断层的节点的所有父节点补齐
     */
    private List<ILevelTreeNode> composeTargetTree(List<ILevelTreeNode> all, List<ILevelTreeNode> targets){
        if(targets == null || targets.isEmpty())
            return new ArrayList<ILevelTreeNode>();
        
        Map<Long, ILevelTreeNode> entitiesMap = new HashMap<Long, ILevelTreeNode>();
        for( ILevelTreeNode entity : all){
            entitiesMap.put(entity.getId(), entity);
        }
        
        Map<Long, ILevelTreeNode> targetEntitiesMap = new HashMap<Long, ILevelTreeNode>();
        for( ILevelTreeNode entity : targets){
            targetEntitiesMap.put(entity.getId(), entity);
        }
        
        //此处是递归过程，targets会变大，会将断层的节点一直往上取到所有父节点
        for( ILevelTreeNode entity : targets){
            Long parentId = entity.getParentId();
            ILevelTreeNode parent = targetEntitiesMap.get(parentId);
            if(parent == null && !parentId.equals(PortalConstants.ROOT_ID)){
                targets.add(parent = entitiesMap.get(parentId));
            }
        }
        return targets;
    }
           
    /**
     * <p>
     * 获取单个门户结构的节点信息，并转换成XForm相应的xml数据格式；
     * 如果该门户结构是根节点，则要一块取出其对应门户Portal的信息
     * </p>
     */
    public void getPortalStructureInfo(HttpServletResponse response, Long id, Long parentId, int type){
        XFormEncoder encoder;
        if( DEFAULT_NEW_ID.equals(id) ) { // 如果是新增,则返回一个空的无数据的模板
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", type);
            map.put("parentId", parentId);
            encoder = new XFormEncoder(PortalConstants.PORTALSTRUCTURE_XFORM_PATH, map);           
        }
        else {
            Structure info = service.getStructureWithTheme(id);            
            encoder = new XFormEncoder(PortalConstants.PORTALSTRUCTURE_XFORM_PATH, info);
            if(info.isRootPortal()){
                Object[] objs = genComboThemes(info.getPortalId());
                encoder.setColumnAttribute("currentThemeId", "editorvalue", (String) objs[0]);
                encoder.setColumnAttribute("currentThemeId", "editortext",  (String) objs[1]);
                encoder.setColumnAttribute("themeName", "editable", "false");
            }           
        }
        print("DetailInfo", encoder);
    }
    
    private Object[] genComboThemes(Long portalId){
        List<?> data = service.getThemesByPortal(portalId);
        return EasyUtils.generateComboedit(data, "id", "name", "|");
    }
    
    /**
     * <p>
     * 保存门户结构信息，如果该门户结构PortalStructure是根节点，则要一块保存其门户Portal的信息。
     * </p>
     */    
    public void save(HttpServletResponse response, Structure ps, String code) {
        boolean isNew = (ps.getId() == null);
        
        Structure portalStructure;
        if(isNew) {        
            ps.setCode("PS" + System.currentTimeMillis()); // 设置code值 = PS ＋ 当前时间
            portalStructure = service.createStructure(ps);
            
            /**
             * 如果是在新增门户根节点时上传文件，则此时code和Id还没生成。
             * code值=页面随机生成的一个全局变量（注：code为本action的一个单独属性值），id="null"。<br/>
             * 待保存门户根节点再将名字重新根据生成 code + portalId 值命名。
             */
            if( ps.getType().equals(Structure.TYPE_PORTAL) ) {
                File tempDir = Structure.getPortalResourceFileDir(code + "_null");
                if(tempDir.exists()) {
                    tempDir.renameTo(portalStructure.getPortalResourceFileDir());
                }
            } 
        } 
        else {
            portalStructure = service.updateStructure(ps);
        }
            
        doAfterSave(isNew, portalStructure, "SourceTree");
    }
    
    /**
     * <p>
     * 删除门户结构PortalStructure 
     * 如果有子节点，同时删除子节点（递归过程，子节点的子节点......)
     * </p>
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(HttpServletResponse response, @PathVariable("id") Long id) {
        service.deleteStructure(id);
        printSuccessMessage();
    }
    
    /**
     * <p>
     * 停用/启用 门户结构PortalStructure（将其下的disabled属性设为"1"/"0"）
     * 停用时，如果有子节点，同时停用所有子节点（递归过程，子节点的子节点......)
     * 启用时，如果有父节点且父节点为停用状态，则启用父节点（也是递归过程，父节点的父节点......）
     * </p>
     */
    @RequestMapping("/disable/{id}/{state}")
    public void disable(HttpServletResponse response, 
    		@PathVariable("id") Long id, 
    		@PathVariable("state") int state) {
    	
        service.disable(id, state);
        printSuccessMessage();
    }
    
    /**
     * <p>
     * 跨父节点移动门户结构PortalStructure节点。
     * 移动到弹出窗口中选中的门户结构下（一般为"门户、页面、版面"节点）。
     * </p>
     */
	@RequestMapping(value = "/move/{id}/{targetId}", method = RequestMethod.POST)
    public void moveTo(HttpServletResponse response, 
    		@PathVariable("id") Long id, 
    		@PathVariable("targetId") Long targetId) {
		
		if(id.equals(targetId)){
            throw new BusinessException("节点不能移动到自身节点下");
        }
        service.move(id, targetId);
        printSuccessMessage();
    }
    
    /**
     * 排序，同层节点排序
     */
    @RequestMapping(value = "/sort/{id}/{targetId}/{direction}", method = RequestMethod.POST)
	public void sort(HttpServletResponse response, 
            @PathVariable("id") Long id, 
            @PathVariable("targetId") Long targetId,  
            @PathVariable("direction") int direction) {
    	
        service.sort(id, targetId, direction);
        printSuccessMessage();
    }
 
    /**
     * 复制门户节点到不同的父节点下。
     */
    public void copyTo(HttpServletResponse response, Long id, Long targetId) {
        List<?> list = service.copyTo(id, targetId);        
        TreeEncoder encoder = new TreeEncoder(list, new LevelTreeParser());
        encoder.setNeedRootNode(false);
        print("SourceTree", encoder);
    }

    //******************************** 以下为主题管理  ***************************************
    
    /**
     * 获取一个Portal的所有主题
     */
    public void getThemes4Tree(HttpServletResponse response, Long id){
        Structure root = service.getStructureWithTheme(id); 
        
        List<?> themeList = service.getThemesByPortal(root.getPortalId());
        TreeEncoder encoder = new TreeEncoder(themeList);
        final Long defalutThemeId = root.getTheme().getId();
        encoder.setTranslator(new ITreeTranslator() {
            public Map<String, Object> translate(Map<String, Object> attributes) {
                if(defalutThemeId.equals(attributes.get("id"))){
                    attributes.put("isDefault", "1");
                    attributes.put("icon", "../framework/images/default_theme.gif");
                }
                return attributes;
            }
        });
        print("ThemeList", encoder);        
    }
    
    /**
     * 将Portal的一套主题另存为。。。
     */    
    public void saveThemeAs(HttpServletResponse response, Long themeId, String name){
        Theme newTheme = service.saveThemeAs(themeId, name);       
        doAfterSave(true, newTheme, "ThemeList");
    }
    
    public void renameTheme(HttpServletResponse response, Long themeId, String name){
        service.renameTheme(themeId, name);
        printSuccessMessage();
    }
    
    public void specifyDefaultTheme(HttpServletResponse response, Long portalId, Long themeId){
         service.specifyDefaultTheme(portalId, themeId);
         printSuccessMessage();
    }
    public void removeTheme(HttpServletResponse response, Long portalId, Long themeId){
        service.removeTheme(portalId, themeId);
        printSuccessMessage();
    }
    
    public void savePersonalTheme(HttpServletResponse response, Long portalId, Long themeId){
        service.savePersonalTheme(portalId, Environment.getOperatorId(), themeId);
        printSuccessMessage("更改主题成功");
    }
    
    //******************************** 以下为门户发布管理 ***************************************
    public void getAllIssues4Tree(HttpServletResponse response){        
        TreeEncoder encoder = new TreeEncoder(service.getAllIssues()); 
        print("IssueTree", encoder);
    }
    
    public void getIssueInfoById(HttpServletResponse response, Long id){
        XFormEncoder encoder = null;
        if( DEFAULT_NEW_ID.equals(id) ){
            encoder = new XFormEncoder(PortalConstants.ISSUE_XFORM_TEMPLET_PATH);           
        }
        else{
            IssueInfo info = service.getIssueInfo(id);            
            encoder = new XFormEncoder(PortalConstants.ISSUE_XFORM_TEMPLET_PATH, info);
            Object[] objs = genComboThemes(info.getPortal().getId());
            encoder.setColumnAttribute("theme.id", "editorvalue", (String) objs[0]);
            encoder.setColumnAttribute("theme.id", "editortext",  (String) objs[1]);
         
        }        
        print("IssueInfo", encoder);
    }
    
    public void saveIssue(HttpServletResponse response, IssueInfo issueInfo){
        boolean isNew = issueInfo.getId() == null ? true : false;             
        issueInfo = service.saveIssue(issueInfo);         
        doAfterSave(isNew, issueInfo, "PublishTree");
    }
    
    public void removeIssue(HttpServletResponse response, Long id){
        service.removeIssue(id);
        printSuccessMessage();
    }
    
    public void getActivePortals4Tree(HttpServletResponse response){
        List<?> data = service.getActivePortals();
        TreeEncoder encoder = new TreeEncoder(data, new StrictLevelTreeParser());
        encoder.setNeedRootNode(false);
        print("SourceTree", encoder);
    }
    
    public void getThemesByPortal(HttpServletResponse response, Long portalId){
        Object[] objs = genComboThemes(portalId);       
        String returnStr = "<column name=\"themeId\" caption=\"主题\" mode=\"string\" " +
        		" editor=\"comboedit\" editorvalue=\"" + objs[0] + "\" editortext=\"" + objs[1] + "\"/>";
        
        print("ThemeList", returnStr);
    }
    
    public void getActivePagesByPortal4Tree(HttpServletResponse response, Long portalId){
        List<?> data = service.getActivePagesByPortal(portalId);
        TreeEncoder encoder = new TreeEncoder(data, new LevelTreeParser());
        encoder.setNeedRootNode(false);
        print("PageTree", encoder);
    }
       
    //******************************* 以下为门户缓存管理 ***************************************
    public void cacheManage(HttpServletResponse response, Long portalId) {
        List<?> data = service.getThemesByPortal(portalId);
        StringBuffer sb= new StringBuffer("<actionSet>");
        for( Object temp : data ){
            Theme theme = (Theme) temp;
            sb.append("<cacheItem id=\"").append(theme.getId()).append("\" name=\"").append(theme.getName()).append("\" />");
        }
        print("CacheManage", sb.append("</actionSet>").toString());
    }
    
    public void flushCache(HttpServletResponse response, Long portalId, Long themeId) {
        Pool pool = JCache.getInstance().getCachePool(PortalConstants.PORTAL_CACHE);        
        pool.removeObject(portalId + "_" + themeId);
        printSuccessMessage();
    }
    
    //******************************* 获取门户流量信息 ******************************************
    public void getFlowRate(HttpServletResponse response, Long portalId) {
        List<?> rateItems = service.getFlowRate(portalId);
        List<IGridNode> gridList = new ArrayList<IGridNode>();
        for(Iterator<?> it = rateItems.iterator(); it.hasNext();){
            Object[] objs = (Object[]) it.next();
            DefaultGridNode gridNode = new DefaultGridNode();
            gridNode.getAttrs().put("name", objs[0]);
            gridNode.getAttrs().put("rate", objs[1]);
            gridList.add(gridNode);
        }
        
        StringBuffer template = new StringBuffer();
        template.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><grid version=\"2\"><declare sequence=\"true\">");
        template.append("<column name=\"name\" caption=\"页面名称\" mode=\"string\" align=\"center\"/>");
        template.append("<column name=\"rate\" caption=\"访问次数\" mode=\"string\" align=\"center\"/>");
        template.append("</declare><data></data></grid>");
        
        GridDataEncoder gEncoder = new GridDataEncoder(gridList, XMLDocUtil.dataXml2Doc(template.toString()));
        print("PageViewList", gEncoder);
    }
}