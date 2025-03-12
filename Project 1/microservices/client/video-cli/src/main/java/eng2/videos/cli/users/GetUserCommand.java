package eng2.videos.cli.users;

import eng2.videos.cli.domain.User;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "get-user", description = "...", mixinStandardHelpOptions = true)
public class GetUserCommand implements Runnable{
	
	@Inject
	private UsersClient client;
	
	@Parameters(index="0")
	private Long id;

	@Override
	public void run() {
		
		User u = client.getUser(id);
		if (u==null) {
			System.out.println("User not found!");
		} else {
			System.out.println(u);
		}
		
	}

}
