package com.jinhe.tss.um.ldap;

import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.directory.server.constants.ServerDNConstants;
import org.apache.directory.server.ldap.LdapServer;

public class LdapUtils {
	
	public static final String DEFAULT_HOST = "localhost";

	public static final String DEFAULT_ADMIN = ServerDNConstants.ADMIN_SYSTEM_DN; // "uid=admin,ou=system";

	public static final String DEFAULT_PASSWORD = "secret";

	public static LdapNetworkConnection createAdminConnection(LdapServer ldapServer) throws Exception {
		int port = ldapServer.getPort();
		LdapNetworkConnection conn = new LdapNetworkConnection(DEFAULT_HOST, port);
		conn.bind(DEFAULT_ADMIN, DEFAULT_PASSWORD);
		return conn;
	}

}
