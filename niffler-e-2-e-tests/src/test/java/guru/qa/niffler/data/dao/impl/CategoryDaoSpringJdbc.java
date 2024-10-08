package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoSpringJdbc implements CategoryDao {

    private static final Config CFG = Config.getInstance();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));

    @Override
    public CategoryEntity create(CategoryEntity category) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("""
                    INSERT INTO category (username, name, archived)
                    VALUES (?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, category.getUsername());
            ps.setString(2, category.getName());
            ps.setBoolean(3, category.isArchived());
            return ps;
        }, kh);
        category.setId((UUID) kh.getKeys().get("id"));
        return category;
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject("""
                        SELECT * FROM category
                        WHERE id = ?
                        """, CategoryEntityRowMapper.instance, id
                )
        );
    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        return jdbcTemplate.query("""
                SELECT * FROM category
                WHERE username = ?
                """, CategoryEntityRowMapper.instance, username
        );
    }

    @Override
    public void deleteCategory(CategoryEntity category) {
        jdbcTemplate.update("""
                DELETE FROM category
                WHERE id = ?
                """, category.getId());
    }

    @Override
    public List<CategoryEntity> findAll() {
        return jdbcTemplate.query("SELECT * FROM category", CategoryEntityRowMapper.instance);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("""
                SELECT * FROM category
                WHERE username = ?
                AND name = ?
                """, CategoryEntityRowMapper.instance, username, categoryName)
        );
    }
}