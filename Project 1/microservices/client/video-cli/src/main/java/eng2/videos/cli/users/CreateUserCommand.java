package eng2.videos.cli.users;

import eng2.videos.cli.dto.UserDTO;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "create-user", description = "...", mixinStandardHelpOptions = true)
public class CreateUserCommand implements Runnable {
	
	@Inject
	private UsersClient client;
	
	@Parameters(index="0")
	private String name;

	@Override
	public void run() {
		
		UserDTO user = new UserDTO();
		user.setName(name);
		
		HttpResponse<Void> response = client.createUser(user);
		System.out.println("Server responded with " + response.getStatus());
		
	}

}
