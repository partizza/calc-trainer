package ua.dev.gal.calctrainer.component;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.shell.command.CommandContext;
import org.springframework.shell.component.StringInput;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import ua.dev.gal.calctrainer.component.renderer.TrainInputRenderer;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@ShellComponent
@Scope
@Slf4j
public class TrainCommand extends AbstractShellComponent {

    public static final String EXIT_KEYWORD = "stop";

    public void train(@NonNull CommandContext commandContext) {
        LOGGER.info("About to start training");
        long upperBound = getUpperBound(commandContext);
        List<TrainOperation> operations = getOperations(commandContext);

        run(upperBound, operations);
    }

    private long getUpperBound(@NonNull CommandContext commandContext) {
        int digits = commandContext.getOptionValue(TrainOptionNames.NUMBER_OF_DIGITS);
        return (long) Math.pow(10, digits);
    }

    private List<TrainOperation> getOperations(@NonNull CommandContext commandContext) {
        List<TrainOperation> operations = new ArrayList<>();
        Optional.ofNullable(commandContext.<Boolean>getOptionValue(Character.toString(TrainOptionNames.ADD_OPERATION)))
                .filter(Boolean::booleanValue)
                .ifPresent(it -> operations.add(TrainOperation.ADDITION));

        Optional.ofNullable(commandContext.<Boolean>getOptionValue(Character.toString(TrainOptionNames.SUB_OPERATION)))
                .filter(Boolean::booleanValue)
                .ifPresent(it -> operations.add(TrainOperation.SUBTRACTION));

        Optional.ofNullable(commandContext.<Boolean>getOptionValue(Character.toString(TrainOptionNames.MUL_OPERATION)))
                .filter(Boolean::booleanValue)
                .ifPresent(it -> operations.add(TrainOperation.MULTIPLICATION));

        if (operations.isEmpty()) {
            LOGGER.debug("No provided operations, defaults will be used");
            operations.addAll(List.of(TrainOperation.ADDITION, TrainOperation.SUBTRACTION, TrainOperation.MULTIPLICATION));
        }

        return operations;
    }

    private void run(long upperBound, @NonNull List<TrainOperation> operations) {
        Random random = new Random();
        while (true) {
            long a = random.nextLong(1, upperBound);
            long b = random.nextLong(1, upperBound);
            TrainOperation operation = operations.get(random.nextInt(0, operations.size()));

            StringInput component = new StringInput(getTerminal(), String.format("%s %s %s = ", a, operation.getSymbol(), b), null);
            component.setRenderer(new TrainInputRenderer(EXIT_KEYWORD));
            StringInput.StringInputContext inputContext = component.run(StringInput.StringInputContext.empty());

            if (EXIT_KEYWORD.equalsIgnoreCase(inputContext.getResultValue())) {
                break;
            }

            checkAndPrintAnswer(operation.apply(a, b), inputContext.getResultValue());
        }
    }

    private void checkAndPrintAnswer(long expected, String receivedValue) {
        LOGGER.debug("About to compare expected '{}' with input '{}'", expected, receivedValue);
        String response = tryParseLong(receivedValue)
                .filter(it -> it == expected)
                .map(it -> "Correct!!!")
                .orElse(String.format("Not correct!!! Correct answer: %s", expected));

        PrintWriter writer = getTerminal().writer();
        writer.println(response);
        writer.flush();
    }

    private Optional<Long> tryParseLong(String str) {
        try {

            return Optional.of(Long.valueOf(str));

        } catch (NumberFormatException e) {
            LOGGER.warn("Can't parse long value from string: " + str, e);
            return Optional.empty();
        }
    }

}
