package com.oldman.msbrewery.services;

import com.oldman.msbrewery.web.model.BeerDto;
import com.oldman.msbrewery.web.model.BeerStyleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    @Override
    public BeerDto getBeerById(UUID beerId) {
        return BeerDto.builder()
                .id(UUID.randomUUID())
                .beerName("Black Sheep")
                .beerStyle(BeerStyleEnum.ALE)
                .build();
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return BeerDto.builder()
                .id(UUID.randomUUID())
                .build();
    }

    @Override
    public void updateBeer(UUID beerId, BeerDto beerDto) {
        // todo impl
    }

    @Override
    public void deleteById(UUID beerId) {
        log.debug("Deleting a beer..");
    }
}
