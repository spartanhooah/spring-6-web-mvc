package net.frey.spring6webmvc.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Customer {
    private UUID id;
    private String name;
    private int version;
    private LocalDateTime created;
    private LocalDateTime lastModified;
}
