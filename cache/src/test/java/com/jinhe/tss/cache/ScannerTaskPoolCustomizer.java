package com.jinhe.tss.cache;

import com.jinhe.tss.cache.extension.workqueue.TaskPoolCustomizer;

public class ScannerTaskPoolCustomizer extends TaskPoolCustomizer {

	protected String getTaskClass() {
		return ScannerTask.class.getName();
	}

}
