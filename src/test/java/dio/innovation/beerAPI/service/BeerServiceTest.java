package dio.innovation.beerAPI.service;

import dio.innovation.beerAPI.utils.BeerDTOBuilder;
import dio.innovation.beerAPI.dto.BeerDTO;
import dio.innovation.beerAPI.entity.BeerEntity;
import dio.innovation.beerAPI.exception.BeerAlreadyRegisteredException;
import dio.innovation.beerAPI.exception.BeerNoSuchElementException;
import dio.innovation.beerAPI.exception.BeerQuantityException;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
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
    void whenBeerInformed_ThenItShouldBeCreated() throws BeerAlreadyRegisteredException {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();
        BeerEntity beerEntityToSave = beerMapper.toModel(beerDTO);

        when(beerRepository.findByName(beerEntityToSave.getName())).thenReturn(Optional.empty());
        when(beerRepository.save(beerEntityToSave)).thenReturn(beerEntityToSave);

        String expectedSaveBeer = beerService.createBeer(beerDTO);

        assertThat(expectedSaveBeer, is(equalTo(String.format("Cerveja salva com ID: %o.", beerDTO.getId()))));
    }

    @Test
    @DisplayName("Não deve salvar duas cervejas com o mesmo nome.")
    void whenGivenSameBeerNamesShouldNotSave() {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();
        BeerEntity duplicatedBeer = beerMapper.toModel(beerDTO);

        when(beerRepository.findByName(beerDTO.getName())).thenReturn(Optional.of(duplicatedBeer));

        Assertions.assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(beerDTO));

    }

    @Test
    @DisplayName("Deve retornar uma lista de beer.")
    void whenRequestListAllBeerThenItShouldReturnListToBeer() {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();
        BeerEntity beerEntitie = beerMapper.toModel( beerDTO );

        when(beerRepository.findAll()).thenReturn(Collections.singletonList(beerEntitie));

        List<BeerDTO> listBeerDTO = beerService.listAllBeer();

        assertThat(listBeerDTO, is(not(empty())));
        assertThat(listBeerDTO.get(0), is(equalTo(beerDTO)));
    }

    @Test
    @DisplayName("Deve retornar uma lista de beer vazia.")
    void whenRequestListAllBeerThenItShouldReturnListEmptyToBeer() {

        when(beerRepository.findAll()).thenReturn(Collections.emptyList());

        assertThat(Collections.EMPTY_LIST, is(beerService.listAllBeer()));
    }

    @Test
    @DisplayName("Deve atualizar a beer.")
    void whenBeerInformed_ThenItShouldUpdateToBeer() throws BeerAlreadyRegisteredException {
        BeerDTO beerDTOUpdate = BeerDTOBuilder.updateBeerDTOBuilder();
        BeerEntity expectedBeerToUpdate = beerMapper.toModel(beerDTOUpdate);

        when(beerRepository.findById( expectedBeerToUpdate.getId() )).thenReturn(Optional.of(expectedBeerToUpdate));
        when(beerRepository.save(expectedBeerToUpdate)).thenReturn(expectedBeerToUpdate);

        assertThat(String.format("Cerveja com ID: %o atualizada!", beerDTOUpdate.getId()),
                is(beerService.updateBeer(beerDTOUpdate.getId(), beerDTOUpdate)));
    }

    @Test
    @DisplayName("Deve retornar beer não encontrada.")
    void whenBeerInformed_ThenItShouldBeerNotFound() {
        BeerDTO beerUpdate = BeerDTOBuilder.createBeerDTOBuilder();
        BeerEntity expectedBeerToUpdate = beerMapper.toModel(beerUpdate);

        when(beerRepository.findById( expectedBeerToUpdate.getId() )).thenReturn(Optional.empty());

        Assertions.assertThrows(BeerNoSuchElementException.class, () ->
                beerService.updateBeer( beerUpdate.getId(), beerUpdate ));
    }

    @Test
    @DisplayName("Deve retornar a beer ao pesquisar pelo id.")
    void whenIdBeerInformed_ThenItShouldBeer() {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();
        BeerEntity expectedBeer = beerMapper.toModel(beerDTO);

        when(beerRepository.findById( beerDTO.getId() )).thenReturn(Optional.of(expectedBeer));

        assertThat(beerDTO,
                is(equalTo(beerService.findByIdBeer( beerDTO.getId() ))));
    }

    @Test
    @DisplayName("Deve retornar a beer ao pesquisar pelo id.")
    void whenIdBeerInformed_ThenItShouldBeerNotFound() {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();

        when(beerRepository.findById( beerDTO.getId() ))
                .thenReturn(Optional.empty());

        Assertions.assertThrows( BeerNoSuchElementException.class ,
                () -> beerService.findByIdBeer( beerDTO.getId() ));
    }

    @Test
    @DisplayName("Não deve retornar a beer ao pesquisar pelo nome.")
    void whenNameBeerInformed_ThenItShouldBeerNotFound() {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();

        when(beerRepository.findByName( beerDTO.getName() ))
                .thenReturn(Optional.empty());

        Assertions.assertThrows( BeerNoSuchElementException.class ,
                () -> beerService.findByNameBeer( beerDTO.getName() ));
    }

    @Test
    @DisplayName("Deve deletar a beer.")
    void whenBeerIdInformed_ThenItShouldDeleteBeer() {
        BeerDTO beerDelete = BeerDTOBuilder.createBeerDTOBuilder();
        BeerEntity expectedBeer = beerMapper.toModel(beerDelete);

        when(beerRepository.findById( beerDelete.getId() )).thenReturn(Optional.of(expectedBeer));
        doNothing().when(beerRepository).deleteById( beerDelete.getId() );

        assertThat(String.format("Cerveja com ID: %o deleteda!", beerDelete.getId() ),
                is(beerService.deleteBeer(beerDelete.getId())));
    }

    @Test
    @DisplayName("Deve retornar beer não encontrada, ao deletar a beer.")
    void whenBeerIdInformed_ThenItShouldDeleteBeerNotFound() {

        when(beerRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(BeerNoSuchElementException.class,
                () -> beerService.deleteBeer( 1L));
    }

    @Test
    @DisplayName("Deve permitir incrementar a quantidade de beer.")
    void whenQuantityBeerInformed_ThenIncrementToBeer() throws BeerQuantityException {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();
        BeerEntity beerEntity = beerMapper.toModel(beerDTO);

        when(beerRepository.findById( beerDTO.getId() )).thenReturn(Optional.of(beerEntity));
        when(beerRepository.save(beerEntity)).thenReturn(beerEntity);

        int incrementToBeer = 5;
        int expectedToAfterIncrement = beerDTO.getQuantity() + incrementToBeer;

        BeerDTO beerDTOAfterIncrement = beerService.incrementQuantityBeer( beerDTO.getId(),  incrementToBeer);

        assertThat(expectedToAfterIncrement, equalTo(beerDTOAfterIncrement.getQuantity()));
        assertThat(expectedToAfterIncrement, lessThan(beerDTOAfterIncrement.getMax()));
    }

    @Test
    @DisplayName("Deve lançar a Exception após a soma do incremento com a quantidade de beer ultrapassar o max.")
    void whenQuantityPlusCurrentBeerInformed_OverrunMaxThenThrowsExceptionQuantityBeer() {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();
        BeerEntity beerEntity = beerMapper.toModel(beerDTO);

        when(beerRepository.findById( beerDTO.getId() )).thenReturn(Optional.of(beerEntity));

        int incrementToBeer = 35;

        Assertions.assertThrows(BeerQuantityException.class,
                () -> beerService.incrementQuantityBeer( beerDTO.getId(), incrementToBeer ));
    }

    @Test
    @DisplayName("Deve lançar a Exception ao incrementar a quantidade de beer.")
    void whenQuantityBeerInformed_ThenThrowsExceptionQuantityBeer() {
        BeerDTO beerDTO = BeerDTOBuilder.createBeerDTOBuilder();
        BeerEntity beerEntity = beerMapper.toModel(beerDTO);

        when(beerRepository.findById( beerDTO.getId() )).thenReturn(Optional.of(beerEntity));

        int incrementToBeer = beerDTO.getMax() + 5;

        Assertions.assertThrows(BeerQuantityException.class,
                () -> beerService.incrementQuantityBeer(beerDTO.getId(), incrementToBeer));
    }

}
