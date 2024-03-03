package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.City;

import java.util.Collection;

public interface CityService {
    Collection<City> findAll();
}
