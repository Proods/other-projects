package eng2.trending.hashtags;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eng2.trending.hashtags.domain.Hashtag;
import eng2.trending.hashtags.repositories.HashtagsRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;


@MicronautTest(transactional=false, environments="no_streams")
public class TrendingHashtagsControllerTest {
	
	@Inject
	TrendingHashtagsClient client;
	
	@Inject
	HashtagsRepository repo;
	
	
	@BeforeEach
	public void clean() {
		repo.deleteAll();
	}
	
	@Test
	public void noHashtags() {
		Iterable<Hashtag> hashtags = client.getTrendingHashtags();
		assertFalse(hashtags.iterator().hasNext());
	}
	
	@Test
	public void hashtagOrder() {
		
		for (int i = 1; i < 4; i++) {
			Hashtag hashtag = new Hashtag();
			hashtag.setId((long) i);
			hashtag.setName("#Hashtag" + i);
			hashtag.setNumOfLikes(i);
			repo.save(hashtag);
		}
		
		List<Hashtag> hashtags = iterableToList(client.getTrendingHashtags());
		
		List<Integer> numOfLikes = new ArrayList<>();
		List<Integer> result = new ArrayList<>();
		
		for (Hashtag h: hashtags) {
			numOfLikes.add(h.getNumOfLikes());
			result.add(h.getNumOfLikes());
		}
		
		Collections.sort(numOfLikes);
		Collections.reverse(numOfLikes);

		assertTrue(result.equals(numOfLikes));
	}
	
	@Test
	public void showOnlyTopTen() {
		
		for (int i = 1; i < 16; i++) {
			Hashtag hashtag = new Hashtag();
			hashtag.setId((long) i);
			hashtag.setName("#Hashtag" + i);
			hashtag.setNumOfLikes(i);
			repo.save(hashtag);
		}
		
		List<Hashtag> hashtags = iterableToList(client.getTrendingHashtags());
		assertEquals(hashtags.size(), 10);
	}
	
	private <T> List<T> iterableToList(Iterable<T> iterable) {
		List<T> l = new ArrayList<>();
		iterable.forEach(l::add);
		return l;
	}

}
