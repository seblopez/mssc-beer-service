package guru.springframework.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.service.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    private final BeerDto beerDtoToSave = BeerDto.builder()
            .beerName("Antares")
            .beerStyle(BeerStyleEnum.PILSENER)
            .upc("0083783375213")
            .price(BigDecimal.valueOf(12.23))
            .quantityOnHand(20)
            .build();

    private final BeerDto savedBeerDto = BeerDto.builder()
            .beerName("Antares")
            .beerStyle(BeerStyleEnum.PILSENER)
            .upc("0083783375213")
            .price(BigDecimal.valueOf(12.23))
            .quantityOnHand(20)
            .build();

    @Test
    void getBeersShowInventoryOk() throws Exception {
        BeerPagedList beerList = new BeerPagedList(Arrays.asList(savedBeerDto));

        given(beerService.listBeers(nullable(String.class), nullable(BeerStyleEnum.class), any(PageRequest.class), any(Boolean.class)))
                .willReturn(beerList);

        mockMvc.perform(get(API_V1_BEER + "?showInventoryOnHand=true")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].quantityOnHand", equalTo(20)));

        verify(beerService).listBeers(nullable(String.class), nullable(BeerStyleEnum.class), any(PageRequest.class), any(Boolean.class));

    }

    @Test
    void getBeerByIdNoShowInventoryOk() throws Exception {
        given(beerService.getBeerById(any(UUID.class), any(Boolean.class)))
                .willReturn(BeerDto.builder()
                        .beerName("Antares")
                        .beerStyle(BeerStyleEnum.PILSENER)
                        .upc("0083783375213")
                        .price(BigDecimal.valueOf(12.23))
                        .build());

        mockMvc.perform(get(API_V1_BEER + "/" + UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityOnHand").doesNotExist());

        verify(beerService).getBeerById(any(UUID.class), any(Boolean.class));

    }

    @Test
    void getBeerByIdShowInventoryOk() throws Exception {
        given(beerService.getBeerById(any(UUID.class), any(Boolean.class)))
                .willReturn(beerDtoToSave);

        mockMvc.perform(get(API_V1_BEER + "/" + UUID.randomUUID().toString() + "?showInventoryOnHand=true")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityOnHand", equalTo(20)));


        verify(beerService).getBeerById(any(UUID.class), any(Boolean.class));

    }

    @Test
    void saveNewBeerOk() throws Exception {
        given(beerService.saveNewBeer(any(BeerDto.class)))
                .willReturn(savedBeerDto);

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
        given(beerService.getBeerById(any(UUID.class), any(Boolean.class)))
                .willReturn(beerDtoToSave);
        given(beerService.updateBeer(any(UUID.class), any(BeerDto.class)))
                .willReturn(savedBeerDto);

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
                .upc("0083783375213")
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
