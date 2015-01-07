package com.jinhe.tss.framework.persistence.entityaop;

import java.util.Date;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.framework.sso.Environment;

/**
 * <p>
 *  对象操作者信息记录拦截器
 * </p>
 */
@Component("operateInfoInterceptor")
public class OperateInfoInterceptor extends MatchByDaoMethodNameInterceptor {

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		if(args != null) {
            for (int i = 0; i < args.length; i++) {
                int manipulateKind = judgeManipulateKind(invocation.getMethod().getName());
                if (args[i] instanceof IOperatable && (manipulateKind == SAVE || manipulateKind == UPDATE)) {
                   
                    IOperatable operateInfo = (IOperatable) args[i];
                    
                    if(((IEntity)operateInfo).getPK() == null) { // ID为null，说明是新建
                        operateInfo.setCreateTime(new Date());
                        operateInfo.setCreatorId(Environment.getUserId());
                        operateInfo.setCreatorName(Environment.getUserCode());           
                    } 
                    else {
                        operateInfo.setUpdateTime(new Date());
                        operateInfo.setUpdatorId(Environment.getUserId());
                        operateInfo.setUpdatorName(Environment.getUserCode());   
                    }
                }
            }
		}		
        return invocation.proceed();
	}
}

	