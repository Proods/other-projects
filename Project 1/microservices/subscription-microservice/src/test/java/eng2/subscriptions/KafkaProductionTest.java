package eng2.subscriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eng2.subscriptions.domain.Hashtag;
import eng2.subscriptions.domain.User;
import eng2.subscriptions.domain.Video;
import eng2.subscriptions.events.FactoryConsumer;
import eng2.subscriptions.events.SubscriptionConsumer;
import eng2.subscriptions.repositories.HashtagsRepository;
import eng2.subscriptions.repositories.UsersRepository;
import eng2.subscriptions.repositories.VideosRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;


@MicronautTest(transactional = false, environments = "no_streams")
public class KafkaProductionTest {
	
	@Inject
	UsersRepository usersRepo;
	
	@Inject
	VideosRepository videosRepo;
	
	@Inject
	HashtagsRepository hashtagsRepo;
	
	@Inject
	FactoryConsumer factoryConsumer;
	
	@Inject
	SubscriptionConsumer subscriptionConsumer;
	
	
	@BeforeEach
	public void clean() {
		usersRepo.deleteAll();
		videosRepo.deleteAll();
		hashtagsRepo.deleteAll();
	}
	
	@Test
	public void createAndDeleteUserTest() {
		
		User user = new User();
		user.setId((long) 1);
		
		factoryConsumer.createUser(user.getId(), user);
		assertTrue(usersRepo.existsById(user.getId()));
		user = usersRepo.findById(user.getId()).get();
		
		Hashtag hashtag = new Hashtag();
		hashtag.setId((long) 3);
		hashtag.setName("HASHTAG");
		hashtagsRepo.save(hashtag);
		hashtag = hashtagsRepo.findById(hashtag.getId()).get();
		
		user.getSubscriptions().add(hashtag);
		usersRepo.update(user);
		
		factoryConsumer.deleteUser(user.getId(), user);
		assertFalse(usersRepo.existsById(user.getId()));
	}
	
	@Test
	public void createAndDeleteVideoTest() {
		
		User user = new User();
		user.setId((long) 1);
		
		Video video = new Video();
		video.setId((long) 2);
		video.setTitle("TITLE");
		
		factoryConsumer.createVideo(user, video);
		assertTrue(videosRepo.existsById(video.getId()));
		video = videosRepo.findById(video.getId()).get();
		
		Hashtag hashtag = new Hashtag();
		hashtag.setId((long) 3);
		hashtag.setName("HASHTAG");
		hashtagsRepo.save(hashtag);
		hashtag = hashtagsRepo.findById(hashtag.getId()).get();
		
		hashtag.getVideos().add(video);
		hashtagsRepo.update(hashtag);
		
		factoryConsumer.deleteVideo(user, video);
		assertFalse(videosRepo.existsById(video.getId()));
	}
	
	@Test
	public void updateVideoTest() {
		
		User user = new User();
		user.setId((long) 1);
		
		Video video = new Video();
		video.setId((long) 2);
		video.setTitle("TITLE");
		videosRepo.save(video);
		
		Video temp = new Video();
		temp.setId(video.getId());
		temp.setTitle("UPDATED");
		
		factoryConsumer.updateVideo(user, temp);
		video = videosRepo.findById(video.getId()).get();
		assertEquals(temp.getTitle(), video.getTitle());
	}
	
	@Test
	public void createAndDeleteHashtagTest() {
		
		Hashtag hashtag = new Hashtag();
		hashtag.setId((long) 3);
		hashtag.setName("HASHTAG");
		
		factoryConsumer.createHashtag(hashtag.getId(), hashtag);
		assertTrue(hashtagsRepo.existsById(hashtag.getId()));
		hashtag = hashtagsRepo.findById(hashtag.getId()).get();
		
		User user = new User();
		user.setId((long) 1);
		usersRepo.save(user);
		user = usersRepo.findById(user.getId()).get();
		
		Video video = new Video();
		video.setId((long) 2);
		video.setTitle("TITLE");
		videosRepo.save(video);
		video = videosRepo.findById(video.getId()).get();
		
		user.getSubscriptions().add(hashtag);
		usersRepo.update(user);
		hashtag.getVideos().add(video);
		hashtagsRepo.update(hashtag);
		
		factoryConsumer.deleteHashtag(hashtag.getId(), hashtag);
		assertFalse(hashtagsRepo.existsById(hashtag.getId()));
	}
	
	@Test
	public void likeAndRemoveLikeVideoTest() {
		
		User user = new User();
		user.setId((long) 1);
		
		Integer likes = 5;
		Video video = new Video();
		video.setId((long) 2);
		video.setTitle("TITLE");
		video.setLikes(likes);
		videosRepo.save(video);
		
		Video temp = new Video();
		temp.setId(video.getId());
		temp.setTitle("TEMP");
		
		subscriptionConsumer.likeVideo(temp, user);
		video = videosRepo.findById(video.getId()).get();
		assertEquals(likes+1, video.getLikes());
		
		subscriptionConsumer.removeLike(temp, user);
		video = videosRepo.findById(video.getId()).get();
		assertEquals(likes, video.getLikes());
	}
	
	@Test
	public void addAndRemoveHashtagVideoTest() {
		
		Video video = new Video();
		video.setId((long) 2);
		video.setTitle("TITLE");
		videosRepo.save(video);
		
		Hashtag hashtag = new Hashtag();
		hashtag.setId((long) 3);
		hashtag.setName("HASHTAG");
		hashtagsRepo.save(hashtag);
		
		subscriptionConsumer.addHashtagVideo(hashtag, video);
		video = videosRepo.findById(video.getId()).get();
		hashtag = hashtagsRepo.findById(hashtag.getId()).get();
		
		List<Hashtag> hashtags = iterableToList(video.getHashtags());
		List<Video> videos = iterableToList(hashtag.getVideos());
		
		assertEquals(hashtag.getId(), hashtags.get(0).getId());
		assertEquals(video.getId(), videos.get(0).getId());
		
		subscriptionConsumer.removeHashtagVideo(hashtag, video);
		video = videosRepo.findById(video.getId()).get();
		hashtag = hashtagsRepo.findById(hashtag.getId()).get();
		
		assertFalse(video.getHashtags().iterator().hasNext());
		assertFalse(hashtag.getVideos().iterator().hasNext());
	}
	
	private <T> List<T> iterableToList(Iterable<T> iterable) {
		List<T> l = new ArrayList<>();
		iterable.forEach(l::add);
		return l;
	}

}
