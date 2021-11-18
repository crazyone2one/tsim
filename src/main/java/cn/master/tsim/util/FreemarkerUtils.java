package cn.master.tsim.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * @author Created by 11's papa on 2021/11/18
 * @version 1.0.0
 */
public class FreemarkerUtils {
    /**
     * 生成word文件
     *
     * @param request      HttpServletRequest
     * @param response     HttpServletResponse
     * @param dataMap      填充数据
     * @param templateName 模板名称
     */
    public static void generateWord(HttpServletRequest request, HttpServletResponse response, Map<String, Object> dataMap, String templateName) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            /* Create and adjust the configuration singleton */
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            //设置编码
            cfg.setDefaultEncoding("UTF-8");
            cfg.setClassForTemplateLoading(FreemarkerUtils.class, "/template");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            /* Get the template (uses cache internally) */
            Template temp = cfg.getTemplate(templateName + ".ftl");
            /* Merge data-model with template */
            // TODO: 2021/11/18 0018 使用相对路径
            File outFile = new File("E:\\Projects\\demo\\" + request.getAttribute("projectName")
                    + DateUtils.parse2String(new Date(), "yyyyMMddHHmmss") + ".doc");
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8));
            temp.process(dataMap, out);
            out.close();
            request.removeAttribute("projectName");
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }
}
