package eng2.videos.events;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;


@KafkaClient
public interface HashtagsProducer {
	
	@Topic("create-hashtag")
	public void createHashtag(@KafkaKey Long id, Hashtag hashtag);

	@Topic("delete-hashtag")
	public void deleteHashtag(@KafkaKey Long id, Hashtag hashtag);

	@Topic("top-liked-hashtags")
	public void topHashtags(@KafkaKey Hashtag hashtag, Integer numOfLikes);

	@Topic("add-hashtag-video")
	public void addHashtagVideo(@KafkaKey Hashtag hashtag, Video video);

	@Topic("remove-hashtag-video")
	public void removeHashtagVideo(@KafkaKey Hashtag hashtag, Video video);

}
