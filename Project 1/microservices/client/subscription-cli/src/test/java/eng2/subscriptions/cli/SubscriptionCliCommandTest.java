package eng2.subscriptions.cli;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubscriptionCliCommandTest {

    @Test
    public void testWithCommandLineOption() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            String[] args = new String[] { "-v" };
            PicocliRunner.run(SubscriptionCliCommand.class, ctx, args);

            // subscription-cli
            assertTrue(baos.toString().contains("Hi!"));
        }
    }
    
    @Test
    public void subscriptionCommands() throws Exception {
        
        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            
        	String hashtag = "4";
        	String user = "1";
        	
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(SubscriptionCliCommand.class, ctx, new String[] {"subscribe-hashtag", hashtag, user});
            assertTrue(baos.toString().trim().equals("Server responded with OK: User "+ user + " has subscribed to Hashtag " + hashtag + "!"));
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(SubscriptionCliCommand.class, ctx, new String[] {"get-recommendations", "4", "1"});
            assertFalse(baos.toString().isBlank());
            
            String[] videos = baos.toString().trim().split(System.lineSeparator());
            
            List<Integer> likes = new ArrayList<>();
            List<Integer> ordered = new ArrayList<>();
            for (String v : videos) {
            	likes.add(getLikes(v));
            	ordered.add(getLikes(v));
            }
            
            Collections.sort(ordered);
            Collections.reverse(ordered);
            assertTrue(likes.equals(ordered));
                                    
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(SubscriptionCliCommand.class, ctx, new String[] {"unsubscribe-hashtag", hashtag, user});
            assertTrue(baos.toString().trim().equals("Server responded with OK: User " + user + " has unsubscribed to Hashtag " + hashtag + "!"));
            
            baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(SubscriptionCliCommand.class, ctx, new String[] {"get-recommendations", hashtag, user});
            assertTrue(baos.toString().isBlank());
        }
    }
    
    private Integer getLikes(String string) {
		
		int start = string.indexOf("likes");
        int end = string.indexOf("]");
        
        String numOfLikes = string.substring(start,end).replace("likes=", "");
        
        return Integer.parseInt(numOfLikes);
	}
}
