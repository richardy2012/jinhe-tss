package com.jinhe.tss.framework.component.log;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.TxTestSupport;
import com.jinhe.tss.framework.mock.model._Group;
import com.jinhe.tss.framework.mock.model._User;
import com.jinhe.tss.framework.mock.service._IUMSerivce;

public class LogActionTest extends TxTestSupport {

	private LogAction action = new LogAction();

	@Autowired private _IUMSerivce umSerivce;

	public void setUp() throws Exception {
		super.setUp();
	}

	public void testLogAction() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			final int index = i;
			for (int j = 0; j < 10; j++) {
				_Group group = new _Group();
				group.setCode("RD" + index + "_" + j);
				group.setName("研发");
				umSerivce.createGroup(group);

				_User user = new _User();
				user.setGroup(group);
				user.setUserName("JohnXa" + index + "_" + j);
				user.setPassword("123456");
				user.setAge(new Integer(25));
				user.setAddr("New York");
				user.setEmail("john@hotmail.com");
				umSerivce.createUser(user);
			}
		}

		Thread.sleep(3 * 1000);
		
		action.queryLogs4Grid(null, null, new LogQueryCondition(), 1);
	}

}
