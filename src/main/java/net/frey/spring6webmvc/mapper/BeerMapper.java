package net.frey.spring6webmvc.mapper;

import net.frey.spring6webmvc.model.dto.BeerDTO;
import net.frey.spring6webmvc.model.entity.BeerEntity;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    BeerEntity dtoToEntity(BeerDTO dto);

    BeerDTO entityToDto(BeerEntity entity);
}
