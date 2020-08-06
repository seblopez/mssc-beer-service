package guru.springframework.msscbeerservice.service.brewing;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.event.BrewBeerEvent;
import guru.springframework.msscbeerservice.repository.BeerRepository;
import guru.springframework.msscbeerservice.service.inventory.BeerInventoryService;
import guru.springframework.msscbeerservice.web.mapper.BeerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

import static guru.springframework.msscbeerservice.config.JmsConfig.BREW_REQUEST_QUEUE;

@Slf4j
@RequiredArgsConstructor
@Service
public class BrewingServiceImpl implements BrewingService {
    private final BeerRepository beerRepository;
    private final BeerInventoryService beerInventoryService;
    private final JmsTemplate jmsTemplate;
    private final BeerMapper beerMapper;

    @Override
    @Scheduled(fixedRate = 5000)
    public void checkForLowInventory() {
        final List<Beer> beers = beerRepository.findAll();

        beers.forEach(beer -> {
            final Integer onHandInventory = beerInventoryService.getOnHandInventory(beer.getId());

            log.debug(MessageFormat.format("Min On Hand for {0} is {1}", beer.getBeerName(), beer.getMinOnHand()));
            log.debug(MessageFormat.format("Inventory count for {0} is {1}", beer.getBeerName(), onHandInventory));

            if(onHandInventory <= beer.getMinOnHand()) {
                log.debug(MessageFormat.format("Making new addition to inventory for {0}, qty {1}", beer.getBeerName(), beer.getQuantityToBrew()));
                jmsTemplate.convertAndSend(BREW_REQUEST_QUEUE, new BrewBeerEvent(beerMapper.beerToBeerDto(beer)));
            }
        });

    }
}
