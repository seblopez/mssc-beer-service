package guru.springframework.msscbeerservice.event;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class BeerEvent implements Serializable {

    static final long serialVersionUID = -328541268806064236L;

    private final BeerDto beerDto;

}
