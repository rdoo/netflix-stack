package com.rdoo.netflixstack.apigateway;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Base64;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StreamUtils;

public class AuthFilter extends ZuulFilter {
    @Autowired
    private PublicPaths publicPaths;

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    public boolean shouldFilter() {
        String requestURI = RequestContext.getCurrentContext().getRequest().getRequestURI();
        return requestURI.equals(this.publicPaths.getLoginPath()) || requestURI.equals(this.publicPaths.getRefreshTokenPath());
    }

    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        // add basic authentication header
        String authString = this.oAuth2ClientProperties.getClientId() + ":" + this.oAuth2ClientProperties.getClientSecret();
        ctx.addZuulRequestHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(authString.getBytes()));

        // redirect to OAuth endpoint
        ctx.put(FilterConstants.REQUEST_URI_KEY, "/oauth/token");

        // modify request body to include proper grant type, based on
        // https://github.com/spring-cloud-samples/sample-zuul-filters/blob/master/src/main/java/org/springframework/cloud/samplezuulfilters/PrefixRequestEntityFilter.java
        try {
            InputStream inputStream = request.getInputStream();
            String body = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
            body += "&grant_type=" + (request.getRequestURI().equals(this.publicPaths.getRefreshTokenPath()) ? "refresh_token" : "password");
            byte[] bytes = body.getBytes("UTF-8");

            ctx.setRequest(new HttpServletRequestWrapper(request) {
                @Override
                public ServletInputStream getInputStream() throws IOException {
                    return new ServletInputStreamWrapper(bytes);
                }

                @Override
                public int getContentLength() {
                    return bytes.length;
                }

                @Override
                public long getContentLengthLong() {
                    return bytes.length;
                }
            });
        } catch (IOException e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }

        return null;
    }
}