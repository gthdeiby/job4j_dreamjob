package ru.job4j.dreamjob.repository;

import ru.job4j.dreamjob.model.City;

import java.util.Collection;

public interface CityRepository {
    Collection<City> findAll();
}
