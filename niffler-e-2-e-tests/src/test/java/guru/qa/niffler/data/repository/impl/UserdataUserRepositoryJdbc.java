package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.UdUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import org.apache.commons.lang3.NotImplementedException;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.PENDING;
import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.userdataJdbcUrl();

    @Override
    public UserEntity update(UserEntity user) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement("""
                INSERT INTO friendship (requester_id, addressee_id, status, created_date)
                VALUES(?, ?, ?, ?)
                """)) {
            if (!user.getFriendshipRequests().isEmpty()) {
                for (FriendshipEntity fr : user.getFriendshipRequests()) {
                    if (fr.getStatus().equals(ACCEPTED)) {
                        ps.setObject(1, fr.getAddressee().getId());
                        ps.setObject(2, fr.getRequester().getId());
                        ps.setString(3, fr.getStatus().name());
                        ps.setDate(4, new java.sql.Date(fr.getCreatedDate().getTime()));
                        ps.addBatch();
                        ps.clearParameters();

                    }
                    ps.setObject(1, fr.getRequester().getId());
                    ps.setObject(2, fr.getAddressee().getId());
                    ps.setString(3, fr.getStatus().name());
                    ps.setDate(4, new java.sql.Date(fr.getCreatedDate().getTime()));
                    ps.addBatch();
                    ps.clearParameters();
                }
            }
            if (!user.getFriendshipAddressees().isEmpty()) {
                for (FriendshipEntity fa : user.getFriendshipAddressees()) {
                    ps.setObject(1, fa.getRequester().getId());
                    ps.setObject(2, fa.getAddressee().getId());
                    ps.setString(3, fa.getStatus().name());
                    ps.setDate(4, new java.sql.Date(fa.getCreatedDate().getTime()));
                    ps.addBatch();
                    ps.clearParameters();
                }
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new NotImplementedException();
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement("""
                INSERT INTO friendship (requester_id, addressee_id, status, created_date)
                VALUES(?, ?, ?, ?)
                """)) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, ACCEPTED.name());
            ps.setDate(4, new java.sql.Date(new java.util.Date().getTime()));
            ps.addBatch();
            ps.clearParameters();
            ps.setObject(1, addressee.getId());
            ps.setObject(2, requester.getId());
            ps.setString(3, ACCEPTED.name());
            ps.setDate(4, new java.sql.Date(new java.util.Date().getTime()));
            ps.addBatch();
            ps.clearParameters();
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement("""
                INSERT INTO friendship (requester_id, addressee_id, status, created_date)
                VALUES(?, ?, ?, ?)
                """)) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, PENDING.name());
            ps.setDate(4, new java.sql.Date(new java.util.Date().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(UserEntity user) {
        new UdUserDaoJdbc().remove(user);

    }

    @Override
    public UserEntity create(UserEntity user) {
        return new UdUserDaoJdbc().create(user);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return new UdUserDaoJdbc().findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return new UdUserDaoJdbc().findByUsername(username);
    }
}
