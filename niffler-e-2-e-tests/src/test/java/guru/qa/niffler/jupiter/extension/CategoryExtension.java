package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private static final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                    CategoryJson created = spendApiClient.addCategory(jsonForCreate(anno));
                    if (anno.archived()) {
                        created = spendApiClient.updateCategory(jsonForUpdate(created));
                    }
                    context.getStore(NAMESPACE)
                            .put(context.getUniqueId(), created);
                });
    }
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson categoryJson = context.getStore(NAMESPACE)
                .get(context.getUniqueId(), CategoryJson.class);
        if (!categoryJson.archived()) {
            new SpendApiClient().updateCategory(
                    new CategoryJson(
                            categoryJson.id(),
                            categoryJson.name(),
                            categoryJson.username(),
                            true
                    )
            );
        }
    }

    private CategoryJson jsonForCreate(Category anno) {
        return new CategoryJson(
                null,
                new Faker().funnyName().name(),
                anno.username(),
                false
        );
    }

    private CategoryJson jsonForUpdate(CategoryJson created) {
        return new CategoryJson(
                created.id(),
                created.name(),
                created.username(),
                true
        );
    }
}
