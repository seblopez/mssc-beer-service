package guru.springframework.msscbeerservice.service.inventory;

import guru.springframework.msscbeerservice.service.inventory.model.BeerInventoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Profile("!local-discovery")
@Slf4j
// @ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = false)
@Component
public class BeerInventoryServiceRestTemplateImpl implements BeerInventoryService {
    public static final String INVENTORY_PATH = "/api/v1/beer/{beerId}/inventory";
    private final RestTemplate restTemplate;
    private String beerInventoryServiceHost;

    public BeerInventoryServiceRestTemplateImpl(RestTemplateBuilder restTemplateBuilder,
                                                @Value("${sfg.brewery.inventory-user}") String inventoryUser,
                                                @Value("${sfg.brewery.inventory-password}") String inventoryPassword) {
        this.restTemplate = restTemplateBuilder
                .basicAuthentication(inventoryUser, inventoryPassword)
                .build();
    }

    public void setBeerInventoryServiceHost(String beerInventoryServiceHost) {
        this.beerInventoryServiceHost = beerInventoryServiceHost;
    }


    @Override
    public Integer getOnHandInventory(UUID beerId) {
        log.debug("Calling inventory service");

        ResponseEntity<List<BeerInventoryDto>> responseEntity = restTemplate
                .exchange(beerInventoryServiceHost + INVENTORY_PATH, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<BeerInventoryDto>>() {}, (Object) beerId);

        Integer onHand = Objects.requireNonNull(responseEntity.getBody())
                .stream()
                .mapToInt(BeerInventoryDto::getQuantityOnHand)
                .sum();

        log.debug(MessageFormat.format("Quantity on hand for Beer {0}:{1}", beerId, onHand));

        return onHand;
    }
}
