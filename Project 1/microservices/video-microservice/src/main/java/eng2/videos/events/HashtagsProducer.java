package eng2.videos.events;

import eng2.videos.domain.Hashtag;
import eng2.videos.domain.Video;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;


@KafkaClient
public interface HashtagsProducer {
	
	@Topic("create-hashtag")
	void createHashtag(@KafkaKey Long id, Hashtag hashtag);
	
	@Topic("delete-hashtag")
	void deleteHashtag(@KafkaKey Long id, Hashtag hashtag);
	
	@Topic("top-liked-hashtags")
	void topHashtags(@KafkaKey Hashtag hashtag, Integer numOfLikes);
	
	@Topic("add-hashtag-video")
	void addHashtagVideo(@KafkaKey Hashtag hashtag, Video video);
	
	@Topic("remove-hashtag-video")
	void removeHashtagVideo(@KafkaKey Hashtag hashtag, Video video);
	
}
