package guru.springframework.msscbeerservice.bootstrap;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.domain.BeerStyle;
import guru.springframework.msscbeerservice.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerLoader implements CommandLineRunner {

    final private BeerRepository beerRepository;
    private static final String BEER_1_UPC = "0631234200036";
    private static final String BEER_2_UPC = "0631234300019";
    private static final String BEER_3_UPC = "0083783375213";

    @Override
    public void run(String... args) throws Exception {
        loadBeer();
    }

    private void loadBeer() {
        if(beerRepository.count() == 0) {
            List<Beer> beers = Arrays.asList(
                    Beer.builder().beerName("Mango Bobs")
                            .id(UUID.fromString("0a818933-087d-47f2-ad83-2f986ed087eb"))
                            .beerStyle(BeerStyle.IPA)
                            .quantityToBrew(200)
                            .minOnHand(12)
                            .upc(BEER_1_UPC)
                            .price(BigDecimal.valueOf(12.95))
                            .build(),
                    Beer.builder().beerName("Galaxy Cat")
                            .id(UUID.fromString("a712d914-61ea-4623-8bd0-32c0f6545bfd"))
                            .beerStyle(BeerStyle.ALE)
                            .quantityToBrew(200)
                            .minOnHand(12)
                            .upc(BEER_2_UPC)
                            .price(BigDecimal.valueOf(11.95))
                            .build(),
                    Beer.builder().beerName("Pinball Porter")
                            .id(UUID.fromString("026cc3c8-3a0c-4083-a05b-e908048c1b08"))
                            .beerStyle(BeerStyle.PORTER)
                            .quantityToBrew(140)
                            .minOnHand(30)
                            .upc(BEER_3_UPC)
                            .price(BigDecimal.valueOf(13.95))
                            .build()
            );

            beerRepository.saveAll(beers);

            log.debug(MessageFormat.format("Beers loaded: {0}", beerRepository.count()));

        }
    }
}
