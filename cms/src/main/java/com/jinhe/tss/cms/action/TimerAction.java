package com.jinhe.tss.cms.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.entity.TimerStrategy;
import com.jinhe.tss.cms.service.ITimerService;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.ProgressActionSupport;
import com.jinhe.tss.util.EasyUtils;
 
@Controller
@RequestMapping("timer")
public class TimerAction extends ProgressActionSupport {

	@Autowired private ITimerService timerService;
    
    /**
     *  初始化定时策略树
     */
    public void initIndexStrategy() {
        List<?> allTimerStrategys = timerService.getAllTimerStrategy();
        print("StrategyTree", new TreeEncoder(allTimerStrategys, new LevelTreeParser()));
    }
    
    /**
     * 	新增时间策略
     */
    public void addTimeStrategy(TimerStrategy strategy) {
        strategy = timerService.addTimeStrategy(strategy);
        doAfterSave(true, strategy, "StrategyTree");
    }
    
    /**
     * 	新增索引/发布策略/文章过期策略
     */
    public void addStrategy(TimerStrategy strategy) {
        strategy = timerService.addStrategy(strategy);
        doAfterSave(true, strategy, "StrategyTree");
    }
    
    /**
     *  删除时间策略
     */
    public void removeTimeStrategy(Long id) {
        timerService.removeTimeStrategy(id);
        printSuccessMessage("删除成功！");       
    }
    
    /**
     *  删除 索引/发布 策略
     */
    public void removeStrategy(Long id) {
        timerService.removeStrategy(id);
        printSuccessMessage("删除成功！");       
    }
 
    /**
     *  更新时间策略
     */
    public void updateTimeStrategy(TimerStrategy strategy) {
        timerService.updateTimeStrategy(strategy);
        printSuccessMessage("修改成功！");       
    }
    
    /**
     *  更新索引/发布策略/文章过期策略
     */
    public void updateStrategy(TimerStrategy strategy) {
        timerService.updateStrategy(strategy);
        printSuccessMessage("修改成功！");       
    }
    
    /**
     *  启用时间策略
     */
    public void startTimeStrategy(Long id) {      
        timerService.startTimeStrategy(id);
        printSuccessMessage("启用成功！");       
    }
    
    /**
     * 	启用索引/发布策略/文章过期策略
     */
    public void enableStrategy(Long id) {
        timerService.enableStrategy(id);
        printSuccessMessage("启用成功！");       
    }
    
    /**
     *  停用时间策略
     */
    public void stopTimeStrategy(Long id) {
        timerService.stopTimeStrategy(id);
        printSuccessMessage("停用成功！");       
    }
    
    /**
     *  停用索引/发布策略/文章过期策略
     */
    public void disableStrategy(Long id) {
        timerService.disableStrategy(id);
        printSuccessMessage("停用成功！");       
    }

    /**
     *  获取索引/发布策略/文章过期策略详细信息
     */
    public void getStrategy(Long id) {
		Object[] data = timerService.getStrategyAndChannels(id);
		TimerStrategy strategy = (TimerStrategy) data[0];
		List<?> channelList = (List<?>) data[1];

		String templateUri;
		Integer strategyType = timerService.getStrategyById(id).getType();
        if (CMSConstants.STRATEGY_TYPE_INDEX.equals(strategyType)) {
			templateUri = CMSConstants.XFORM_INDEX_STRATEGY;
		} 
		else if (CMSConstants.STRATEGY_TYPE_PUBLISH.equals(strategyType)) {
			templateUri = CMSConstants.XFORM_PUBLISH_STRATEGY;
		} 
		else if (CMSConstants.STRATEGY_TYPE_EXPIRE.equals(strategyType)) {
			templateUri = CMSConstants.XFORM_EXPIRE_STRATEGY;
		} else {
		    throw new BusinessException("策略类型值不正确:" + strategyType);
		}
		strategy.setType(strategyType);
		
		XFormEncoder encoder = new XFormEncoder(templateUri, strategy);

		TreeEncoder channelTreeEncoder = new TreeEncoder(channelList, new LevelTreeParser());
		
		String channelIds = strategy.getContent();
		if ( !EasyUtils.isNullOrEmpty(channelIds) ) {
			final List<String> channelIdList = Arrays.asList(channelIds.split(","));
			final List<Long> parentNodeIds = getParentNodes(channelList, channelIdList);
			
			channelTreeEncoder.setTranslator(new ITreeTranslator() {
				public Map<String, Object> translate(Map<String, Object> attributes) {
					// 父节点打半勾
					if (parentNodeIds.contains(attributes.get("id"))) {
						attributes.put("checktype", "2");
					}
					
					// 栏目节点被选中则打全勾
					if (channelIdList.contains(attributes.get("id").toString())) {
						attributes.put("checktype", "1");
					}
					return attributes;
				}
			});
		}
		print(new String[] { "IndexStrategyInfo", "SequenceTree" }, new Object[] { encoder, channelTreeEncoder });
	}

    private List<Long> getParentNodes(List<?> channelList,  List<String> exsitIds) {
        List<Long> parentNodeIds = new ArrayList<Long>();
        for ( Object temp : channelList ) {
        	Channel channel = (Channel) temp;
        	
			if (exsitIds.contains(channel.getId().toString())) {
				parentNodeIds.add(channel.getParentId());
            }
        }
        return parentNodeIds;
    }
    
    /**
     *  获取时间策略详细信息
     */
    public void getTimeStrategy(Long id) {
        TimerStrategy strategy = new TimerStrategy();
        if ( !CMSConstants.DEFAULT_NEW_ID.equals(id) ) {          
        	strategy = timerService.getStrategyById(id);
        }     
        XFormEncoder encoder = new XFormEncoder(CMSConstants.XFORM_TIME_STRATEGY, strategy);
        print("TimeStrategyInfo", encoder);
    }

    /**
     *  即时执行策略
     * @param id
     * @param increment 是否增量操作  0：否  1：是
     */
    public void instantStrategy(Long id, int increment) {
        TimerStrategy strategy = timerService.getStrategyById(id);
        String channelIds = strategy.getContent();
        if (null == channelIds) {
            throw new BusinessException("您未选择栏目,请在栏目列表里选择需要的栏目.");
        }
        
        Long parentId = strategy.getParentId();
        TimerStrategy parent = timerService.getStrategyById(parentId);
        strategy.setIndexPath(parent.getIndexPath()); //取父节点的indexPath做为索引路径
        
        strategy.setIncrement(CMSConstants.TRUE == increment);
        String code = timerService.excuteStrategy(strategy);
        
        printScheduleMessage(code);  
    }
}

