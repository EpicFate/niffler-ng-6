package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.common.CategoryButtons;
import guru.qa.niffler.common.ModalButtons;
import org.openqa.selenium.By;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Calendar;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.common.CategoryButtons.ARCHIVE_CATEGORY;
import static guru.qa.niffler.common.CategoryButtons.UNARCHIVE_CATEGORY;
import static guru.qa.niffler.common.ModalButtons.ARCHIVE;
import static guru.qa.niffler.common.ModalButtons.UNARCHIVE;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {

    public static String url = Config.getInstance().frontUrl() + "profile";

    private final SelenideElement newSpendingButton = $("a[href='/spending']");
    private final SelenideElement uploadImageInput = $("input#image__input");
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement saveChangesButton = $("button[type='submit']");
    private final SelenideElement archiveCheckbox = $x(
            "//*[text()='Show archived']//preceding::input[@type='checkbox']");
    private final SelenideElement addNewCategoryInput = $("#category");
    private final ElementsCollection categories = $$x(
            "//*[@aria-label]//ancestor::div[contains(@class, 'MuiGrid-root MuiGrid-item')]");
    private final SelenideElement dialogModal = $("[role=dialog]");
    private final SelenideElement alertModal = $("[role='alert']");
    private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");
    private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");

    private final Calendar calendar = new Calendar($(".ProfileCalendar"));

    private By categoryButton(CategoryButtons button) {
        return By.cssSelector("button[aria-label='%s']".formatted(button.getAriaLabel()));
    }

    private By modalButton(ModalButtons button) {
        return By.xpath(".//button[text()='%s']".formatted(button.getButtonName()));
    }

//    private SelenideElement categoryByName(String categoryName) {
//        return $x("//*[text()='%s']//ancestor::div[contains(@class, 'MuiGrid-root MuiGrid-item')]"
//                .formatted(categoryName));
//    }

    @Nonnull
    public ProfilePage activateShowArchivedCheckbox() {
        if(!archiveCheckbox.isSelected())
            archiveCheckbox.click();
        return new ProfilePage();
    }

    @Nonnull
    public ProfilePage deactivateShowArchivedCheckbox() {
        if(archiveCheckbox.isSelected())
            archiveCheckbox.click();
        return new ProfilePage();
    }

    @Nonnull
    public ProfilePage archiveCategoryAndAcceptModal(String categoryName) {
        clickCategoryButtonAndAcceptModal(categoryName, ARCHIVE_CATEGORY, ARCHIVE);
        return new ProfilePage();
    }

    @Nonnull
    public ProfilePage unarchivedCategoryAndAcceptModal(String categoryName) {
        clickCategoryButtonAndAcceptModal(categoryName, UNARCHIVE_CATEGORY, UNARCHIVE);
        return new ProfilePage();
    }

    @Nonnull
    public ProfilePage clickCategoryButtonAndAcceptModal(String categoryName, CategoryButtons categoryButton, ModalButtons modalButton) {
        SelenideElement category = categories.find(exactText(categoryName));
        category.$(categoryButton(categoryButton))
                .click();
        dialogModal.shouldBe(visible)
                .$(modalButton(modalButton))
                .click();
        dialogModal.shouldNotBe(visible);
        return new ProfilePage();
    }

    @Nonnull
    public ProfilePage checkAlertModal(String expectedMassage) {
        alertModal
                .shouldBe(visible)
                .shouldHave(exactText(expectedMassage));
        return new ProfilePage();
    }

    @Nonnull
    public ProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }

    @Nonnull
    public ProfilePage checkArchivedCategoryExists(String category) {
        activateShowArchivedCheckbox();
        bubblesArchived.find(text(category)).shouldBe(visible);
        return this;
    }

    @Nonnull
    public ProfilePage checkCategoryExists(String category) {
        bubbles.find(text(category)).shouldBe(visible);
        return this;
    }


}
