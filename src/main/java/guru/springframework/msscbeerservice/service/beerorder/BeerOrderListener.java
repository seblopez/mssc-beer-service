package guru.springframework.msscbeerservice.service.beerorder;

import guru.springframework.msscbeerservice.service.beerorder.model.ValidateBeerOrderRequest;
import guru.springframework.msscbeerservice.service.beerorder.model.ValidateBeerOrderResponse;
import guru.springframework.msscbeerservice.service.beerorder.validator.BeerOrderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static guru.springframework.msscbeerservice.config.JmsConfig.VALIDATE_ORDER_REQUEST_QUEUE;
import static guru.springframework.msscbeerservice.config.JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderListener {
    private final BeerOrderValidator beerOrderValidator;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = VALIDATE_ORDER_REQUEST_QUEUE)
    public void listen(ValidateBeerOrderRequest validateOrderEvent) {
        final Boolean isValidOrder = beerOrderValidator.validate(validateOrderEvent);

        jmsTemplate.convertAndSend(VALIDATE_ORDER_RESPONSE_QUEUE,
                ValidateBeerOrderResponse.builder()
                        .orderId(validateOrderEvent.getBeerOrderDto().getId())
                        .isValid(isValidOrder)
                        .build());

    }
}
