package eng2.videos.cli.users;

import java.util.HashSet;
import java.util.Set;

import eng2.videos.cli.dto.VideoDTO;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;


@Command(name = "update-video", description = "...", mixinStandardHelpOptions = true)
public class UpdateVideoCommand implements Runnable{
	
	@Inject
	private UsersClient client;
	
	@Parameters(index="0")
	private Long user_id;
	
	@Parameters(index="1")
	private Long video_id;
	
	@Option(names="-t", arity="*")
	private String title;
	
	@Option(names="-ht", arity="*")
	private Set<String> hashtags;

	@Override
	public void run() {
		
		Set<String> htags = new HashSet<>();
		
		if (hashtags!=null) {
			for (String hashtag: hashtags) {
				htags.add("#" + hashtag.replaceAll("\\s", "").replaceAll("[^a-zA-Z0-9_-]", ""));
			}
		}
		
		VideoDTO v = new VideoDTO();
		if (title!=null) {
			v.setTitle(title);
		}
		if(hashtags!=null) {
			v.setHashtags(htags);
		}
		
		HttpResponse<String> response = client.updateVideo(user_id, video_id, v);
		System.out.printf("Server responded with %s: %s%n", response.getStatus(), response.getBody().orElse("(no text)"));
		
	}

}
