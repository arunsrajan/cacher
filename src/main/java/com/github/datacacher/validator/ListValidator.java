package com.github.datacacher.validator;

import com.github.datacacher.exceptions.ListException;
import com.github.datacacher.model.ListRequest;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.github.datacacher.constants.CacheConstants.PROVIDECACHENAME;
import static com.github.datacacher.constants.ListConstants.LISTREQUEST;
import static com.github.datacacher.constants.ListConstants.MANDATORYFIELDLISTNAME;

@Component("listValidator")
public class ListValidator {

    public void validate(Exchange exchange) throws ListException {
        ListRequest request = (ListRequest) exchange.getProperty(LISTREQUEST);
        if(Objects.isNull(request.getCacheName())){
            throw new ListException(PROVIDECACHENAME);
        }
        if(Objects.isNull(request.getListName())){
            throw new ListException(MANDATORYFIELDLISTNAME);
        }
    }

}
