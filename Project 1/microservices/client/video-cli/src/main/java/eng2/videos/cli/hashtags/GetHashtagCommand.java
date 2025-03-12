package eng2.videos.cli.hashtags;

import eng2.videos.cli.domain.Hashtag;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "get-hashtag", description = "...", mixinStandardHelpOptions = true)
public class GetHashtagCommand implements Runnable{
	
	@Inject
	private HashtagsClient client;
	
	@Parameters(index="0")
	private Long id;

	@Override
	public void run() {
		
		Hashtag h = client.getHashtag(id);
		if (h==null) {
			System.out.println("Hashtag not found!");
		} else {
			System.out.println(h);
		}
		
	}

}
