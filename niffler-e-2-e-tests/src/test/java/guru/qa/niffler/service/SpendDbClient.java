package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();
    private final SpendRepositoryHibernate spendHibernate = new SpendRepositoryHibernate();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(CFG.spendJdbcUrl());



    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        return jdbcTxTemplate.execute(() -> {
            return spendHibernate.findByUsernameAndSpendDescription(username, description);
        });
    }

    public Optional<CategoryEntity> findCategoryByUserNameAndSpendName(String username, String spendName) {
        return jdbcTxTemplate.execute(() -> {
            return spendHibernate.findCategoryByUserNameAndSpendName(username, spendName);
        });
    }


  public SpendJson createSpend(SpendJson spend) {
    return jdbcTxTemplate.execute(() -> {
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
    return jdbcTxTemplate.execute(() ->
            SpendJson.fromEntity(
                    spendDao.findSpendById(id)
                            .orElseThrow(() -> new NoSuchElementException("Spend not found by id -> [%s]"
                                    .formatted(id)))
    ));
  }

  public List<SpendJson> findAllByUsername(String username) {
    return jdbcTxTemplate.execute(() ->
            SpendJson.fromEntities(spendDao.findAllByUsername(username)));
  }

  public void deleteSpend(SpendEntity spend) {
      jdbcTxTemplate.execute(() -> {
          spendDao.deleteSpend(spend);
          return null;
      });
  }

    public CategoryJson createCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        return jdbcTxTemplate.execute(() ->
                CategoryJson.fromEntity(categoryDao.create(categoryEntity)));
    }

    public CategoryJson findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return jdbcTxTemplate.execute(() ->
                CategoryJson.fromEntity(
                        categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName)
                                .orElseThrow(() -> new NoSuchElementException(
                                        "Category not found by username -> [%s] and categoryName -> [%s]"
                                                .formatted(username, categoryName)))
        ));
    }

    public List<CategoryJson> findAllCategoryByUsername(String username) {
        return jdbcTxTemplate.execute(() ->
                CategoryJson.fromEntities(categoryDao.findAllByUsername(username)));
    }

    public void deleteCategory(CategoryEntity spend) {
        jdbcTxTemplate.execute(() -> {
            categoryDao.deleteCategory(spend);
            return null;
        });
    }
}
