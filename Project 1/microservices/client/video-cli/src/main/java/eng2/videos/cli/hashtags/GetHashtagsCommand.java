package eng2.videos.cli.hashtags;

import eng2.videos.cli.domain.Hashtag;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;


@Command(name = "get-hashtags", description = "...", mixinStandardHelpOptions = true)
public class GetHashtagsCommand implements Runnable{
	
	@Inject
	private HashtagsClient client;

	@Override
	public void run() {
		
		for (Hashtag h: client.list()) {
			System.out.println(h);
		}
		
	}

}
