package tests;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.Locale;

import static com.codeborne.selenide.Browsers.FIREFOX;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.logevents.SelenideLogger.addListener;
import static helpers.AttachmentsHelper.*;
import static io.qameta.allure.Allure.step;

public class StudentRegistrationFormTest {
    @BeforeAll
    static void setUp() {
        addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
        Configuration.startMaximized = true;
//        Configuration.browser = FIREFOX;
        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud:4444/wd/hub/";

    }
    @AfterEach
    @Step("Attachments")
    public void afterEach(){
        attachScreenshot("Last screenshot");
        attachPageSource();
        attachAsText("Browser console logs", getConsoleLogs());
        attachVideo();
        closeWebDriver();
    }

    Faker faker = new Faker();
    FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());

    @Test
    @Owner("Telepnev")
    @DisplayName("Successful fill registration form")
    void successfulFillFormTest() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String userEmail = fakeValuesService.bothify("????##@mail.ru");
        String userMobile = fakeValuesService.regexify("[1-9]{10}");
        String gender = "Female";
        String dayOfBirth = "31";
        String monthOfBirth = "March";
        String yearOfBirth = "1983";

        String currentAddress = faker.address().fullAddress();
        String picture = "test.jpg";
        String state = "Haryana";
        String city = "Karnal";
        open("https://demoqa.com/automation-practice-form");
        $("h5").shouldHave(text("Student Registration Form"));
        step("Fill in the Name field", () -> {
            $("#firstName").val(firstName).pressTab();
            $("#lastName").val(lastName).pressTab();
        });
        step("Fill in the Name field", () -> $("#userEmail").val(userEmail));
        step("Choose a Gender 'Male'", () -> {
        });
        $(byText("Female")).click();
        step("Fill in the Mobile field", () -> $("#userNumber").val(userMobile));
        step("Select date Of Birth 'March 1983 31'", () -> {
            $("#dateOfBirthInput").click();
            $(".react-datepicker__month-select").selectOption(monthOfBirth);
            $(".react-datepicker__year-select").selectOption(yearOfBirth);
            $(byText(dayOfBirth)).click();
        });
        step("Fill in the Subjects", () -> $("#subjectsInput").val("Computer Science").pressEnter());
        step("Choose a Hobbies 'Music'", () -> $(byText("Music")).click());
        step("Upload the Picture", () -> $("#uploadPicture").uploadFile(new File("src/test/resources/" + picture)));
        step("Fill in the 'Current Address' field", () -> $("#currentAddress").val(currentAddress)).click();
        step("Select State and City", () -> {
            $(byText("Select State")).scrollTo();
            $(byText("Select State")).click();
            $("#stateCity-wrapper").$(byText(state)).click();
            $(byText("Select City")).click();
            $("#stateCity-wrapper").$(byText(city)).click();
        });
        step("Click to Submit", () -> {
            $(byText("Submit")).scrollTo();
            $(byText("Submit")).click();
        });

        step("Verify successful form submit", () -> {
            $(".modal-header").shouldHave(text("Thanks for submitting the form"));
            $x("//td[text()='Student Name']").parent().shouldHave(text(firstName + " " + lastName));
            $x("//td[text()='Student Email']").parent().shouldHave(text(userEmail));
            $x("//td[text()='Gender']").parent().shouldHave(text(gender));
            $x("//td[text()='Mobile']").parent().shouldHave(text(userMobile));
            $x("//td[text()='Date of Birth']").parent().shouldHave(text(dayOfBirth + " " + monthOfBirth + "," + yearOfBirth));
            $x("//td[text()='Picture']").parent().shouldHave(text(picture));
            $x("//td[text()='Address']").parent().shouldHave(text(currentAddress));
            $x("//td[text()='State and City']").parent().shouldHave(text(state + " " + city));
        });

        step("Close submitting form", () -> $("#closeLargeModal").click());

    }

    @Test
    @Owner("Telepnev")
    @DisplayName("unSuccessful fill registration form with short Phone number")
    void unSuccessfulFillFormWithShortNumberTest() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String userEmail = fakeValuesService.bothify("????##@mail.ru");
        String userMobile = fakeValuesService.regexify("[1-9]{9}");
        String gender = "Female";
        String dayOfBirth = "31";
        String monthOfBirth = "March";
        String yearOfBirth = "1983";

        String currentAddress = faker.address().fullAddress();
        String picture = "test.jpg";
        String state = "Haryana";
        String city = "Karnal";

        open("https://demoqa.com/automation-practice-form");
        $("h5").shouldHave(text("Student Registration Form"));
        step("Fill in the Name field", () -> {
            $("#firstName").val(firstName).pressTab();
            $("#lastName").val(lastName).pressTab();
        });
        step("Fill in the Name field", () -> $("#userEmail").val(userEmail));
        step("Choose a Gender 'Male'", () -> {
        });
        $(byText("Female")).click();
        step("Fill in the Mobile field", () -> $("#userNumber").val(userMobile));
        step("Select date Of Birth 'March 1983 31'", () -> {
            $("#dateOfBirthInput").click();
            $(".react-datepicker__month-select").selectOption(monthOfBirth);
            $(".react-datepicker__year-select").selectOption(yearOfBirth);
            $(byText(dayOfBirth)).click();
        });
        step("Fill in the Subjects", () -> $("#subjectsInput").val("Computer Science").pressEnter());
        step("Choose a Hobbies 'Music'", () -> $(byText("Music")).click());
        step("Upload the Picture", () -> $("#uploadPicture").uploadFile(new File("src/test/resources/" + picture)));
        step("Fill in the 'Current Address' field", () -> $("#currentAddress").val(currentAddress)).click();
        step("Select State and City", () -> {
            $(byText("Select State")).scrollTo();
            $(byText("Select State")).click();
            $("#stateCity-wrapper").$(byText(state)).click();
            $(byText("Select City")).click();
            $("#stateCity-wrapper").$(byText(city)).click();
        });
        step("Click to Submit", () -> {
            $(byText("Submit")).scrollTo();
            $(byText("Submit")).click();
        });

        step("Verify successful form submit", () -> {
            $(".modal-header").shouldHave(text("Thanks for submitting the form"));
            $x("//td[text()='Student Name']").parent().shouldHave(text(firstName + " " + lastName));
            $x("//td[text()='Student Email']").parent().shouldHave(text(userEmail));
            $x("//td[text()='Gender']").parent().shouldHave(text(gender));
            $x("//td[text()='Mobile']").parent().shouldHave(text(userMobile));
            $x("//td[text()='Date of Birth']").parent().shouldHave(text(dayOfBirth + " " + monthOfBirth + "," + yearOfBirth));
            $x("//td[text()='Picture']").parent().shouldHave(text(picture));
            $x("//td[text()='Address']").parent().shouldHave(text(currentAddress));
            $x("//td[text()='State and City']").parent().shouldHave(text(state + " " + city));
        });

        step("Close submitting form", () -> $("#closeLargeModal").click());

    }
}
