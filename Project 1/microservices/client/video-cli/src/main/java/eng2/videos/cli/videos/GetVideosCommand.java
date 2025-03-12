package eng2.videos.cli.videos;

import eng2.videos.cli.domain.Video;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;


@Command(name = "get-videos", description = "...", mixinStandardHelpOptions = true)
public class GetVideosCommand implements Runnable {
	
	@Inject
	VideosClient client;

	@Override
	public void run() {
		
		for (Video v: client.list()) {
			System.out.println(v);
		}
		
	}

}
