package eng2.videos.cli.videos;

import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "remove-dislike", description = "...", mixinStandardHelpOptions = true)
public class RemoveDislikeCommand implements Runnable {
	
	@Inject
	private VideosClient client;
	
	@Parameters(index="0")
	private Long video_id;
	
	@Parameters(index="1")
	private Long user_id;

	@Override
	public void run() {
		
		HttpResponse<String> response = client.removeDislike(video_id, user_id);
		System.out.printf("Server responded with %s: %s%n", response.getStatus(), response.getBody().orElse("(no text)"));
		
	}

}
