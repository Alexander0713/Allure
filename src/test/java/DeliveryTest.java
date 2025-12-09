import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.byText;
import static org.openqa.selenium.Keys.BACK_SPACE;


public class DeliveryTest {
    private DataGenerator.UserInfo user;

    @BeforeEach
    void setUp() {

        open("http://localhost:9999");
        user = DataGenerator.Registration.generateUser("ru");
    }

    @Test
    void shouldTestDeliveryCard() {
        // 1. Первая запись
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick().press(BACK_SPACE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(4));
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=success-notification]")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Успешно!"))
                .shouldHave(text("Встреча успешно запланирована"));

        // 2. Изменяем дату
        $("[data-test-id=date] input").doubleClick().press(BACK_SPACE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(7));
        $(byText("Запланировать")).click();

        $("[data-test-id=replan-notification]")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Необходимо подтверждение"))
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));

        $("[data-test-id=replan-notification] .button").click();

        $("[data-test-id=success-notification]")
                .shouldBe(visible, Duration.ofSeconds(15));
    }
}