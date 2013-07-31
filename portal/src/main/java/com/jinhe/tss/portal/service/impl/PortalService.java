package com.jinhe.tss.portal.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.dao.IComponentDao;
import com.jinhe.tss.portal.dao.INavigatorDao;
import com.jinhe.tss.portal.dao.IPortalDao;
import com.jinhe.tss.portal.engine.PortalGenerator;
import com.jinhe.tss.portal.engine.model.PortalNode;
import com.jinhe.tss.portal.entity.IssueInfo;
import com.jinhe.tss.portal.entity.Navigator;
import com.jinhe.tss.portal.entity.Structure;
import com.jinhe.tss.portal.entity.Theme;
import com.jinhe.tss.portal.entity.ThemeInfo;
import com.jinhe.tss.portal.entity.ThemePersonal;
import com.jinhe.tss.portal.entity.ThemeInfo.ThemeInfoId;
import com.jinhe.tss.portal.helper.ComponentHelper;
import com.jinhe.tss.portal.service.IPortalService;
import com.jinhe.tss.portal.sso.Filter3PortalPermission;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.XMLDocUtil;
 
@Service("PortalService")
public class PortalService implements IPortalService {

    protected Logger log = Logger.getLogger(this.getClass());
    
    @Autowired private IPortalDao portalDao;
    @Autowired private IComponentDao componentDao;
    @Autowired private INavigatorDao navigatorDao;
    
    //* ******************************************      获取门户结构操作     ******************************************************

    public Structure getStructure(Long id) { 
        return portalDao.getEntity(id); 
    }
   
    public List<?> getAllStructures() { 
        return portalDao.getEntities("from Structure o order by o.decode "); 
    }

    public List<?> getTargetStructures() { 
        return portalDao.getEntities("from Structure o where o.type <> 3 order by o.decode ");
    }

    public List<?> getActivePortals() { 
        return portalDao.getEntities("from Structure o where o.disabled <> 1 and o.type = 0 order by o.decode ");
    }

    public List<?> getActivePagesByPortal(Long portalId) { 
        return portalDao.getEntities("from Structure o where o.portalId = ? and o.disabled <> 1" +
                " and o.type in (1,2) order by o.decode ", portalId);
    }

    @SuppressWarnings("unchecked")
    public List<Structure> getStructuresByPortal(Long portalId) { 
        return (List<Structure>) portalDao.getEntities("from Structure o where o.portalId = ? and o.disabled <> 1 " +
                "and o.type <> 0 order by o.decode ", portalId);
        }
    
    
   //* *********************************************   读取组装门户所需数据   ********************************************************
    
    public PortalNode getPortal(Long portalId, Long selectThemeId, String method) {
        Structure portal = portalDao.getEntity(portalId);
        portalDao.evict(portal);
        
        if( selectThemeId != null ) {
            portal.setTheme(new Theme(selectThemeId));
        }
        
        // 如果是匿名访问, 则直接访问默认门户
        if(Context.getIdentityCard().isAnonymous()) {
            return getNormalPortal(portal);
        }
        
        ThemePersonal personalTheme = portalDao.getPersonalTheme(portalId);
        if(personalTheme != null) {
            Long personalThemeId = personalTheme.getThemeId();
            portal.setTheme(new Theme(personalThemeId));
        }
        
        return getNormalPortal(portal);
    }
    
    /**
     * 缓存池里如果get不到，则生成新的门户对象，返回其clone对象。。
     */
    private PortalNode getNormalPortal(Structure portal) {
        PortalNode portalNode;
        
        String portalCacheKey = portal.getDefaultKey();
        Pool pool = JCache.getInstance().getCachePool(PortalConstants.PORTAL_CACHE);
        Cacheable item = pool.getObject(portalCacheKey);
        if( item != null ) {
            portalNode = (PortalNode) item.getValue();
        } else {
            portalNode = getPortalNode(portal.getId(), portal.getTheme().getId());
            pool.putObject(portalCacheKey, portalNode);
        }
        
        return (PortalNode) portalNode.clone();
    }
 
    private PortalNode getPortalNode(Long portalId, Long themeId){
        Structure root = portalDao.getEntity(portalId);
        portalDao.evict(root);
        
        List<?> themeInfos = portalDao.getEntities("from ThemeInfo t where t.id.themeId = ?", themeId);
        Map<Long, ThemeInfo> themeInfosMap = new HashMap<Long, ThemeInfo>();
        for(Object temp : themeInfos){
            ThemeInfo info = (ThemeInfo) temp;
            themeInfosMap.put(info.getId().getStructureId(), info);
        }
        
        /* 
         * 根据portalId获取一个完整的门户结构，不包括根节点；
         * 并且根据传入的themeId获取各个门户结构相对应该主题的主题信息，设置到门户结构对象上 
         */
        List<Structure> structuresList = getStructuresByPortal(portalId);
        for( Structure ps : structuresList ) {
            portalDao.evict(ps);
            
            ThemeInfo info = themeInfosMap.get(ps.getId());
            ps.setDecorator(info != null ? info.getDecorator() : componentDao.getDefaultDecorator());
            
            /* 
             * portlet实例: portlet参数取存于门户结构中的参数(ps.getParameters())，修饰器参数取主题中的(info.getParameters())
             * portlet和修饰器的参数分开来放，如果portlet实例的参数也存放于主题信息中的话，那么修改参数将非常麻烦，需要一个个主题修改过来。 
             */
            if(ps.isPortletInstanse()) {                   
                String portletParam = ComponentHelper.getPortletConfig(ps.getParameters());
                String docoratorParam = "<decorator/>" ;
                if( info != null ) { 
                    docoratorParam = ComponentHelper.getDecoratorConfig(info.getParameters());
                }
                ps.setParameters(ComponentHelper.createPortletInstanseConfig(portletParam, docoratorParam));
            } 
            else{
                // 页面和版面的参数取各主题信息的。 页面和版面的布局器和修饰器取默认主题中相应布局器和修饰器
                ps.setDefiner (info != null ? info.getLayout() : componentDao.getDefaultLayout());
                ps.setParameters(info != null ? info.getParameters() : "<params><layout/><portlet/><decorator/></params>");
            }
        }  

        Object[] components = portalDao.getPortalComponents(portalId, themeId);
        PortalNode node = PortalGenerator.genPortalNode(root, structuresList, components);
        
        if(node == null) {
            throw new BusinessException("获取门户结构树【PortalGenerator.genPortalNode()】失败！");
        }
        return node;
    }

    public Structure getStructureWithTheme(Long id) {
        Structure ps = portalDao.getEntity(id);
        if(ps.isRootPortal()) {
            return ps;
        }
        
        // 加载主题信息
        Structure portal = portalDao.getEntity(ps.getPortalId());
        ThemeInfoId themeInfoId = new ThemeInfoId(portal.getCurrentTheme().getId(), id);
        ThemeInfo themeInfo = (ThemeInfo) portalDao.getEntity(ThemeInfo.class, themeInfoId);
        if( themeInfo == null ) {
            // 如果该门户结构在当前主题下找不到主题信息，则取默认的修饰和布局
            themeInfo = new ThemeInfo();
            themeInfo.setDecorator(componentDao.getDefaultLayout());
            themeInfo.setLayout(componentDao.getDefaultDecorator());
        }
        
        ps.setDecorator(themeInfo.getDecorator());
        
        // 如果是portlet实例，则取主题的修饰器参数和门户结构上的portlet参数做为新的参数配置。（portlet参数不分主题，只保存在门户结构表中）。
        if( ps.isPortletInstanse() ) {
            String portletParam = ComponentHelper.getPortletConfig(ps.getParameters());
            String docoratorParam = ComponentHelper.getDecoratorConfig(themeInfo.getParameters());
            ps.setParameters(ComponentHelper.createPortletInstanseConfig(portletParam, docoratorParam));
        } 
        // 只有不是portlet实例才直接用主题表中的参数信息（页面、版面的布局修饰器等信息）
        else {
            ps.setDefiner(themeInfo.getLayout());
            ps.setParameters(themeInfo.getParameters());
        }
        
        return ps;
    } 

    public Structure createStructure(Structure ps) {
        saveStructure(ps);
        
        if( ps.isRootPortal() ) {  // 新增门户根节点
            ps.setPortalId(ps.getId());
            
            // 先为新门户新建一套主题
            Theme theme = new Theme();
            theme.setName(ps.getTheme().getName());
            theme.setPortalId(ps.getPortalId());
            theme = (Theme) portalDao.createObject(theme);
            
            ps.setTheme(theme);
            ps.setCurrentTheme(theme);
            portalDao.update(ps);
            
            /* 默认新增一个菜单根节点，专门用于新建门户的菜单管理 */
            Navigator portalMenu = new Navigator();
            portalMenu.setType(Navigator.TYPE_MENU);
            portalMenu.setName(ps.getName());
            portalMenu.setPortalId(ps.getPortalId());
            portalMenu.setParentId(PortalConstants.ROOT_ID);
            portalMenu.setSeqNo(navigatorDao.getNextSeqNo(portalMenu.getParentId()));
            navigatorDao.save(portalMenu);
        }
        
        saveThemeInfo(ps);
        return ps;
    }
    
    public Structure updateStructure(Structure ps) {
        saveStructure(ps);
        saveThemeInfo(ps);
        return ps;
    }

    /**
     * 因为decode生成需要拦截dao的方法（执行前拦截），如果在dao中设置seqNo，会导致拦截时还没有生成seqNo。
     */
    private Structure saveStructure(Structure ps){
        if(ps.getId() == null) {
            ps.setSeqNo(portalDao.getNextSeqNo(ps.getParentId()));
        }
        ps = portalDao.saveStructure(ps);
        
        if(ps.getCode() == null) {
            ps.setCode(ps.getPortalId() + "_" + ps.getId());
        }
        
        return ps;
    }

    /**
     * 保存主题信息。
     * 注：保存门户结构主题信息的同时，门户结构中也保存了最近一次修改的主题信息。
     * @param ps
     */
    private void saveThemeInfo(Structure ps){
        // 门户节点没有主题信息
        if(ps.isRootPortal()) return;
        
        Structure portal = portalDao.getEntity(ps.getPortalId());
        Long currentThemeId = portal.getCurrentTheme().getId();

        ThemeInfo info = new ThemeInfo();
        info.setId(new ThemeInfoId(currentThemeId, ps.getId()));
        
        info.setDecorator(ps.getDecorator());
        
        if( ps.isPage() || ps.isSection() ) { // 页面、版面
            info.setLayout(ps.getDefiner());
        }
        
        /* 
         * 参数信息即可保存于门户结构上，也可保存于主题信息。
         * 如果考虑主题自定义，则优先使用存于主题上的各组件参数信息。
         */
        info.setParameters(ps.getParameters());
        
        portalDao.createObject(info);
    }

    public void deleteStructure(Long id) {
        Structure ps = portalDao.getEntity(id);
        
        // 删除一个枝
        List<Structure> branch = portalDao.getChildrenById(id); 
        for( Structure node : branch ) {
            portalDao.deleteStructure(node);
        }
 
        // 如果删除的是门户：
        if( ps.isRootPortal() ) {
            Long portalId = ps.getPortalId();
            
            // 1、删除门户的菜单信息
            List<?> menus = portalDao.getEntities("from Navigator o where o.portalId=?", portalId);
            for( Object temp : menus ){
                navigatorDao.deleteNavigator((Navigator) temp);
            }
 
            // 2、删除主题信息 
            List<?> themeList = getThemesByPortal(portalId);
            portalDao.deleteAll(themeList);
            
            // 3、则将原门户的上传文件一并删除，删除上传文件前需确保所有逻辑数据已经删除
            FileHelper.deleteFilesInDir("", ps.getPortalResourceFileDir());
        }
    }

    public void disable(Long id, Integer disabled) {
        Structure ps = portalDao.getEntity(id);
        List<Structure> list;
        
        // 如果是启用或者操作的是门户根节点，则处理操作节点以下的所有子节点
        if(disabled.equals(PortalConstants.TRUE) || ps.isRootPortal()){
            list = portalDao.getChildrenById(id, PortalConstants.PORTAL_STOP_OPERRATION );
        }  
        else { // 启用向上
            list = portalDao.getParentsById(id, PortalConstants.PORTAL_START_OPERRATION); 
        }
        
        for( Structure entity : list ){
            entity.setDisabled(disabled);
            portalDao.update(entity);
        }
    }

    public void sort(Long id, Long targetId, int direction) {
        portalDao.sort(id, targetId, direction);
    }

    public void move(Long psId, Long targetId) {
        Structure ps = portalDao.getEntity(psId);
        Structure parent = portalDao.getEntity(targetId);
        
        // 判断是否是跨门户移动，如果portalId不相等，则说明是跨门户移动
        Long targetPortalId = parent.getPortalId();
        boolean isAcrossPortal = !ps.getPortalId().equals(targetPortalId);

        List<Structure> sons = portalDao.getChildrenById(psId, PortalConstants.PORTAL_DEL_OPERRATION);
        
        ps.setSeqNo(portalDao.getNextSeqNo(targetId));
        ps.setParentId(targetId);
        portalDao.moveStructure(ps);

        // 如果是跨门户移动，则删除和本子节点相关的菜单和主题信息
        if( isAcrossPortal ){
            for( Structure temp : sons ){
                temp.setPortalId(targetPortalId);
                portalDao.updateWithoutFlush(temp);
                clearRelation(temp.getId());
            }
            
            // 如果是跨门户且复制的是页面，则一带将页面的资源复制过去
            if(ps.isPage()){ 
                Structure targetPortal = portalDao.getEntity(targetPortalId);
                Structure sourcePortal = portalDao.getEntity(ps.getPortalId());
                copyPageResources(sourcePortal, targetPortal, ps.getSupplement());
            }
        }
        
        // 如果目标节点是停用的，则移动的枝全部停用
        if(parent.getDisabled().equals(PortalConstants.TRUE)) {
            disable(psId, PortalConstants.TRUE);
        }
    }
    
    private void clearRelation(Long id) {
        portalDao.deleteAll(portalDao.getEntities("from Navigator o where o.targetId = ? or o.contentId = ?", id, id));
        portalDao.deleteAll(portalDao.getEntities("from ThemeInfo o where o.id.portalStructureId = ?", id));
    }
    
    /* 复制页面下挂载的资源文件，css/js等 */
    private void copyPageResources(Structure sourcePortal, Structure targetPortal, String supplement){
        if(supplement == null)  return;
        
        File dir = sourcePortal.getPortalResourceFileDir();
        if( dir.exists() ) {
            Document doc = XMLDocUtil.dataXml2Doc(supplement);
            String script = doc.selectSingleNode("//page/script/file").getText();
            String style  = doc.selectSingleNode("//page/style/file").getText();

            List<String> files = new ArrayList<String>();
            files.addAll(Arrays.asList(script.split(",")));
            files.addAll(Arrays.asList(style.split(",")));

            File newDir = targetPortal.getPortalResourceFileDir();
            for( String jsCssFile : files ){
                File file = new File(dir.getPath() + "/" + jsCssFile);
                if(file.isFile()) {
                    FileHelper.copyFile(newDir, file);
                }
            }
        }
    }


    // * ************************************************************************************************************************
    // * ***************************    以下为门户结构"复制、复制到"操作   *************************************************************
    // * ************************************************************************************************************************

    public List<Structure> copyTo(Long id, Long targetId){
        Structure sourceNode = portalDao.getEntity(id);
        Structure targetNode = portalDao.getEntity(targetId);
        Long targetPortalId = targetNode.getPortalId();
        
        if( !sourceNode.isPortletInstanse() ) { 
            // 复制到可见的节点
            List<Structure> children = portalDao.getChildrenById(id, PortalConstants.PORTAL_VIEW_OPERRATION); 
            children.remove(sourceNode);
            sourceNode.compose(children);
        }
        portalDao.evict(sourceNode);

        // 是否跨门户复制
        Long sourcePortalId = sourceNode.getPortalId();
        boolean isAcrossPortal = targetPortalId.equals(sourcePortalId);
        if( isAcrossPortal ) {
            sourceNode.setPortalId(targetPortalId);
        }
        
        //如果目标节点和原父节点是同一个，则当"复制"操作处理，name前面加前缀
        if(sourceNode.getParentId().equals(targetId)) {
            sourceNode.setName(PortalConstants.COPY_PREFIX + sourceNode.getName());
        }
        sourceNode.setParentId(targetId);

        List<Structure> returnList = new ArrayList<Structure>();
        boolean isTargetDisableed = PortalConstants.TRUE.equals(targetNode.getDisabled());  // 目标父节点是否为停用状态
        copyNodeOneByOne(sourceNode, returnList, isAcrossPortal, isTargetDisableed);

        if( isAcrossPortal && sourceNode.isPage() ) {
            Structure sourcePortalNode = portalDao.getEntity(sourcePortalId);
            Structure targetPortalNode = portalDao.getEntity(targetPortalId);
            copyPageResources(sourcePortalNode, targetPortalNode, sourceNode.getSupplement());
        }

        return returnList;
    }

    /**
     * 递归执行逐个复制到
     * 
     * @param sourcePs
     * @param returnList
     * @param isAcrossPortal
     *              是否跨门户复制到
     * @param isTargetDisableed
     *              目标父节点是否为停用状态
     */
    private void copyNodeOneByOne(Structure sourcePs, List<Structure> returnList, 
            boolean isAcrossPortal, boolean isTargetDisableed) {
        
        Long sourceId = sourcePs.getId(); // 复制源的ID
        
        sourcePs.setId(null);
        sourcePs.setCode(null);
        if(isTargetDisableed) {
            sourcePs.setDisabled(PortalConstants.TRUE);
        }
        Structure newPs = saveStructure(sourcePs);
        Long targetPortalId = newPs.getPortalId();

        //如果是复制到同一门户下的其他节点，则需要将主题信息一块复制出来。跨门户复制则不需要。
        if( !isAcrossPortal ) {
            List<?> themes = portalDao.getThemesByPortal(targetPortalId);
            for( Object temp : themes ){
                Theme theme = (Theme) temp;
                ThemeInfoId themeInfoId = new ThemeInfoId(theme.getId(), sourceId);
                ThemeInfo info = (ThemeInfo) portalDao.getEntity(ThemeInfo.class, themeInfoId);
                if(info != null){
                    portalDao.evict(info);
                    info.getId().setStructureId(newPs.getId());
                    portalDao.createObject(info);
                }
            }
        }

        returnList.add(newPs);
        for( Structure child : sourcePs.getChildren()){
            portalDao.evict(child);
            child.setPortalId(targetPortalId);
            child.setParentId(newPs.getId());
            copyNodeOneByOne(child, returnList, isAcrossPortal, isTargetDisableed);
        }
    }
    
    
   /****************************   门户相关的（包括门户主题、门户发布等）的相关维护  *************************************************/
   
    //********************************  以下为主题管理  ***************************************************************
    
    public void specifyDefaultTheme(Long portalId, Long themeId) {
        Structure portal = portalDao.getEntity(portalId);
        Theme theme = (Theme) portalDao.getEntity(Theme.class, themeId);
        portal.setTheme(theme);
        portalDao.update(portal);
    }

    public void removeTheme(Long portalId, Long themeId) {
        Structure portal = portalDao.getEntity(portalId);
        if(themeId.equals(portal.getTheme().getId()) || themeId.equals(portal.getCurrentTheme().getId())) {
            throw new BusinessException("该主题为门户的默认主题或者当前主题，正在使用中，删除失败！");
        }

        portalDao.delete(portalDao.getEntity(Theme.class, themeId));
    }

    public Theme saveThemeAs(Long themeId, String themeName){
        Theme theme = (Theme) portalDao.getEntity(Theme.class, themeId);
        portalDao.evict(theme);
        theme.setName(themeName);
        theme.setId(null);
        theme = (Theme) portalDao.createObject(theme);

        List<?> list = portalDao.getEntities("from ThemeInfo o where o.id.themeId = ?", themeId);
        for( Object temp : list ){
            ThemeInfo info = (ThemeInfo) temp;
            portalDao.evict(info);
            
            info.getId().setThemeId(theme.getId());
            portalDao.createObject(info);
        }
        return theme;
    }

    public List<?> getThemesByPortal(Long portalId) {
        return portalDao.getThemesByPortal(portalId);
    }

    public void renameTheme(Long themeId, String name) {
        if( EasyUtils.isNullOrEmpty(name) ) {
            throw new BusinessException("主题名称不能为空");
        }
        
        Theme theme = (Theme) portalDao.getEntity(Theme.class, themeId);
        theme.setName(name);
        portalDao.update(theme);
    }

    //********************************  以下为门户发布管理  **************************************************************

    public IssueInfo getIssueInfo(String visitUrl) {
        List<?> list = portalDao.getEntities("from IssueInfo o where o.visitUrl = ?", visitUrl);
        if( list.isEmpty() ) {
            throw new BusinessException("访问地址有误，找不到相应的门户发布消息。");
        }
        return (IssueInfo) list.get(0);
    }

    public List<?> getAllIssues() {
        return portalDao.getEntities( "from IssueInfo o order by o.portalId " );
    }

    public IssueInfo saveIssue(IssueInfo issueInfo) {
        String visitUrl = issueInfo.getVisitUrl();
        if( !visitUrl.endsWith(Filter3PortalPermission.PORTAL_REDIRECT_URL_SUFFIX) ){
            visitUrl += Filter3PortalPermission.PORTAL_REDIRECT_URL_SUFFIX;
            issueInfo.setVisitUrl(visitUrl);
        }
        
        List<?> list = portalDao.getEntities("from IssueInfo o where o.visitUrl = ?", visitUrl);
        if(issueInfo.getId() == null) {
            if( list.size() > 0) {
                throw new BusinessException("相同的映射地址已经存在，请更换。");
            }
            return (IssueInfo) portalDao.createObject(issueInfo);
        } 
        else {
            if( list.size() > 0) {
                IssueInfo temp = (IssueInfo) list.get(0);
                if( !temp.getId().equals(issueInfo.getId()) ) {
                    throw new BusinessException("相同的映射地址已经存在，请更换。");
                }
            }
            
            portalDao.update(issueInfo);
            return issueInfo ;
        }
    }

    public void removeIssue(Long id) {
        portalDao.delete(IssueInfo.class, id);
    }

    public IssueInfo getIssueInfo(Long id) {
        return (IssueInfo) portalDao.getEntity(IssueInfo.class, id);
    }
    
    //******************************** 以下为门户自定义管理 ***************************************************************

    public void savePersonalTheme(Long portalId, Long userId, Long themeId) {
         // 一个用户对一个门户只能有一套自定义主题，保存新的自定义主题之前需要删除老的
         String hql = "from ThemePersonal o where o.portalId = ? and o.userId = ? ";
         portalDao.deleteAll(portalDao.getEntities(hql, portalId, userId));
         
         ThemePersonal pt = new ThemePersonal(portalId, userId, themeId);
         portalDao.createObject(pt);
     }
  
    //***********************************  门户流量统计获取 ***************************************************************
    
    public List<?> getFlowRate(Long portalId) {
         List<Object> returnList = new ArrayList<Object>();
         
         String hql = "select p.name, count(f.id) from FlowRate f, Structure p " +
                 "where f.pageId = p.id and p.portalId=? group by p.name,p.decode order by p.decode";
         returnList.addAll(portalDao.getEntities(hql, portalId));
         
         hql = "select '合计', count(f.id) from FlowRate f, Structure p where f.pageId = p.id and p.portalId=?";
         returnList.addAll(portalDao.getEntities(hql, portalId));
         
         return returnList;
     }
}