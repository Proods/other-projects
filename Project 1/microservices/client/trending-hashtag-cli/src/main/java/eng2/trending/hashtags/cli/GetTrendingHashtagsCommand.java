package eng2.trending.hashtags.cli;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;


@Command(name = "get-trending-hashtags", description = "...", mixinStandardHelpOptions = true)
public class GetTrendingHashtagsCommand implements Runnable {
	
	@Inject
	HashtagsClient client;

	@Override
	public void run() {
		
		for (Hashtag hashtag : client.getTrendingHashtags()) {
			System.out.println(hashtag);
		}
		
	}

}
