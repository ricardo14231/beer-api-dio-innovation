package dio.innovation.beerAPI.service;

import dio.innovation.beerAPI.builder.BeerDTOBulder;
import dio.innovation.beerAPI.dto.BeerDTO;
import dio.innovation.beerAPI.entity.BeerEntity;
import dio.innovation.beerAPI.exception.BeerAlreadyRegisteredException;
import dio.innovation.beerAPI.exception.BeerNoSuchElementException;
import dio.innovation.beerAPI.mapper.BeerMapper;
import dio.innovation.beerAPI.repository.BeerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {

    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    @Mock
    private BeerRepository beerRepository;

    @InjectMocks
    private BeerService beerService;

    @Test
    @DisplayName("Deve salvar a beer.")
    void whenBeerInformatedThenItShouldBeCreated() throws BeerAlreadyRegisteredException {
        BeerDTO beerDTO = BeerDTOBulder.createBeerDTOBuilder();
        BeerEntity beerEntityToSave = beerMapper.toModel(beerDTO);

        when(beerRepository.findByName(beerEntityToSave.getName())).thenReturn(Optional.empty());
        when(beerRepository.save(beerEntityToSave)).thenReturn(beerEntityToSave);

        String expectedSaveBeer = beerService.createBeer(beerDTO);

        assertThat(expectedSaveBeer, is(equalTo(String.format("Cerveja salva com ID: %o.", beerDTO.getId()))));
    }

    @Test
    @DisplayName("Não deve salvar duas cervejas com o mesmo nome.")
    void whenGivenSameBeerNamesShouldNotSave() {
        BeerDTO beerDTO = BeerDTOBulder.createBeerDTOBuilder();
        BeerEntity duplicatedBeer = beerMapper.toModel(beerDTO);

        when(beerRepository.findByName(beerDTO.getName())).thenReturn(Optional.of(duplicatedBeer));

        Assertions.assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(beerDTO));

    }

    @Test
    @DisplayName("Deve retornar uma lista de beer.")
    void whenRequestListAllBeerThenItShouldReturnListToBeer() {
        List<BeerDTO> beerDTOs = Collections.singletonList(BeerDTOBulder.createBeerDTOBuilder());
        List<BeerEntity> beerEntities = beerDTOs.stream().map(beerMapper::toModel).collect(Collectors.toList());

        when(beerRepository.findAll()).thenReturn(beerEntities);

        assertThat(beerDTOs, is(beerService.listAllBeer()));
    }

    @Test
    @DisplayName("Deve retornar uma lista de beer vazia.")
    void whenRequestListAllBeerThenItShouldReturnListEmptyToBeer() {

        when(beerRepository.findAll()).thenReturn(Collections.emptyList());

        assertThat(Collections.EMPTY_LIST, is(beerService.listAllBeer()));
    }

    @Test
    @DisplayName("Deve atualizar a beer.")
    void whenBeerInformatedThenItShouldUpdateToBeer() throws BeerAlreadyRegisteredException {
        BeerDTO beerDTOUpdate = BeerDTOBulder.updateBeerDTOBuilder();
        BeerEntity expectedBeerToUpdate = beerMapper.toModel(beerDTOUpdate);

        when(beerRepository.findById( expectedBeerToUpdate.getId() )).thenReturn(Optional.of(expectedBeerToUpdate));
        when(beerRepository.save(expectedBeerToUpdate)).thenReturn(expectedBeerToUpdate);

        assertThat(String.format("Cerveja com ID: %o atualizada!", 1L),
                is(beerService.updateBeer(1L, beerDTOUpdate)));
    }

    @Test
    @DisplayName("Deve retornar beer não encontrada.")
    void whenBeerInformatedThenItShouldBeerNotFound() {
        BeerDTO beerUpdate = BeerDTOBulder.createBeerDTOBuilder();
        BeerEntity expectedBeerToUpdate = beerMapper.toModel(beerUpdate);

        when(beerRepository.findById( expectedBeerToUpdate.getId() )).thenReturn(Optional.empty());

        Assertions.assertThrows(BeerNoSuchElementException.class, () ->
                beerService.updateBeer( 1L, beerUpdate ));
    }

    @Test
    @DisplayName("Deve deletar a beer.")
    void whenBeerIdInformatedThenItShouldDeleteBeer() {
        BeerDTO beerDelete = BeerDTOBulder.createBeerDTOBuilder();
        BeerEntity expectedBeer = beerMapper.toModel(beerDelete);

        when(beerRepository.findById(1L)).thenReturn(Optional.of(expectedBeer));

        assertThat(String.format("Cerveja com ID: %o deleteda!", 1L ),
                is(beerService.deleteBeer(1L)));
    }

    @Test
    @DisplayName("Deve retornar beer não encontrada, ao deletar a beer.")
    void whenBeerIdInformatedThenItShouldDeleteBeerNotFound() {

        when(beerRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(BeerNoSuchElementException.class, () ->
                beerService.deleteBeer( 1L));
    }

}
