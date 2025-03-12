package eng2.trending.hashtags;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eng2.trending.hashtags.domain.Hashtag;
import eng2.trending.hashtags.events.HashtagsConsumer;
import eng2.trending.hashtags.repositories.HashtagsRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest(transactional = false, environments = "no_streams")
public class KafkaProductionTest {
	
	@Inject
	HashtagsRepository repo;
	
	@Inject
	HashtagsConsumer consumer;
	
	
	@BeforeEach
	public void clean() {
		repo.deleteAll();
	}
	
	@Test
	public void newHashtagTest() {
		
		Hashtag temp = new Hashtag();
		temp.setId((long) 3);
		temp.setName("HASHTAG");
		
		Integer likes = 2;
		
		consumer.trendingHashtag(temp, likes);
		assertTrue(repo.existsById(temp.getId()));
	}
	
	@Test
	public void existedHashtagTest() {
		
		Integer likes = 4;
		Hashtag hashtag = new Hashtag();
		hashtag.setId((long) 3);
		hashtag.setName("HASHTAG");
		hashtag.setNumOfLikes(likes);
		repo.save(hashtag);
		
		Hashtag temp = new Hashtag();
		temp.setId((long) 3);
		temp.setName("HASHTAG");
		
		Integer addLikes = 2;
		
		consumer.trendingHashtag(temp, addLikes);		
		hashtag = repo.findById(temp.getId()).get();
		assertEquals(addLikes, hashtag.getNumOfLikes());				//Kafka-stream does the number crunching, not the consumer
		assertNotEquals(likes + addLikes, hashtag.getNumOfLikes());
	}
	
	@Test
	public void deleteHashtagTest() {
		
		Long id = (long) 3;
		Hashtag hashtag = new Hashtag();
		hashtag.setId(id);
		hashtag.setName("HASHTAG");
		hashtag.setNumOfLikes(4);
		repo.save(hashtag);
		
		Hashtag temp = new Hashtag();
		temp.setId(id);
		temp.setName("HASHTAG");
				
		consumer.deleteHashtag(temp.getId(), temp);
		assertFalse(repo.existsById(id));
	}

}
