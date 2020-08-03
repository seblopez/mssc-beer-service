package guru.springframework.msscbeerservice.service;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface BeerService {

    BeerDto getBeerById(UUID id, Boolean showInventoryOnHand);

    BeerDto getBeerByUpc(String upc, Boolean showInventoryOnHand);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID id, BeerDto beerDto);

    BeerDto updateBeerByUpc(String upc, BeerDto beerDto);

    BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest of, Boolean showInventoryOnHand);
}
