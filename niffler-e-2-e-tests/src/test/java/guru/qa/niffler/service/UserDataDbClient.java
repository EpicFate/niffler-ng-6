package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;

import java.util.NoSuchElementException;
import java.util.UUID;

public class UserDataDbClient {

    private static final Config CFG = Config.getInstance();
    private final UserdataUserDao udUserDao = new UserdataUserDaoJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );



    public UserEntity createUser(UserEntity user) {
        return xaTransactionTemplate.execute(() -> udUserDao.create(user));
    }

    public UserEntity findById(UUID id) {
        return xaTransactionTemplate.execute(() ->
                udUserDao.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("User not found by id -> [%s]".formatted(id)))
        );
    }

    public UserEntity findByUsername(String user) {
        return xaTransactionTemplate.execute(() -> udUserDao.findByUsername(user)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found by username -> [%s]"
                                .formatted(user))
                )
        );
    }

    public void delete(UserEntity user) {
        xaTransactionTemplate.execute(() -> {
            udUserDao.remove(user);
            return null;
        });
    }
}
