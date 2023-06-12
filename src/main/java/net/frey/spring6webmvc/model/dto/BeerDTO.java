package net.frey.spring6webmvc.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import net.frey.spring6webmvc.model.BeerStyle;

@Data
@Builder
public class BeerDTO {
    private UUID id;
    private int version;

    @NotBlank
    private String beerName;

    @NotNull
    private BeerStyle beerStyle;

    @NotBlank
    private String upc;

    private Integer quantityOnHand;

    @NotNull
    private BigDecimal price;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
