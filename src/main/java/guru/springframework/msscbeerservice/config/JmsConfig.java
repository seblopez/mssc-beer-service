package guru.springframework.msscbeerservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.service.beerorder.model.BeerOrderDto;
import guru.springframework.msscbeerservice.service.beerorder.model.BeerOrderLineDto;
import guru.springframework.msscbeerservice.service.beerorder.model.BeerOrderStatusDto;
import guru.springframework.msscbeerservice.service.beerorder.model.ValidateBeerOrderRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class JmsConfig {

    public static final String BREW_REQUEST_QUEUE = "brewing-request";
    public static final String NEW_INVENTORY_QUEUE = "new-inventory-request";
    public static final String VALIDATE_ORDER_REQUEST_QUEUE = "validate-order";
    public static final String VALIDATE_ORDER_RESPONSE_QUEUE = "validate-order-result";

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("guru.springframework.beer.order.service.event.ValidateBeerOrderRequest", ValidateBeerOrderRequest.class);
        typeIdMappings.put("guru.springframework.beer.order.service.web.model.BeerOrderDto", BeerOrderDto.class);
        typeIdMappings.put("guru.springframework.beer.order.service.web.model.BeerOrderLineDto", BeerOrderLineDto.class);
        typeIdMappings.put("guru.springframework.beer.order.service.web.model.BeerOrderStatusDto", BeerOrderStatusDto.class);

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMapper);
        converter.setTypeIdMappings(typeIdMappings);

        return converter;

    }

}
