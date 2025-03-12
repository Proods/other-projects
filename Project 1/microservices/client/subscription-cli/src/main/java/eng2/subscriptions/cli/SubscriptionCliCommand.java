package eng2.subscriptions.cli;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "subscription-cli", description = "...",
		subcommands = {SubscribeHashtagCommand.class, UnsubscribeHashtagCommand.class, GetRecommendationsCommand.class},
        mixinStandardHelpOptions = true)
public class SubscriptionCliCommand implements Runnable {

    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(SubscriptionCliCommand.class, args);
    }

    public void run() {
        // business logic here
        if (verbose) {
            System.out.println("Hi!");
        }
    }
}
