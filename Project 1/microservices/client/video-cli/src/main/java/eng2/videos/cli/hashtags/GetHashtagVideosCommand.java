package eng2.videos.cli.hashtags;

import eng2.videos.cli.domain.Video;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "get-hashtag-videos", description = "...", mixinStandardHelpOptions = true)
public class GetHashtagVideosCommand implements Runnable{
	
	@Inject
	private HashtagsClient client;
	
	@Parameters(index="0")
	private Long id;

	@Override
	public void run() {
		
		Iterable<Video> videos = client.getHashtagVideos(id);
		
		if (videos==null) {
			System.err.println("Hashtag not found!");
			System.exit(1);
		} else {
			for (Video v: videos) {
				System.out.println(v);
			}
		}
	}

}
