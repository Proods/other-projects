package eng2.videos.cli.users;

import eng2.videos.cli.dto.UserDTO;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "update-user", description = "...", mixinStandardHelpOptions = true)
public class UpdateUserCommand implements Runnable{
	
	@Inject
	private UsersClient client;
	
	@Parameters(index="0")
	private Long id;
	
	@Parameters(index="1")
	private String name;

	@Override
	public void run() {
		
		UserDTO user = new UserDTO();
		user.setName(name);
		
		HttpResponse<String> response = client.updateUser(id, user);
		System.out.printf("Server responded with %s: %s%n", response.getStatus(), response.getBody().orElse("(no text)"));
		
	}

}
