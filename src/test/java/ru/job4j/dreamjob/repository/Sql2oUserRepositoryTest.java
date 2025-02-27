package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearTable() {
        sql2oUserRepository.clearUsers();
    }

    @Test
    public void whenSaveAndGetSame() {
        var user = sql2oUserRepository.save(new User(0, "ivan@mail.ru", "Ivan", "Qwerty123"));
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword());
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSaveSeveralThenGet() {
        var user0 = sql2oUserRepository.save(new User());
        var user1 = sql2oUserRepository.save(new User(0, "dmitry@mail.ru", "Dmitry", "Qwerty123"));
        var user2 = sql2oUserRepository.save(new User(0, "ivan@mail.ru", "Ivan", "Qwerty123"));
        var savedUser2 = sql2oUserRepository.findByEmailAndPassword(user2.get().getEmail(), user2.get().getPassword());
        assertThat(savedUser2).usingRecursiveComparison().isEqualTo(user2);
    }

    @Test
    public void whenSaveSameThenException() {
        var user = sql2oUserRepository.save(new User(0, "dmitry@mail.ru", "Dmitry", "Qwerty123"));
        var user2 = sql2oUserRepository.save(new User(0, "dmitry@mail.ru", "Dmitry", "Qwerty123"));
        assertThat(user2).isEmpty();
    }

    @Test
    public void whenGetWrongThenEmpty() {
        var user = sql2oUserRepository.save(new User(0, "ivan@mail.ru", "Ivan", "Qwerty123"));
        var savedUser = sql2oUserRepository.findByEmailAndPassword("dmitry@mail.ru", "Qwerty123");
        assertThat(savedUser).isEmpty();
    }
}