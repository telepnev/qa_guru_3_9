import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class StudentRegistrationFormTest {

    Faker faker = new Faker();
    FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());

    String firstName = faker.name().firstName();
    String lastName = faker.name().lastName();
    String userEmail = fakeValuesService.bothify("????##@mail.ru");
    String userMobile = fakeValuesService.regexify("[1-9]{10}");
    String currentAddress = faker.address().fullAddress();
    String picture = "test.jpg";
    String state = "Haryana";
    String city = "Karnal";

    @BeforeAll
    public static void setUp() {
        Configuration.startMaximized = true;
    }

    @Test
    @Owner("Telepnev")
    @DisplayName("Successful fill registration form")
     void successfulFillFormTest() {

        open("https://demoqa.com/automation-practice-form");
        $("h5").shouldHave(text("Student Registration Form"));
        step("Fill in the Name field", () ->{$("#firstName").val(firstName).pressTab();
            $("#lastName").val(lastName).pressTab();});
        step("Fill in the Name field", () -> $("#userEmail").val(userEmail));
        step("Choose a Gender 'Male'", () ->{});
        $(byText("Female")).click();
        step("Fill in the Mobile field", () -> $("#userNumber").val(userMobile));
        step("Select date Of Birth 'March 1983 31'", () ->{$("#dateOfBirthInput").click();
            $(".react-datepicker__month-select").selectOption("March");
            $(".react-datepicker__year-select").selectOption("1983");
            $(byText("31")).click();});
        step("Fill in the Subjects", () -> $("#subjectsInput").val("Computer Science").pressEnter());
        step("Choose a Hobbies 'Music'", () -> $(byText("Music")).click());
        step("Upload the Picture", () -> $("#uploadPicture").uploadFile(new File("src/test/resources/" + picture)));
        step("Fill in the 'Current Address' field", () -> $("#currentAddress").val(currentAddress)).click();
        step("Select State and City", () ->{$(byText("Select State")).scrollTo();
            $(byText("Select State")).click();
            $("#stateCity-wrapper").$(byText(state)).click();
            $(byText("Select City")).click();
            $("#stateCity-wrapper").$(byText(city)).click();});
        step("Click to Submit", () ->{$(byText("Submit")).scrollTo();
            $(byText("Submit")).click();});
        step("Close submitting form", () ->{$(".modal-header").shouldHave(text("Thanks for submitting the form"));
            $("#closeLargeModal").click();});
     }
}
