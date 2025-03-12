package eng2.trending.hashtags.cli;

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

public class TrendingHashtagCliCommandTest {

    @Test
    public void testWithCommandLineOption() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            String[] args = new String[] { "-v" };
            PicocliRunner.run(TrendingHashtagCliCommand.class, ctx, args);

            // trending-hashtag-cli
            assertTrue(baos.toString().contains("Hi!"));
        }
    }
    
    @Test
    public void testGetTrendingHashtagsCommand() throws Exception {

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));
            PicocliRunner.run(TrendingHashtagCliCommand.class, ctx, new String[] {"get-trending-hashtags"});
            
            assertFalse(baos.toString().isBlank());
            
            String[] hashtags = baos.toString().trim().split(System.lineSeparator());
            
            List<Integer> likes = new ArrayList<>();
            List<Integer> ordered = new ArrayList<>();
            for (String h : hashtags) {
            	likes.add(getLikes(h));
            	ordered.add(getLikes(h));
            }
            
            Collections.sort(ordered);
            Collections.reverse(ordered);
            assertTrue(likes.equals(ordered));
        }
    }
    
    private Integer getLikes(String string) {
		
		int start = string.indexOf("Likes");
        int end = string.indexOf("]");
        
        String numOfLikes = string.substring(start,end).replace("Likes=", "");
        
        return Integer.parseInt(numOfLikes);
	}
}
