package dio.innovation.beerAPI.builder;

import dio.innovation.beerAPI.dto.BeerDTO;
import dio.innovation.beerAPI.enums.BeerType;

public class BeerDTOBuilder {

    private Long id;

    private String name;

    private String brand;

    private int max;

    private int quantity;

    private BeerType type;

    private String imagePath;

    private byte consumptionTemperature;

    private byte alcoholicStrenght;

    private String description;

    public static BeerDTO createBeerDTOBuilder() {
        return BeerDTO.builder()
                .id(1L)
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
