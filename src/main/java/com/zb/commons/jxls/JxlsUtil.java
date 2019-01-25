package com.zb.commons.jxls;

import com.zb.commons.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbo
 */
@Slf4j
public final class JxlsUtil {

    private JxlsUtil() {
    }
    
    public static <T> Map<String, Object> getDataMap(List<T> data) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("model", data);
        return dataMap;
    }
    
    public static void export(String excelTemplatePath, String fileNamePrefix, Map<String, Object> data, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(excelTemplatePath);
        
        setFileDownloadHeader(getFullFileName(fileNamePrefix), response);
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        try {
            XLSTransformer transformer = new XLSTransformer();
            Workbook workbook = transformer.transformXLS(is, data);
            workbook.write(response.getOutputStream());
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
    
    public static void setFileDownloadHeader(String fileName, HttpServletResponse response) {
        String encodedFileName = null;
        try {
            encodedFileName = new String(fileName.getBytes(), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            log.error("文件名编码失败", e);
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
    }

    private static String getFullFileName(String fileNamePrefix) {
        return fileNamePrefix + DateUtil.defaultFormatDate(new Date()) + ".xls";
    }
}
