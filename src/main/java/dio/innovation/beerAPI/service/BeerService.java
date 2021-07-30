package dio.innovation.beerAPI.service;

import dio.innovation.beerAPI.dto.BeerDTO;
import dio.innovation.beerAPI.entity.BeerEntity;
import dio.innovation.beerAPI.exception.BeerNoSuchElementExpertion;
import dio.innovation.beerAPI.mapper.BeerMapper;
import dio.innovation.beerAPI.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeerService {

    @Autowired
    private BeerRepository beerRepository;

    private BeerMapper beerMapper = BeerMapper.INSTANCE;

    public String createBeer(BeerDTO beerDTO) {
        BeerEntity beerEntityToSave = beerMapper.toModel(beerDTO);

        try {
            BeerEntity beerEntity = beerRepository.save(beerEntityToSave);
            return String.format("Cerveja salva com ID: %o.", beerEntity.getId());
        }catch (DataIntegrityViolationException err) {
            return String.format("Erro ao salvar cerveja! " + err.getMessage());
        }
    }

    public List<BeerDTO> listAllBeer() {
        return beerRepository.findAll().stream()
                .map(beer -> beerMapper.toDTO(beer))
                .collect(Collectors.toList());
    }

    public BeerDTO findByIdBeer(Long id) throws BeerNoSuchElementExpertion{

        return beerMapper.toDTO( verifyIfExists(id) );
    }

    public String updateBeer(Long id, BeerDTO beerDTO) throws BeerNoSuchElementExpertion {
        verifyIfExists(id);
        beerRepository.save( beerMapper.toModel(beerDTO) );

        return String.format("Cerveja com ID: %o atualizada!", id);
    }

    public String deleteBeer(Long id) throws BeerNoSuchElementExpertion{
        verifyIfExists(id);
        beerRepository.deleteById(id);

        return String.format("Cerveja com ID: %o deleteda!", id );
    }

    private BeerEntity verifyIfExists(Long id) {
        return beerRepository.findById(id)
                .orElseThrow(() -> new BeerNoSuchElementExpertion(id));
    }

}
