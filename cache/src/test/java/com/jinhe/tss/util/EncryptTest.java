/* ==================================================================   
 * Created [2009-4-27 下午11:32:55] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2009-2012 
 * ================================================================== 
 */

package com.jinhe.tss.util;

import junit.framework.TestCase;
 
public class EncryptTest extends TestCase {

	InfoEncoder encrypt = new InfoEncoder();
	EncryptTestThreadGroup group = new EncryptTestThreadGroup();

	protected void setUp() throws Exception {
		super.setUp();
	}
 
	protected void tearDown() throws Exception {
		super.tearDown();
	}
 
	public final void testCreateEncryptor() {
		for (int i = 0; i < 10; i++) {
			new Thread(group, "encryptor") {
				public void run() {
					encrypt.createEncryptor("testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest");
				}
			}.start();
		}
	}
 
	public final void testCreateDecryptor() {
		try {
			final String str = encrypt.createEncryptor("testtesttesttesttesttest");
			for (int i = 0; i < 10; i++) {
				new Thread(group, "decryptor") {
					public void run() {
						encrypt.createDecryptor(str);
					}
				}.start();
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}

class EncryptTestThreadGroup extends ThreadGroup {

	public EncryptTestThreadGroup() {
		super("EncryptTestThreadGroup");
	}

	/**
	 * <p>
	 * 异常处理
	 * </p>
	 * @param t
	 * @param e
	 * @see java.lang.ThreadGroup#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	public void uncaughtException(Thread t, Throwable e) {
		TestCase.fail("Thread:" + t.getName() + " " + e.getMessage());
	}
}
