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
                            .upc(3370232321L)
                            .price(BigDecimal.valueOf(12.95))
                            .build(),
                    Beer.builder().beerName("Galaxy Cat")
                            .beerStyle(BeerStyle.ALE)
                            .quantityToBrew(200)
                            .minOnHand(12)
                            .upc(3370232322L)
                            .price(BigDecimal.valueOf(11.95))
                            .build(),
                    Beer.builder().beerName("Andes Rubia")
                            .beerStyle(BeerStyle.HONEY)
                            .quantityToBrew(400)
                            .minOnHand(12)
                            .upc(3370232323L)
                            .price(BigDecimal.valueOf(14.35))
                            .build(),
                    Beer.builder().beerName("Corona")
                            .beerStyle(BeerStyle.PILSENER)
                            .quantityToBrew(100)
                            .minOnHand(40)
                            .upc(3370232324L)
                            .price(BigDecimal.valueOf(7.09))
                            .build(),
                    Beer.builder().beerName("Rabieta IPA")
                            .beerStyle(BeerStyle.IPA)
                            .quantityToBrew(300)
                            .minOnHand(20)
                            .upc(3370232325L)
                            .price(BigDecimal.valueOf(14.95))
                            .build()
            );

            beerRepository.saveAll(beers);

            log.info(MessageFormat.format("Beers loaded: {0}", beerRepository.count()));

        }
    }
}
