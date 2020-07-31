package guru.springframework.msscbeerservice.service;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.exception.NotFoundException;
import guru.springframework.msscbeerservice.repository.BeerRepository;
import guru.springframework.msscbeerservice.web.mapper.BeerMapper;
import guru.springframework.msscbeerservice.web.mapper.BeerStyleMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerMapper beerMapper;
    private final BeerRepository beerRepository;
    private final BeerStyleMapper beerStyleMapper;

    @Override
    public BeerDto getBeerById(UUID id) {
        return beerMapper.beerToBeerDto(beerRepository.findById(id)
                .orElseThrow(this.getNotFoundExceptionSupplier(id)));
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

    private Supplier<NotFoundException> getNotFoundExceptionSupplier(UUID id) {
        return () -> {
            final String errorMessage = MessageFormat.format("Beer id {0} not found", id);
            log.error(errorMessage);
            return new NotFoundException(errorMessage);
        };
    }
}
