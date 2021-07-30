package dio.innovation.beerAPI.entity;

import dio.innovation.beerAPI.enums.BeerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    @Size(max = 50)
    private String brand;

    @Column
    private int max;

    @Column
    private int quantity;

    @JoinColumn(nullable = false)
    private BeerType type;
}
