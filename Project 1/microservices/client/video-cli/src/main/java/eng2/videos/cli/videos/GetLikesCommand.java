package eng2.videos.cli.videos;

import eng2.videos.cli.domain.User;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "get-likes", description = "...", mixinStandardHelpOptions = true)
public class GetLikesCommand implements Runnable{
	
	@Inject
	private VideosClient client;
	
	@Parameters(index="0")
	private Long id;

	@Override
	public void run() {
		
		Iterable<User> users = client.getLikes(id);
		
		if(users!=null) {
			for (User u: users) {
				System.out.println(u);
			}
		}
		
	}

}
