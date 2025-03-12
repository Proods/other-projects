package eng2.videos.cli.users;

import eng2.videos.cli.domain.Video;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "get-liked-videos", description = "...", mixinStandardHelpOptions = true)
public class GetLikedVideosCommand implements Runnable {
	
	@Inject
	private UsersClient client;
	
	@Parameters(index="0")
	private Long id;

	@Override
	public void run() {
		
		Iterable<Video> videos = client.getLikedVideos(id);
		
		if (videos!=null) {
			for (Video v: videos) {
				System.out.println(v);
			}
		}
		
	}
	
}
