package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jdbc.Connections.holder;

public class AuthUserDaoJdbc implements AuthUserDao {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.authJdbcUrl();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement("""
                        INSERT INTO "user" (username, "password", enabled, account_non_expired, account_non_locked, credentials_non_expired)
                        VALUES (?, ?, ?, ?, ?, ?)
                        """,
                PreparedStatement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedKey);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement("""
                SELECT * FROM "user"
                WHERE id = ?
                """)) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.ofNullable(
                            AuthUserEntityRowMapper.instance.mapRow(rs, rs.getRow())
                    );
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findUserByName(AuthUserEntity authUser) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement("""
                SELECT * FROM "user"
                WHERE username = ?
                """)) {
            ps.setObject(1, authUser.getUsername());
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.ofNullable(
                            AuthUserEntityRowMapper.instance.mapRow(rs, rs.getRow())
                    );
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        try (PreparedStatement ps = holder(url).connection().prepareStatement("""
                SELECT * FROM "user"
                """)) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                ArrayList<AuthUserEntity> list = new ArrayList<>();
                if (rs.next()) {
                    while (rs.next()) {
                        result.add(
                                AuthUserEntityRowMapper.instance.mapRow(rs, rs.getRow())
                        );
                    }
                    return list;
                } else {
                    return List.of();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement("""
                DELETE FROM "user"
                WHERE id = ?
                """)) {
            ps.setObject(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
