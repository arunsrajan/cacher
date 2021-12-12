package com.github.datacacher.validator;

import com.github.datacacher.exceptions.ListException;
import com.github.datacacher.exceptions.MapException;
import com.github.datacacher.model.MapRequest;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.github.datacacher.constants.CacheConstants.PROVIDECACHENAME;
import static com.github.datacacher.constants.MapConstants.MANDATORYFIELDMAPNAME;
import static com.github.datacacher.constants.MapConstants.MAPREQUEST;

@Component("mapValidator")
public class MapValidator {
    public void validate(Exchange exchange) throws MapException {
        MapRequest request = (MapRequest) exchange.getProperty(MAPREQUEST);
        if(Objects.isNull(request.getCacheName())){
            throw new MapException(PROVIDECACHENAME);
        }
        if(Objects.isNull(request.getMapName())){
            throw new MapException(MANDATORYFIELDMAPNAME);
        }
    }
}
