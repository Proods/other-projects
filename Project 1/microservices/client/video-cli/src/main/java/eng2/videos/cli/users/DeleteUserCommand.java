package eng2.videos.cli.users;

import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "delete-user", description = "...", mixinStandardHelpOptions = true)
public class DeleteUserCommand implements Runnable{
	
	@Inject
	private UsersClient client;
	
	@Parameters(index="0")
	private Long id;

	@Override
	public void run() {
		
		HttpResponse<String> response = client.deleteUser(id);
		System.out.printf("Server responded with %s: %s%n", response.getStatus(), response.getBody().orElse("(no text)"));
		
	}

}
