package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;

import static guru.qa.niffler.data.Databases.transaction;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class SpendDbClient {

  private static final Config CFG = Config.getInstance();

  public SpendJson createSpend(SpendJson spend) {
    return transaction(connection -> {
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                .create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
          }
          return SpendJson.fromEntity(
              new SpendDaoJdbc(connection).create(spendEntity)
          );
        }, CFG.spendJdbcUrl()
    );
  }

  public SpendJson findSpendByUUID(UUID id) {
    return transaction(connection -> {
        return SpendJson.fromEntity(
                new SpendDaoJdbc(connection).findSpendById(id)
                        .orElseThrow(() -> new NoSuchElementException("Spend not found by id -> [%s]".formatted(id)))
        );
    }, CFG.spendJdbcUrl());
  }

  public List<SpendJson> findAllByUsername(String username) {
    return transaction(connection -> {
        return SpendJson.fromEntities(new SpendDaoJdbc(connection)
                .findAllByUsername(username));

    }, CFG.spendJdbcUrl());
  }

  public void deleteSpend(SpendEntity spend) {
      transaction(connection -> {
          new SpendDaoJdbc(connection).deleteSpend(spend);
      }, CFG.spendJdbcUrl());
  }

    public CategoryJson createCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        return transaction(connection -> {
            return CategoryJson.fromEntity(
                    new CategoryDaoJdbc(connection).create(categoryEntity));
        }, CFG.spendJdbcUrl());
    }

    public CategoryJson findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(connection -> {
            return CategoryJson.fromEntity(
                    new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, categoryName)
                            .orElseThrow(() -> new NoSuchElementException(
                                    "Category not found by username -> [%s] and categoryName -> [%s]"
                                            .formatted(username, categoryName)))
            );
        }, CFG.spendJdbcUrl());
    }

    public List<CategoryJson> findAllCategoryByUsername(String username) {
        return transaction(connection -> {
            return CategoryJson.fromEntities(
                    new CategoryDaoJdbc(connection).findAllByUsername(username));
        }, CFG.spendJdbcUrl());
    }

    public void deleteCategory(CategoryEntity spend) {
        transaction(connection -> {
            new CategoryDaoJdbc(connection).deleteCategory(spend);
        }, CFG.spendJdbcUrl());
    }
}
