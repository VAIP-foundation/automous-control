package com.autonomous.pm.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import net.sf.jxls.transformer.XLSTransformer;

public class ExcelView extends AbstractXlsView {
    public static final Logger logger = LoggerFactory.getLogger(ExcelView.class);

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String viewName = (String) model.get("viewname");
        String output =  (String) model.get("output");

        if(StringUtils.isEmpty(output))
            output = "report.xls";

        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename="+output);

        OutputStream os = null;
        InputStream is = null;       

        try {
            // 엑셀 템플릿 파일이 존재하는 위치 (classpath 하위)
            is = new ClassPathResource(viewName).getInputStream();
            os = response.getOutputStream();
            XLSTransformer transformer = new XLSTransformer();
            Workbook excel = transformer.transformXLS(is, model);
            excel.write(os);
            os.flush();
        } catch (IOException e) {
            logger.error("buildExcelDocumnet", e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("buildExcelDocument" + e.getMessage());
                }
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("buildExcelDocument" + e.getMessage());
                }
        }
    }

}
