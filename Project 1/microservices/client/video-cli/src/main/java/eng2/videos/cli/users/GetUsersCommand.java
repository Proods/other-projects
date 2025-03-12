package eng2.videos.cli.users;

import eng2.videos.cli.domain.User;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;


@Command(name = "get-users", description = "...", mixinStandardHelpOptions = true)
public class GetUsersCommand implements Runnable{
	
	@Inject
	private UsersClient client;

	@Override
	public void run() {
		
		for (User u: client.list()) {
			System.out.println(u);
		}
		
	}

}
