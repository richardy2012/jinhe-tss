package com.jinhe.tss.framework.component.progress;

/** 
 * <p> 带进度执行反馈信息的业务对象接口 </p>
 * 
 * 进度条执行完成后会返回一个执行结果的反馈信息，
 * 比如_FtpClient上传完后，反馈成功多少、失败多少。
 * 
 */
public interface FeedbackProgressable extends Progressable {
    
	/**
     * 获取执行反馈信息
	 * @param params
	 * @param progress
	 */
    String getFeedback();
}
