package com.jinhe.tss.cms.timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

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

	@Autowired private TimerService timerService;
    
	private Long 	tacticId;  // 策略id
	private Long 	parentId;  // 父节点id
	private Integer type;      // 类型
	
	/** 索引策略 */
	private TimerStrategy strategy = new TimerStrategy(); 
	
	/** 是否增量操作  0：否  1：是 */
	private Integer increment; 
    
    /**
     *  初始化定时策略树
     */
    public void initTacticIndex() {
        List<?> allTimerStrategys = timerService.getAllTimerStrategy();
        print("TacticTree", new TreeEncoder(allTimerStrategys, new LevelTreeParser()));
    }
    
    /**
     * 	新增时间策略
     */
    public void addTacticTime() {
        TimerStrategy tacticIndex = timerService.addTimeStrategy(condition);
        doAfterSave(true, tacticIndex, "TacticTree");
    }
    
    /**
     * 	新增索引/发布策略/文章过期策略
     */
    public void addTacticIndexAndPublish() {
        TimerStrategy tacticIndex = timerService.addStrategy(condition);
        doAfterSave(true, tacticIndex, "TacticTree");
    }
    
    /**
     *  删除时间策略
     */
    public void removeTacticTime() {
        timerService.removeTimeStrategy(condition.getTacticId());
        printSuccessMessage("删除成功！");       
    }
    
    /**
     *  删除 索引/发布 策略
     */
    public void removeTacticIndexAndPublish() {
        timerService.removeStrategy(condition.getTacticId());
        printSuccessMessage("删除成功！");       
    }
 
    /**
     *  更新时间策略
     */
    public void updateTacticTime() {
        timerService.updateTimeStrategy(condition);
        printSuccessMessage("修改成功！");       
    }
    
    /**
     *  更新索引/发布策略/文章过期策略
     */
    public void updateTacticIndexAndPublish() {
        timerService.updateTacticIndexAndPublish(condition);
        printSuccessMessage("修改成功！");       
    }
    
    /**
     *  启用时间策略
     */
    public void startTacticTime() {      
        timerService.startTimeStrategy(condition.getTacticId());
        printSuccessMessage("启用成功！");       
    }
    
    /**
     * 	启用索引/发布策略/文章过期策略
     */
    public void startTacticIndexAndPublish() {
        timerService.startStrategy(condition.getTacticId());
        printSuccessMessage("启用成功！");       
    }
    
    /**
     *  停用时间策略
     */
    public void stopTacticTime() {
        timerService.stopTimeStrategy(condition.getTacticId());
        printSuccessMessage("停用成功！");       
    }
    
    /**
     *  停用索引/发布策略/文章过期策略
     */
    public void stopTacticIndexAndPublish() {
        timerService.stopStrategy(condition.getTacticId());
        printSuccessMessage("停用成功！");       
    }

    /**
     *  获取索引/发布策略/文章过期策略详细信息
     */
    public void getTacticIndexAndPublish() {
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
		print(new String[] { "IndexTacticInfo", "SequenceTree" }, new Object[] { encoder, channelTreeEncoder });
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
    public void getTacticTime() {
        TimerStrategy tacticTime = new TimerStrategy();
        if (condition.getTacticId() != null) {          
            tacticTime = timerService.getStrategyById(condition.getTacticId());
        }     
        XFormEncoder encoder = new XFormEncoder(CMSConstants.XFORM_TACTIC_TIME, tacticTime);
        print("TimeTacticInfo", encoder);
    }

    /**
     *  即时执行策略
     */
    public void instantTactic() {
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
        
        printScheduleMessage(code);  
    }

    public TimerCondition getCondition() { return condition; }

	public void setTimerService(TimerService timerService) {
		this.timerService = timerService;
	}
}

