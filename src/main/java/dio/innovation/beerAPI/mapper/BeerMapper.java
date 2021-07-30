package dio.innovation.beerAPI.mapper;

import dio.innovation.beerAPI.dto.BeerDTO;
import dio.innovation.beerAPI.entity.BeerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BeerMapper {

    BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);

    BeerEntity toModel(BeerDTO beerDTO);

    BeerDTO toDTO(BeerEntity beerEntity);
}
