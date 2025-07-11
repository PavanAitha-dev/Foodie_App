package com.improveId.Order.circuitBreaker;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomFeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        // Custom error handling logic
        return new FeignException.ServiceUnavailable(
                " adhithya  saying Service unavailable",
                response.request(),
                response.body() != null ? response.body().toString().getBytes() : null,
                response.headers()
        );
    }
}
