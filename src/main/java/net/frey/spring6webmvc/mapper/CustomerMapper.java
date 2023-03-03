package net.frey.spring6webmvc.mapper;

import net.frey.spring6webmvc.model.dto.CustomerDTO;
import net.frey.spring6webmvc.model.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    CustomerEntity dtoToEntity(CustomerDTO dto);

    CustomerDTO entityToDto(CustomerEntity entity);
}
