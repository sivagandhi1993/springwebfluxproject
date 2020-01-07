package com.learnreactivespring.router;

import com.learnreactivespring.handler.ItemsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.learnreactivespring.constants.ItemsConstants.ITEMS_CONSTANTS_FUNCTIONAL_URL_V1;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ItemsRouter {

    @Bean
    public RouterFunction<ServerResponse> itemsRoute(ItemsHandler itemsHandler) {
        return RouterFunctions
                .route(GET(ITEMS_CONSTANTS_FUNCTIONAL_URL_V1)
                                .and(accept(MediaType.APPLICATION_JSON_UTF8))
                        , itemsHandler::getAllItems)
                .andRoute(GET(ITEMS_CONSTANTS_FUNCTIONAL_URL_V1.concat("/{id}"))
                                .and(accept(MediaType.APPLICATION_JSON_UTF8))
                        , itemsHandler::getOneItem)
                .andRoute(POST(ITEMS_CONSTANTS_FUNCTIONAL_URL_V1)
                                .and(accept(MediaType.APPLICATION_JSON_UTF8))
                        , itemsHandler::createItem)
                .andRoute(DELETE(ITEMS_CONSTANTS_FUNCTIONAL_URL_V1.concat("/{id}"))
                                .and(accept(MediaType.APPLICATION_JSON_UTF8))
                        , itemsHandler::deleteItem)
                .andRoute(PUT(ITEMS_CONSTANTS_FUNCTIONAL_URL_V1.concat("/{id}"))
                                .and(accept(MediaType.APPLICATION_JSON_UTF8))
                        , itemsHandler::updateItem);
    }
}
