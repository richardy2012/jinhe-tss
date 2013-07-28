package com.jinhe.tss.portal.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
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
import com.jinhe.tss.portal.entity.permission.PortalResourceView;
import com.jinhe.tss.portal.helper.PortalStructureWrapper;
import com.jinhe.tss.portal.helper.StrictLevelTreeParser;
import com.jinhe.tss.portal.service.IPortalService;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.XMLDocUtil;

import freemarker.template.TemplateException;

public class PortalAction extends FreeMarkerSupportAction {

    private Long    id;
    private Long    resourceId;
    private String  name;
    private Integer type;      // 门户结构类型：0，1，2，3
    private Long    parentId;  // 父节点Id，新增时用，门户结构根节点的parentId = 0
    private Long    themeId; 
    
    private Integer disabled;
    private Long    targetId;  // 移动或者排序的目标节点ID
    private int     direction; // ＋1（向下）/ －1（向上）
       
    private String  code;
    private String  method; // browse/view/maintain
    
    private PortalStructureWrapper ps = new PortalStructureWrapper();
    private IssueInfo issueInfo = new IssueInfo();
    
    private IPortalService service;

    /**
     * 预览门户
     * @return
     */
    public void previewPortal()  throws IOException, TemplateException{     
        PortalNode portalNode = service.getPortal(portalId, themeId, method);
        HTMLGenerator gen = new HTMLGenerator(portalNode, id, getFreemarkerParser());

        printHTML(gen.toHTML(), false);
    }
    
    /**
     * 获取页面上某个版面或者portletInstanse的xml格式内容。
     * 用以替换某块指定的区域（targetId对应的版面（或页面）中布局器的替换区域：navigatorContentIndex）。
     * 菜单点击的时候会调用到本方法。
     * @return
     * @throws TemplateException 
     * @throws IOException 
     */
    public void getPortalXML() throws IOException, TemplateException{
        PortalNode portalNode = service.getPortal(portalId, themeId, method);
        HTMLGenerator gen = new HTMLGenerator(portalNode, id, targetId, getFreemarkerParser());
        
        StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
        sb.append("<Response><Portlet>").append(gen.toXML()).append("</Portlet></Response>");
         
        printHTML(sb.toString(), false);
    }

    /**
     * 静态发布匿名访问的门户
     * @return
     */
    public void staticIssuePortal(){
        if(type.intValue() == 1){ // 发布整个站点
            MagicRobot robot = new MagicRobot(id);
            robot.start();
            String feedback = robot.getFeedback();
            
            printSuccessMessage(feedback);
            
        } else if(type.intValue() == 2){ // 只发布当前页
            String visitUrl = service.getIssueInfo(id).getVisitUrl();
            new SimpleRobot(visitUrl).start();
            
            printSuccessMessage("页面静态发布门户成功！");
        }
    }
    
    /**
     * <p>
     * 获取所有的Portal对象（取门户结构PortalStructure）并转换成Tree相应的xml数据格式
     * </p>
     * @return
     */
    public void getAllPortals4Tree() {
        List<?> data = service.getAllPortalStructures();
        TreeEncoder encoder = new TreeEncoder(data, new StrictLevelTreeParser());
        print("SiteTree", encoder);
    }
    
    public void getOperationsByResource() {
        PermissionHelper permissionHelper = PermissionHelper.getInstance();
        
        resourceId = resourceId == null ? PortalConstants.ROOT_ID : resourceId;
        List<String> list = permissionHelper.getOperationsByResource(resourceId, "PortalPermissionsFull", PortalResourceView.class);
        
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
    public void getActivePortalStructures4Tree() {
        List<ILevelTreeNode> all = (List<ILevelTreeNode>) service.getAllPortalStructures();
        List<ILevelTreeNode> targets = (List<ILevelTreeNode>) service.getTargetPortalStructures();
        
        final List<Long> targetIds = new ArrayList<Long>();
        for( ILevelTreeNode temp : targets ){
            targetIds.add( temp.getId() );
        }
   
        List<ILevelTreeNode> composedTree = composeTargetTree(all, targets);
        TreeEncoder encoder = new TreeEncoder(composedTree, new StrictLevelTreeParser());
        
        final int type = service.getPortalStructureById(id).getType();
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
        print("SiteTree", encoder);
    }
    
    /**
     * 将断层的节点的所有父节点补齐
     * @param all
     * @param targets
     * @return
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
    public void getPortalStructureInfo(){
        XFormEncoder encoder;
        if( DEFAULT_NEW_ID.equals(id) ) { // 如果是新增,则返回一个空的无数据的模板
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", type);
            map.put("parentId", parentId);
            map.put("portalId", portalId);
            encoder = new XFormEncoder(PortalConstants.PORTALSTRUCTURE_XFORM_PATH, map);           
        }
        else {
            Structure info = service.getPoratalStructure(id);            
            encoder = new XFormEncoder(PortalConstants.PORTALSTRUCTURE_XFORM_PATH, info);
            if(info.getType().equals(Structure.TYPE_PORTAL)){
                Object[] objs = genComboThemes(info.getPortalId());
                encoder.setColumnAttribute("currentThemeId", "editorvalue", (String) objs[0]);
                encoder.setColumnAttribute("currentThemeId", "editortext",  (String) objs[1]);
                encoder.setColumnAttribute("themeName", "editable", "false");
            }           
        }
        print("SiteInfo", encoder);
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
    public void save(){
        boolean isNew = (ps.getId() == null);
        
        Structure portalStructure;
        if(isNew) {        
            ps.setCode("PS" + System.currentTimeMillis()); // 设置code值 = PS ＋ 当前时间
            portalStructure = service.createPortalStructure(ps);
            
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
            portalStructure = service.updatePortalStructure(ps);
        }
            
        doAfterSave(isNew, portalStructure, "SiteTree");
    }
    
    /**
     * <p>
     * 删除门户结构PortalStructure 
     * 如果有子节点，同时删除子节点（递归过程，子节点的子节点......)
     * </p>
     */
    public void delete(){
        service.delete(id);
        printSuccessMessage();
    }
    
    /**
     * <p>
     * 停用/启用 门户结构PortalStructure（将其下的disabled属性设为"1"/"0"）
     * 停用时，如果有子节点，同时停用所有子节点（递归过程，子节点的子节点......)
     * 启用时，如果有父节点且父节点为停用状态，则启用父节点（也是递归过程，父节点的父节点......）
     * </p>
     */
    public void disable(){
        service.disable(id, disabled);
        printSuccessMessage();
    }
    
    /**
     * <p>
     * 跨父节点移动门户结构PortalStructure节点。
     * 移动到弹出窗口中选中的门户结构下（一般为"门户、页面、版面"节点）。
     * </p>
     */
    public void move() {
        service.move(id, targetId, portalId);
        printSuccessMessage();
    }
    
    /**
     * <p>
     * 排序，同节点下的节点排序（一次只能排一个）
     * </p>
     */
    public void order() {
        service.order(id, targetId, direction);
        printSuccessMessage();
    }
 
    /**
     * <p>
     * 复制门户节点到不同的父节点下。
     * </p>
     */
    public void copyTo() {
        List<?> list = service.copyTo(id, targetId, portalId);        
        TreeEncoder encoder = new TreeEncoder(list, new LevelTreeParser());
        encoder.setNeedRootNode(false);
        print("SiteTree", encoder);
    }

    //******************************** 以下为主题管理  ***************************************
    
    /**
     * 获取一个Portal的所有主题
     */
    public void getThemes4Tree(){
        PortalStructureWrapper rootps = (PortalStructureWrapper) service.getPoratalStructure(id); 
        
        List<?> themeList = service.getThemesByPortal(rootps.getPortalId());
        TreeEncoder encoder = new TreeEncoder(themeList);
        final Long defalutThemeId = rootps.getThemeId();
        encoder.setTranslator(new ITreeTranslator() {
            public Map<String, Object> translate(Map<String, Object> attributes) {
                if(defalutThemeId.equals(attributes.get("id"))){
                    attributes.put("isDefault", "1");
                    attributes.put("icon", "../framework/images/default_theme.gif");
                }
                return attributes;
            }
        });
        print("ThemeManage", encoder);        
    }
    
    /**
     * 将Portal的一套主题另存为。。。
     */    
    public void saveThemeAs(){
        Theme newTheme = service.saveThemeAs(themeId, name);       
        doAfterSave(true, newTheme, "ThemeManage");
    }
    
    public void renameTheme(){
        service.renameTheme(themeId, name);
        printSuccessMessage();
    }
    
    public void specifyDefaultTheme(){
         service.specifyDefaultTheme(portalId, themeId);
         printSuccessMessage();
    }
    public void removeTheme(){
        service.removeTheme(portalId, themeId);
        printSuccessMessage();
    }
    
    public void savePersonalTheme(){
        service.savePersonalTheme(portalId, Environment.getOperatorId(), themeId);
        printSuccessMessage("更改主题成功");
    }
    
    //******************************** 以下为门户发布管理 ***************************************
    public void getAllIssues4Tree(){        
        TreeEncoder encoder = new TreeEncoder(service.getAllIssues()); 
        print("PublishTree", encoder);
    }
    
    public void getIssueInfoById(){
        XFormEncoder encoder = null;
        if( DEFAULT_NEW_ID.equals(id) ){
            encoder = new XFormEncoder(PortalConstants.ISSUE_XFORM_TEMPLET_PATH);           
        }
        else{
            IssueInfo info = service.getIssueInfo(id);            
            encoder = new XFormEncoder(PortalConstants.ISSUE_XFORM_TEMPLET_PATH, info);
            Object[] objs = genComboThemes(info.getPortalId());
            encoder.setColumnAttribute("themeId", "editorvalue", (String) objs[0]);
            encoder.setColumnAttribute("themeId", "editortext",  (String) objs[1]);
         
        }        
        print("PublishInfo", encoder);
    }
    
    public void saveIssue(){
        boolean isNew = issueInfo.getId() == null ? true : false;             
        issueInfo = service.saveIssue(issueInfo);         
        doAfterSave(isNew, issueInfo, "PublishTree");
    }
    
    public void removeIssue(){
        service.removeIssue(id);
        printSuccessMessage();
    }
    
    public void getActivePortals4Tree(){
        List<?> data = service.getActivePortals();
        TreeEncoder encoder = new TreeEncoder(data, new StrictLevelTreeParser());
        encoder.setNeedRootNode(false);
        print("SiteTree", encoder);
    }
    
    public void getThemesByPortal(){
        Object[] objs = genComboThemes(portalId);       
        String returnStr = "<column name=\"themeId\" caption=\"主题\" mode=\"string\" " +
        		" editor=\"comboedit\" editorvalue=\"" + objs[0] + "\" editortext=\"" + objs[1] + "\"/>";
        
        print("ThemeList", returnStr);
    }
    
    public void getActivePagesByPortal4Tree(){
        List<?> data = service.getActivePagesByPortal(portalId);
        TreeEncoder encoder = new TreeEncoder(data, new LevelTreeParser());
        encoder.setNeedRootNode(false);
        print("PageTree", encoder);
    }
       
    //******************************* 以下为门户缓存管理 ***************************************
    public void cacheManage(){
        List<?> data = service.getThemesByPortal(portalId);
        StringBuffer sb= new StringBuffer("<actionSet>");
        for( Object temp : data ){
            Theme theme = (Theme) temp;
            sb.append("<cacheItem id=\"").append(theme.getId()).append("\" name=\"").append(theme.getName()).append("\" />");
        }
        print("CacheManage", sb.append("</actionSet>").toString());
    }
    
    public void flushCache() {
        Pool pool = JCache.getInstance().getCachePool(PortalConstants.PORTAL_CACHE);        
        pool.removeObject(portalId + "_" + themeId);
        printSuccessMessage();
    }
    
    //******************************* 获取门户流量信息 ******************************************
    public void getFlowRate(){
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