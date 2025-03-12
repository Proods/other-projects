package eng2.videos.cli.users;

import java.util.HashSet;
import java.util.Set;

import eng2.videos.cli.dto.VideoDTO;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;


@Command(name = "create-video", description = "...", mixinStandardHelpOptions = true)
public class CreateVideoCommand implements Runnable{
	
	@Inject
	private UsersClient client;
	
	@Parameters(index="0")
	private Long id;
	
	@Parameters(index="1")
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
		v.setTitle(title);
		if(hashtags!=null) {
			v.setHashtags(htags);
		}
		
		HttpResponse<Void> response = client.createVideo(id, v);
		System.out.println("Server responded with " + response.getStatus());
		
	}

}
