package com.oldman.msbrewery.services;

import com.oldman.msbrewery.web.model.BeerDto;

import java.util.UUID;

public interface BeerService {
    BeerDto getBeerById(UUID beerId);
}