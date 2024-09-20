package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileTests {

    @User(
            username = "epic",
            categories = @Category(archived = true)
    )
    @Test
    public void unarchivedCategory(CategoryJson category) {
        String resultAlertMassagePattern = "Category %s is unarchived";

        Selenide.open(Config.getInstance().frontUrl(), LoginPage.class)
                .login("epic", "123")
                .openProfile()
                .activateShowArchivedCheckbox()
                .unarchivedCategoryAndAcceptModal(category.name())
                .checkAlertModal(resultAlertMassagePattern.formatted(category.name()))

        ;
    }



    @User(
            username = "epic",
            categories = @Category(archived = false)
    )
    @Test
    public void archiveCategory(CategoryJson category) {
        String resultAlertMassagePattern = "Category %s is archived";

        Selenide.open(Config.getInstance().frontUrl(), LoginPage.class)
                .login("epic", "123")
                .openProfile()
                .activateShowArchivedCheckbox()
                .archiveCategoryAndAcceptModal(category.name())
                .checkAlertModal(resultAlertMassagePattern.formatted(category.name()))

        ;
    }
}
