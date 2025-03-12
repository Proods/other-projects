package eng2.videos.cli.videos;

import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "add-view", description = "...", mixinStandardHelpOptions = true)
public class AddViewCommand implements Runnable {
	
	@Inject
	private VideosClient client;
	
	@Parameters(index="0")
	private Long video_id;
	
	@Parameters(index="1")
	private Long user_id;

	@Override
	public void run() {
		
		HttpResponse<String> response = client.addView(video_id, user_id);
		System.out.printf("Server responded with %s: %s%n", response.getStatus(), response.getBody().orElse("(no text)"));
		
	}

}
