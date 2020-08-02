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

    @Override
    public BeerDto getBeerById(UUID id, Boolean showInventoryOnHand) {
        final Beer beer = beerRepository.findById(id)
                .orElseThrow(this.getNotFoundExceptionSupplier(id));

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
    public BeerDto updateBeer(UUID id, BeerDto beerDto) {
        final Beer beerToUpdate = beerRepository.findById(id)
                .orElseThrow(getNotFoundExceptionSupplier(id));

        beerToUpdate.setBeerName(beerDto.getBeerName());
        beerToUpdate.setBeerStyle(beerStyleMapper.beerStyleEnumToBeerStyle(beerDto.getBeerStyle()));
        beerToUpdate.setUpc(beerDto.getUpc());
        beerToUpdate.setLastModifiedDate(Timestamp.valueOf(LocalDateTime.now()));

        return beerMapper.beerToBeerDto(beerRepository.save(beerToUpdate));
    }

    @Override
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyleEnum, PageRequest pageRequest, Boolean showInventoryOnHand) {
        final Page<Beer> beerPage;

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

    private Supplier<NotFoundException> getNotFoundExceptionSupplier(UUID id) {
        return () -> {
            final String errorMessage = MessageFormat.format("Beer id {0} not found", id);
            log.error(errorMessage);
            return new NotFoundException(errorMessage);
        };
    }
}
