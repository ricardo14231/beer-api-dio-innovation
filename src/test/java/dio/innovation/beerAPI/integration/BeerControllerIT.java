package dio.innovation.beerAPI.integration;

import dio.innovation.beerAPI.dto.BeerDTO;
import dio.innovation.beerAPI.entity.BeerEntity;
import dio.innovation.beerAPI.repository.BeerRepository;
import dio.innovation.beerAPI.utils.BeerDTOBuilder;
import dio.innovation.beerAPI.utils.BeerEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DisplayName("Tests integration.")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BeerControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BeerRepository beerRepository;

    @Test
    @DisplayName("Returns created beer message.")
    void whenCreateBeerCalled_ThenReturnBeerIsCreated() {
        BeerDTO newBeer = BeerDTOBuilder.createBeerDTOBuilder();

        String response = testRestTemplate.postForEntity("/beer/create", newBeer, String.class).getBody();

        Optional<BeerEntity> beerExpected = beerRepository.findByName(newBeer.getName());

        Assertions.assertNotNull(beerExpected);
        Assertions.assertEquals(String.format("Cerveja salva com ID: %o.", beerExpected.get().getId()), response);
    }

    @Test
    @DisplayName("Returns beer list all.")
    void whenListBeerCalled_ThenReturnList() {

        beerRepository.save(BeerEntityBuilder.createBeerEntityToSavedBuilder());

        List<BeerDTO> listBeer = testRestTemplate.exchange("/beer/list", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<BeerDTO>>() { }).getBody();

        Assertions.assertFalse(listBeer.isEmpty());
        Assertions.assertTrue(listBeer.size() == 1);
    }

    @Test
    @DisplayName("Returns beer per id.")
    void whenFindByIdBeerCalled_ThenBeer() {

        BeerEntity beerSaved = beerRepository.save(BeerEntityBuilder.createBeerEntityToSavedBuilder());

        BeerDTO beer = testRestTemplate.getForObject("/beer/{id}", BeerDTO.class, beerSaved.getId());

        Assertions.assertEquals(beerSaved.getId(), beer.getId());
        Assertions.assertEquals(beerSaved.getName(), beer.getName());
        Assertions.assertNotNull(beer);
    }

    @Test
    @DisplayName("Returns the beer when searched by name.")
    void whenFindNameBeerCalled_ThenReturnBeer() {
        BeerEntity beerToSave = beerRepository.save(BeerEntityBuilder.createBeerEntityToSavedBuilder());

        BeerDTO beer = testRestTemplate.getForObject("/beer/findName/{name}", BeerDTO.class, beerToSave.getName());

        Assertions.assertEquals(beerToSave.getId(), beer.getId());
        Assertions.assertEquals(beerToSave.getName(), beer.getName());
        Assertions.assertNotNull(beer);
    }



    @Test
    @DisplayName("Returns success message when called update beer.")
    void whenUpdateBeerCalled_ThenUpdateBeer() {

        BeerEntity beerSaved = beerRepository.save(BeerEntityBuilder.createBeerEntityToSavedBuilder());

        BeerDTO beerToUpdate = BeerDTOBuilder.updateBeerDTOBuilder();
        beerToUpdate.setId( beerSaved.getId() );

        String response = testRestTemplate.exchange("/beer/update/{id}",
                HttpMethod.PUT, new HttpEntity<>(beerToUpdate), String.class, beerToUpdate.getId()).getBody();

        beerRepository.findByName(beerToUpdate.getName());

        Assertions.assertEquals(String.format("Cerveja com ID: %o atualizada!", beerToUpdate.getId()), response);
    }

    @Test
    @DisplayName("Returns Ok status when delete beer.")
    void whenDeleteBeerCalled_ThenStatusOk() {

        BeerEntity beerSaved = beerRepository.save(BeerEntityBuilder.createBeerEntityToSavedBuilder());

        String response = testRestTemplate.exchange("/beer/delete/{id}",
                HttpMethod.DELETE, null, String.class, beerSaved.getId()).getBody();

        Assertions.assertEquals(String.format("Cerveja com ID: %o deleteda!", beerSaved.getId()), response);
    }

}
