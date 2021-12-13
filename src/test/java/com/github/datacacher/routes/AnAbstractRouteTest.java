package com.github.datacacher.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.datacacher.constants.RouteConstants.*;

public class AnAbstractRouteTest {
    @EndpointInject(MOCK_RESULT)
    protected MockEndpoint resultEndpoint;

    @EndpointInject(MOCK_ERROR)
    protected MockEndpoint errorEndpoint;


    @EndpointInject(MOCK_RESULT_LIST)
    protected MockEndpoint resultEndpointList;

    @EndpointInject(MOCK_RESULT_MAP)
    protected MockEndpoint resultEndpointMap;

    @Autowired
    protected CamelContext camelContext;

    @EndpointInject
    protected ProducerTemplate producer;

    protected static final String MOCK_RESULT = "mock:result";
    protected static final String MOCK_ERROR = "mock:error";

    protected static final String MOCK_RESULT_LIST = "mock:resultList";
    protected static final String MOCK_RESULT_MAP = "mock:resultMap";

    protected static final String DIRECT_IN = "direct:in";

    @Before
    public void initCacheRoutes() throws Exception {
        AdviceWithRouteBuilder.adviceWith(camelContext,CREATE_CACHE_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpoint);
        });
        AdviceWithRouteBuilder.adviceWith(camelContext,REMOVE_CACHE_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpoint);
        });
        AdviceWithRouteBuilder.adviceWith(camelContext,GET_CACHE_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpoint);
        });
        AdviceWithRouteBuilder.adviceWith(camelContext,CACHE_AUDIT_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpoint);
        });
        AdviceWithRouteBuilder.adviceWith(camelContext,CACHE_AUDIT_GET_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpoint);
        });
        AdviceWithRouteBuilder.adviceWith(camelContext,CACHE_GET_AVAILABLE_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpoint);
        });
        resultEndpoint.reset();
    }
}
