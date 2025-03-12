package eng2.videos.events;

import eng2.videos.domain.User;
import eng2.videos.domain.Video;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface VideosProducer {
	
	@Topic("post-video")
	void postVideo(@KafkaKey User user, Video video);
	
	@Topic("update-video")
	void updateVideo(@KafkaKey User user, Video video);
	
	@Topic("delete-video")
	void deleteVideo(@KafkaKey User user, Video video);
	
	@Topic("watch-video")
	void watchVideo(@KafkaKey Video video, User user);
	
	@Topic("like-video")
	void likeVideo(@KafkaKey Video video, User user);
	
	@Topic("dislike-video")
	void dislikeVideo(@KafkaKey Video video, User user);
	
	@Topic("remove-like")
	void removeLike(@KafkaKey Video video, User user);
	
	@Topic("remove-dislike")
	void removeDislike(@KafkaKey Video video, User user);

}
