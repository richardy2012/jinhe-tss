package com.jinhe.tss.framework.component.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinhe.tss.cache.AbstractPool;
import com.jinhe.tss.cache.CacheStrategy;
import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.framework.web.dispaly.grid.DefaultGridNode;
import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.dispaly.grid.IGridNode;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.XMLDocUtil;

@Controller
@RequestMapping("/cache")
public class CacheDisplayAction extends BaseActionSupport {
    
    /** 缓存策略模板目录 */
    final static String CACHESTRATEGY_XFORM_TEMPLET = "template/cache/strategy_xform.xml";
    final static String POOLS_GRID_TEMPLET = "template/cache/pool_grid.xml";
 
    private static JCache cache = JCache.getInstance();
 
    /**
     * 树型展示所有缓存池
     */
    @RequestMapping("/list")
    public void getAllCacheStrategy4Tree(HttpServletResponse response) {
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
                    TreeAttributesMap map = new TreeAttributesMap(stategy.code, stategy.name);
                    map.put("icon", "images/cache.gif");
                    map.put("display", stategy.visible);
                    return map;
                }
            });
        }
        
        TreeEncoder encoder = new TreeEncoder(treeNodeList);
        encoder.setNeedRootNode(false);
        print("CacheTree", encoder);
    }
    
    @RequestMapping("/grid")
    public void getPoolsGrid(HttpServletResponse response) {
    	List<IGridNode> dataList = new ArrayList<IGridNode>();
    	 
    	Set<Entry<String, Pool>> pools = cache.listCachePools(); 
        for(Entry<String, Pool> entry : pools) {
            Pool pool = entry.getValue(); 
            CacheStrategy strategy = pool.getCacheStrategy();
            
            DefaultGridNode gridNode = new DefaultGridNode();
            gridNode.getAttrs().put("code", entry.getKey());
            gridNode.getAttrs().put("name", pool.getName());
            gridNode.getAttrs().put("accessMethod", strategy.getAccessMethod());
            gridNode.getAttrs().put("disabled", strategy.getDisabled());
            gridNode.getAttrs().put("cyclelife", strategy.getCyclelife());
            gridNode.getAttrs().put("interruptTime", strategy.getInterruptTime());
            gridNode.getAttrs().put("poolSize", strategy.getPoolSize());
            gridNode.getAttrs().put("initNum", strategy.getInitNum());
            gridNode.getAttrs().put("requests", pool.getRequests());
            gridNode.getAttrs().put("hitrate", Math.round(pool.getHitRate()) + "%");
            
            if(pool instanceof AbstractPool) {
            	gridNode.getAttrs().put("freeItemNum", ((AbstractPool)pool).getFree().size());
            	gridNode.getAttrs().put("busyItemNum", ((AbstractPool)pool).getUsing().size());
            }
            
            dataList.add(gridNode);
        }
        
        GridDataEncoder gEncoder = new GridDataEncoder(dataList, POOLS_GRID_TEMPLET);
        print("PoolGrid", gEncoder);
    }
    
    /**
     * 获取缓存策略以及缓存池信息
     */
    @RequestMapping("/list/{code}")
    public void getCacheStrategyInfo(HttpServletResponse response, @PathVariable String code) {
        Pool pool = cache.getPool(code);
        CacheStrategy strategy = pool.getCacheStrategy();
        Map<String, Object> strategyProperties = new HashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(strategy, strategyProperties);
        
        XFormEncoder xEncoder = new XFormEncoder(CACHESTRATEGY_XFORM_TEMPLET, strategyProperties); 
        String hitRate = Math.round(pool.getHitRate()) + "%";
        
        Set<Cacheable> cachedItems = pool.listItems();
        long requests = strategy.getPoolInstance().getRequests();
        List<IGridNode> dataList = new ArrayList<IGridNode>();
        for(Cacheable item : cachedItems) {
            int hit = item.getHit();
            Object thisKey = item.getKey();
            int hitrate = (requests == 0) ? 0 : Math.round(((float) hit / requests) * 100f);
            
            DefaultGridNode gridNode = new DefaultGridNode();
            gridNode.getAttrs().put("id", thisKey);
            gridNode.getAttrs().put("key", thisKey);
            gridNode.getAttrs().put("code", code);
            gridNode.getAttrs().put("hit", new Integer(hit));
			gridNode.getAttrs().put("hitRate", hitrate + "%");
            gridNode.getAttrs().put("remark", item.getValue());
            
            if(pool instanceof AbstractPool) {
            	boolean isFree = ( (AbstractPool)pool ).getFree().keySet().contains(thisKey);
            	gridNode.getAttrs().put("state", isFree ? "0" : "1");
            }
            
            dataList.add(gridNode);
        }
        
        StringBuffer template = new StringBuffer();
        template.append("<grid><declare sequence=\"true\">");
        template.append("<column name=\"id\" mode=\"string\" display=\"none\"/>");
        template.append("<column name=\"code\" mode=\"string\" display=\"none\"/>");
        template.append("<column name=\"key\" caption=\"键值\" mode=\"string\" width=\"240px\" />");
        template.append("<column name=\"hit\" caption=\"点击次数\" mode=\"string\" width=\"60px\" align=\"center\"/>");
        template.append("<column name=\"hitRate\" caption=\"点击率\" mode=\"string\" width=\"50px\" align=\"center\"/>");
        template.append("<column name=\"state\" caption=\"状态 \" align=\"center\" mode=\"string\" width=\"60px\" editor=\"comboedit\" editorvalue=\"0|1\" editortext=\"空闲|忙碌\"/>");
        template.append("<column name=\"remark\" caption=\"说明\" mode=\"string\" align=\"center\"/>");
        template.append("</declare><data></data></grid>");
        
        GridDataEncoder gEncoder = new GridDataEncoder(dataList, XMLDocUtil.dataXml2Doc(template.toString()));
           
        int totalRows = cachedItems.size();
        String pageInfo = generatePageInfo(totalRows, 1, totalRows + 1, totalRows); // 加入分页信息，总是只有一页。
        print(new String[]{"CacheStrategy", "CacheItemList", "PageInfo", "HitRate"}, 
                new Object[]{xEncoder, gEncoder, pageInfo, hitRate});
    }
    
    /**
     * 查看详细的缓存项内容。对象XML格式展示
     */
    @RequestMapping("/item/{code}")
    public void viewCachedItem(HttpServletResponse response, 
    		@PathVariable String code, 
    		@RequestParam("key") String key) {
    	
        Cacheable item = cache.getPool(code).getObject(key);
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
    }
    
    @RequestMapping(value = "/item/{code}", method = RequestMethod.DELETE)
    public void removeCachedItem(HttpServletResponse response, 
    		@PathVariable String code, 
    		@RequestParam("key") String key) {
    	
        Pool pool = cache.getPool(code);
		Cacheable item = pool.removeObject(key);
        if(item == null) {
        	printSuccessMessage("该缓存项已经不存在，已经被清空或是已经被刷新！");
        } 
        else {
        	pool.destroyObject(item);
        	printSuccessMessage("成功清除。");
        }
    }
    
    /**
     * 清空释放缓存池
     */
    @RequestMapping("/release/{code}")
    public void releaseCache(HttpServletResponse response, @PathVariable String code){
        cache.getPool(code).flush();
        printSuccessMessage();
    }
    
    /**
     * 初始化缓存池
     */
    @RequestMapping("/init/{code}")
    public void initPool(HttpServletResponse response, @PathVariable String code){
        cache.getPool(code).init();
        printSuccessMessage();
    }
}

