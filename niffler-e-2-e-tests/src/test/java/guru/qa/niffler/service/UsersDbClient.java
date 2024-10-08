package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UdUserDataRepositoryJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

//    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
//    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
    private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
    private final UdUserDataRepositoryJdbc userDataRepositoryJdbc = new UdUserDataRepositoryJdbc();
    private final UdUserDao udUserDao = new UdUserDaoSpringJdbc();

  private final TransactionTemplate txTemplate = new TransactionTemplate(
      new JdbcTransactionManager(
          DataSources.dataSource(CFG.authJdbcUrl())
      )
  );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );


    public void updateUser(UserEntity user) {
        txTemplate.execute(status -> {
            userDataRepositoryJdbc.update(user);
            return null;
        });
    }

  public UserJson createUser(UserJson user) {
    return xaTransactionTemplate.execute(() -> {
          AuthUserEntity authUser = new AuthUserEntity();
          authUser.setUsername(user.username());
          authUser.setPassword(pe.encode("12345"));
          authUser.setEnabled(true);
          authUser.setAccountNonExpired(true);
          authUser.setAccountNonLocked(true);
          authUser.setCredentialsNonExpired(true);
          authUser.setAuthorities(
              Arrays.stream(Authority.values()).map(
                  e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(authUser);
                    ae.setAuthority(e);
                    return ae;
                  }
              ).toList()
          );
          authUserRepository.create(authUser);
          return UserJson.fromEntity(
              udUserDao.create(UserEntity.fromJson(user)),
              null
          );
        }
    );
  }
    public UserJson createUserSpringJdbc(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("123"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);
            authUser.setAuthorities(
                    Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(authUser);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toList()
            );
            authUserRepository.create(authUser);
            return UserJson.fromEntity(
                    udUserDao.create(UserEntity.fromJson(user)),
                    null);
        });
    }
//    public UserJson createUserSpringJdbc(UserJson user) {
//        return xaTransactionTemplate.execute(() -> {
//            AuthUserEntity authUser = new AuthUserEntity();
//            authUser.setUsername(user.username());
//            authUser.setPassword(pe.encode("123"));
//            authUser.setEnabled(true);
//            authUser.setAccountNonExpired(true);
//            authUser.setAccountNonLocked(true);
//            authUser.setCredentialsNonExpired(true);
//            AuthUserEntity createdAuthUser = authUserDao.create(authUser);
//            AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
//                    e -> {
//                        AuthorityEntity ae = new AuthorityEntity();
//                        ae.setUserId(createdAuthUser.getId());
//                        ae.setAuthority(e);
//                        return ae;
//                    }
//            ).toArray(AuthorityEntity[]::new);
//            authAuthorityDao.create(authorityEntities);
//            return UserJson.fromEntity(
//                    udUserDao.create(UserEntity.fromJson(user)),
//                    null);
//        });
//    }


//    public void deleteUser(UserJson user) {
//        txTemplate.execute(status -> {
//            AuthUserEntity authUser = new AuthUserEntity();
//            authUser.setUsername(user.username());
//            authUser = authUserDao.findUserByName(authUser)
//                    .orElseThrow(() -> new NoSuchElementException("User not found by username -> [%s]"
//                            .formatted(user.username())));
//            authAuthorityDao.deleteAuthority(authUser.getId());
//            authUserDao.deleteUser(authUser.getId());
//            udUserDao.delete(UserEntity.fromJson(user));
//            return null;
//        });
//    }
}
