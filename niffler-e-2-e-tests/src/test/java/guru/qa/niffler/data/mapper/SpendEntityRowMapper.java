package guru.qa.niffler.data.mapper;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

    public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();

    private SpendEntityRowMapper() {
    }

    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpendEntity spend = new SpendEntity();
        spend.setId(rs.getObject("id", UUID.class));
        spend.setUsername(rs.getString("username"));
        spend.setCurrency(CurrencyValues.findByName(rs.getString("currency")));
        spend.setSpendDate(rs.getDate("spend_date"));
        spend.setAmount(rs.getDouble("amount"));
        spend.setDescription(rs.getString("description"));
        UUID categoryId = rs.getObject("category_id", UUID.class);
        CategoryEntity category = new CategoryDaoSpringJdbc(Databases.dataSource(Config.getInstance().spendJdbcUrl()))
                .findCategoryById(categoryId)
                .orElseThrow(() -> new SQLException("Category not found by id -> [%s]".formatted(categoryId)));
        spend.setCategory(category);
        return spend;
    }
}
