package guru.springframework.msscbeerservice.service.beerorder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ValidateBeerOrderRequest {
    private BeerOrderDto beerOrderDto;
}
