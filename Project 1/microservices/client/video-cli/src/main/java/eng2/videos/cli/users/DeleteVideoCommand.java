package eng2.videos.cli.users;

import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "delete-video", description = "...", mixinStandardHelpOptions = true)
public class DeleteVideoCommand implements Runnable{
	
	@Inject
	private UsersClient client;
	
	@Parameters(index="0")
	private Long user_id;
	
	@Parameters(index="1")
	private Long video_id;
	
	@Override
	public void run() {
		
		HttpResponse<String> response = client.deleteVideo(user_id, video_id);
		System.out.printf("Server responded with %s: %s%n", response.getStatus(), response.getBody().orElse("(no text)"));
		
	}
	
}
