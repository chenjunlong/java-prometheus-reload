package com.github.jpreload;

import com.github.jpreload.model.Params;
import com.github.jpreload.utils.ResConvert;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by chenjunlong on 2018/8/3.
 */
public class ReloadHandler implements HttpHandler {

    private Logger logger = LoggerFactory.getLogger(Bootstrap.class);
    private String SUCCESS = "success";
    private String PARAMS_ERROR = "params error";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        OutputStream os = httpExchange.getResponseBody();
        String postFormData = IOUtils.toString(httpExchange.getRequestBody());
        Map<String, String> reqParamsMap = ResConvert.formData2Map(postFormData);
        try {
            if (null == reqParamsMap || reqParamsMap.isEmpty()) {
                throw new IllegalArgumentException("form data is empty");
            }

            Params params = new Params();
            params.setJobName(reqParamsMap.get("appName"));
            params.setHost(reqParamsMap.get("host"));
            params.setPort(reqParamsMap.get("port"));
            BlockingQueueStore.getBlockingQueue().add(params);

            httpExchange.sendResponseHeaders(200, SUCCESS.length());
            os.write(SUCCESS.getBytes());
        } catch (IllegalArgumentException e) {
            logger.warn(e.getMessage(), e);
            httpExchange.sendResponseHeaders(200, PARAMS_ERROR.length());
            os.write(PARAMS_ERROR.getBytes());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            os.close();
        }
    }
}
