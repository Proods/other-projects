package eng2.videos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eng2.videos.clients.HashtagsClient;
import eng2.videos.clients.UsersClient;
import eng2.videos.clients.VideosClient;
import eng2.videos.domain.Hashtag;
import eng2.videos.domain.User;
import eng2.videos.domain.Video;
import eng2.videos.dto.VideoDTO;
import eng2.videos.events.HashtagsProducer;
import eng2.videos.repositories.HashtagsRepository;
import eng2.videos.repositories.UsersRepository;
import eng2.videos.repositories.VideosRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;


@MicronautTest(transactional=false, environments="no_streams")
public class HashtagsControllerTest {
	
	@Inject
	HashtagsRepository hashtagRepo;
	
	@Inject
	UsersRepository usersRepo;
	
	@Inject
	VideosRepository videosRepo;
	
	@Inject
	HashtagsClient hashtagsClient;
	
	@Inject
	UsersClient usersClient;
	
	@Inject
	VideosClient videosClient;
	
	private final Map<Long,Hashtag> createHashtagTest = new HashMap<>();
	
	private final Map<Long,Hashtag> deleteHashtagTest = new HashMap<>();
	
	private final Map<Long,Integer> topHashtagTest = new HashMap<>();
	
	private final Map<Long,Video> addHashtagVideoTest = new HashMap<>();
	
	private final Map<Long,Video> removeHashtagVideoTest = new HashMap<>();
	
	
	@MockBean(HashtagsProducer.class)
	HashtagsProducer testHashtagsProducer() {
		return new HashtagsProducer() {

			@Override
			public void createHashtag(Long id, Hashtag hashtag) {
				createHashtagTest.put(id, hashtag);
			}
			
			@Override
			public void deleteHashtag(Long id, Hashtag hashtag) {
				deleteHashtagTest.put(id, hashtag);
			}
			
			@Override
			public void topHashtags(Hashtag hashtag, Integer numOfLikes) {
				topHashtagTest.put(hashtag.getId(), numOfLikes);
			}

			@Override
			public void addHashtagVideo(Hashtag hashtag, Video video) {
				addHashtagVideoTest.put(hashtag.getId(), video);
			}

			@Override
			public void removeHashtagVideo(Hashtag hashtag, Video video) {
				removeHashtagVideoTest.put(hashtag.getId(), video);
			}
			
		};
	}
	
	@BeforeEach
	public void clean() {
		hashtagRepo.deleteAll();
		videosRepo.deleteAll();
		usersRepo.deleteAll();
		createHashtagTest.clear();
		deleteHashtagTest.clear();
		topHashtagTest.clear();
		addHashtagVideoTest.clear();
		removeHashtagVideoTest.clear();
	}
	
	@Test
	public void noHashtag() {
		Iterable<Hashtag> hashtags = hashtagsClient.list();
		assertFalse(hashtags.iterator().hasNext());
	}
	
	@Test
	public void getMissingHashtag() {
		Hashtag hashtag = hashtagsClient.getHashtag((long) 0);
		assertNull(hashtag);
	}
	
	@Test
	public void getHashtag() {
		
		User user = new User();
		user.setName("George");
		usersRepo.save(user);
		
		String hashtag = "#hashtag";
		Set<String> hashtags = new HashSet<>();
		hashtags.add(hashtag);
		VideoDTO video = new VideoDTO();
		video.setTitle("Title");
		video.setHashtags(hashtags);
		
		usersClient.createVideo(user.getId(), video);
		
		List<Hashtag> hts = iterableToList(hashtagsClient.list());
		assertEquals(1, hts.size());
		assertEquals(hashtag, hts.get(0).getName());
		
		assertTrue(createHashtagTest.containsKey(hts.get(0).getId()));
		assertTrue(addHashtagVideoTest.containsKey(hts.get(0).getId()));
	}
	
	@Test
	public void getHashtagVideos() {
		
		User user = new User();
		user.setName("Ethan");
		usersRepo.save(user);
		
		Set<String> hashtags = new HashSet<>();
		hashtags.add("#ethan");
		String title = "ETHAN";
		VideoDTO video = new VideoDTO();
		video.setTitle(title);
		video.setHashtags(hashtags);
		
		usersClient.createVideo(user.getId(), video);
		
		Hashtag hashtag = iterableToList(hashtagsClient.list()).get(0);
		
		List<Video> videos = iterableToList(hashtagsClient.getHashtagVideos(hashtag.getId()));		
		assertEquals(1, videos.size());
		assertEquals(title, videos.get(0).getTitle());
	}
	
	@Test
	public void updateHashtag() {
		
		User user = new User();
		user.setName("Emily");
		usersRepo.save(user);
		
		Set<String> hashtags = new HashSet<>();
		hashtags.add("#Hello");
		VideoDTO video = new VideoDTO();
		video.setTitle("EMILY");
		video.setHashtags(hashtags);
		
		usersClient.createVideo(user.getId(), video);
		createHashtagTest.clear();
		addHashtagVideoTest.clear();

		Video v = iterableToList(videosClient.list()).get(0);
		Hashtag hashtag = iterableToList(hashtagsClient.list()).get(0);
		
		String newHashtag = "Bye";
		Set<String> newHashtags = new HashSet<>();
		newHashtags.add(newHashtag);
		video.setTitle("EMILY");
		video.setHashtags(newHashtags);
		
		HttpResponse<String> response = usersClient.updateVideo(user.getId(), v.getId(), video);
		assertEquals(HttpStatus.OK, response.getStatus());
		
		assertTrue(removeHashtagVideoTest.containsKey(hashtag.getId()));
		assertTrue(topHashtagTest.containsKey(hashtag.getId()));
		assertTrue(deleteHashtagTest.containsKey(hashtag.getId()));
		
		hashtag = iterableToList(hashtagsClient.list()).get(0);
		assertEquals(newHashtag, hashtag.getName());
		
		assertTrue(createHashtagTest.containsKey(hashtag.getId()));
		assertTrue(addHashtagVideoTest.containsKey(hashtag.getId()));
	}
	
	@Test
	public void deleteHashtag() {
		
		User user = new User();
		user.setName("Emily");
		usersRepo.save(user);
		Long id = user.getId();
		
		Set<String> hashtags = new HashSet<>();
		hashtags.add("#Delete");
		VideoDTO video = new VideoDTO();
		video.setTitle("EMILY");
		video.setHashtags(hashtags);
		
		usersClient.createVideo(id, video);

		Video v = iterableToList(videosClient.list()).get(0);
		Long hashId = iterableToList(hashtagsClient.list()).get(0).getId();
		usersClient.deleteVideo(id, v.getId());
		
		assertTrue(deleteHashtagTest.containsKey(hashId));
		assertTrue(topHashtagTest.containsKey(hashId));
		
		Iterable<Hashtag> iterHash = hashtagsClient.list();
		assertFalse(iterHash.iterator().hasNext());
		assertFalse(hashtagRepo.existsById(hashId));
	}
	
	private <T> List<T> iterableToList(Iterable<T> iterable) {
		List<T> l = new ArrayList<>();
		iterable.forEach(l::add);
		return l;
	}
}
