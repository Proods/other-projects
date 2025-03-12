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
import eng2.videos.dto.UserDTO;
import eng2.videos.dto.VideoDTO;
import eng2.videos.events.HashtagsProducer;
import eng2.videos.events.UsersProducer;
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
public class UsersControllerTest {
	
	@Inject
	UsersRepository usersRepo;
	
	@Inject
	VideosRepository videosRepo;
	
	@Inject
	HashtagsRepository hashtagsRepo;
	
	@Inject
	UsersClient usersClient;
	
	@Inject
	VideosClient videosClient;
	
	@Inject
	HashtagsClient hashtagsClient;
	
	private final Map<Long,User> userTest = new HashMap<>();
	
	private final Map<Long,Video> videoTest = new HashMap<>();
		
	private final Map<Long,Hashtag> deleteHashtagTest = new HashMap<>();
		
	private final Map<Long,Integer> topHashtagTest = new HashMap<>();
	
	@MockBean(UsersProducer.class)
	UsersProducer testUsersProducer() {
		return new UsersProducer() {

			@Override
			public void createUser(Long id, User user) {
				userTest.put(id, user);
			}

			@Override
			public void deleteUser(Long id, User user) {
				userTest.put(id, user);				
			}
			
		};
	}
	
	@MockBean(VideosProducer.class)
	VideosProducer testVideosProducer() {
		return new VideosProducer() {

			@Override
			public void postVideo(User user, Video video) {
				videoTest.put(video.getId(), video); 
			}
			
			@Override
			public void updateVideo(User user, Video video) {
				videoTest.put(video.getId(), video);
			}

			@Override
			public void deleteVideo(User user, Video video) {
				videoTest.put(video.getId(), video);
			}

			@Override
			public void watchVideo(Video video, User user) {
			}

			@Override
			public void likeVideo(Video video, User user) {
			}

			@Override
			public void dislikeVideo(Video video, User user) {
			}

			@Override
			public void removeLike(Video video, User user) {
			}

			@Override
			public void removeDislike(Video video, User user) {
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
			public void deleteHashtag(Long id, Hashtag hashtag) {
				deleteHashtagTest.put(id, hashtag);
			}
			
			@Override
			public void topHashtags(Hashtag hashtag, Integer numOfLikes) {
				topHashtagTest.put(hashtag.getId(), numOfLikes);
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
		userTest.clear();
		videoTest.clear();
		deleteHashtagTest.clear();
		topHashtagTest.clear();
	}
	
	@Test
	public void noUser() {
		Iterable<User> iterUsers = usersClient.list();
		assertFalse(iterUsers.iterator().hasNext());
	}
	
	@Test
	public void createUser() {
		
		String name = "Harry";
		UserDTO user = new UserDTO();
		user.setName(name);
		
		HttpResponse<Void> response = usersClient.createUser(user);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		
		List<User> users = iterableToList(usersClient.list());
		assertEquals(1, users.size());
		assertEquals(name, users.get(0).getName());
		
		assertTrue(userTest.containsKey(users.get(0).getId()));
	}
	
	@Test
	public void getUser() {
		
		String name = "James";
		User user = new User();
		user.setName(name);
		usersRepo.save(user);
		
		User u = usersClient.getUser(user.getId());
		assertEquals(name, u.getName());
	}
	
	@Test
	public void getMissingUser() {
		User user = usersClient.getUser((long) 0);
		assertNull(user);
	}
	
	@Test
	public void updateUser() {
		
		User user = new User();
		user.setName("Amy");
		usersRepo.save(user);
		Long id = user.getId();
		
		String name = "Karina";
		UserDTO u = new UserDTO();
		u.setName(name);
		
		HttpResponse<String> response = usersClient.updateUser(id, u);
		assertEquals(HttpStatus.OK, response.getStatus());
				
		user = usersRepo.findById(id).get();
		assertEquals(name, user.getName());
	}
	
	@Test
	public void updateUserNotFound() {
		
		UserDTO u = new UserDTO();
		u.setName("Karina");
		
		HttpResponse<String> response = usersClient.updateUser((long) 0, u);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
	}
	
	@Test
	public void deleteUser() {
		
		User user = new User();
		user.setName("Lucas");
		usersRepo.save(user);
		Long id = user.getId();
		
		HttpResponse<String> response = usersClient.deleteUser(id);
		assertEquals(HttpStatus.OK, response.getStatus());
		
		assertTrue(userTest.containsKey(id));
		
		assertFalse(usersRepo.existsById(id));
	}
	
	@Test
	public void deleteUserNotFound() {
		
		HttpResponse<String> response = usersClient.deleteUser((long) 0);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
	}
	
	@Test
	public void createVideo() {
		
		String name = "Gary";
		User user = new User();
		user.setName(name);
		usersRepo.save(user);
		
		String title = "Hello_World";
		VideoDTO video = new VideoDTO();
		video.setTitle(title);
		
		HttpResponse<Void> response = usersClient.createVideo(user.getId(), video);
		assertEquals(HttpStatus.CREATED, response.getStatus());
				
		List<Video> videos = iterableToList(videosClient.list());
		assertEquals(1, videos.size());
		assertEquals(title, videos.get(0).getTitle());
		
		assertTrue(videoTest.containsKey(videos.get(0).getId()));
		
		Video v = videosRepo.findById(videos.get(0).getId()).get();
		assertEquals(name, v.getCreator().getName());
	}
	
	@Test
	public void createVideoNotFound() {
		
		String title = "Hello_World";
		VideoDTO video = new VideoDTO();
		video.setTitle(title);
		
		HttpResponse<Void> response = usersClient.createVideo((long) 0, video);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
	}
	
	@Test
	public void updateVideo() {
		
		User user = new User();
		user.setName("James");
		usersRepo.save(user);
		
		Video video = new Video();
		video.setCreator(user);
		video.setTitle("JAMES");
		videosRepo.save(video);
		
		String title = "Hello_World";
		VideoDTO v = new VideoDTO();
		v.setTitle(title);
		
		HttpResponse<String> response = usersClient.updateVideo(user.getId(), video.getId(), v);
		assertEquals(HttpStatus.OK, response.getStatus());
		
		assertTrue(videoTest.containsKey(video.getId()));
		
		video = videosRepo.findById(video.getId()).get();
		assertEquals(title, video.getTitle());
	}
	
	@Test
	public void updateVideoNotFound() {
		
		User user = new User();
		user.setName("James");
		usersRepo.save(user);
		
		String title = "JAMES";
		Video video = new Video();
		video.setCreator(user);
		video.setTitle(title);
		videosRepo.save(video);
		
		VideoDTO v = new VideoDTO();
		v.setTitle("Hello_World");
		
		HttpResponse<String> response = usersClient.updateVideo((long) 0, video.getId(), v);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		response = usersClient.updateVideo(user.getId(), (long) 0, v);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		video = videosRepo.findById(video.getId()).get();
		assertEquals(title, video.getTitle());
	}
	
	@Test
	public void updateVideoPermission() {
		
		User user = new User();
		user.setName("James");
		usersRepo.save(user);
		
		String title = "JAMES";
		Video video = new Video();
		video.setCreator(user);
		video.setTitle(title);
		videosRepo.save(video);
		
		VideoDTO v = new VideoDTO();
		v.setTitle("Hello_World");
		
		User userTwo = new User();
		userTwo.setName("John");
		usersRepo.save(userTwo);
		
		HttpResponse<String> response = usersClient.updateVideo(userTwo.getId(), video.getId(), v);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		video = videosRepo.findById(video.getId()).get();
		assertEquals(title, video.getTitle());
	}
	
	@Test
	public void deleteVideo() {
		
		User user = new User();
		user.setName("User");
		usersRepo.save(user);
		
		Video video = new Video();
		video.setCreator(user);
		video.setTitle("Video");
		videosRepo.save(video);
		
		HttpResponse<String> response = usersClient.deleteVideo(user.getId(), video.getId());
		
		assertTrue(videoTest.containsKey(video.getId()));
		
		assertEquals(HttpStatus.OK, response.getStatus());
		assertFalse(videosRepo.existsById(video.getId()));
	}
	
	@Test
	public void deleteVideoNotFound() {
		
		User user = new User();
		user.setName("User");
		usersRepo.save(user);
		
		Video video = new Video();
		video.setCreator(user);
		video.setTitle("Video");
		videosRepo.save(video);
		
		HttpResponse<String> response = usersClient.deleteVideo((long) 0, video.getId());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		response = usersClient.deleteVideo(user.getId(), (long) 0);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		assertTrue(videosRepo.existsById(video.getId()));
	}
	
	@Test
	public void deleteVideoPermission() {
		
		User user = new User();
		user.setName("User");
		usersRepo.save(user);
		
		Video video = new Video();
		video.setCreator(user);
		video.setTitle("Video");
		videosRepo.save(video);
		
		User userTwo = new User();
		userTwo.setName("User_2");
		usersRepo.save(userTwo);
		
		HttpResponse<String> response = usersClient.deleteVideo(userTwo.getId(), video.getId());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		assertTrue(videosRepo.existsById(video.getId()));
	}
	
	@Test
	public void getUploads() {
		
		User user = new User();
		user.setName("George");
		usersRepo.save(user);
		
		String title = "Title";
		Video video = new Video();
		video.setCreator(user);
		video.setTitle(title);
		videosRepo.save(video);
		
		List<Video> uploads = iterableToList(usersClient.getUploads(user.getId()));
		assertEquals(1, uploads.size());
		assertEquals(title, uploads.get(0).getTitle());
	}
	
	@Test
	public void getUploadsNotFound() {
		Iterable<Video> videos = usersClient.getUploads((long) 0);
		assertNull(videos);
	}
	
	@Test
	public void getViewedVideos() {
		
		User user = new User();
		user.setName("George");
		usersRepo.save(user);
		
		String title = "Title";
		Video video = new Video();
		video.setCreator(user);
		video.setTitle(title);
		videosRepo.save(video);
		
		videosClient.addView(video.getId(), user.getId());
		
		List<Video> viewedVids = iterableToList(usersClient.getViewedVideos(user.getId()));
		assertEquals(1, viewedVids.size());
		assertEquals(title, viewedVids.get(0).getTitle());
	}
	
	@Test
	public void getViewedVideosNotFound() {
		Iterable<Video> videos = usersClient.getViewedVideos((long) 0);
		assertNull(videos);
	}
	
	@Test
	public void getLikedVideos() {
		
		User user = new User();
		user.setName("George");
		usersRepo.save(user);
		
		String title = "Title";
		Video video = new Video();
		video.setCreator(user);
		video.setTitle(title);
		videosRepo.save(video);
		
		videosClient.addLike(video.getId(), user.getId());
		
		List<Video> likedVids = iterableToList(usersClient.getLikedVideos(user.getId()));
		assertEquals(1, likedVids.size());
		assertEquals(title, likedVids.get(0).getTitle());
		
		Iterable<Video> dislikedVids = usersClient.getDislikedVideos(user.getId());
		assertFalse(dislikedVids.iterator().hasNext());
		
		List<Video> viewedVids = iterableToList(usersClient.getViewedVideos(user.getId()));
		assertEquals(1, viewedVids.size());
		assertEquals(title, viewedVids.get(0).getTitle());
	}
	
	@Test
	public void getLikedVideosNotFound() {
		Iterable<Video> videos = usersClient.getLikedVideos((long) 0);
		assertNull(videos);
	}
	
	@Test
	public void getDislikedVideos() {
		
		User user = new User();
		user.setName("Georgia");
		usersRepo.save(user);
		
		String title = "Video";
		Video video = new Video();
		video.setCreator(user);
		video.setTitle(title);
		videosRepo.save(video);
		
		videosClient.addDislike(video.getId(), user.getId());
		
		List<Video> dislikedVids = iterableToList(usersClient.getDislikedVideos(user.getId()));
		assertEquals(1, dislikedVids.size());
		assertEquals(title, dislikedVids.get(0).getTitle());
		
		Iterable<Video> likedVids = usersClient.getLikedVideos(user.getId());
		assertFalse(likedVids.iterator().hasNext());
		
		List<Video> viewedVids = iterableToList(usersClient.getViewedVideos(user.getId()));
		assertEquals(1, viewedVids.size());
		assertEquals(title, viewedVids.get(0).getTitle());
	}
	
	@Test
	public void getDislikedVideosNotFound() {
		Iterable<Video> videos = usersClient.getDislikedVideos((long) 0);
		assertNull(videos);
	}
	
	@Test
	public void deleteUserCascade() {
		
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
		
		Long video_id = iterableToList(videosClient.list()).get(0).getId();
		
		assertTrue(videoTest.containsKey(video_id));
		videoTest.clear();
		
		Long hashId = iterableToList(hashtagsClient.list()).get(0).getId();
		
		HttpResponse<String> response = usersClient.deleteUser(id);
		assertEquals(HttpStatus.OK, response.getStatus());
		
		assertTrue(deleteHashtagTest.containsKey(hashId));
		assertTrue(topHashtagTest.containsKey(hashId));
		assertTrue(videoTest.containsKey(video_id));
		assertTrue(userTest.containsKey(id));
		
		assertFalse(usersRepo.existsById(id));
		
		Iterable<Video> videos = videosClient.list();
		assertFalse(videos.iterator().hasNext());
		
		Iterable<Hashtag> hts = hashtagsClient.list();
		assertFalse(hts.iterator().hasNext());
	}
	
	private <T> List<T> iterableToList(Iterable<T> iterable) {
		List<T> l = new ArrayList<>();
		iterable.forEach(l::add);
		return l;
	}

}
