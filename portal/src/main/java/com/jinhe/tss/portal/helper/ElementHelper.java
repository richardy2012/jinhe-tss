package com.jinhe.tss.portal.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.portal.entity.ElementGroup;
import com.jinhe.tss.portal.entity.Layout;
import com.jinhe.tss.portal.service.IElementService;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.XMLDocUtil;

/** 
 * 元素操作的帮助类。主要是元素导入、导出等操作。
 * 
 */
public class ElementHelper {   
    
    /**
     * 从参数配置中获取布局器、修饰器、portlet的参数。
     * 参数的存放形式是:
     *  <params>
     *     <portlet action="">model/portlet/gg299/paramsXForm.xml</portlet>
     *     <decorator moreUrl="">model/decorator/shyggxshq334/paramsXForm.xml</decorator>
     *  </params>
     *  
     * 通过本方法可以获取到portlet实例或者修饰器实例相关的参数。
     * 
     * @param typeName
     * @param params
     * @return
     */
    static String getElementConfigByType(String typeName, String params){
        if(params == null) {
            params = "<params><layout/><portlet/><decorator/></params>";
        }
        
        Document paramsDoc = XMLDocUtil.dataXml2Doc(params);
        return paramsDoc.selectSingleNode("/params//" + typeName.toLowerCase()).asXML();
    }
    
    public static String getPortletInstanseConfig(String params){
        return getElementConfigByType(ElementGroup.PORTLET, params);
    }
    
    public static String getDecoratorInstanseConfig(String params){
        return getElementConfigByType(ElementGroup.DECORATOR, params);
    }
    
    public static String getLayoutInstanseConfig(String params){
        return getElementConfigByType(ElementGroup.LAYOUT, params);
    }
    
    /**
     * 重新组合参数，上面方法的逆过程。
     * @param layoutConfig
     * @param decoratorConfig
     * @return
     */
    public static String createPageOrSectionConfig(String layoutConfig, String decoratorConfig){
        StringBuffer sb  = new StringBuffer("<params>");
        sb.append(layoutConfig).append(decoratorConfig);
        return sb.append("</params>").toString();
    }
    
    public static String createPortletInstanseConfig(String portletConfig, String decoratorConfig){
        StringBuffer sb  = new StringBuffer("<params>");
        sb.append(portletConfig).append(decoratorConfig);
        return sb.append("</params>").toString();
    }
    
    
    /**
     * *******************************************************************************************************************
     * ****************************************      以下为元素导入、导出    ************************************************
     * *******************************************************************************************************************
     */
    
    /**
     * 导入元素，XML格式或者zip包格式
     */
    public static void importElement(IElementService service, File file, IElement newElement, String desDir, String eXMLFile) {
        if (null == file) {
            throw new BusinessException("导入文件为空！");
        }
        
        String fileName = file.getName();
        if (fileName.endsWith(".xml")) {
            IElement element = importXml(service, newElement, file);
            if (element == null) {      
                throw new BusinessException(fileName + "不符合" + eXMLFile + "导入文件规范!!");   
            }
        } 
        else if (fileName.endsWith(".zip")) {
            importZip(service, newElement, file, desDir, eXMLFile);
        }
    }
  
    /**
     * 根据元素XML配置文件，将各个属性设置到元素实体中，保存实体。
     * @param service
     * @param newElement
     * @param file
     * @return
     */
    private static IElement importXml(IElementService service, IElement newElement, File file) {
        Document document = null;
        SAXReader reader = new SAXReader();
        try {
        	reader.setEncoding("UTF-8");
            document = reader.read(file);
        } catch (Exception e) {
            try{
            	reader = new SAXReader();
            	reader.setEncoding("GBK");
                document = reader.read(file);
            }catch (Exception e2) {
            	try{
                	reader = new SAXReader();
                    document = reader.read(file);
                }catch (Exception e3) {
                    throw new BusinessException("文件的编码存在问题，请换成GBK或者UTF-8再重新导入！", e3);
                }
            }
        }
        try {
            Element rootElement = document.getRootElement();
            if(!rootElement.getName().equals(newElement.getElementName())) {
                throw new BusinessException("导入的XML文件不是规定的导入文件，根节点名称不匹配！");
            }
            Element propertyElement = rootElement.element("property");
            newElement.setName(propertyElement.elementText("name"));
            newElement.setDescription(propertyElement.elementText("description"));
            newElement.setVersion(propertyElement.elementText("version"));
            newElement.setDefinition(document.asXML());
            if(newElement instanceof Layout) {
                ((Layout)newElement).setPortNumber(Integer.valueOf(propertyElement.elementText("portNumber")));
            }
        } catch (Exception e) {
            throw new BusinessException("导入文件可能不是规定的" + newElement.getClass().getName() + "导入文件", e);
        }
        return service.saveElement(newElement);
    }
    
    /**
     * 如果是导入zip包，则先将包解压到一个临时文件夹，
     * 然后导入其中的元素XML配置文件，成功导入元素后在重新命名临时文件夹为正式名。
     * @param service
     * @param newElement
     * @param importDir
     * @param desDir
     * @param eXMLFile
     */
    private static void importZip(IElementService service, IElement newElement, File importDir, String desDir, String eXMLFile) {
        File tempDir = new File(desDir + "/" + System.currentTimeMillis());
        try {
			FileHelper.upZip(importDir, tempDir);
		} catch (Exception e) {
			throw new BusinessException("解压文件" + importDir + "到" + desDir + "目录失败!!!", e);
		}
        
        if ( !FileHelper.checkFile(tempDir, eXMLFile) ) {
            FileHelper.deleteFile(tempDir);
            throw new BusinessException("文件导入错误,可能不是规范的" + eXMLFile + "导入文件!!!");
        }
        
        File eXMLFilePath = new File(desDir + "/" + tempDir.getName() + "/" + eXMLFile);
        IElement element = importXml(service, newElement, eXMLFilePath);

        File newFile = new File(desDir + "/" + element.getCode() + element.getId());
        if ( !tempDir.renameTo(newFile) ) {
            FileHelper.deleteFile(tempDir);
            throw new BusinessException("导入元素时路径重命名时出错!!!");
        }
    }
    
    /**
     * 导出一个元素
     * @param modelPath   pms/model/layout(docorator or portlet)的绝对路径
     * @param info        元素实体
     * @param eXMLFile    元素的XML文件名 "/decorator.xml"
     */
    public static void exportElement(String modelPath, IElement info, String eXMLFile) {
        String elementName = EasyUtils.toUtf8String(info.getName());
        String exportFileName;
        String outPath; // 导出zip或xml文件路径
        
        Document doc = XMLDocUtil.dataXml2Doc(info.getDefinition());
        doc.setXMLEncoding(System.getProperty("file.encoding"));
        
        File filePath = FileHelper.findPathByName(new File(modelPath), info.getCode() + info.getId());
        if (null != filePath) {
            // 如果在pms/model/layout(docorator or portlet)文件夹下有该导出的xml文件就覆盖该文件,并以zip的形式导出
            exportFileName = elementName + ".zip";
            
            // 写回原来的文件
            FileHelper.writeXMLDoc(doc, filePath + "/" + eXMLFile); 
            
            //导出成zip文件,并获得zip文件的路径
            outPath = FileHelper.exportZip(modelPath, filePath); 
        }
        else {
            //如果在pms/model/layout(docorator or portlet)文件夹下没有导出的xml文件，则先将文件内容写到一个临时xml文件，再导出该临时文件
            exportFileName = elementName + ".xml";
            FileHelper.writeXMLDoc(doc, outPath = modelPath + "/" + System.currentTimeMillis() + ".xml");
        }
        
        downloadFileByHttp(outPath, exportFileName);
    }
    
    /**
     * 使用http请求下载附件。
     * @param sourceFilePath 导出文件路径
     * @param exportName  导出名字
     */
    public static void downloadFileByHttp(String sourceFilePath, String exportName) {
    	
    	HttpServletResponse response = Context.getResponse();
        response.reset(); // 设置附件下载页面
        response.setContentType("application/octet-stream"); // 设置附件类型
        response.setContentLength((int) new File(sourceFilePath).length()); // 文件长度
        response.setHeader("Content-Disposition", "attachment; filename=\"" + exportName + "\""); // 设置标头
        
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            outStream = response.getOutputStream();
            inStream = new FileInputStream(sourceFilePath);
            
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = inStream.read(b)) != -1) {
                outStream.write(b, 0, len);
                outStream.flush();
            }           
        } catch (IOException e) {
            throw new RuntimeException("导出时发生IOException!!", e);
        } finally {
            try {
            	if(inStream != null){
            		inStream.close();
            	}
            	if(outStream != null){
            		outStream.close();
            	}
                outStream.close();
            } catch (IOException e) {
                throw new RuntimeException("导出时发生IOException!!", e);
            }           
        }
        new File(sourceFilePath).delete();  // 删除资源文件夹下面的zip文件
    }
    
}

