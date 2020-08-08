package guru.springframework.msscbeerservice.service.beerorder.validator;

import guru.springframework.msscbeerservice.repository.BeerRepository;
import guru.springframework.msscbeerservice.service.beerorder.model.BeerOrderDto;
import guru.springframework.msscbeerservice.service.beerorder.model.ValidateBeerOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderValidator {
    private final BeerRepository beerRepository;

    public Boolean validate(ValidateBeerOrderRequest validateOrderEvent) {
        final List<String> upcsNotFound = new ArrayList<>();
        final BeerOrderDto beerOrder = validateOrderEvent.getBeerOrderDto();
        beerOrder.getBeerOrderLines()
                .forEach( beerOrderLine -> {
                    final String upc = beerOrderLine.getUpc();
                    if(beerRepository.findByUpc(upc).isEmpty()) {
                        log.error(MessageFormat.format("UPC {0} from Beer Order {1} not found!", beerOrder.getId(), beerOrderLine.getUpc()));
                        upcsNotFound.add(upc);
                    }
                });

        return upcsNotFound.isEmpty();

    }
}
