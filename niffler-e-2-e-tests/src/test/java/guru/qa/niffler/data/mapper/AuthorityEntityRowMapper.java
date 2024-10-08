package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthorityEntityRowMapper implements RowMapper<AuthorityEntity> {

    public static final AuthorityEntityRowMapper instance = new AuthorityEntityRowMapper();

    private AuthorityEntityRowMapper() {
    }

    @Override
    public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

        UUID userId = rs.getObject("user_id", UUID.class);
        AuthUserEntity authUserEntity = new AuthUserRepositoryJdbc()
                .findById(userId)
                .orElseThrow(() -> new SQLException("AuthUser not found by id -> [%s]".formatted(userId)));

        AuthorityEntity authority = new AuthorityEntity();
        authority.setUser(authUserEntity);
        authority.setAuthority(Authority.valueOf(rs.getString("authority")));
        return authority;
    }
}
