package com.oldman.msbrewery.web.mappers;

import com.oldman.msbrewery.domain.Beer;
import com.oldman.msbrewery.web.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper( uses = DateMapper.class )
public interface BeerMapper {
    BeerDto beerToBeerDto(Beer beer);
    Beer beerDtoToBeer(BeerDto dto);
}
