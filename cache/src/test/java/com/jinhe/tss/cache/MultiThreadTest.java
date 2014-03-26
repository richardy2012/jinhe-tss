package com.jinhe.tss.cache;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class MultiThreadTest {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	JCache cache;
	
	@Before
	public void setUp() {
		cache = JCache.getInstance();
		
		try {
			Thread.sleep(3000); // 等待初始化完成
		} catch (InterruptedException e) {
		}
	}
	
	@Test
	public void testMultiThreadCheckOutAndIn() {
//		final Pool pool = cache.getTaskPool();
		final Pool pool = (Pool) cache.getThreadPool();
		
		final CountDownLatch begin = new CountDownLatch(1);
		final CountDownLatch end = new CountDownLatch(50);
		final ExecutorService exec = Executors.newFixedThreadPool(20); // 线程数

		for (int i = 0; i < 10000; i++) {
			Runnable run = new Runnable() {
				public void run() {
					try {
						begin.await();
						
						Cacheable taskItem = pool.checkOut(0);
						Thread.sleep(30);
						pool.checkIn(taskItem);

					} catch (Exception e) {
						log.error("出错了!", e);
						
					} finally {
						end.countDown();
					}
				}
			};
			exec.submit(run);
		}
		
		begin.countDown();
		try {
			end.await();
		} catch (InterruptedException e) {
		}
		exec.shutdown();
	}
}
