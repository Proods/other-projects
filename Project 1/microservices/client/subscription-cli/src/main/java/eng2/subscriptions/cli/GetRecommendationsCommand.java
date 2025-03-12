package eng2.subscriptions.cli;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-recommendations", description = "...", mixinStandardHelpOptions = true)
public class GetRecommendationsCommand implements Runnable {
	
	@Inject
	SubscriptionsClient client;
	
	@Parameters(index="0")
	private Long hashtag_id;
	
	@Parameters(index="1")
	private Long user_id;

	@Override
	public void run() {
		
		Iterable<Video> recommendations = client.recommend(hashtag_id, user_id);
		
		if (recommendations!=null) {
			for (Video recommendation : recommendations) {
				System.out.println(recommendation);
			}
		}
		
	}

}
