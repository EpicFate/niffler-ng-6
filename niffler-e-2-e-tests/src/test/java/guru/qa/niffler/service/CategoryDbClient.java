package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import java.util.List;
import java.util.NoSuchElementException;

public class CategoryDbClient {

    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public CategoryJson createSpend(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        return CategoryJson.fromEntity(
                categoryDao.create(categoryEntity)
        );
    }

    public CategoryJson findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return CategoryJson.fromEntity(
                categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName)
                        .orElseThrow(() -> new NoSuchElementException(
                                "Category not found by username -> [%s] and categoryName -> [%s]"
                                        .formatted(username, categoryName)))
        );
    }

    public List<CategoryJson> findAllByUsername(String username) {
        return CategoryJson.fromEntities(
                categoryDao.findAllByUsername(username)
        );
    }

    public void deleteCategory(CategoryEntity spend) {
        categoryDao.deleteCategory(spend);
    }
}