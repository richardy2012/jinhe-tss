package com.jinhe.tss.framework.web.dispaly;

/** 
 * <p> 消息编码对象接口 </p> 
 * 
 * 根据请求分别返回XML或HTML
 * 
 */
public interface IMessageEncoder extends IDataEncoder {
	
	/**
	 * <p>
     * 输出成xml字符串
	 * </p>
	 * @return String XML字符串
	 */
	String toHTML();
}
