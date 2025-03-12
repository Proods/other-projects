package eng2.videos.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;


public class CRUDTest {
	
	@Test
	public void userCRUD() throws Exception {
		
		try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
			
			String name = "Harry";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));            
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"create-user", name});
            assertTrue(baos.toString().contains("CREATED"));
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-users"});
            assertTrue(baos.toString().contains("name=" + name));
            
            String id = getId(baos.toString());            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-user", id});
            assertTrue(baos.toString().contains("name=" + name));
            
            name = "Gary";            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"update-user", id, name});
            assertTrue(baos.toString().trim().equals("Server responded with OK: User " + id + " has been updated!"));
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-user", id});
            assertTrue(baos.toString().contains("name=" + name));
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"delete-user", id});
            assertTrue(baos.toString().trim().equals("Server responded with OK: User " + id + " has been deleted!"));
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-user", id});
            assertTrue(baos.toString().trim().equals("User not found!"));
        }
	}
	
	@Test
	public void videoCRUD() throws Exception {
		
		try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
			
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"create-user", "Harry"});
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));            
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-users"});
            String user_id = getId(baos.toString());
            
            String title = "HARRY";
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));            
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"create-video", user_id, title});
            assertTrue(baos.toString().contains("CREATED"));
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-videos"});
            assertTrue(baos.toString().contains("title=" + title));
            
            String video_id = getId(baos.toString());            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-video", video_id});
            assertTrue(baos.toString().contains("title=" + title));
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));            
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-uploads", user_id});
            assertTrue(baos.toString().trim().equals("Video [id=" + video_id + ", title=" + title + "]"));
            
            title = "TITLE";
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"update-video", user_id, video_id, "-t", title});
            assertTrue(baos.toString().trim().equals("Server responded with OK: Video " + video_id + " has been updated!"));
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-video", video_id});
            assertTrue(baos.toString().contains("title=" + title));
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"delete-video", user_id, video_id});
            assertTrue(baos.toString().trim().equals("Server responded with OK: Video " + video_id + " has been deleted!"));
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-video", video_id});
            assertTrue(baos.toString().trim().equals("Video not found!"));
            
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"delete-user", user_id});
		}
	}
	
	@Test
	public void hashtagCRUD() throws Exception {
		
		try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
			
			PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"create-user", "Harry"});			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));            
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-users"});
            String user_id = getId(baos.toString());
            
            String name = "HARRY";
            String title = "TITLE";
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"create-video", user_id, title, "-ht", name});
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-videos"});
            String video_id = getId(baos.toString());
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-hashtags"});
            String hashtag_id = getId(baos.toString());
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-hashtag", hashtag_id});
            assertTrue(baos.toString().contains("name=#" + name));
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-hashtag-videos", hashtag_id});
            assertTrue(baos.toString().trim().equals("Video [id=" + video_id + ", title=" + title + "]"));
            
            name = "HASHTAG";
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"update-video", user_id, video_id, "-ht", name});
            assertTrue(baos.toString().trim().equals("Server responded with OK: Video " + video_id + " has been updated!"));
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-hashtags"});
            assertTrue(baos.toString().contains("name=#" + name));
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"delete-video", user_id, video_id});
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"get-hashtags"});
            assertTrue(baos.toString().isEmpty());
            
            PicocliRunner.run(VideoCliCommand.class, ctx, new String[] {"delete-user", user_id});
		}
	}
	
	private String getId(String string) {
		
		int start = string.indexOf("=");
        int end = string.indexOf(",");
        
        return string.substring(start,end).replace("=", "");
	}
	
}
