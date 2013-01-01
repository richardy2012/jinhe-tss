package com.jinhe.tss.cms.timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.entity.TimerStrategy;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.ProgressActionSupport;
import com.jinhe.tss.util.EasyUtils;
 
public class TimerAction extends ProgressActionSupport {

	private TimerService timerService;
    
    private TimerCondition condition = new TimerCondition();
    
    /**
     *  初始化定时策略树
     */
    public String initTacticIndex(){
        List<?> allTimerStrategys = timerService.getAllTimerStrategy();
        return print("TacticTree", new TreeEncoder(allTimerStrategys, new LevelTreeParser()));
    }
    
    /**
     * 	新增时间策略
     */
    public String addTacticTime(){
        TimerStrategy tacticIndex = timerService.addTimeStrategy(condition);
        return doAfterSave(true, tacticIndex, "TacticTree");
    }
    
    /**
     * 	新增索引/发布策略/文章过期策略
     */
    public String addTacticIndexAndPublish(){
        TimerStrategy tacticIndex = timerService.addStrategy(condition);
        return doAfterSave(true, tacticIndex, "TacticTree");
    }
    
    /**
     *  删除时间策略
     */
    public String removeTacticTime(){
        timerService.removeTimeStrategy(condition.getTacticId());
        return printSuccessMessage("删除成功！");       
    }
    
    /**
     *  删除 索引/发布 策略
     */
    public String removeTacticIndexAndPublish(){
        timerService.removeStrategy(condition.getTacticId());
        return printSuccessMessage("删除成功！");       
    }
 
    /**
     *  更新时间策略
     */
    public String updateTacticTime(){
        timerService.updateTimeStrategy(condition);
        return printSuccessMessage("修改成功！");       
    }
    
    /**
     *  更新索引/发布策略/文章过期策略
     */
    public String updateTacticIndexAndPublish(){
        timerService.updateTacticIndexAndPublish(condition);
        return printSuccessMessage("修改成功！");       
    }
    
    /**
     *  启用时间策略
     */
    public String startTacticTime(){      
        timerService.startTimeStrategy(condition.getTacticId());
        return printSuccessMessage("启用成功！");       
    }
    
    /**
     * 	启用索引/发布策略/文章过期策略
     */
    public String startTacticIndexAndPublish(){
        timerService.startStrategy(condition.getTacticId());
        return printSuccessMessage("启用成功！");       
    }
    
    /**
     *  停用时间策略
     */
    public String stopTacticTime(){
        timerService.stopTimeStrategy(condition.getTacticId());
        return printSuccessMessage("停用成功！");       
    }
    
    /**
     *  停用索引/发布策略/文章过期策略
     */
    public String stopTacticIndexAndPublish(){
        timerService.stopStrategy(condition.getTacticId());
        return printSuccessMessage("停用成功！");       
    }

    /**
     *  获取索引/发布策略/文章过期策略详细信息
     */
    public String getTacticIndexAndPublish() {
		Object[] data = timerService.getStrategyAndChannels(condition.getTacticId());
		TimerStrategy _tacticIndex = (TimerStrategy) data[0];
		List<?> channelList = (List<?>) data[1];

		String templateUri;
		Integer strategyType = condition.getType();
        if (CMSConstants.TACTIC_INDEX_TYPE.equals(strategyType)) {
			templateUri = CMSConstants.XFORM_TACTIC_INDEX;
		} 
		else if (CMSConstants.TACTIC_PUBLISH_TYPE.equals(strategyType)) {
			templateUri = CMSConstants.XFORM_TACTIC_PUBLISH;
		} 
		else if (CMSConstants.TACTIC_EXPIRE_TYPE.equals(strategyType)) {
			templateUri = CMSConstants.XFORM_EXPIRE_PUBLISH;
		} else {
		    throw new BusinessException("策略类型值不正确。 condition.tacticId=" + strategyType);
		}
		_tacticIndex.setType(strategyType);
		
		XFormEncoder encoder = new XFormEncoder(templateUri, _tacticIndex);

		TreeEncoder channelTreeEncoder = new TreeEncoder(channelList, new LevelTreeParser());
		
		String channelIds = _tacticIndex.getContent();
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
		return print(new String[] { "IndexTacticInfo", "SequenceTree" }, new Object[] { encoder, channelTreeEncoder });
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
    public String getTacticTime(){
        TimerStrategy tacticTime = new TimerStrategy();
        if (condition.getTacticId() != null) {          
            tacticTime = timerService.getStrategyById(condition.getTacticId());
        }     
        XFormEncoder encoder = new XFormEncoder(CMSConstants.XFORM_TACTIC_TIME, tacticTime);
        return print("TimeTacticInfo", encoder);
    }

    /**
     *  即时执行策略
     */
    public String instantTactic(){
        Long strategyId = condition.getTacticId();
        TimerStrategy strategy = timerService.getStrategyById(strategyId);
        String channelIds = strategy.getContent();
        if (null == channelIds) 
            throw new BusinessException("您未选择栏目,请在栏目列表里选择需要的栏目.");
        
        Long parentId = strategy.getParentId();
        TimerStrategy parent = timerService.getStrategyById(parentId);
        strategy.setIndexPath(parent.getIndexPath()); //取父节点的indexPath做为索引路径
        
        strategy.setIncrement(CMSConstants.TRUE.equals(condition.getIncrement()));
        String code = timerService.excuteStrategy(strategy);
        
        return printScheduleMessage(code);  
    }

    public TimerCondition getCondition() { return condition; }

	public void setTimerService(TimerService timerService) {
		this.timerService = timerService;
	}
}

