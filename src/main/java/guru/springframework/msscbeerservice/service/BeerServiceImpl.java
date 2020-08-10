package guru.springframework.msscbeerservice.service;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.domain.BeerStyle;
import guru.springframework.msscbeerservice.exception.NotFoundException;
import guru.springframework.msscbeerservice.repository.BeerRepository;
import guru.springframework.msscbeerservice.web.mapper.BeerMapper;
import guru.springframework.msscbeerservice.web.mapper.BeerStyleMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerMapper beerMapper;
    private final BeerRepository beerRepository;
    private final BeerStyleMapper beerStyleMapper;

    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getBeerById(UUID beerId, Boolean showInventoryOnHand) {
        log.debug("getBeerId is called");
        final Beer beer = beerRepository.findById(beerId)
                .orElseThrow(this.getNotFoundByIdExceptionSupplier(beerId));

        if(showInventoryOnHand) {
            return beerMapper.beerToBeerDtoWithInventory(beer);
        } else {
            return beerMapper.beerToBeerDto(beer);
        }
    }

    @Cacheable(cacheNames = "beerUpcCache", key = "#upc", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getBeerByUpc(String upc, Boolean showInventoryOnHand) {
        log.debug("getBeerByUpc is called");
        final Beer beer = beerRepository.findByUpc(upc).orElseThrow(this.getNotFoundByUpcExceptionSupplier(upc));

        if(showInventoryOnHand) {
            return beerMapper.beerToBeerDtoWithInventory(beer);
        } else {
            return beerMapper.beerToBeerDto(beer);
        }

    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        final Beer beerToUpdate = beerRepository.findById(beerId)
                .orElseThrow(this.getNotFoundByIdExceptionSupplier(beerId));

        beerToUpdate.setBeerName(beerDto.getBeerName());
        beerToUpdate.setBeerStyle(beerStyleMapper.beerStyleEnumToBeerStyle(beerDto.getBeerStyle()));
        beerToUpdate.setPrice(beerDto.getPrice());
        beerToUpdate.setUpc(beerDto.getUpc());
        beerToUpdate.setLastModifiedDate(Timestamp.valueOf(LocalDateTime.now()));

        return beerMapper.beerToBeerDto(beerRepository.save(beerToUpdate));
    }

    @Override
    public BeerDto updateBeerByUpc(String upc, BeerDto beerDto) {
        final Beer beerToUpdate = beerRepository.findByUpc(upc)
                .orElseThrow(this.getNotFoundByUpcExceptionSupplier(upc));

        beerToUpdate.setBeerName(beerDto.getBeerName());
        beerToUpdate.setBeerStyle(beerStyleMapper.beerStyleEnumToBeerStyle(beerDto.getBeerStyle()));
        beerToUpdate.setUpc(beerDto.getUpc());
        beerToUpdate.setPrice(beerDto.getPrice());
        beerToUpdate.setLastModifiedDate(Timestamp.valueOf(LocalDateTime.now()));

        return beerMapper.beerToBeerDto(beerRepository.save(beerToUpdate));

    }

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyleEnum, PageRequest pageRequest, Boolean showInventoryOnHand) {
        final Page<Beer> beerPage;
        log.debug("listBeers is called");

        if(!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyleEnum)) {
            final BeerStyle beerStyle = beerStyleMapper.beerStyleEnumToBeerStyle(beerStyleEnum);
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        } else if(!StringUtils.isEmpty(beerName) && StringUtils.isEmpty(beerStyleEnum)) {
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if(StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyleEnum)) {
            final BeerStyle beerStyle = beerStyleMapper.beerStyleEnumToBeerStyle(beerStyleEnum);
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if(showInventoryOnHand) {
            return new BeerPagedList(beerPage.getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDtoWithInventory)
                    .collect(Collectors.toList()),
                    PageRequest.of(beerPage.getPageable().getPageNumber(),
                            beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        } else {
            return new BeerPagedList(beerPage.getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDto)
                    .collect(Collectors.toList()),
                    PageRequest.of(beerPage.getPageable().getPageNumber(),
                            beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        }

    }

    private Supplier<NotFoundException> getNotFoundByIdExceptionSupplier(UUID id) {
        return () -> {
            final String errorMessage = MessageFormat.format("Beer id {0} not found", id);
            log.error(errorMessage);
            return new NotFoundException(errorMessage);
        };
    }

    private Supplier<NotFoundException> getNotFoundByUpcExceptionSupplier(String upc) {
        return () -> {
            final String errorMessage = MessageFormat.format("UPC {0} not found", upc);
            log.error(errorMessage);
            return new NotFoundException(errorMessage);
        };
    }
}
