package net.frey.spring6webmvc.model.dto;

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
    private String beerName;
    private BeerStyle beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
