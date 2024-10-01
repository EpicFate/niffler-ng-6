package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class SpendDaoJdbc implements SpendDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public SpendEntity create(SpendEntity spend) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
        "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
            "VALUES ( ?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, spend.getUsername());
      ps.setDate(2, spend.getSpendDate());
      ps.setString(3, spend.getCurrency().name());
      ps.setDouble(4, spend.getAmount());
      ps.setString(5, spend.getDescription());
      ps.setObject(6, spend.getCategory().getId());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      spend.setId(generatedKey);
      return spend;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<SpendEntity> findSpendById(UUID id) {
      try (PreparedStatement ps = connection.prepareStatement("""
              SELECT * FROM spend
              WHERE id = ?
              """)) {
        ps.setObject(1, id);
        ps.execute();

        try (ResultSet rs = ps.getResultSet()) {
          if (rs.next()) {
            return Optional.of(fillSpend(rs));
          } else {
            return Optional.empty();
          }
        }
      } catch (SQLException e) {
          throw new RuntimeException(e);
    }
  }

  @Override
  public List<SpendEntity> findAllByUsername(String username) {
      try (PreparedStatement ps = connection.prepareStatement("""
              SELECT * FROM spend
              WHERE username = ?
              """)) {
        ps.setObject(1, username);
        ps.execute();

        try (ResultSet rs = ps.getResultSet()) {
          ArrayList<SpendEntity> list = new ArrayList<>();
          if (rs.next()) {
            while (rs.next()) {
              list.add(fillSpend(rs));
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
  public void deleteSpend(SpendEntity spend) {
      try (PreparedStatement ps = connection.prepareStatement("""
              DELETE FROM spend
              WHERE id = ?
              """)) {
        ps.setObject(1, spend.getId());
        ps.execute();
      } catch (SQLException e) {
          throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteByCategoryId(UUID id) {
      try (PreparedStatement ps = connection.prepareStatement(
              "DELETE FROM spend WHERE category_id = ?"
      )) {
        ps.setObject(1, id);
        ps.execute();
      } catch (SQLException e) {
          throw new RuntimeException(e);
    }
  }

  @Override
  public List<SpendEntity> findAll() {
    try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM spend")) {
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        ArrayList<SpendEntity> list = new ArrayList<>();
        if (rs.next()) {
          while (rs.next()) {
            list.add(fillSpend(rs));
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


  private SpendEntity fillSpend(ResultSet rs) throws SQLException {
    SpendEntity spend = new SpendEntity();
    spend.setId(rs.getObject("id", UUID.class));
    spend.setUsername(rs.getString("username"));
    spend.setCurrency(CurrencyValues.findByName(rs.getString("currency")));
    spend.setSpendDate(rs.getDate("spend_date"));
    spend.setAmount(rs.getDouble("amount"));
    spend.setDescription(rs.getString("description"));
    UUID categoryId = rs.getObject("category_id", UUID.class);
    CategoryEntity category = new CategoryDaoJdbc(connection).findCategoryById(categoryId)
            .orElseThrow(() -> new SQLException("Category not found by id -> [%s]".formatted(categoryId)));
    spend.setCategory(category);
    return spend;
  }
}
