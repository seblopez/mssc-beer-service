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

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerLoader implements CommandLineRunner {

    final private BeerRepository beerRepository;
    private static final String BEER_1_UPC = "0631234200036";
    private static final String BEER_2_UPC = "0631234300019";
    private static final String BEER_3_UPC = "0083783375213";
    private static final String BEER_4_UPC = "0083783373456";
    private static final String BEER_5_UPC = "0083783373489";

    @Override
    public void run(String... args) throws Exception {
        loadBeer();
    }

    private void loadBeer() {
        if(beerRepository.count() == 0) {
            List<Beer> beers = Arrays.asList(
                    Beer.builder().beerName("Mango Bobs")
                            .beerStyle(BeerStyle.IPA)
                            .quantityToBrew(200)
                            .minOnHand(12)
                            .upc(BEER_1_UPC)
                            .price(BigDecimal.valueOf(12.95))
                            .build(),
                    Beer.builder().beerName("Galaxy Cat")
                            .beerStyle(BeerStyle.ALE)
                            .quantityToBrew(200)
                            .minOnHand(12)
                            .upc(BEER_2_UPC)
                            .price(BigDecimal.valueOf(11.95))
                            .build(),
                    Beer.builder().beerName("Andes Rubia")
                            .beerStyle(BeerStyle.HONEY)
                            .quantityToBrew(400)
                            .minOnHand(12)
                            .upc(BEER_3_UPC)
                            .price(BigDecimal.valueOf(14.35))
                            .build(),
                    Beer.builder().beerName("Corona")
                            .beerStyle(BeerStyle.PILSENER)
                            .quantityToBrew(100)
                            .minOnHand(40)
                            .upc(BEER_4_UPC)
                            .price(BigDecimal.valueOf(7.09))
                            .build(),
                    Beer.builder().beerName("Rabieta IPA")
                            .beerStyle(BeerStyle.IPA)
                            .quantityToBrew(300)
                            .minOnHand(20)
                            .upc(BEER_5_UPC)
                            .price(BigDecimal.valueOf(14.95))
                            .build()
            );

            beerRepository.saveAll(beers);

            log.info(MessageFormat.format("Beers loaded: {0}", beerRepository.count()));

        }
    }
}
