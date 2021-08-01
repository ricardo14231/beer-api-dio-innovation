package dio.innovation.beerAPI.controller;

import dio.innovation.beerAPI.builder.BeerDTOBuilder;
import dio.innovation.beerAPI.builder.StringBeerDTOBuilder;
import dio.innovation.beerAPI.dto.BeerDTO;
import dio.innovation.beerAPI.exception.BeerAlreadyRegisteredException;
import dio.innovation.beerAPI.exception.BeerNoSuchElementException;
import dio.innovation.beerAPI.service.BeerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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
public class BeerControllerTest {

    private static final String CREATE_BEER = "/beer/create";
    private static final String LIST_BEER = "/beer/list";
    private static final String FIND_BY_ID_BEER = "/beer/{id}";
    private static final String UPDATE_BEER = "/beer/update/{id}";
    private static final String FIND_BY_NAME_BEER = "/beer/findName/{name}";
    private static final String DELETE_BEER = "/beer/delete/{id}";
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
    void whenCreateBeerCalledThenReturnBeerIsCreated() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();
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
    void whenCreateBeerCalledThenReturnBadRequest() throws Exception {
        String beerDTORequest = StringBeerDTOBuilder.createBeerNoNameBuilder();

        mockMvc.perform(post(CREATE_BEER)
               .contentType(MediaType.APPLICATION_JSON)
               .content(beerDTORequest))
               .andExpect(status().isBadRequest());
    }

    @Test
    void whenListBeerCalledThenReturnList() throws Exception {
        List<BeerDTO> beerDTOs = Collections.singletonList(BeerDTOBuilder.createBeerDTOBuilder());

        when(beerService.listAllBeer()).thenReturn(beerDTOs);

        mockMvc.perform(get(LIST_BEER)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(beerDTOs.size())))
                .andExpect(jsonPath("$[0].name" , Matchers.is(beerDTOs.get(0).getName())));
    }

    @Test
    void whenListBeerCalledThenReturnListEmpty() throws Exception {

        when(beerService.listAllBeer()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(LIST_BEER)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void whenFindByIdBeerCalledThenBeer() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();

        when(beerService.findByIdBeer(1L)).thenReturn(beerDTO);

        mockMvc.perform(get(FIND_BY_ID_BEER, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(beerDTO.getName()));
    }

    @Test
    void whenFindByIdBeerCalledThenBeerNotFound() throws Exception {

        when(beerService.findByIdBeer(1L)).thenThrow(BeerNoSuchElementException.class);

        mockMvc.perform(get(FIND_BY_ID_BEER, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenFindNameBeerCalledThenReturnBeer() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();

        when(beerService.findByNameBeer(beerDTO.getName()))
                .thenReturn(beerDTO);

        mockMvc.perform(get(FIND_BY_NAME_BEER, beerDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(beerDTO.getName()));
    }

    @Test
    void whenFindNameBeerCalledThenReturnBeerNotFound() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();

        when(beerService.findByNameBeer(beerDTO.getName()))
                .thenThrow(BeerNoSuchElementException.class);

        mockMvc.perform(get(FIND_BY_NAME_BEER, beerDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //TODO VERIFICAR
    @Test
    void whenUpdateBeerCalledThenUpdateBeer() throws Exception {
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
    void whenUpdateBeerCalledThenUpdateBeerNotFound() throws Exception {
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
    void whenDeleteBeerCalledThenStatusOk() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();

        when(beerService.deleteBeer( beerDTO.getId() ))
                .thenReturn(String.format("Cerveja com ID: %o deleteda!", beerDTO.getId()));

        mockMvc.perform(delete(DELETE_BEER, beerDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Cerveja com ID: %o deleteda!", beerDTO.getId())));
    }

    @Test
    void whenDeleteBeerCalledThenBeerNotFound() throws Exception {

        when(beerService.deleteBeer( INVALID_ID ))
                .thenThrow(BeerNoSuchElementException.class);

        mockMvc.perform(delete(DELETE_BEER, INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
