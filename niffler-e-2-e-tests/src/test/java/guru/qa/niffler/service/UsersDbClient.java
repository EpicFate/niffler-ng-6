package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserDataDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.xaTransaction;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserJson createUserSpringJdbc(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authorityEntities);

        return UserJson.fromEntity(
                new UdUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
                        .create(
                                UserEntity.fromJson(user)
                        ),
                null
        );
    }

    public UserJson createUser(UserJson user) {
        return UserJson.fromEntity(
                xaTransaction(

                        new XaFunction<>(connection -> {

                            AuthUserEntity authUser = new AuthUserEntity();
                            authUser.setUsername(user.username());
                            authUser.setPassword(pe.encode("123"));
                            new AuthUserDaoJdbc(connection).create(authUser);
                            new AuthAuthorityDaoJdbc(connection).create(
                                    Arrays.stream(Authority.values())
                                            .map(a -> {
                                                        AuthorityEntity ae = new AuthorityEntity();
                                                        ae.setUserId(authUser.getId());
                                                        ae.setAuthority(a);
                                                        return ae;
                                                    }
                                            ).toArray(AuthorityEntity[]::new));
                            return null;
                        }, CFG.authJdbcUrl()),

                        new XaFunction<>(connection -> {
                            UserEntity ue = new UserEntity();
                            ue.setUsername(user.username());
                            ue.setFullname(user.fullname());
                            ue.setCurrency(user.currency());
//                            ue.setFirstname(user.firstname());
//                            ue.setSurname(user.surname());
                            new UserDataDaoJdbc(connection).createUser(ue);
                            return ue;
                        }, CFG.userdataJdbcUrl())
                ), null
        );
    }


    public void deleteUser(UserJson user) {
        xaTransaction(
                new Databases.XaConsumer(connection -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser = new AuthUserDaoJdbc(connection).findUserByName(authUser)
                            .orElseThrow(() -> new NoSuchElementException("User not found by username -> [%s]"
                                    .formatted(user.username())));

                    new AuthAuthorityDaoJdbc(connection).deleteAuthority(authUser.getId());
                    new AuthUserDaoJdbc(connection).deleteUser(authUser.getId());
                }, CFG.authJdbcUrl()),

                new Databases.XaConsumer(connection -> {
                    new UserDataDaoJdbc(connection).delete(UserEntity.fromJson(user));
                }, CFG.userdataJdbcUrl())
        );
    }

}
