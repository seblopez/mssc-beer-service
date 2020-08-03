package guru.springframework.msscbeerservice.service.inventory;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
@Slf4j
@SpringBootTest
public class BeerInventoryServiceRestTemplateImplIT {

    @Autowired
    BeerInventoryService beerInventoryService;

    @Test
    public void getQOHInventoryOk() {
        Integer qoh = beerInventoryService.getOnHandInventory(UUID.fromString("0a818933-087d-47f2-ad83-2f986ed087eb"));

        assertNotNull(qoh);
        assertEquals(50, qoh);

    }

}
