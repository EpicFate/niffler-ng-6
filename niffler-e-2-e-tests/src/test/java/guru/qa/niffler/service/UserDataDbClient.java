package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.UserDataDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.NoSuchElementException;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class UserDataDbClient {

    private static final Config CFG = Config.getInstance();


    public UserEntity createUser(UserEntity user) {
        return transaction(connection -> {
            return new UserDataDaoJdbc(connection).createUser(user);
        }, CFG.userdataJdbcUrl());
    }

    public UserEntity findById(UUID id) {
        return transaction(connection -> {
            return new UserDataDaoJdbc(connection).findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found by id -> [%s]".formatted(id)));
        }, CFG.userdataJdbcUrl());
    }

    public UserEntity findByUsername(String username) {
        return transaction(connection -> {
            return new UserDataDaoJdbc(connection).findByUsername(username)
                    .orElseThrow(() -> new NoSuchElementException("User not found by username -> [%s]".formatted(username)));
        }, CFG.userdataJdbcUrl());
    }

    public void delete(UserEntity user) {
        transaction(connection -> {
            new UserDataDaoJdbc(connection).delete(user);
        }, CFG.userdataJdbcUrl());
    }
}
