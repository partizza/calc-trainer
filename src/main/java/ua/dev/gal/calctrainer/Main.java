package ua.dev.gal.calctrainer;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.jline.PromptProvider;
import ua.dev.gal.calctrainer.component.TrainCommand;
import ua.dev.gal.calctrainer.component.TrainOptionNames;


@SpringBootApplication
public class Main {

    @Autowired
    private ResourceLoader resourceLoader;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandRegistration trainCommandRegistration(TrainCommand trainCommand) {
        return CommandRegistration.builder()
                .command("train")
                .group("Calculation training")
                .description("Start training")
                .withOption()
                    .description("Sets the max count of digits in number")
                    .longNames(TrainOptionNames.NUMBER_OF_DIGITS)
                    .type(int.class)
                    .arity(1, 1)
                    .defaultValue("2")
                    .and()
                .withOption()
                    .shortNames(TrainOptionNames.ADD_OPERATION)
                    .description("Includes operation of addition to training")
                    .type(Boolean.class)
                    .and()
                .withOption()
                    .shortNames(TrainOptionNames.SUB_OPERATION)
                    .description("Includes operation of subtraction to training")
                    .type(Boolean.class)
                    .and()
                .withOption()
                    .shortNames(TrainOptionNames.MUL_OPERATION)
                    .description("Includes operation of multiplication to training")
                    .type(Boolean.class)
                    .and()
                .withTarget()
                    .consumer(trainCommand::train)
                    .and()
                .build();
    }

    @Bean
    public PromptProvider myPromptProvider() {
        return () -> new AttributedString("calc-trainer:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }

}