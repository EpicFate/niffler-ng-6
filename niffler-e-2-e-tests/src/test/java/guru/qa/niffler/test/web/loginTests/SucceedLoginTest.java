package guru.qa.niffler.test.web.loginTests;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SucceedLoginTest {

    @Test
    public void checkSucceedRegister() {

        Selenide.open(Config.getInstance().frontUrl(), LoginPage.class)
                .login("epic", "123")
                .checkSpendingTitleIsVisible();
    }
}