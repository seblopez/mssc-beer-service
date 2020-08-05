package guru.springframework.msscbeerservice.event;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.Builder;

@Builder
public class NewInventoryEvent extends BeerEvent{

    NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
