package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {

    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthUserEntity createUser(AuthUserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement("""
                        INSERT INTO "user" (username, "password", enabled, account_non_expired, account_non_locked, credentials_non_expired)
                        VALUES (?, ?, ?, ?, ?, ?)
                        """,
                Statement.RETURN_GENERATED_KEYS
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
    public Optional<AuthUserEntity> findUserByName(AuthUserEntity authUser) {
        try (PreparedStatement ps = connection.prepareStatement("""
                SELECT * FROM "user"
                WHERE username = ?
              """)) {
            ps.setObject(1, authUser.getUsername());
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.of(fillUser(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement("""
              DELETE FROM "user"
              WHERE id = ?
              """)) {
            ps.setObject(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement("""
                SELECT * FROM "user"
                WHERE id = ?
                """)) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.of( fillUser(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private AuthUserEntity fillUser(ResultSet rs) throws SQLException {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setId(rs.getObject("id", UUID.class));
        authUser.setUsername(rs.getString("username"));
        authUser.setPassword(rs.getString("password"));
        authUser.setEnabled(rs.getBoolean("enabled"));
        authUser.setAccountNonExpired(rs.getBoolean("account_non_expired"));
        authUser.setAccountNonLocked(rs.getBoolean("account_non_locked"));
        authUser.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        return authUser;
    }
}
