package dio.innovation.beerAPI.utils;

import dio.innovation.beerAPI.entity.BeerEntity;
import dio.innovation.beerAPI.enums.BeerType;

public class BeerEntityBuilder {

    public static BeerEntity createBeerEntityToSavedBuilder() {
        return BeerEntity.builder()
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

    public static BeerEntity updateBeerEntityBuilder() {
        return BeerEntity.builder()
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
