package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserDataDao;
import guru.qa.niffler.data.dao.impl.UsetDataDAOJdbc;
import guru.qa.niffler.data.entity.userData.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.NoSuchElementException;
import java.util.UUID;

public class UserDataDbClient {

    private final UserDataDao userDataDao = new UsetDataDAOJdbc();


    public UserEntity createUser(UserEntity user) {
        return userDataDao.createUser(user);
    }

    public UserEntity findById(UUID id) {
        return userDataDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found by id -> [%s]".formatted(id)));
    }

    public UserEntity findByUsername(String username) {
        return userDataDao.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found by username -> [%s]".formatted(username)));
    }

    public void delete(UserEntity user) {
        userDataDao.delete(user);
    }
}
