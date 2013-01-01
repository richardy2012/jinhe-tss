package com.jinhe.tss.um.permission;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.persistence.entityaop.MatchByDaoMethodNameInterceptor;

/**
 * 资源自动注册、补齐等维护操作的拦截器。 </br>
 * 拦截资源注册、修改、删除等操作，根据权限模型中依赖关系对被操作的资源进行权限修复。 </br>
 * 
 * 注: UserDao.create or update User 时，无需拦截补齐权限，其权限在GroupUserDao里显示补齐。 </br>
 */
public class ResourcePermissionInterceptor extends MatchByDaoMethodNameInterceptor {
    
    @Autowired private ResourcePermission resourcePermission;
    
    public static final int MOVE = 5;
    
    protected int judgeManipulateKind(String methodName){
        if(methodName.startsWith("move")) {
            return MOVE;
        } 
        else {
        	return super.judgeManipulateKind(methodName);
        }
    }
    
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
        String methodName = invocation.getMethod().getName();
        
        //get...(...)一般为读取操作，无须进行资源注册
		if( args == null || methodName.startsWith("get")){
	        return invocation.proceed();			
		}	
		
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof IResource) {
				switch (judgeManipulateKind(invocation.getMethod().getName())) {
				
				// 注册资源。
		        // 注：修改的时候也会被本拦截器拦住，但IResourcePermission.addResource方法会判断节点的权限是否已经补齐了， 如果已经补齐则不再补齐。 
				// TODO 这种做法需要改进，最好能区分“修改”还是“新增”。
				case SAVE:
					Object returnObj = invocation.proceed();
                    addResource((IResource)args[i]);  //拦截新增资源的权限补齐操作需要在新增保存完成后。
					return returnObj;
				// 移动资源
				case MOVE: 
                    returnObj = invocation.proceed();
                    if(returnObj != null && returnObj instanceof IResource) {
                        moveResource((IResource)returnObj); //拦截移动资源的权限重新补齐操作需要在整个枝移动保存完成后。
                    }
                    return returnObj;
                // 删除资源
				case DELETE:
					deleteResource((IResource)args[i]);
					break;
				}
			}
		}
        return invocation.proceed();
	}
	
	/** 新注册一个资源 */
	protected void addResource(IResource resource) {
		Long resourceId = resource.getId();
		String resourceType = resource.getResourceType();
		if(resourceId == null || resourceType == null) return;
		
		resourcePermission.addResource(resourceId, resourceType);
	}

	/** 资源被移动时修改资源 */
	protected void moveResource(IResource resource){
		Long resourceId = resource.getId();
		String resourceType = resource.getResourceType();
		if(resourceId == null || resourceType == null) return;
		
		resourcePermission.updateResource(resourceId, resourceType);
	}

	/** 删除一个注册资源 */
	protected void deleteResource(IResource resource) {
		Long resourceId = resource.getId();
		String resourceType = resource.getResourceType();
		if(resourceId == null || resourceType == null) return;
		
		resourcePermission.delResource(resourceId, resourceType);		
	}
}

	