package eng2.subscriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eng2.subscriptions.domain.Hashtag;
import eng2.subscriptions.domain.User;
import eng2.subscriptions.domain.Video;
import eng2.subscriptions.events.SubscriptionsProducer;
import eng2.subscriptions.repositories.HashtagsRepository;
import eng2.subscriptions.repositories.UsersRepository;
import eng2.subscriptions.repositories.VideosRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;


@MicronautTest(transactional=false, environments="no_streams")
public class SubscriptionsControllerTests {
	
	@Inject
	UsersRepository usersRepo;
	
	@Inject
	HashtagsRepository hashtagsRepo;
	
	@Inject
	VideosRepository videosRepo;
	
	@Inject
	SubscriptionsClient client;
	
	private final Map<Long,User> producerTest = new HashMap<>();
	
	
	@MockBean(SubscriptionsProducer.class)
	SubscriptionsProducer testProducer() {
		return new SubscriptionsProducer() {

			@Override
			public void subscribe(User user, Hashtag hashtag) {
				producerTest.put(user.getId(), user);
			}

			@Override
			public void unsubscribe(User user, Hashtag hashtag) {
				producerTest.put(user.getId(), user);
			}
			
		};
	}
	
	@BeforeEach
	public void clean() {
		usersRepo.deleteAll();
		hashtagsRepo.deleteAll();
		videosRepo.deleteAll();
		producerTest.clear();
	}
	
	@Test
	public void subscribe() {
		
		Hashtag hashtag = new Hashtag();
		hashtag.setId((long) 5);
		hashtag.setName("#HASHTAG");
		hashtagsRepo.save(hashtag);
		
		User user = new User();
		user.setId((long) 2);
		usersRepo.save(user);
		
		HttpResponse<String> response = client.subscribe(hashtag.getId(), user.getId());
		assertEquals(HttpStatus.OK, response.getStatus());
		
		assertTrue(producerTest.containsKey(user.getId()));
		
		hashtag = hashtagsRepo.findById(hashtag.getId()).get();
		user = usersRepo.findById(user.getId()).get();
		
		List<Hashtag> hashtags = iterableToList(user.getSubscriptions());
		List<User> users = iterableToList(hashtag.getSubscribers());
		
		assertEquals(hashtag.getId(), hashtags.get(0).getId());
		assertEquals(user.getId(), users.get(0).getId());
	}
	
	@Test
	public void alreadySubscribed() {
		
		Hashtag hashtag = new Hashtag();
		hashtag.setId((long) 5);
		hashtag.setName("#HASHTAG");
		hashtagsRepo.save(hashtag);
		
		Set<Hashtag> hts = new HashSet<>();
		hts.add(hashtag);
		
		User user = new User();
		user.setId((long) 2);
		user.setSubscriptions(hts);
		usersRepo.save(user);
		
		HttpResponse<String> response = client.subscribe(hashtag.getId(), user.getId());
		assertEquals(HttpStatus.OK, response.getStatus());
		
		assertFalse(producerTest.containsKey(user.getId()));
		
		hashtag = hashtagsRepo.findById(hashtag.getId()).get();
		user = usersRepo.findById(user.getId()).get();
		
		List<Hashtag> hashtags = iterableToList(user.getSubscriptions());
		List<User> users = iterableToList(hashtag.getSubscribers());
		
		assertEquals(hashtag.getId(), hashtags.get(0).getId());
		assertEquals(user.getId(), users.get(0).getId());
	}
	
	@Test
	public void subscribeNotFound() {
		
		Long hashtag_id = (long) 5;
		Hashtag hashtag = new Hashtag();
		hashtag.setId(hashtag_id);
		hashtag.setName("#HASHTAG");
		hashtagsRepo.save(hashtag);
		
		Long user_id = (long) 2;
		User user = new User();
		user.setId(user_id);
		usersRepo.save(user);
		
		HttpResponse<String> response = client.subscribe((long) 0, user_id);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		response = client.subscribe(hashtag_id, (long) 0);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		hashtag = hashtagsRepo.findById(hashtag_id).get();
		user = usersRepo.findById(user_id).get();
		
		List<Hashtag> hashtags = iterableToList(user.getSubscriptions());
		List<User> users = iterableToList(hashtag.getSubscribers());
		
		assertTrue(hashtags.isEmpty());
		assertTrue(users.isEmpty());
	}
	
	@Test
	public void unsubscribe() {
		
		Hashtag hashtag = new Hashtag();
		hashtag.setId((long) 5);
		hashtag.setName("#HASHTAG");
		hashtagsRepo.save(hashtag);
		
		Set<Hashtag> hts = new HashSet<>();
		hts.add(hashtag);
		
		User user = new User();
		user.setId((long) 2);
		user.setSubscriptions(hts);
		usersRepo.save(user);
		
		HttpResponse<String> response = client.unsubscribe(hashtag.getId(), user.getId());
		assertEquals(HttpStatus.OK, response.getStatus());
		
		assertTrue(producerTest.containsKey(user.getId()));
		
		hashtag = hashtagsRepo.findById(hashtag.getId()).get();
		user = usersRepo.findById(user.getId()).get();
		
		List<Hashtag> hashtags = iterableToList(user.getSubscriptions());
		List<User> users = iterableToList(hashtag.getSubscribers());
		
		assertTrue(hashtags.isEmpty());
		assertTrue(users.isEmpty());		
	}
	
	@Test
	public void alreadyNotSubscribed() {
		
		Hashtag hashtag = new Hashtag();
		hashtag.setId((long) 5);
		hashtag.setName("#HASHTAG");
		hashtagsRepo.save(hashtag);
		
		User user = new User();
		user.setId((long) 2);
		usersRepo.save(user);
		
		HttpResponse<String> response = client.unsubscribe(hashtag.getId(), user.getId());
		assertEquals(HttpStatus.OK, response.getStatus());
		
		assertFalse(producerTest.containsKey(user.getId()));
		
		hashtag = hashtagsRepo.findById(hashtag.getId()).get();
		user = usersRepo.findById(user.getId()).get();
		
		List<Hashtag> hashtags = iterableToList(user.getSubscriptions());
		List<User> users = iterableToList(hashtag.getSubscribers());
		
		assertTrue(hashtags.isEmpty());
		assertTrue(users.isEmpty());
	}
	
	@Test
	public void unsubscribeNotFound() {
		
		Hashtag hashtag = new Hashtag();
		hashtag.setId((long) 5);
		hashtag.setName("#HASHTAG");
		hashtagsRepo.save(hashtag);
		
		Set<Hashtag> hts = new HashSet<>();
		hts.add(hashtag);
		
		User user = new User();
		user.setId((long) 2);
		user.setSubscriptions(hts);
		usersRepo.save(user);
		
		HttpResponse<String> response = client.unsubscribe((long) 0, user.getId());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		response = client.unsubscribe(hashtag.getId(), (long) 0);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		hashtag = hashtagsRepo.findById(hashtag.getId()).get();
		user = usersRepo.findById(user.getId()).get();
		
		List<Hashtag> hashtags = iterableToList(user.getSubscriptions());
		List<User> users = iterableToList(hashtag.getSubscribers());
		
		assertEquals(hashtag.getId(), hashtags.get(0).getId());
		assertEquals(user.getId(), users.get(0).getId());
	}
	
	@Test
	public void recommendOrder() {
		
		Set<Video> vds = new HashSet<>();
		
		for (int i = 1; i < 5; i++) {
			Video video = new Video();
			video.setId((long) i);
			video.setTitle("Video" + i);
			video.setLikes(i);
			videosRepo.save(video);
			vds.add(video);
		}
		
		Hashtag hashtag = new Hashtag();
		hashtag.setId((long) 6);
		hashtag.setName("#HASHTAG");
		hashtag.setVideos(vds);
		hashtagsRepo.save(hashtag);
		
		Set<Hashtag> hts = new HashSet<>();
		hts.add(hashtag);
		
		User user = new User();
		user.setId((long) 7);
		user.setSubscriptions(hts);
		usersRepo.save(user);
		
		List<Video> videos = iterableToList(client.recommend(hashtag.getId(), user.getId()));
		assertEquals(4, videos.size());
		
		List<Integer> numOfLikes = new ArrayList<>();
		List<Integer> result = new ArrayList<>();
		
		for (Video v : videos) {
			numOfLikes.add(v.getLikes());
			result.add(v.getLikes());
		}
		
		Collections.sort(numOfLikes);
		Collections.reverse(numOfLikes);

		assertTrue(result.equals(numOfLikes));
	}
	
	@Test
	public void showOnlyTopTen() {
		
		Set<Video> vds = new HashSet<>();
		
		for (int i = 0; i < 16; i++) {
			Video video = new Video();
			video.setId((long) i);
			video.setTitle("Video" + i);
			video.setLikes(i);
			videosRepo.save(video);
			vds.add(video);
		}
		
		Hashtag hashtag = new Hashtag();
		hashtag.setId((long) 17);
		hashtag.setName("#HASHTAG");
		hashtag.setVideos(vds);
		hashtagsRepo.save(hashtag);
		
		Set<Hashtag> hts = new HashSet<>();
		hts.add(hashtag);
		
		User user = new User();
		user.setId((long) 18);
		user.setSubscriptions(hts);
		usersRepo.save(user);
		
		List<Video> videos = iterableToList(client.recommend(hashtag.getId(), user.getId()));
		assertEquals(videos.size(),10);
	}
	
	@Test
	public void recommendNotSubscribed() {
		
		Video video = new Video();
		video.setId((long) 3);
		video.setTitle("TITLE");
		video.setLikes(4);
		videosRepo.save(video);
		
		Set<Video> vds = new HashSet<>();
		vds.add(video);
		
		Hashtag hashtag = new Hashtag();
		hashtag.setId((long) 5);
		hashtag.setName("#HASHTAG");
		hashtag.setVideos(vds);
		hashtagsRepo.save(hashtag);
		
		User user = new User();
		user.setId((long) 2);
		usersRepo.save(user);		
		
		Iterable<Video> videos = client.recommend(hashtag.getId(), user.getId());
		assertNull(videos);
	}
	
	@Test
	public void recommendNotFound() {
		
		Video video = new Video();
		video.setId((long) 3);
		video.setTitle("TITLE");
		video.setLikes(4);
		videosRepo.save(video);
		
		Set<Video> vds = new HashSet<>();
		vds.add(video);
		
		Hashtag hashtag = new Hashtag();
		hashtag.setId((long) 5);
		hashtag.setName("#HASHTAG");
		hashtag.setVideos(vds);
		hashtagsRepo.save(hashtag);
		
		Set<Hashtag> hts = new HashSet<>();
		hts.add(hashtag);
		
		User user = new User();
		user.setId((long) 2);
		user.setSubscriptions(hts);
		usersRepo.save(user);
		
		Iterable<Video> videos = client.recommend((long) 0, user.getId());
		assertNull(videos);
		
		videos = client.recommend(hashtag.getId(), (long) 0);
		assertNull(videos);
	}
	
	private <T> List<T> iterableToList(Iterable<T> iterable) {
		List<T> l = new ArrayList<>();
		iterable.forEach(l::add);
		return l;
	}

}
