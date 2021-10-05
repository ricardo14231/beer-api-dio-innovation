package dio.innovation.beerAPI.controller;

import dio.innovation.beerAPI.utils.BeerDTOBuilder;
import dio.innovation.beerAPI.utils.StringBeerDTOBuilder;
import dio.innovation.beerAPI.dto.BeerDTO;
import dio.innovation.beerAPI.exception.BeerAlreadyRegisteredException;
import dio.innovation.beerAPI.exception.BeerNoSuchElementException;
import dio.innovation.beerAPI.exception.BeerQuantityException;
import dio.innovation.beerAPI.service.BeerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for beer controller.")
public class BeerControllerTest {

    private static final String CREATE_BEER = "/beer/create";
    private static final String LIST_BEER = "/beer/list";
    private static final String FIND_BY_ID_BEER = "/beer/{id}";
    private static final String UPDATE_BEER = "/beer/update/{id}";
    private static final String FIND_BY_NAME_BEER = "/beer/findName/{name}";
    private static final String DELETE_BEER = "/beer/delete/{id}";
    private static final String INCREMENT_BEER = "/beer/{id}/increment/{quantity}";
    private static final Long INVALID_ID = 5L;

    private MockMvc mockMvc;

    @Mock
    private BeerService beerService;

    @InjectMocks
    private BeerController beerController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(beerController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    @DisplayName("Returns created beer message.")
    void whenCreateBeerCalled_ThenReturnBeerIsCreated() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();
        beerDTO.setId(1L);

        String beerDTORequest = StringBeerDTOBuilder.createBeerBuilder();

        when(beerService.createBeer(beerDTO))
            .thenReturn(String.format("Cerveja salva com ID: %o.", beerDTO.getId()));

        mockMvc.perform(post(CREATE_BEER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDTORequest))
                .andExpect(status().isCreated())
                .andExpect(content().string(String.format("Cerveja salva com ID: %o.", beerDTO.getId())));
    }

    @Test
    @DisplayName("Returns bad request status when called to create beer.")
    void whenCreateBeerCalled_ThenReturnBadRequest() throws Exception {
        String beerDTORequest = StringBeerDTOBuilder.createBeerNoNameBuilder();

        mockMvc.perform(post(CREATE_BEER)
               .contentType(MediaType.APPLICATION_JSON)
               .content(beerDTORequest))
               .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns beer list.")
    void whenListBeerCalled_ThenReturnList() throws Exception {
        List<BeerDTO> beerDTOs = Collections.singletonList(BeerDTOBuilder.createBeerDTOBuilder());

        when(beerService.listAllBeer()).thenReturn(beerDTOs);

        mockMvc.perform(get(LIST_BEER)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(beerDTOs.size())))
                .andExpect(jsonPath("$[0].name" , Matchers.is(beerDTOs.get(0).getName())));
    }

    @Test
    @DisplayName("Returns beer list empty.")
    void whenListBeerCalled_ThenReturnListEmpty() throws Exception {

        when(beerService.listAllBeer()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(LIST_BEER)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    @DisplayName("Returns beer per id.")
    void whenFindByIdBeerCalled_ThenBeer() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();

        when(beerService.findByIdBeer(1L)).thenReturn(beerDTO);

        mockMvc.perform(get(FIND_BY_ID_BEER, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(beerDTO.getName()));
    }

    @Test
    @DisplayName("Returns not found status when searching beer by id.")
    void whenFindByIdBeerCalled_ThenBeerNotFound() throws Exception {

        when(beerService.findByIdBeer(1L)).thenThrow(BeerNoSuchElementException.class);

        mockMvc.perform(get(FIND_BY_ID_BEER, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Returns the beer when searched by name.")
    void whenFindNameBeerCalled_ThenReturnBeer() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();

        when(beerService.findByNameBeer(beerDTO.getName()))
                .thenReturn(beerDTO);

        mockMvc.perform(get(FIND_BY_NAME_BEER, beerDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(beerDTO.getName()));
    }

    @Test
    @DisplayName("Returns not found status when called to create beer.")
    void whenFindNameBeerCalled_ThenReturnBeerNotFound() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();

        when(beerService.findByNameBeer(beerDTO.getName()))
                .thenThrow(BeerNoSuchElementException.class);

        mockMvc.perform(get(FIND_BY_NAME_BEER, beerDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Returns success message when called update beer.")
    void whenUpdateBeerCalled_ThenUpdateBeer() throws Exception {
        BeerDTO updateBeerDTO = BeerDTOBuilder.updateBeerDTOBuilder();
        String beerUpdate = StringBeerDTOBuilder.updateBeerBuilder();

        when(beerService.updateBeer(1L, updateBeerDTO))
                .thenReturn(String.format("Cerveja com ID: %o atualizada!", 1L));

        mockMvc.perform(put(UPDATE_BEER, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerUpdate))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Cerveja com ID: %o atualizada!", 1L)));
    }

    @Test
    @DisplayName("Returns not found status when called update beer.")
    void whenUpdateBeerCalled_ThenUpdateBeerNotFound() throws Exception {
        BeerDTO updateBeerDTO = BeerDTOBuilder.updateBeerDTOBuilder();
        String beerUpdate = StringBeerDTOBuilder.updateBeerBuilder();

        when(beerService.updateBeer(INVALID_ID, updateBeerDTO))
               .thenThrow(BeerAlreadyRegisteredException.class);

        mockMvc.perform(put(UPDATE_BEER, INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerUpdate))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Returns Ok status when delete beer.")
    void whenDeleteBeerCalled_ThenStatusOk() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();
        beerDTO.setId(1L);

        when(beerService.deleteBeer( beerDTO.getId() ))
                .thenReturn(String.format("Cerveja com ID: %o deleteda!", beerDTO.getId()));

        mockMvc.perform(delete(DELETE_BEER, beerDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Cerveja com ID: %o deleteda!", beerDTO.getId())));
    }

    @Test
    @DisplayName("Returns not found status when delete beer")
    void whenDeleteBeerCalled_ThenBeerNotFound() throws Exception {

        when(beerService.deleteBeer( INVALID_ID ))
                .thenThrow(BeerNoSuchElementException.class);

        mockMvc.perform(delete(DELETE_BEER, INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Returns the beer increment.")
    void whenIncrementBeerCalled_ThenIncrementQuantityBeer() throws Exception {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.createBeerDTOBuilder();
        expectedBeerDTO.setId(1L);

        int incrementToBeer = 10;

        when(beerService.incrementQuantityBeer( expectedBeerDTO.getId(), incrementToBeer ))
                .thenReturn(expectedBeerDTO);

        mockMvc.perform(patch(INCREMENT_BEER, expectedBeerDTO.getId(), incrementToBeer)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedBeerDTO.getId()))
                .andExpect(jsonPath("$.name").value(expectedBeerDTO.getName()))
                .andExpect(jsonPath("$.quantity").value(expectedBeerDTO.getQuantity()));
    }

    @Test
    @DisplayName("Returns bad request status when calling the maximum beer quantity increment.")
    void whenIncrementBeerCalled_ThenExceptionQuantityBeer() throws Exception {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.createBeerDTOBuilder();
        expectedBeerDTO.setId(1L);

        int incrementToBeer = 50;

        when(beerService.incrementQuantityBeer( expectedBeerDTO.getId(), incrementToBeer ))
                .thenThrow(BeerQuantityException.class);

        mockMvc.perform(patch(INCREMENT_BEER, expectedBeerDTO.getId(), incrementToBeer)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns bad request status when called beer quantity.")
    void whenIncrementBeerCalled_ThenBeerNotFound() throws Exception {
        int incrementToBeer = 10;

        when(beerService.incrementQuantityBeer( INVALID_ID, incrementToBeer ))
                .thenThrow(BeerNoSuchElementException.class);

        mockMvc.perform(patch(INCREMENT_BEER, INVALID_ID, incrementToBeer)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
