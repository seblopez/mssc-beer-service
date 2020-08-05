package guru.springframework.msscbeerservice.service.brewing;

import guru.springframework.msscbeerservice.config.JmsConfig;
import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.event.BrewBeerEvent;
import guru.springframework.msscbeerservice.event.NewInventoryEvent;
import guru.springframework.msscbeerservice.exception.NotFoundException;
import guru.springframework.msscbeerservice.repository.BeerRepository;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

import static guru.springframework.msscbeerservice.config.JmsConfig.NEW_INVENTORY_QUEUE;

@Slf4j
@RequiredArgsConstructor
@Service
public class BrewBeerListener {
    
    private final BeerRepository beerRepository;
    private final JmsTemplate jmsTemplate;
    
    @JmsListener(destination = JmsConfig.BREW_REQUEST_QUEUE)
    public void listen(BrewBeerEvent brewBeerEvent) {
        final BeerDto beerDto = brewBeerEvent.getBeerDto();

        final Beer beer = beerRepository.findById(beerDto.getId()).orElseThrow(() -> {
            final String errorMessage = MessageFormat.format("Beer Id {0} not found!", beerDto.getId());
            log.error(errorMessage);
            return new NotFoundException(errorMessage);
        });

        beerDto.setQuantityOnHand(beer.getQuantityToBrew());

        NewInventoryEvent newInventoryEvent = new NewInventoryEvent(beerDto);

        log.debug(MessageFormat.format("Brewed beer {0}, QOH: {1}", beer.getMinOnHand(), beerDto.getQuantityOnHand()));

        jmsTemplate.convertAndSend(NEW_INVENTORY_QUEUE, newInventoryEvent);

    }

}
