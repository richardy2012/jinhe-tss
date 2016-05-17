package com.jinhe.tss.framework.img;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * 生成图形验证码、条形码、二维码等图形
 * http://localhost:9000/tss/imgapi/ckcode/8341
 * http://localhost:9000/tss/imgapi/barcode/8341
 * http://localhost:9000/tss/imgapi/qrcode/8341
 */
@Controller
@RequestMapping("/imgapi")
public class ImageCodeAPI {
	
	/**
	 * 生成图形验证码：http://localhost:9000/tss/imgapi/ckcode/8341
	 */
	@RequestMapping(value = "/ckcode/{code}", method = RequestMethod.GET)
	public void createCKCodeImg(@PathVariable("code") String code,
			HttpServletRequest request, HttpServletResponse response) {
 
		ServletOutputStream outputStream = null;
		int width = 62, height = 30;
        
		try {   
	        Font font = new Font("Serif", Font.ITALIC, 24);   
	        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);   
	        Graphics2D g2 = (Graphics2D)bi.getGraphics();   
	        g2.setBackground(Color.WHITE);   
	        g2.clearRect(0, 0, width, height);   
	        g2.setPaint(Color.RED);   
	        g2.setFont(font);
	           
	        FontRenderContext context = g2.getFontRenderContext();   
	        Rectangle2D bounds = font.getStringBounds(code, context);   
	        double x = (width - bounds.getWidth()) / 2.5;   
	        double y = (height - bounds.getHeight()) / 1.2;   
	        double baseY = y - bounds.getY();   
	           
	        g2.drawString(code, (int)x, (int)baseY);   
	        
	        outputStream = response.getOutputStream(); 
	        ImageIO.write(bi, "jpg", outputStream); 
	        
		} catch (IOException e) {
		} finally {
			if(outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) { }
			}
		}	
	}
	
	/**
	 * 生成条形码：http://localhost:9000/tss/imgapi/barcode/8341
	 */
	@RequestMapping(value = "/barcode/{code}", method = RequestMethod.GET)  
    public void createBarCodeImg(@PathVariable("code") String code, 
    		HttpServletRequest request, HttpServletResponse response) throws IOException { 
   
        generate(code, response.getOutputStream());
    }   
 
    /** 生成到流 */
    public static void generate(String msg, OutputStream ous) {
        if (StringUtils.isEmpty(msg) || ous == null) return;
 
        Code39Bean bean = new Code39Bean();
        
        final int dpi = 150; // 精细度
        final double moduleWidth = UnitConv.in2mm(1.0f / dpi); // module宽度
 
        // 配置对象
        bean.setModuleWidth(moduleWidth);
        bean.setWideFactor(5);
        bean.doQuietZone(false);
 
        try {
            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, "image/png", dpi,
                    BufferedImage.TYPE_BYTE_BINARY, false, 0);
            // 生成条形码
            bean.generateBarcode(canvas, msg);
            // 结束绘制
            canvas.finish();
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /** 生成文件 */
    public static File generateFile(String msg, String path) {
        File file = new File(path);
        try {
            generate(msg, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
 
	/**
	 * 生成二维码码：http://localhost:9000/tss/imgapi/qrcode/8341
	 */
    @RequestMapping(value = "/qrcode/{code}", method = RequestMethod.GET)  
    public void createQrBarCodeImg(@PathVariable("code") String code,
			HttpServletRequest request, HttpServletResponse response) {
		
		QRCodeWriter writer = new QRCodeWriter();
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = writer.encode(code, BarcodeFormat.QR_CODE, 300, 300);
			
			ServletOutputStream outputStream = response.getOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "jpeg", outputStream);
			outputStream.flush();
			outputStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
    
}
