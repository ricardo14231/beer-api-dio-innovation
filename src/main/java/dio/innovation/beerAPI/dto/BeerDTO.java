package dio.innovation.beerAPI.dto;

import dio.innovation.beerAPI.enums.BeerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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

    private int max;

    @Size(min = 0)
    private int quantity;

    @NotEmpty
    @Size(max = 8)
    private BeerType type;
}
