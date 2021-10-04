package dio.innovation.beerAPI;

import dio.innovation.beerAPI.builder.BeerDTOBuilder;
import dio.innovation.beerAPI.builder.BeerEntityBuilder;
import dio.innovation.beerAPI.dto.BeerDTO;
import dio.innovation.beerAPI.entity.BeerEntity;
import dio.innovation.beerAPI.exception.BeerAlreadyRegisteredException;
import dio.innovation.beerAPI.exception.BeerNoSuchElementException;
import dio.innovation.beerAPI.exception.BeerQuantityException;
import dio.innovation.beerAPI.repository.BeerRepository;
import dio.innovation.beerAPI.service.BeerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for beer services.")
class BeerServiceTests {

	@InjectMocks
	BeerService beerService;

	@Mock
	BeerRepository beerRepository;

	@BeforeEach
	void setUp() {
		BeerEntity beerSaved = BeerEntityBuilder.createBeerEntityToSavedBuilder();
		BDDMockito.when(beerRepository.save(Mockito.any())).thenReturn(beerSaved);
	}

	@Test
	@DisplayName("Successful when create beer.")
	void createBeer_ReturnMessageSuccessful_whenSuccessful() throws BeerAlreadyRegisteredException {
		BeerDTO beerToSave = BeerDTOBuilder.createBeerDTOBuilder();
		Long idExpected = beerToSave.getId();
		beerToSave.setId(null);

		String response = beerService.createBeer(beerToSave);

		Assertions.assertEquals(String.format("Cerveja salva com ID: %o.", idExpected), response);
	}

	@Test
	@DisplayName("Return List when called list beer.")
	void listBeer_ReturnList_whenSuccessful() {
		List<BeerEntity> listExpected = Collections.singletonList(BeerEntityBuilder.createBeerEntityToSavedBuilder());

		BDDMockito.when(beerRepository.findAll()).thenReturn(listExpected);

		List<BeerDTO> listReturned = beerService.listAllBeer();

		Assertions.assertEquals(1, listReturned.size());
		Assertions.assertEquals(listReturned.get(0).getId(), listExpected.get(0).getId());
		Assertions.assertEquals(listReturned.get(0).getName(), listExpected.get(0).getName());
	}

	@Test
	@DisplayName("Return List empty when called list beer.")
	void listBeer_ReturnListEmpty_whenSuccessful() {

		BDDMockito.when(beerRepository.findAll()).thenReturn(Collections.emptyList());

		Assertions.assertEquals(Collections.emptyList(), beerService.listAllBeer());
	}

	@Test
	@DisplayName("Return beer per id.")
	void findByIdBeer_ReturnBeer_whenSuccessful() {
		BeerEntity beerExpected = BeerEntityBuilder.createBeerEntityToSavedBuilder();

		BDDMockito.when(beerRepository.findById(Mockito.any())).thenReturn(Optional.of(beerExpected));

		BeerDTO beerReturned = beerService.findByIdBeer(1L);

		Assertions.assertEquals(beerExpected.getId(), beerReturned.getId());
		Assertions.assertEquals(beerExpected.getName(), beerReturned.getName());
	}

	@Test
	@DisplayName("Return element not found when called find by id.")
	void findById_ReturnNoSuchElementException_whenElementNotFound() {

		BDDMockito.when(beerRepository.findById(Mockito.any())).thenReturn(Optional.empty());

		Assertions.assertThrows(BeerNoSuchElementException.class, () -> beerService.findByIdBeer(1L));
	}

	@Test
	@DisplayName("Return beer per name.")
	void findByNameBeer_ReturnBeer_whenSuccessful() {
		BeerEntity beerExpected = BeerEntityBuilder.createBeerEntityToSavedBuilder();

		BDDMockito.when(beerRepository.findByName(Mockito.any())).thenReturn(Optional.of(beerExpected));

		BeerDTO beerReturned = beerService.findByNameBeer(beerExpected.getName());

		Assertions.assertEquals(beerExpected.getId(), beerReturned.getId());
		Assertions.assertEquals(beerExpected.getName(), beerReturned.getName());
	}

	@Test
	@DisplayName("Return element not found when called find by name.")
	void findByName_ReturnNoSuchElementException_whenElementNotFound() {

		BDDMockito.when(beerRepository.findByName(Mockito.any())).thenReturn(Optional.empty());

		Assertions.assertThrows(BeerNoSuchElementException.class, () -> beerService.findByNameBeer("Brahma"));
	}

	@Test
	@DisplayName("Successful when update beer.")
	void updateBeer_ReturnMessageSuccessful_whenSuccessful() throws BeerAlreadyRegisteredException {
		BeerEntity beerToUpdate = BeerEntityBuilder.updateBeerEntityBuilder();
		BeerDTO beerSendToUpdate = BeerDTOBuilder.updateBeerDTOBuilder();

		BDDMockito.when(beerRepository.findById(Mockito.any())).thenReturn(Optional.of(beerToUpdate));
		BDDMockito.when(beerRepository.save(Mockito.any())).thenReturn(beerToUpdate);

		String response = beerService.updateBeer(beerSendToUpdate.getId(), beerSendToUpdate);

		Assertions.assertEquals(String.format("Cerveja com ID: %o atualizada!", beerToUpdate.getId()), response);
	}

	@Test
	@DisplayName("Return element not found when called update beer.")
	void updateBeer_ReturnNoSuchElementException_whenElementNotFound() throws BeerAlreadyRegisteredException {
		BeerDTO beerToUpdate = BeerDTOBuilder.updateBeerDTOBuilder();

		BDDMockito.when(beerRepository.findById(Mockito.any())).thenReturn(Optional.empty());

		Assertions.assertThrows(BeerNoSuchElementException.class,
				() -> beerService.updateBeer(beerToUpdate.getId(), beerToUpdate));
	}

	@Test
	@DisplayName("Successful when delete beer.")
	void deleteBeer_ReturnMessageSuccessful_whenSuccessful() throws BeerAlreadyRegisteredException {
		BeerEntity beerToDelete = BeerEntityBuilder.updateBeerEntityBuilder();

		BDDMockito.when(beerRepository.findById(Mockito.any())).thenReturn(Optional.of(beerToDelete));
		BDDMockito.doNothing().when(beerRepository).deleteById(beerToDelete.getId());

		String response = beerService.deleteBeer(beerToDelete.getId());

		Assertions.assertEquals(String.format("Cerveja com ID: %o deleteda!", beerToDelete.getId() ), response);
	}

	@Test
	@DisplayName("Return element not found when called delete beer.")
	void deleteBeer_ReturnNoSuchElementException_whenElementNotFound() throws BeerAlreadyRegisteredException {
		BeerDTO beerToDelete = BeerDTOBuilder.updateBeerDTOBuilder();

		BDDMockito.when(beerRepository.findById(Mockito.any())).thenReturn(Optional.empty());

		Assertions.assertThrows(BeerNoSuchElementException.class,
				() -> beerService.updateBeer(beerToDelete.getId(), beerToDelete));
	}

	@Test
	@DisplayName("Return beer quantity increase.")
	void incrementQuantity_ReturnBeerQuantityIncrease_whenSuccessful() throws BeerQuantityException {
		BeerEntity beerToExpected = BeerEntityBuilder.createBeerEntityToSavedBuilder();

		BDDMockito.when(beerRepository.findById(Mockito.any())).thenReturn(Optional.of(beerToExpected));
		beerToExpected.setQuantity(beerToExpected.getQuantity() + 10);
		BDDMockito.when(beerRepository.save(Mockito.any())).thenReturn(beerToExpected);

		BeerDTO beerToIncrease = beerService.incrementQuantityBeer(beerToExpected.getId(), 10);

		Assertions.assertEquals(beerToExpected.getId(), beerToIncrease.getId());
		Assertions.assertEquals(beerToExpected.getQuantity(), beerToIncrease.getQuantity());
	}

	@Test
	@DisplayName("Returns beer not found when incrementing beer.")
	void incrementQuantity_ReturnNoSuchElementException_whenElementNotFound() throws BeerQuantityException {

		BDDMockito.when(beerRepository.findById(Mockito.any())).thenReturn(Optional.empty());

		Assertions.assertThrows(NoSuchElementException.class,
				() -> beerService.incrementQuantityBeer(1L, 10));
	}

	@Test
	@DisplayName("Returns exception when incrementing the maximum beer")
	void incrementQuantity_ReturnLimitMaximumQuantity_when() throws BeerQuantityException {
		BeerEntity beerToExpected = BeerEntityBuilder.createBeerEntityToSavedBuilder();

		BDDMockito.when(beerRepository.findById(Mockito.any())).thenReturn(Optional.of(beerToExpected));

		Assertions.assertThrows(BeerQuantityException.class,
				() -> beerService.incrementQuantityBeer(beerToExpected.getId(), beerToExpected.getMax() + 10));
	}

	@Test
	@DisplayName("Returns exception when incrementing the minimum beer")
	void incrementQuantity_ReturnLimitMinimumQuantity_whenElementNotFound() throws BeerQuantityException {

		BeerEntity beerToExpected = BeerEntityBuilder.createBeerEntityToSavedBuilder();

		BDDMockito.when(beerRepository.findById(Mockito.any())).thenReturn(Optional.of(beerToExpected));

		Assertions.assertThrows(BeerQuantityException.class,
				() -> beerService.incrementQuantityBeer(beerToExpected.getId(), (beerToExpected.getQuantity() * -1) - 10));
	}
}
