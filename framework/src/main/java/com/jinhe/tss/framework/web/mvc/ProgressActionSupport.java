package com.jinhe.tss.framework.web.mvc;

import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.component.progress.ProgressPool;
import com.jinhe.tss.framework.exception.BusinessException;

/** 
 * <p> 支持进度条显示的MVC Action基类 </p>
 * 
 */
public class ProgressActionSupport extends BaseActionSupport {

	protected String code;
	
	/**
     * 获取进度信息
	 * @return
	 */
	public String getProgress(){
		return printScheduleMessage(code);
	}
	
	/**
     * 取消，中止进度
	 * @return
	 */
	public String doConceal(){
		Progress progress = (Progress)ProgressPool.getSchedule(code);
		progress.setIsConceal(true); //设置中止标志
		return printScheduleMessage(code);
	}
	
	protected String printScheduleMessage(String code){
		Progress progress = (Progress)ProgressPool.getSchedule(code);
		if(!progress.isNormal()){
			ProgressPool.removeSchedule(code);
			Throwable t = progress.getException();
			throw new BusinessException("cause:" + t.getCause() + ".Message:" + t.getMessage());
		} 
        if(progress.isConceal())
            throw new BusinessException("取消进度成功");

		Object[] info = progress.getProgressInfo();
		StringBuffer progressInfo = new StringBuffer("<actionSet>");
        progressInfo.append("<percent>"+ info[0] + "</percent>");
		progressInfo.append("<delay>"  + info[1] + "</delay>");
		progressInfo.append("<estimateTime>" + info[2] + "</estimateTime>");
		progressInfo.append("<code>" + code + "</code>");
		progressInfo.append("</actionSet>");
        
        if(progress.isCompleted()){
            ProgressPool.removeSchedule(code); //执行结束则将将进度对象从池中移除
        }
        return print("ProgressInfo", progressInfo.toString());
	}

	public void setCode(String code) {
		this.code = code;
	}
}
