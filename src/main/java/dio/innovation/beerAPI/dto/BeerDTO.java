package dio.innovation.beerAPI.dto;

import dio.innovation.beerAPI.enums.BeerType;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("Id da cerveja.")
    private Long id;

    @ApiModelProperty("Nome.")
    @NotEmpty
    @Size(max = 50)
    private String name;

    @ApiModelProperty("Marca.")
    @NotEmpty
    @Size(max = 50)
    private String brand;

    @ApiModelProperty("Quantidade máxima de cervejas no estoque.")
    @NotNull
    @Min(0)
    @Max(500)
    private int max;

    @ApiModelProperty("Quantidade de cerveja no estoque.")
    @NotNull
    @Min(0)
    @Max(100)
    private int quantity;

    @ApiModelProperty("Tipo da cerveja.")
    @NotNull
    @Enumerated(EnumType.STRING)
    private BeerType type;

    @ApiModelProperty("Temperatura de consumo.")
    private byte consumptionTemperature;

    @ApiModelProperty("Dosagem alcoólica.")
    @NotNull
    private byte alcoholicStrenght;

    @ApiModelProperty("Descrição.")
    private String description;
}
