package com.jinhe.tss.um;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.jinhe.tss.um.module.GroupModuleTest;
import com.jinhe.tss.um.module.RoleModuleTest;
import com.jinhe.tss.um.module.SubAuthorizeModuleTest;
import com.jinhe.tss.um.module.UserModuleTest;
import com.jinhe.tss.um.search.GeneralSearchTest;
import com.jinhe.tss.um.servlet.GetLoginInfoTest;
import com.jinhe.tss.um.servlet.GetPasswordTest;
import com.jinhe.tss.um.servlet.GetPasswordStrengthTest;
import com.jinhe.tss.um.servlet.RegisterTest;
import com.jinhe.tss.um.servlet.ResetPasswordTest;
import com.jinhe.tss.um.sso.FetchPermissionAfterLoginTest;
import com.jinhe.tss.um.sso.UMIdentityGetterTest;
import com.jinhe.tss.um.sso.UMPasswordIdentifierTest;
import com.jinhe.tss.um.zlast.ResourceModuleTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	GroupModuleTest.class,
	UserModuleTest.class,
	RoleModuleTest.class,
	SubAuthorizeModuleTest.class,
	ResourceModuleTest.class,
	FetchPermissionAfterLoginTest.class,
	UMPasswordIdentifierTest.class,
	UMIdentityGetterTest.class,
	GetLoginInfoTest.class,
	GetPasswordTest.class,
	GetPasswordStrengthTest.class,
	RegisterTest.class,
	ResetPasswordTest.class,
	GeneralSearchTest.class,
})
public class AllUMTests {
 
}
