package eng2.videos.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;


public class UserVideoInteractionsTest {
	
	@Test
	public void viewTest() throws Exception {
		
		try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
			
			List<String> vars = setUp(ctx);
			String userTwoName = vars.get(0);
			String userOneId = vars.get(1);
			String userTwoId = vars.get(2);
			String userOneVideoTitle = vars.get(3);
			String userOneVideoId = vars.get(4);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(baos));
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"add-view", userOneVideoId, userTwoId});
			assertTrue(baos.toString().trim().equals("Server responded with OK: User " + userTwoId + " has viewed Video " + userOneVideoId));
			
			baos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(baos));
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-viewers", userOneVideoId});
			assertTrue(baos.toString().trim().equals("User [id=" + userTwoId + ", name=" + userTwoName + "]"));
			
			baos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(baos));
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-viewed-videos", userTwoId});
			assertTrue(baos.toString().trim().equals("Video [id=" + userOneVideoId + ", title=" + userOneVideoTitle + "]"));
			
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"delete-user", userOneId});
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"delete-user", userTwoId});
		}
	}
	
	@Test
	public void likeTest() throws Exception {
		
		try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
			
			List<String> vars = setUp(ctx);
			String userTwoName = vars.get(0);
			String userOneId = vars.get(1);
			String userTwoId = vars.get(2);
			String userOneVideoTitle = vars.get(3);
			String userOneVideoId = vars.get(4);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(baos));
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"add-like", userOneVideoId, userTwoId});
			assertTrue(baos.toString().trim().equals("Server responded with OK: User " + userTwoId + " has liked Video " + userOneVideoId));
			
			baos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(baos));
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-likes", userOneVideoId});
			assertTrue(baos.toString().trim().equals("User [id=" + userTwoId + ", name=" + userTwoName + "]"));
			
			baos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(baos));
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-liked-videos", userTwoId});
			assertTrue(baos.toString().trim().equals("Video [id=" + userOneVideoId + ", title=" + userOneVideoTitle + "]"));
			
			baos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(baos));
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"remove-like", userOneVideoId, userTwoId});
			assertTrue(baos.toString().trim().equals("Server responded with OK: User " + userTwoId + " has undoed like from Video " + userOneVideoId));
			
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"delete-user", userOneId});
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"delete-user", userTwoId});
		}
	}
	
	@Test
	public void dislikeTest() throws Exception {
		
		try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
			
			List<String> vars = setUp(ctx);
			String userTwoName = vars.get(0);
			String userOneId = vars.get(1);
			String userTwoId = vars.get(2);
			String userOneVideoTitle = vars.get(3);
			String userOneVideoId = vars.get(4);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(baos));
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"add-dislike", userOneVideoId, userTwoId});
			assertTrue(baos.toString().trim().equals("Server responded with OK: User " + userTwoId + " has disliked Video " + userOneVideoId));
			
			baos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(baos));
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-dislikes", userOneVideoId});
			assertTrue(baos.toString().trim().equals("User [id=" + userTwoId + ", name=" + userTwoName + "]"));
			
			baos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(baos));
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-disliked-videos", userTwoId});
			assertTrue(baos.toString().trim().equals("Video [id=" + userOneVideoId + ", title=" + userOneVideoTitle + "]"));
			
			baos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(baos));
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"remove-dislike", userOneVideoId, userTwoId});
			assertTrue(baos.toString().trim().equals("Server responded with OK: User " + userTwoId + " has undoed dislike from Video " + userOneVideoId));
			
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"delete-user", userOneId});
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"delete-user", userTwoId});
		}
	}
	
	private List<String> setUp(ApplicationContext ctx) {
		
		String userName = "Gary";
		
		PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"create-user", "Harry"});
		PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"create-user", userName});
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-users"});
		
		String[] user_ids = baos.toString().trim().split(System.lineSeparator());
		for (int i = 0; i < user_ids.length; i++) {
			user_ids[i] = getId(user_ids[i]);
		}            
		
		String videoTitle = "HARRY";
		
		PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"create-video", user_ids[0], videoTitle});
		
		baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-videos"});
		String video_id = getId(baos.toString());
		
		List<String> result = new ArrayList<>();
		result.add(userName);
		result.add(user_ids[0]);
		result.add(user_ids[1]);
		result.add(videoTitle);
		result.add(video_id);
		
		return result;
	}
	
	private String getId(String string) {
		
		int start = string.indexOf("=");
        int end = string.indexOf(",");
        
        return string.substring(start,end).replace("=", "");
	}
	
}
