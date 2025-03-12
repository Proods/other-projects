package eng2.subscriptions.events;

import javax.transaction.Transactional;

import eng2.subscriptions.domain.Hashtag;
import eng2.subscriptions.domain.User;
import eng2.subscriptions.domain.Video;
import eng2.subscriptions.repositories.HashtagsRepository;
import eng2.subscriptions.repositories.VideosRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

@KafkaListener(groupId="subscription")
public class SubscriptionConsumer {
	
	@Inject
	VideosRepository videosRepo;
	
	@Inject
	HashtagsRepository hashtagsRepo;
	
	
	@Topic("like-video")
	@Transactional
	public void likeVideo(@KafkaKey Video video, User user) {
		Video v = videosRepo.findById(video.getId()).get();
		v.setLikes(v.getLikes() + 1);
		videosRepo.update(v);
	}
	
	@Topic("remove-like")
	@Transactional
	public void removeLike(@KafkaKey Video video, User user) {
		Video v = videosRepo.findById(video.getId()).get();
		v.setLikes(v.getLikes() - 1);
		videosRepo.update(v);
	}
	
	@Topic("add-hashtag-video")
	@Transactional
	public void addHashtagVideo(@KafkaKey Hashtag hashtag, Video video) {
		
		Video v = videosRepo.findById(video.getId()).get();
		Hashtag h = hashtagsRepo.findById(hashtag.getId()).get();
		
		h.getVideos().add(v);
		hashtagsRepo.update(h);
	}
	
	@Topic("remove-hashtag-video")
	@Transactional
	public void removeHashtagVideo(@KafkaKey Hashtag hashtag, Video video) {
		
		Video v = videosRepo.findById(video.getId()).get();
		Hashtag h = hashtagsRepo.findById(hashtag.getId()).get();
		
		h.getVideos().remove(v);
		hashtagsRepo.update(h);
		v.getHashtags().remove(h);
		videosRepo.update(v);
	}
	
}
