package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement spendingTitle = $x("//h2[text()='History of Spendings']");
    private final SelenideElement menuButton = $("svg[data-testid='PersonIcon']");
    private final SelenideElement menu = $("ul[role='menu']");
    private final SelenideElement profileButton = $("a[href='/profile']");
    private final SelenideElement profile = $x("//h2[text()='Profile']");

    public ProfilePage openProfile() {
        menuButton.click();
        menu.shouldBe(visible);
        profileButton.click();
        profile.shouldBe(visible);
        return new ProfilePage();
    }

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public MainPage checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
        return new MainPage();
    }

    public MainPage checkSpendingTitleIsVisible() {
        spendingTitle.shouldBe(visible);
        return new MainPage();
    }
}
