package com.latico.commons.spring.util;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2020-03-25 18:27
 * @version: 1.0
 */
public class MyRestTemplateImpl extends RestTemplate {
    /** 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(MyRestTemplateImpl.class);
    public MyRestTemplateImpl() {
    }

    public MyRestTemplateImpl(HttpComponentsClientHttpRequestFactory httpRequestFactory) {
        super(httpRequestFactory);
    }

    @Override
    protected void handleResponse(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        ResponseErrorHandler errorHandler = getErrorHandler();
        boolean hasError = errorHandler.hasError(response);
        if (logger.isDebugEnabled()) {
            try {
                logger.debug(method.name() + " request for \"" + url + "\" resulted in " +
                        response.getRawStatusCode() + " (" + response.getStatusText() + ")" +
                        (hasError ? "; invoking error handler" : ""));
            }
            catch (IOException ex) {
                // ignore
            }
        }
        if (hasError) {
            // errorHandler.handleError(response);
        }
    }
}
