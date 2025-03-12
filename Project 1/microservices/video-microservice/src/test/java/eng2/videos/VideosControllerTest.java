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

import eng2.videos.clients.UsersClient;
import eng2.videos.clients.VideosClient;
import eng2.videos.domain.Hashtag;
import eng2.videos.domain.User;
import eng2.videos.domain.Video;
import eng2.videos.events.HashtagsProducer;
import eng2.videos.events.VideosProducer;
import eng2.videos.repositories.HashtagsRepository;
import eng2.videos.repositories.UsersRepository;
import eng2.videos.repositories.VideosRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest(transactional=false, environments="no_streams")
public class VideosControllerTest {
	
	@Inject
	VideosRepository videosRepo;
	
	@Inject
	UsersRepository usersRepo;
	
	@Inject
	HashtagsRepository hashtagsRepo;
	
	@Inject
	VideosClient videosClient;	
	
	@Inject
	UsersClient usersClient;
	
	private final Map<Long,User> videoTest = new HashMap<>();
	
	private final Map<Long,Integer> hashtagTest = new HashMap<>();
	
	
	@MockBean(VideosProducer.class)
	VideosProducer testProducer() {
		return new VideosProducer() {

			@Override
			public void postVideo(User user, Video video) {
			}
			
			@Override
			public void updateVideo(User user, Video video) {
			}

			@Override
			public void deleteVideo(User user, Video video) {
			}

			@Override
			public void watchVideo(Video video, User user) {
				videoTest.put(video.getId(), user);
			}

			@Override
			public void likeVideo(Video video, User user) {
				videoTest.put(video.getId(), user);
			}

			@Override
			public void dislikeVideo(Video video, User user) {
				videoTest.put(video.getId(), user);
			}

			@Override
			public void removeLike(Video video, User user) {
				videoTest.put(video.getId(), user);
			}

			@Override
			public void removeDislike(Video video, User user) {
				videoTest.put(video.getId(), user);
			}
			
		};
	}
	
	@MockBean(HashtagsProducer.class)
	HashtagsProducer testHashtagsProducer() {
		return new HashtagsProducer() {

			@Override
			public void createHashtag(Long id, Hashtag hashtag) {
			}
			
			@Override
			public void deleteHashtag(Long id, Hashtag hashtag) {}
			
			@Override
			public void topHashtags(Hashtag hashtag, Integer numOfLikes) {
				hashtagTest.put(hashtag.getId(), numOfLikes);
			}
			
			@Override
			public void addHashtagVideo(Hashtag hashtag, Video video) {
			}

			@Override
			public void removeHashtagVideo(Hashtag hashtag, Video video) {				
			}
			
		};
	}
	
	
	@BeforeEach
	public void clean() {
		hashtagsRepo.deleteAll();
		videosRepo.deleteAll();
		usersRepo.deleteAll();
		videoTest.clear();
		hashtagTest.clear();
	}
	
	@Test
	public void noVideo() {
		Iterable<Video> videos = videosClient.list();
		assertFalse(videos.iterator().hasNext());
	}
	
	@Test
	public void getMissingVideo() {
		Video video = videosClient.getVideo((long) 0);
		assertNull(video);
	}
	
	@Test
	public void getVideo() {
		
		User user = new User();
		user.setName("George");
		usersRepo.save(user);
		
		String title = "Title";
		Video video = new Video();
		video.setCreator(user);
		video.setTitle(title);
		videosRepo.save(video);
		
		video = videosClient.getVideo(video.getId());
		assertEquals(title, video.getTitle());
	}
	
	@Test
	public void addView() {
		
		String name = "John";
		User user = new User();
		user.setName(name);
		usersRepo.save(user);
		
		Video video = new Video();
		video.setCreator(user);
		video.setTitle("Title");
		videosRepo.save(video);
		
		HttpResponse<String> response = videosClient.addView(video.getId(), user.getId());
		assertEquals(HttpStatus.OK, response.getStatus());
		
		assertTrue(videoTest.containsKey(video.getId()));
		
		List<User> viewers = iterableToList(videosClient.getViewers(video.getId()));
		assertEquals(1, viewers.size());
		assertEquals(name, viewers.get(0).getName());
	}
	
	@Test
	public void addViewNotFound() {
		
		String name = "John";
		User user = new User();
		user.setName(name);
		usersRepo.save(user);
		
		Video video = new Video();
		video.setCreator(user);
		video.setTitle("Title");
		videosRepo.save(video);
		
		HttpResponse<String> response = videosClient.addView((long) 0, user.getId());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		response = videosClient.addView(video.getId(), (long) 0);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		Iterable<User> viewers = videosClient.getViewers(video.getId());
		assertFalse(viewers.iterator().hasNext());
	}
	
	@Test
	public void getViewersNotFound() {
		Iterable<User> users = videosClient.getViewers((long) 0);
		assertNull(users);
	}
	
	@Test
	public void addLike() {
		
		String name = "Tom";
		User user = new User();
		user.setName(name);
		usersRepo.save(user);
		
		Hashtag hashtag = new Hashtag();
		hashtag.setName("#HASHTAG");
		hashtagsRepo.save(hashtag);
		Set<Hashtag> hashtags = new HashSet<>();
		hashtags.add(hashtag);
		
		Video video = new Video();
		video.setCreator(user);
		video.setTitle("Title");
		video.setHashtags(hashtags);
		videosRepo.save(video);
		
		HttpResponse<String> response = videosClient.addLike(video.getId(), user.getId());
		assertEquals(HttpStatus.OK, response.getStatus());
		
		assertTrue(videoTest.containsKey(video.getId()));
		assertTrue(hashtagTest.containsKey(hashtag.getId()));
		
		List<User> users = iterableToList(videosClient.getLikes(video.getId()));
		assertEquals(1, users.size());
		assertEquals(name, users.get(0).getName());
		
		Iterable<User> dislikers = videosClient.getDislikes(video.getId());
		assertFalse(dislikers.iterator().hasNext());
		
		List<User> viewers = iterableToList(videosClient.getViewers(video.getId()));
		assertEquals(1, viewers.size());
		assertEquals(name, viewers.get(0).getName());
	}
	
	@Test
	public void addLikeNotFound() {
		
		String name = "Tom";
		User user = new User();
		user.setName(name);
		usersRepo.save(user);
		
		Video video = new Video();
		video.setCreator(user);
		video.setTitle("Title");
		videosRepo.save(video);
		
		HttpResponse<String> response = videosClient.addLike((long) 0, user.getId());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		response = videosClient.addLike(video.getId(), (long) 0);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		Iterable<User> users = videosClient.getLikes(video.getId());
		assertFalse(users.iterator().hasNext());
		
		Iterable<User> viewers = videosClient.getViewers(video.getId());
		assertFalse(viewers.iterator().hasNext());
	}
	
	@Test
	public void getLikesNotFound() {
		Iterable<User> users = videosClient.getLikes((long) 0);
		assertNull(users);
	}
	
	@Test
	public void addDislike() {
		
		String name = "Jamie";
		User user = new User();
		user.setName(name);
		usersRepo.save(user);
		
		Hashtag hashtag = new Hashtag();
		hashtag.setName("#HASHTAG");
		hashtagsRepo.save(hashtag);
		Set<Hashtag> hashtags = new HashSet<>();
		hashtags.add(hashtag);
		
		Video video = new Video();
		video.setCreator(user);
		video.setTitle("Title");
		video.setHashtags(hashtags);
		videosRepo.save(video);
		
		HttpResponse<String> response = videosClient.addDislike(video.getId(), user.getId());
		assertEquals(HttpStatus.OK, response.getStatus());
		
		assertTrue(videoTest.containsKey(video.getId()));
		
		List<User> users = iterableToList(videosClient.getDislikes(video.getId()));
		assertEquals(1, users.size());
		assertEquals(name, users.get(0).getName());
		
		Iterable<User> likers = videosClient.getLikes(video.getId());
		assertFalse(likers.iterator().hasNext());
		
		List<User> viewers = iterableToList(videosClient.getViewers(video.getId()));
		assertEquals(1, viewers.size());
		assertEquals(name, viewers.get(0).getName());
	}
	
	@Test
	public void addDislikeNotFound() {
		
		String name = "Jamie";
		User user = new User();
		user.setName(name);
		usersRepo.save(user);
		
		Video video = new Video();
		video.setCreator(user);
		video.setTitle("Title");
		videosRepo.save(video);
		
		HttpResponse<String> response = videosClient.addDislike((long) 0, user.getId());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		response = videosClient.addDislike(video.getId(), (long) 0);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		Iterable<User> viewers = videosClient.getViewers(video.getId());
		assertFalse(viewers.iterator().hasNext());
	}
	
	@Test
	public void getDislikesNotFound() {
		Iterable<User> users = videosClient.getDislikes((long) 0);
		assertNull(users);
	}
	
	@Test
	public void removeLike() {
		
		String name = "Tom";
		User user = new User();
		user.setName(name);
		usersRepo.save(user);
		
		Hashtag hashtag = new Hashtag();
		hashtag.setName("HASHTAG");
		hashtagsRepo.save(hashtag);
		Set<Hashtag> hashtags = new HashSet<>();
		hashtags.add(hashtag);
		
		Video video = new Video();
		video.setCreator(user);
		video.setTitle("Title");
		video.setHashtags(hashtags);
		videosRepo.save(video);
		
		videosClient.addLike(video.getId(), user.getId());
		
		HttpResponse<String> response = videosClient.removeLike(video.getId(), user.getId());
		assertEquals(HttpStatus.OK, response.getStatus());
		
		assertTrue(videoTest.containsKey(video.getId()));
		assertTrue(hashtagTest.containsKey(hashtag.getId()));
		
		Iterable<User> users = videosClient.getLikes(video.getId());
		assertFalse(users.iterator().hasNext());
		
		List<User> viewers = iterableToList(videosClient.getViewers(video.getId()));
		assertEquals(1, viewers.size());
		assertEquals(name, viewers.get(0).getName());
	}
	
	@Test
	public void removeLikeNotFound() {
		
		String name = "Tom";
		User user = new User();
		user.setName(name);
		usersRepo.save(user);
		
		Video video = new Video();
		video.setCreator(user);
		video.setTitle("Title");
		videosRepo.save(video);
		
		videosClient.addLike(video.getId(), user.getId());
		
		HttpResponse<String> response = videosClient.removeLike((long) 0, user.getId());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		response = videosClient.removeLike(video.getId(), (long) 0);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		List<User> users = iterableToList(videosClient.getLikes(video.getId()));
		assertEquals(1, users.size());
		assertEquals(name, users.get(0).getName());
		
		List<User> viewers = iterableToList(videosClient.getViewers(video.getId()));
		assertEquals(1, viewers.size());
		assertEquals(name, viewers.get(0).getName());
	}
	
	@Test
	public void removeDislike() {
		
		String name = "Tom";
		User user = new User();
		user.setName(name);
		usersRepo.save(user);
		
		Video video = new Video();
		video.setCreator(user);
		video.setTitle("Title");
		videosRepo.save(video);
		
		videosClient.addDislike(video.getId(), user.getId());
		
		HttpResponse<String> response = videosClient.removeDislike(video.getId(), user.getId());
		assertEquals(HttpStatus.OK, response.getStatus());
		
		assertTrue(videoTest.containsKey(video.getId()));
		
		Iterable<User> users = videosClient.getDislikes(video.getId());
		assertFalse(users.iterator().hasNext());
		
		List<User> viewers = iterableToList(videosClient.getViewers(video.getId()));
		assertEquals(1, viewers.size());
		assertEquals(name, viewers.get(0).getName());
	}
	
	@Test
	public void removeDislikeNotFound() {
		
		String name = "Tom";
		User user = new User();
		user.setName(name);
		usersRepo.save(user);
		
		Video video = new Video();
		video.setCreator(user);
		video.setTitle("Title");
		videosRepo.save(video);
		
		videosClient.addDislike(video.getId(), user.getId());
		
		HttpResponse<String> response = videosClient.removeDislike((long) 0, user.getId());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		response = videosClient.removeDislike(video.getId(), (long) 0);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		List<User> users = iterableToList(videosClient.getDislikes(video.getId()));
		assertEquals(1, users.size());
		assertEquals(name, users.get(0).getName());
		
		List<User> viewers = iterableToList(videosClient.getViewers(video.getId()));
		assertEquals(1, viewers.size());
		assertEquals(name, viewers.get(0).getName());
	}
	
	private <T> List<T> iterableToList(Iterable<T> iterable) {
		List<T> l = new ArrayList<>();
		iterable.forEach(l::add);
		return l;
	}

}
