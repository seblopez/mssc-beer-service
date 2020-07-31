package guru.springframework.msscbeerservice.web.mapper;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class, BeerStyleMapper.class})
public interface BeerMapper {
    Beer beerDtoToBeer(BeerDto beer);

    BeerDto beerToBeerDto(Beer beerDto);

}
