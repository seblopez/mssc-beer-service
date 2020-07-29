package guru.springframework.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    private static final String API_V1_BEER = "/api/v1/beer";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    BeerController beerController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getBeerById() throws Exception {
        mockMvc.perform(get(API_V1_BEER + "/" + UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void saveNewBeerOk() throws Exception {
        String beerDtoJson = objectMapper.writeValueAsString(BeerDto.builder()
                .beerName("Antares")
                .beerStyle(BeerStyleEnum.LAGER)
                .price(BigDecimal.valueOf(12.23))
                .upc(1212323231L)
                .quantityOnHand(20)
                .build());

        mockMvc.perform(post(API_V1_BEER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isCreated());
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
        String beerDtoJson = objectMapper.writeValueAsString(BeerDto.builder()
                .beerName("Antares")
                .beerStyle(BeerStyleEnum.LAGER)
                .price(BigDecimal.valueOf(12))
                .upc(1L)
                .quantityOnHand(10)
                .build());

        mockMvc.perform(put(API_V1_BEER + "/" + UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isNoContent());

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

    }
}
