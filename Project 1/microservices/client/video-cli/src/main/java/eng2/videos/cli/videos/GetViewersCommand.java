package eng2.videos.cli.videos;

import eng2.videos.cli.domain.User;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "get-viewers", description = "...", mixinStandardHelpOptions = true)
public class GetViewersCommand implements Runnable{
	
	@Inject
	private VideosClient client;
	
	@Parameters(index="0")
	private Long id;

	@Override
	public void run() {
		
		Iterable<User> users = client.getViewers(id);
		
		if(users!=null) {
			for (User u: users) {
				System.out.println(u);
			}
		}
	}

}
