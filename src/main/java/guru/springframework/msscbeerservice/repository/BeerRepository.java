package guru.springframework.msscbeerservice.repository;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.domain.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface BeerRepository extends PagingAndSortingRepository<Beer, UUID> {
    Page<Beer> findAllByBeerNameAndBeerStyle(String beerName, BeerStyle beerStyle, PageRequest pageRequest);

    Optional<Beer> findByUpc(String upc);

    Page<Beer> findAllByBeerName(String beerName, PageRequest pageRequest);

    Page<Beer> findAllByBeerStyle(BeerStyle beerStyle, PageRequest pageRequest);
}
