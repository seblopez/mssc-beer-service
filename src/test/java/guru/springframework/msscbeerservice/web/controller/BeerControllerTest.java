package guru.springframework.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.service.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    private static final String API_V1_BEER = "/api/v1/beer";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    private BeerDto beerDtoToSave = BeerDto.builder()
            .beerName("Antares")
            .beerStyle(BeerStyleEnum.PILSENER)
            .upc(12213232L)
            .price(BigDecimal.valueOf(12.23))
            .upc(1212323231L)
            .quantityOnHand(20)
            .build();

    private BeerDto beerDtoSaved = BeerDto.builder()
            .beerName("Antares")
            .beerStyle(BeerStyleEnum.PILSENER)
            .upc(12213232L)
            .price(BigDecimal.valueOf(12.23))
            .upc(1212323231L)
            .quantityOnHand(20)
            .build();

    @Test
    void getBeerById() throws Exception {
        given(beerService.getBeerById(any(UUID.class)))
                .willReturn(beerDtoToSave);

        mockMvc.perform(get(API_V1_BEER + "/" + UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(beerService).getBeerById(any(UUID.class));

    }

    @Test
    void saveNewBeerOk() throws Exception {
        given(beerService.saveNewBeer(any(BeerDto.class)))
                .willReturn(beerDtoSaved);

        String beerDtoJson = objectMapper.writeValueAsString(beerDtoToSave);

        mockMvc.perform(post(API_V1_BEER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isCreated());

        verify(beerService).saveNewBeer(any(BeerDto.class));

    }

    @Test
    void saveNewBeerNotOk() throws Exception {
        String beerDtoJson = objectMapper.writeValueAsString(BeerDto.builder().build());

        mockMvc.perform(post(API_V1_BEER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBeerById() throws Exception {
        given(beerService.getBeerById(any(UUID.class)))
                .willReturn(beerDtoToSave);
        given(beerService.updateBeer(any(UUID.class), any(BeerDto.class)))
                .willReturn(beerDtoSaved);

        String beerDtoJson = objectMapper.writeValueAsString(beerDtoToSave);

        mockMvc.perform(put(API_V1_BEER + "/" + UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isNoContent());

        verify(beerService).updateBeer(any(UUID.class), any(BeerDto.class));

    }

    @Test
    void updateBeerByIdNotOk() throws Exception {
        String beerDtoJson = objectMapper.writeValueAsString(BeerDto.builder()
                .beerName("Antares")
                .beerStyle(BeerStyleEnum.LAGER)
                .price(BigDecimal.valueOf(0))
                .upc(1L)
                .quantityOnHand(10)
                .build());

        mockMvc.perform(put(API_V1_BEER + "/" + UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isBadRequest());

        verify(beerService, times(0)).updateBeer(any(UUID.class), any(BeerDto.class));

    }
}
