package com.jinhe.tss.portal.engine.releasehtml;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Test;

import com.jinhe.tss.util.XMLDocUtil;

public class Test_FtpClient {
	
	@Test
	public void testFtpUpload() {
        Document doc = XMLDocUtil.createDoc("META-INF/ftpConfig.xml");
        List<Element> ftpServerList = XMLDocUtil.selectNodes(doc, "//ftpServer");
		for ( final Element element : ftpServerList ) {
            new Thread() {
                public void run() {
                    _FtpClient _ftp = new _FtpClient(true);
                    _ftp.ftpUpload(element);
                }
            }.start();
        }
	}

}
