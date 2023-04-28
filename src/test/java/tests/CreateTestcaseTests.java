package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import models.CreateTestCaseBody;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class CreateTestcaseTests {

    static String login = "allure8",
            password = "allure8",
            projectId = "2220";

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = "https://allure.autotests.cloud";
        Configuration.holdBrowserOpen = true;

        RestAssured.baseURI = "https://allure.autotests.cloud";
    }

    @Test
    void createWitUIOnlyTest() {
        Faker faker = new Faker();
        String testCaseName = faker.name().fullName();

        step("Authorize", () -> {
            open("/");
            $(byName("username")).setValue(login);
            $(byName("password")).setValue(password);
            $("button[type='submit']").click();
        });
        step("Go to project", () -> {
            open("/project/2220/test-cases");
        });

        step("Create testcase", () -> {
            $("[data-testid=input__create_test_case]").setValue(testCaseName)
                    .pressEnter();
        });

        step("Verify testcase name", () -> {
            $(".LoadableTree__view").shouldHave(text(testCaseName));
        });
    }

    @Test
    void createWitApiOnlyTest() {
        Faker faker = new Faker();
        String testCaseName = faker.name().fullName();

//        step("Authorize", () -> {
//            open("/");
//            $(byName("username")).setValue(login);
//            $(byName("password")).setValue(password);
//            $("button[type='submit']").click();
//        });
//        step("Go to project", () -> {
//            open("/project/2220/test-cases");
//        });

        step("Create testcase", () -> {
            CreateTestCaseBody testCaseBody = new CreateTestCaseBody();
            testCaseBody.setName(testCaseName);

            given()
                    .log().all()
                    .header("X-XSRF-TOKEN", "9b5cd8b4-8b79-4f0b-9444-2360d68184e4")
                    .cookies("XSRF-TOKEN", "9b5cd8b4-8b79-4f0b-9444-2360d68184e4",
                            "ALLURE_TESTOPS_SESSION", "20af13f6-96b3-4cd3-8d35-b5cfe01c442c")
                    .contentType("application/json;charset=UTF-8")
                    .body(testCaseBody)
                    .queryParam("projectId", projectId)
                    .when()
                    .post("/api/rs/testcasetree/leaf")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(200)
                    .body("statusName", is("Draft"))
                    .body("name", is(testCaseName));
        /*

  -H 'Cookie: XSRF-TOKEN=9b5cd8b4-8b79-4f0b-9444-2360d68184e4; ALLURE_TESTOPS_SESSION=20af13f6-96b3-4cd3-8d35-b5cfe01c442c' \
  -H 'Origin: https://allure.autotests.cloud' \
            -H 'Sec-Fetch-Dest: empty' \
            -H 'Sec-Fetch-Mode: cors' \
            -H 'Sec-Fetch-Site: same-origin' \
            -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36' \
            -H 'X-XSRF-TOKEN: 9b5cd8b4-8b79-4f0b-9444-2360d68184e4' \
            -H 'sec-ch-ua: "Chromium";v="112", "Google Chrome";v="112", "Not:A-Brand";v="99"' \
            -H 'sec-ch-ua-mobile: ?0' \
            -H 'sec-ch-ua-platform: "macOS"' \
            --data-raw '{"name":"test test 3"}' \
            --compressed
         */






//            $("[data-testid=input__create_test_case]").setValue(testCaseName)
//                    .pressEnter();
        });

        step("Verify testcase name", () -> {
//            $(".LoadableTree__view").shouldHave(text(testCaseName));
        });
    }
}
