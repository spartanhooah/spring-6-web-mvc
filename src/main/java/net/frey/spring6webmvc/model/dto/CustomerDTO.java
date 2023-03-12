package net.frey.spring6webmvc.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    private UUID id;
    private String name;
    private int version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;
}
