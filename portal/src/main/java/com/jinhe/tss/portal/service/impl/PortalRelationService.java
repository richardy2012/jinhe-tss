package com.jinhe.tss.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.dao.IElementDao;
import com.jinhe.tss.portal.dao.IPortalDao;
import com.jinhe.tss.portal.entity.IssueInfo;
import com.jinhe.tss.portal.entity.PersonalPage;
import com.jinhe.tss.portal.entity.PersonalTheme;
import com.jinhe.tss.portal.entity.Portal;
import com.jinhe.tss.portal.entity.PortalStructure;
import com.jinhe.tss.portal.entity.Theme;
import com.jinhe.tss.portal.entity.ThemeInfo;
import com.jinhe.tss.portal.service.IPortalRelationService;
import com.jinhe.tss.portal.sso.PortalPermissionFilter;
import com.jinhe.tss.util.EasyUtils;
 
public class PortalRelationService implements IPortalRelationService {
    
	protected Logger log = Logger.getLogger(this.getClass());
    
    @Autowired protected IPortalDao portalDao;
    @Autowired protected IElementDao elementDao;

   //********************************  以下为主题管理  ***************************************************************
   public void specifyDefaultTheme(Long portalId, Long themeId) {
       Portal portal = (Portal) portalDao.getEntity(Portal.class, portalId);
       Theme theme = (Theme) portalDao.getEntity(Theme.class, themeId);
       portal.setThemeId(themeId);
       portal.setThemeName(theme.getName());
       portalDao.update(portal);
   }

   public void removeTheme(Long portalId, Long themeId) {
       Portal portal = (Portal) portalDao.getEntity(Portal.class, portalId);
       if(themeId.equals(portal.getThemeId()) || themeId.equals(portal.getCurrentThemeId())) {
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
       Portal portal = (Portal) portalDao.getEntity(Portal.class, theme.getPortalId());
       if(portal.getThemeId().equals(theme.getId())){
           portal.setThemeName(name);
           portalDao.update(portal);
       }
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
       if( !visitUrl.endsWith(PortalPermissionFilter.PORTAL_REDIRECT_URL_SUFFIX) ){
           visitUrl += PortalPermissionFilter.PORTAL_REDIRECT_URL_SUFFIX;
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
   
    public void removePersonalInfo(Long portalId, Long themeId, Long userId, Long pageId) {
        String hql = "from PersonalPage o where o.portalId = ? and themeId = ? and o.pageId = ? and o.userId = ? ";
        portalDao.deleteAll(portalDao.getEntities(hql, portalId, themeId, pageId, userId));
    }

    public void savePersonalInfo(Long portalId, Long themeId, Long userId, Long pageId, String personalXML) {
        // 一个用户对一个页面只能有一套自定义信息，保存新的自定义信息之前需要删除老的
        removePersonalInfo(portalId, themeId, userId, pageId);
        
        PersonalPage  pp = new PersonalPage(portalId, themeId, pageId, userId, personalXML);
        portalDao.createObject(pp);
        
        // 刷新自定义门户的缓存项
        Portal portal = portalDao.getPortalById(portalId);
        PersonalTheme personalTheme = portalDao.getPersonalTheme(portalId);
        if(personalTheme != null) {
            portal.setPersonalThemeId(personalTheme.getThemeId());
        }
        Pool personalPool = JCache.getInstance().getCachePool(PortalConstants.PERSONAL_CACHE);
        personalPool.removeObject("主题：" + portal.getThemeName() + "，用户：" + Environment.getOperatorName());
    }

    public void savePersonalTheme(Long portalId, Long userId, Long themeId) {
        //一个用户对一个门户只能有一套自定义主题，保存新的自定义主题之前需要删除老的
        String hql = "from PersonalTheme o where o.portalId = ? and o.userId = ? ";
        portalDao.deleteAll(portalDao.getEntities(hql, portalId, userId));
        
        PersonalTheme pt = new PersonalTheme(portalId, userId, themeId);
        portalDao.createObject(pt);
    }
    
    public List<?> getPortletInstansesInPortal(Long portalId) {
        String hql  = "from PortalStructure o where o.portalId = ? and o.disabled <> 1 and o.type = ? ";                
        return portalDao.getEntities(hql, portalId, PortalStructure.TYPE_PORTLET_INSTANCE);
    }

    public List<?> getFlowRate(Long portalId) {
    	List<Object> returnList = new ArrayList<Object>();
    	
        String hql = "select p.name, count(f.id) from FlowRate f, PortalStructure p " +
                "where f.pageId = p.id and p.portalId=? group by p.name,p.decode order by p.decode";
        returnList.addAll(portalDao.getEntities(hql, portalId));
        
        hql = "select '合计', count(f.id) from FlowRate f, PortalStructure p where f.pageId = p.id and p.portalId=?";
        returnList.addAll(portalDao.getEntities(hql, portalId));
        
        return returnList;
    }
}