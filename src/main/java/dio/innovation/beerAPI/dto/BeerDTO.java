package dio.innovation.beerAPI.dto;

import dio.innovation.beerAPI.enums.BeerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeerDTO {

    private Long id;

    @NotEmpty
    @Size(max = 50)
    private String name;

    @NotEmpty
    @Size(max = 50)
    private String brand;

    @NotNull
    @Min(0)
    @Max(500)
    private int max;

    @NotNull
    @Min(0)
    @Max(100)
    private int quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BeerType type;

    private byte consumptionTemperature;

    @NotNull
    private byte alcoholicStrenght;

    private String description;
}
