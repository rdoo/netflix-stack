package com.rdoo.netflixstack.apigateway;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpHeaders;
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

        // create query parameters for OAuth server
        Map<String, List<String>> parameterMap = new HashMap<>();

        try {
            // read request body
            InputStream inputStream = request.getInputStream();
            String body = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));

            // parse json
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> bodyMap = mapper.readValue(body, new TypeReference<Map<String, String>>() {
            });

            for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
                parameterMap.put(entry.getKey(), List.of(entry.getValue()));
            }
        } catch (Exception e) {
            // do nothing
        }

        parameterMap.put("grant_type", List.of((request.getRequestURI().equals(this.publicPaths.getRefreshTokenPath()) ? "refresh_token" : "password")));
        ctx.setRequestQueryParams(parameterMap);

        // add basic authentication header
        String authString = this.oAuth2ClientProperties.getClientId() + ":" + this.oAuth2ClientProperties.getClientSecret();
        ctx.addZuulRequestHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(authString.getBytes()));

        // redirect to OAuth endpoint
        ctx.put(FilterConstants.REQUEST_URI_KEY, "/oauth/token");

        return null;
    }
}