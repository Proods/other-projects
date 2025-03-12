package eng2.subscriptions.cli;

import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "unsubscribe-hashtag", description = "...", mixinStandardHelpOptions = true)
public class UnsubscribeHashtagCommand implements Runnable {
	
	@Inject
	SubscriptionsClient client;
	
	@Parameters(index="0")
	private Long hashtag_id;
	
	@Parameters(index="1")
	private Long user_id;

	@Override
	public void run() {
		
		HttpResponse<String> response = client.unsubscribe(hashtag_id, user_id);
		System.out.printf("Server responded with %s: %s%n", response.getStatus(), response.getBody().orElse("(no text)"));
		
	}

}
