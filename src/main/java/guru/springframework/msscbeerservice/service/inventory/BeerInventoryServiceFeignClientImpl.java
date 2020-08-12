package guru.springframework.msscbeerservice.service.inventory;

import guru.springframework.msscbeerservice.service.inventory.model.BeerInventoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Profile("local-discovery")
@RequiredArgsConstructor
@Service
public class BeerInventoryServiceFeignClientImpl implements BeerInventoryService {
    private final BeerInventoryServiceFeignClient feignClient;

    @Override
    public Integer getOnHandInventory(@PathVariable UUID beerId) {
        log.debug(MessageFormat.format("Calling Inventory feign service for Beer Id {0}", beerId));

        final ResponseEntity<List<BeerInventoryDto>> responseEntity = feignClient.getOnHandInventory(beerId);

        final int qtyOnHand = Objects.requireNonNull(responseEntity.getBody())
                .stream()
                .mapToInt(BeerInventoryDto::getQuantityOnHand)
                .sum();

        log.debug(MessageFormat.format("Beer Id {0}, qty on hand: {1}", beerId, qtyOnHand));

        return qtyOnHand;

    }
}
