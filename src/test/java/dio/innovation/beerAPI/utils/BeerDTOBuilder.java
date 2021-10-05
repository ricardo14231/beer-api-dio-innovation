package dio.innovation.beerAPI.utils;

import dio.innovation.beerAPI.dto.BeerDTO;
import dio.innovation.beerAPI.enums.BeerType;

public class BeerDTOBuilder {

    public static BeerDTO createBeerDTOBuilder() {
        return BeerDTO.builder()
                .name("Brahma")
                .brand("Ambev")
                .max(50)
                .quantity(20)
                .type(BeerType.ALE)
                .consumptionTemperature((byte) 5)
                .alcoholicStrenght((byte) 60)
                .description("description...")
                .build();
    }

    public static BeerDTO updateBeerDTOBuilder() {
        return BeerDTO.builder()
                .id(1L)
                .name("Skol")
                .brand("Ambev")
                .max(50)
                .quantity(20)
                .type(BeerType.ALE)
                .consumptionTemperature((byte) 5)
                .alcoholicStrenght((byte) 60)
                .description("description...")
                .build();
    }

}
