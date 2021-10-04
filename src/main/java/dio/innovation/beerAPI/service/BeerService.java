package dio.innovation.beerAPI.service;

import dio.innovation.beerAPI.dto.BeerDTO;
import dio.innovation.beerAPI.entity.BeerEntity;
import dio.innovation.beerAPI.exception.BeerAlreadyRegisteredException;
import dio.innovation.beerAPI.exception.BeerNoSuchElementException;
import dio.innovation.beerAPI.exception.BeerQuantityException;
import dio.innovation.beerAPI.mapper.BeerMapper;
import dio.innovation.beerAPI.repository.BeerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BeerService {

    private final BeerRepository beerRepository;

    public BeerService(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    private static final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public String createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {

        beerAlreadyRegistered(beerDTO.getName());

        try {
            BeerEntity beerEntityToSave = beerMapper.toModel(beerDTO);

            BeerEntity beerEntity = beerRepository.save(beerEntityToSave);
            return String.format("Cerveja salva com ID: %o.", beerEntity.getId());
        }catch (DataIntegrityViolationException err) {
            return "Erro ao salvar cerveja! Erro:" + err.getMessage();
        }
    }

    public List<BeerDTO> listAllBeer() {
        return beerRepository.findAll().stream()
                .map(beerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BeerDTO findByIdBeer(Long id) throws BeerNoSuchElementException {

        return beerMapper.toDTO( verifyIfExists(id) );
    }

    public BeerDTO findByNameBeer(String name) {
        BeerEntity beerEntity = beerRepository.findByName(name)
                .orElseThrow(() -> new BeerNoSuchElementException(null));

        return beerMapper.toDTO(beerEntity);
    }

    public String updateBeer(Long id, BeerDTO beerDTO) throws BeerAlreadyRegisteredException {

        beerAlreadyRegistered(beerDTO.getName());

        verifyIfExists(id);
        beerRepository.save( beerMapper.toModel(beerDTO) );

        return String.format("Cerveja com ID: %o atualizada!", id);
    }

    public String deleteBeer(Long id) throws BeerNoSuchElementException {
        verifyIfExists(id);
        beerRepository.deleteById(id);

        return String.format("Cerveja com ID: %o deleteda!", id );
    }

    public BeerDTO incrementQuantityBeer(Long id, int quantityToIncrement) throws BeerQuantityException {
        BeerEntity beerEntity = verifyIfExists(id);

        int quantityToMaxIncrement = quantityToIncrement + beerEntity.getQuantity();

        if(quantityToMaxIncrement <= beerEntity.getMax() && quantityToMaxIncrement >= 0) {
            int currentQuantity = beerEntity.getQuantity();
            beerEntity.setQuantity(currentQuantity + quantityToIncrement);
            beerRepository.save(beerEntity);
            return beerMapper.toDTO(beerEntity);
        }
        throw new BeerQuantityException();
    }

    private BeerEntity verifyIfExists(Long id) {
        return beerRepository.findById(id)
                .orElseThrow(() -> new BeerNoSuchElementException(id));
    }

    private void beerAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
        Optional<BeerEntity> beer = beerRepository.findByName(name);

        if(beer.isPresent()) {
            throw new BeerAlreadyRegisteredException();
        }
    }
}
