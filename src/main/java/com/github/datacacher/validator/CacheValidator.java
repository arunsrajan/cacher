package com.github.datacacher.validator;

import com.github.datacacher.exceptions.CacheException;
import com.github.datacacher.model.CacheRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.springframework.stereotype.Component;

import static com.github.datacacher.constants.CacheConstants.*;
import static java.util.Objects.isNull;

@Component("cacheValidator")
public class CacheValidator {

    public void validate(@Header(CACHENAME) String cacheName) throws CacheException {
        if (isNull(cacheName)) {
            throw new CacheException(PROVIDECACHENAME);
        }
    }

    public void validateCacheRequest(Exchange exchange) throws CacheException {
        CacheRequest cacheRequest = exchange.getIn().getBody(CacheRequest.class);
        if (isNull(cacheRequest.cacheName)) {
            throw new CacheException(PROVIDECACHENAME);
        }
        if (isNull(cacheRequest.expiry)) {
            throw new CacheException(PROVIDECACHEEXPIRY);
        }
    }
}
