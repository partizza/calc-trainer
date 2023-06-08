package ua.dev.gal.calctrainer.component;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.test.ShellAssertions;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.autoconfigure.ShellTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ShellTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TrainCommandNonInteractiveTest {

    @Autowired
    ShellTestClient client;

    @Test
    void shouldStopTrain() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("train")
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText(String.format("Enter answer or '%s' to exit", TrainCommand.EXIT_KEYWORD));
        });

        session.write(session.writeSequence().text("stop").carriageReturn().build());

        await().atMost(2, TimeUnit.SECONDS)
                .untilAsserted(() -> assertTrue(session.isComplete()));
    }

    @Test
    void shouldTrainAdditionOperationWithCorrectAnswer() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("train", String.format("-%s", TrainOptionNames.ADD_OPERATION))
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText(TrainOperation.ADDITION.getSymbol());
        });

        String answer = resolveAnswer(session.screen().lines().get(0), TrainOperation.ADDITION);
        session.write(session.writeSequence().text(answer).carriageReturn().build());

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("Correct!!!");
        });
    }

    @Test
    void shouldTrainAdditionOperationWithNotCorrectAnswer() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("train", String.format("-%s", TrainOptionNames.ADD_OPERATION))
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText(TrainOperation.ADDITION.getSymbol());
        });

        String answer = resolveAnswer(session.screen().lines().get(0), TrainOperation.ADDITION);
        session.write(session.writeSequence().text(answer + "1").carriageReturn().build());

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText(String.format("Not correct!!! Correct answer: %s", answer));
        });
    }

    @Test
    void shouldTrainSubtractionOperationWithCorrectAnswer() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("train", String.format("-%s", TrainOptionNames.SUB_OPERATION))
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText(TrainOperation.SUBTRACTION.getSymbol());
        });

        String answer = resolveAnswer(session.screen().lines().get(0), TrainOperation.SUBTRACTION);
        session.write(session.writeSequence().text(answer).carriageReturn().build());

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("Correct!!!");
        });
    }

    @Test
    void shouldTrainSubtractionOperationWithNotCorrectAnswer() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("train", String.format("-%s", TrainOptionNames.SUB_OPERATION))
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText(TrainOperation.SUBTRACTION.getSymbol());
        });

        String answer = resolveAnswer(session.screen().lines().get(0), TrainOperation.SUBTRACTION);
        session.write(session.writeSequence().text(answer + "1").carriageReturn().build());

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText(String.format("Not correct!!! Correct answer: %s", answer));
        });
    }

    @Test
    void shouldTrainMultiplicationOperationWithCorrectAnswer() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("train", String.format("-%s", TrainOptionNames.MUL_OPERATION))
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText(TrainOperation.MULTIPLICATION.getSymbol());
        });

        String answer = resolveAnswer(session.screen().lines().get(0), TrainOperation.MULTIPLICATION);
        session.write(session.writeSequence().text(answer).carriageReturn().build());

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("Correct!!!");
        });
    }

    @Test
    void shouldTrainMultiplicationOperationWithNotCorrectAnswer() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("train", String.format("-%s", TrainOptionNames.MUL_OPERATION))
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText(TrainOperation.MULTIPLICATION.getSymbol());
        });

        String answer = resolveAnswer(session.screen().lines().get(0), TrainOperation.MULTIPLICATION);
        session.write(session.writeSequence().text(answer + "1").carriageReturn().build());

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText(String.format("Not correct!!! Correct answer: %s", answer));
        });
    }

    private String resolveAnswer(String expression, TrainOperation operation) {
        long a = Long.parseLong(expression.substring(0, expression.indexOf(operation.getSymbol())).trim());
        long b = Long.parseLong(expression.substring(expression.indexOf(operation.getSymbol()) + 1, expression.indexOf("=")).trim());

        return String.valueOf(operation.apply(a, b));
    }
}