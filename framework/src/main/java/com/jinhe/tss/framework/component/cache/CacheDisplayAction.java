package com.jinhe.tss.framework.component.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.stereotype.Controller;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.cache.strategy.CacheConstants;
import com.jinhe.tss.cache.strategy.CacheStrategy;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.dispaly.grid.DefaultGridNode;
import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.dispaly.grid.IGridNode;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.PTActionSupport;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.XMLDocUtil;

@Controller
public class CacheDisplayAction extends PTActionSupport {

    private String code;
    private String key;
    
    private static JCache cache = JCache.getInstance();
    
    /**
     * 树型展示缓存策略
     * @return
     */
    public String getAllCacheStrategy4Tree() {
        List<CacheStrategy> strategyList = new ArrayList<CacheStrategy>();
        
        Set<Entry<String, Pool>> pools = cache.listCachePools(); 
        for(Entry<String, Pool> entry : pools) {
            Pool pool = entry.getValue(); 
            strategyList.add(pool.getCacheStrategy());
        }
        
        List<ITreeNode> treeNodeList = new ArrayList<ITreeNode>();
        for(final CacheStrategy stategy : strategyList) {
            treeNodeList.add(new ITreeNode(){
                public TreeAttributesMap getAttributes() {
                    TreeAttributesMap map = new TreeAttributesMap(stategy.getCode(), stategy.getName());
                    map.put("icon", "images/cache.gif");
                    map.put("visible", stategy.getVisible());
                    return map;
                }
            });
        }
        
        TreeEncoder encoder = new TreeEncoder(treeNodeList);
        encoder.setNeedRootNode(false);
        return print("CacheTree", encoder);
    }
    
    /**
     * 获取缓存策略以及缓存池信息
     * @return
     */
    public String getCacheStrategyInfo() {
        CacheStrategy strategy = cache.getCachePool(code).getCacheStrategy();
        Map<String, Object> strategyProperties = new HashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(strategy, strategyProperties);
        
        XFormEncoder xEncoder = new XFormEncoder(CacheConstants.CACHESTRATEGY_XFORM_TEMPLET, strategyProperties); 
        String hitRate = cache.getCachePool(code).getHitRate() + "%";
        
        Set<Cacheable> cachedItems = cache.getCachePool(code).listItems();
        long requests = strategy.getPoolInstance().getRequests();
        List<IGridNode> temp = new ArrayList<IGridNode>();
        String name = null;
        for(Cacheable item : cachedItems) {
            int hit = item.getHit();
            Object thisKey = item.getKey();
            
            DefaultGridNode gridNode = new DefaultGridNode();
            gridNode.getAttrs().put("id", thisKey);
            gridNode.getAttrs().put("key", thisKey);
            gridNode.getAttrs().put("code", code);
            gridNode.getAttrs().put("name", name = (String) BeanUtil.getPropertyValue(item.getValue(), "name"));
            gridNode.getAttrs().put("hit", new Integer(hit));
            gridNode.getAttrs().put("hitRate", ((requests == 0) ? 0 : (((float) hit / requests) * 100f)) + "%");
            temp.add(gridNode);
        }
        
        StringBuffer template = new StringBuffer();
        template.append("<grid=><declare sequence=\"true\">");
        template.append("<column name=\"id\" mode=\"string\" caption=\"项Id\" display=\"none\"/>");
        template.append("<column name=\"code\" mode=\"string\" caption=\"池code\" display=\"none\"/>");
        template.append("<column name=\"key\" caption=\"键值\" mode=\"string\"/>");
        if(name != null) {
            template.append("<column name=\"name\" caption=\"名称\" mode=\"string\" align=\"center\"/>");
        }
        template.append("<column name=\"hit\" caption=\"点击次数\" mode=\"string\" align=\"center\"/>");
        template.append("<column name=\"hitRate\" caption=\"点击率\" mode=\"string\" align=\"center\"/>");
        template.append("<column name=\"remark\" caption=\"说明\" mode=\"string\" align=\"center\"/>");
        template.append("</declare><data></data></grid>");
        
        GridDataEncoder gEncoder = new GridDataEncoder(temp, XMLDocUtil.dataXml2Doc(template.toString()));
           
        return print(new String[]{"CacheInfo", "CacheOption", "PageList", "PoolHitRate"}, 
                new Object[]{xEncoder, gEncoder, createPageInfo(cachedItems.size()), hitRate});
    }
    
    //加入分页信息，只有一页。
    private String createPageInfo(int totalRows){
        int currentPageRows = totalRows;
        int pagesize = totalRows + 1;         
        StringBuffer sb = new StringBuffer("<pagelist totalpages=\"1\" totalrecords=\"");
        sb.append(totalRows).append("\" currentpage=\"1\" pagesize=\"").append(pagesize);
        sb.append("\" currentpagerows=\"").append(currentPageRows).append("\" />");
        
        return sb.toString();
    }
    
    /**
     * 刷新缓存.
     * 如果不能重新加载更新的缓存项，则返回null。
     */
    public String refresh() {
        Pool pool = cache.getCachePool(code);
        Cacheable object = pool.getObject(key);
        if(object == null){
            throw new BusinessException("缓存项不在缓存池中，可能已被清除！");
        }
        
        Cacheable item = pool.reload(object);
        if(item == null){
            return printSuccessMessage("刷新成功，缓存项被清除出缓存。");
        }
        else {
            int hit = item.getHit();
            long requests = pool.getRequests();
            String hitRate = ((requests == 0) ? 0 : (((float) hit / requests) * 100f)) + "%";
            return print("CacheOption", "<row key=\"" + key + "\" hit=\"" + hit + "\" code=\"" + code + "\" hitRate=\"" + hitRate + "\"/>");
        }
    }
    
    /**
     * 查看详细的缓存项内容。对象XML格式展示
     * @return
     */
    public String viewCachedItem(){
        Cacheable item = cache.getCachePool(code).getObject(key);
        if(item != null) {
            String returnStr = "";
            try{
                String valueStr = BeanUtil.toXml(item.getValue());
                returnStr = XMLDocUtil.dataXml2Doc(valueStr).asXML();
            } catch(Exception e) {
                returnStr = "缓存项(" + item.getValue() + ")内容生成XML不成功，无法查看！\n" + e.getMessage();
            }
            print(returnStr);
        }
        else {
            print("该缓存项已经不存在，已经被清空或是已经被刷新！");
        }
            
        return XML;
    }
    
    /**
     * 清空释放缓存池
     * @return
     */
    public String releaseCache(){
        cache.getCachePool(code).flush();
        return printSuccessMessage();
    }
    
    /**
     * 初始化缓存池
     * @return
     */
    public String initPool(){
        cache.getCachePool(code).init();
        return printSuccessMessage();
    }
 
    public void setCode(String code) {
        this.code = code;
    }
 
    public void setKey(String key) {
        this.key = key;
    }
}

