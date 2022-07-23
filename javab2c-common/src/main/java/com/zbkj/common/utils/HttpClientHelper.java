package com.zbkj.common.utils;

import cn.hutool.http.HttpException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;

public class HttpClientHelper {

    public static String sendPost(String urlParam,String paramJson) throws HttpException, IOException {
        // 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：25000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(25000);
        // 创建post请求方法实例对象
        PostMethod postMethod = new PostMethod(urlParam);
        // 设置post请求超时时间
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 80000);
        postMethod.addRequestHeader("Content-Type", "application/json");
        //json格式的参数解析
        RequestEntity entity = new StringRequestEntity(paramJson, "application/json", "UTF-8");
        postMethod.setRequestEntity(entity);
        httpClient.executeMethod(postMethod);

        String result = postMethod.getResponseBodyAsString();
        postMethod.releaseConnection();
        return result;
    }
}
