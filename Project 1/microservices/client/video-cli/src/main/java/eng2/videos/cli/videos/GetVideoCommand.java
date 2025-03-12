package eng2.videos.cli.videos;

import eng2.videos.cli.domain.Video;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "get-video", description = "...", mixinStandardHelpOptions = true)
public class GetVideoCommand implements Runnable{
	
	@Inject
	private VideosClient client;
	
	@Parameters(index="0")
	private Long id;

	@Override
	public void run() {
		
		Video v = client.getVideo(id);
		if (v==null) {
			System.out.println("Video not found!");
		} else {
			System.out.println(v);
		}
		
	}

}
