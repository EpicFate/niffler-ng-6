package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;
import org.apache.commons.lang3.NotImplementedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserRepositoryJdbc implements AuthUserRepository {

  private static final Config CFG = Config.getInstance();
  private final Connection CONNECTION = holder(CFG.authJdbcUrl()).connection();

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    try (PreparedStatement userPs = CONNECTION.prepareStatement(
        "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
            "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
         PreparedStatement authorityPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
             "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)")) {
      userPs.setString(1, user.getUsername());
      userPs.setString(2, user.getPassword());
      userPs.setBoolean(3, user.getEnabled());
      userPs.setBoolean(4, user.getAccountNonExpired());
      userPs.setBoolean(5, user.getAccountNonLocked());
      userPs.setBoolean(6, user.getCredentialsNonExpired());

      userPs.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = userPs.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      user.setId(generatedKey);

      for (AuthorityEntity a : user.getAuthorities()) {
        authorityPs.setObject(1, generatedKey);
        authorityPs.setString(2, a.getAuthority().name());
        authorityPs.addBatch();
        authorityPs.clearParameters();
      }
      authorityPs.executeBatch();
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    try (PreparedStatement ps = CONNECTION.prepareStatement(
            """
                    SELECT * FROM "user" u
                      JOIN authority a
                        on u.id = a.user_id
                    where u.id = ?
                    """
    )) {
      ps.setObject(1, id);

      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        AuthUserEntity user = null;
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        while (rs.next()) {
          if (user == null) {
            user = AuthUserEntityRowMapper.instance.mapRow(rs, 1);
          }
          AuthorityEntity ae = new AuthorityEntity();
          ae.setUser(user);
          ae.setId(rs.getObject("a.id", UUID.class));
          ae.setAuthority(Authority.valueOf(rs.getString("authority")));
          authorityEntities.add(ae);
        }
        if (user == null) {
          return Optional.empty();
        } else {
          user.setAuthorities(authorityEntities);
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    throw new NotImplementedException();
  }

  @Override
  public AuthUserEntity update(AuthUserEntity user) {
    throw new NotImplementedException();
  }

  @Override
  public void remove(AuthUserEntity user) {
    throw new NotImplementedException();
  }
}
