package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AuthorityEntity... authority) {
        try (PreparedStatement ps = connection.prepareStatement("""
                        INSERT INTO authority (user_id,  authority)
                        VALUES (?, ?)
                        """,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (AuthorityEntity a : authority) {
                ps.setObject(1, a.getUserId());
                ps.setString(2, a.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAuthority(UUID uuid) {
        try (PreparedStatement ps = connection.prepareStatement("""
              DELETE FROM authority
              WHERE user_id = ?
              """)) {
            ps.setObject(1, uuid);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<AuthorityEntity> findAll() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM authority")) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                ArrayList<AuthorityEntity> list = new ArrayList<>();
                if (rs.next()) {
                    while (rs.next()) {
                        AuthorityEntity authority = new AuthorityEntity();
                        authority.setId(rs.getObject("id", UUID.class));
                        authority.setAuthority(Authority.valueOf(rs.getString("authority")));
                        authority.setUserId(rs.getObject("user_id", UUID.class));
                        list.add(authority);
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
}
