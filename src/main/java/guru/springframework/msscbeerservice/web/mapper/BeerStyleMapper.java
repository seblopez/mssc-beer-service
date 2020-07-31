package guru.springframework.msscbeerservice.web.mapper;

import guru.springframework.msscbeerservice.domain.BeerStyle;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import org.mapstruct.Mapper;

@Mapper
public interface BeerStyleMapper {
    BeerStyle beerStyleEnumToBeerStyle(BeerStyleEnum beerStyleEnum);

    BeerStyleEnum beerStyleToBeerStyleEnum(BeerStyle beerStyle);

}
