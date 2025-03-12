package eng2.subscriptions.events;

import javax.transaction.Transactional;

import eng2.subscriptions.domain.Hashtag;
import eng2.subscriptions.domain.User;
import eng2.subscriptions.domain.Video;
import eng2.subscriptions.repositories.HashtagsRepository;
import eng2.subscriptions.repositories.UsersRepository;
import eng2.subscriptions.repositories.VideosRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

@KafkaListener(groupId="factory")
public class FactoryConsumer {
	
	@Inject
	UsersRepository usersRepo;
	
	@Inject
	VideosRepository videosRepo;
	
	@Inject
	HashtagsRepository hashtagsRepo;
	
	
	@Topic("create-user")
	public void createUser(@KafkaKey Long id, User user) {
		usersRepo.save(user);
	}
	
	@Topic("delete-user")
	@Transactional
	public void deleteUser(@KafkaKey Long id, User user) {
		User u = usersRepo.findById(id).get();
		
		for (Hashtag h : u.getSubscriptions()) {
			h.getSubscribers().remove(u);
			hashtagsRepo.update(h);
		}
				
		usersRepo.deleteById(id);
	}
	
	@Topic("post-video")
	public void createVideo(@KafkaKey User user, Video video) {
		videosRepo.save(video);
	}
	
	@Topic("update-video")
	@Transactional
	public void updateVideo(@KafkaKey User user, Video video) {
		Video v = videosRepo.findById(video.getId()).get();
		v.setTitle(video.getTitle());
		videosRepo.update(v);
	}
	
	@Topic("delete-video")
	@Transactional
	public void deleteVideo(@KafkaKey User user, Video video) {
		
		Video v = videosRepo.findById(video.getId()).get();
		
		for (Hashtag h : v.getHashtags()) {
			h.getVideos().remove(v);
			hashtagsRepo.update(h);
		}
		
		videosRepo.delete(v);
	}
	
	@Topic("create-hashtag")
	public void createHashtag(@KafkaKey Long id, Hashtag hashtag) {
		hashtagsRepo.save(hashtag);
	}
	
	@Topic("delete-hashtag")
	@Transactional
	public void deleteHashtag(@KafkaKey Long id, Hashtag hashtag) {
		
		Hashtag h = hashtagsRepo.findById(id).get();
		
		for (User u : h.getSubscribers()) {
			u.getSubscriptions().remove(h);
			usersRepo.update(u);
		}
		
		for (Video v : h.getVideos()) {
			v.getHashtags().remove(h);
			videosRepo.update(v);
		}
		
		hashtagsRepo.delete(h);
	}

}
