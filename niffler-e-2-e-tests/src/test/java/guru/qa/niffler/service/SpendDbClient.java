package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(CFG.spendJdbcUrl());


  public SpendJson createSpend(SpendJson spend) {
    return jdbcTxTemplate(connection -> {
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
          }
          return SpendJson.fromEntity(
                  spendDao.create(spendEntity)
          );
        }
    );
  }

  public SpendJson findSpendByUUID(UUID id) {
    return jdbcTxTemplate(connection -> {
        return SpendJson.fromEntity(
                spendDao.findSpendById(id)
                        .orElseThrow(() -> new NoSuchElementException("Spend not found by id -> [%s]".formatted(id)))
        );
    });
  }

  public List<SpendJson> findAllByUsername(String username) {
    return jdbcTxTemplate(connection -> {
        return SpendJson.fromEntities(spendDao.findAllByUsername(username));

    });
  }

  public void deleteSpend(SpendEntity spend) {
      jdbcTxTemplate(connection -> {
          spendDao.deleteSpend(spend);
      });
  }

    public CategoryJson createCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        return jdbcTxTemplate(connection -> {
            return CategoryJson.fromEntity(
                    categoryDao.create(categoryEntity));
        });
    }

    public CategoryJson findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return jdbcTxTemplate(connection -> {
            return CategoryJson.fromEntity(
                    categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName)
                            .orElseThrow(() -> new NoSuchElementException(
                                    "Category not found by username -> [%s] and categoryName -> [%s]"
                                            .formatted(username, categoryName)))
            );
        });
    }

    public List<CategoryJson> findAllCategoryByUsername(String username) {
        return jdbcTxTemplate(connection -> {
            return CategoryJson.fromEntities(
                    categoryDao.findAllByUsername(username));
        });
    }

    public void deleteCategory(CategoryEntity spend) {
        jdbcTxTemplate(connection -> {
            categoryDao.deleteCategory(spend);
        });
    }
}
