package com.improveId.Order.config;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void apply(RequestTemplate template) {
        // Get the current HTTP request
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();

            // Get the Authorization header from the current request
            String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

            // If header exists, add it to the Feign request
            if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
                template.header(AUTHORIZATION_HEADER, authorizationHeader);
            }
        }
    }
}