package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthorityEntityRowMapper;
import org.apache.commons.lang3.NotImplementedException;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.authJdbcUrl();

    @Override
    public void create(AuthorityEntity... authority) {
        new JdbcTemplate(DataSources.dataSource(url)).batchUpdate(
                "INSERT INTO authority (user_id, authority) VALUES (? , ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authority[i].getUser().getId());
                        ps.setString(2, authority[i].getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authority.length;
                    }
                }
        );
    }

    @Override
    public void deleteAuthority(UUID uuid) {
        new JdbcTemplate(DataSources.dataSource(url)).update("""
              DELETE FROM authority
              WHERE user_id = ?
              """, uuid);
    }

    @Override
    public List<AuthorityEntity> findAll() {
        return new JdbcTemplate(DataSources.dataSource(url)).query("""
                SELECT * FROM authority
                """, AuthorityEntityRowMapper.instance);
    }
}
