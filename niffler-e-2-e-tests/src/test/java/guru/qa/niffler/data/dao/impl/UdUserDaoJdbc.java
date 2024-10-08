package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UdUserDaoJdbc implements UdUserDao {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.userdataJdbcUrl();

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement(
                """
                        INSERT INTO "user" (username, currency, firstname, surname, photo, photo_small, full_name)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """,
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());

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
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement("""
              SELECT * FROM "user"
              WHERE id = ?
              """)) {
            ps.setObject(1, id);
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
    public Optional<UserEntity> findByUsername(UserEntity user) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement("""
              SELECT * FROM "user"
              WHERE username = ?
              """)) {
            ps.setObject(1, user.getUsername());
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
    public void delete(UserEntity user) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement("""
              DELETE FROM "user"
              WHERE id = ?
              """)) {
            ps.setObject(1, user.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserEntity> findAll() {
        try (PreparedStatement ps = holder(url).connection().prepareStatement("SELECT * FROM \"user\"")) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                ArrayList<UserEntity> list = new ArrayList<>();
                if (rs.next()) {
                    while (rs.next()) {
                        list.add(fillUser(rs));
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


    private UserEntity fillUser(ResultSet rs) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(rs.getObject("id", UUID.class));
        user.setUsername(rs.getString("username"));
        user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        user.setFirstname(rs.getString("firstname"));
        user.setSurname(rs.getString("surname"));
        user.setFullname(rs.getString("full_name"));
        user.setPhoto(rs.getBytes("photo"));
        user.setPhotoSmall(rs.getBytes("photo_small"));
        return user;
    }
}
