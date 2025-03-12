package eng2.videos.events;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;


@KafkaClient
public interface VideosProducer {
	
	@Topic("post-video")
	public void postVideo(@KafkaKey User user, Video video);

	@Topic("update-video")
	public void updateVideo(@KafkaKey User user, Video video);

	@Topic("delete-video")
	public void deleteVideo(@KafkaKey User user, Video video);

	@Topic("watch-video")
	public void watchVideo(@KafkaKey Video video, User user);

	@Topic("like-video")
	public void likeVideo(@KafkaKey Video video, User user);

	@Topic("dislike-video")
	public void dislikeVideo(@KafkaKey Video video, User user);

	@Topic("remove-like")
	public void removeLike(@KafkaKey Video video, User user);

	@Topic("remove-dislike")
	public void removeDislike(@KafkaKey Video video, User user);

}
