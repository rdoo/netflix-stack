package com.rdoo.netflixstack.apigateway;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Base64;

import javax.servlet.ServletInputStream;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StreamUtils;

public class AuthFilter extends ZuulFilter {
    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

	public String filterType() {
		return "pre";
    }
    
    public int filterOrder() {
		return 1;
    }
    
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        // System.out.println(this.clientId);
		return ctx.getRequest().getRequestURI().endsWith("/oauth/token");
    }
    
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        
        String authString = this.clientId + ":" + this.clientSecret;
        ctx.addZuulRequestHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(authString.getBytes()));

        // based on https://github.com/spring-cloud-samples/sample-zuul-filters/blob/master/src/main/java/org/springframework/cloud/samplezuulfilters/PrefixRequestEntityFilter.java
        try {
            InputStream in = ctx.getRequest().getInputStream();
            String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
            body = body + "&grant_type=password";
            // System.out.println(body);
            byte[] bytes = body.getBytes("UTF-8");
            
            ctx.setRequest(new HttpServletRequestWrapper(ctx.getRequest()) {
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
        }
        catch (IOException e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }

        return null;
    }
}