package eng2.trending.hashtags.events;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;


@KafkaListener(groupId="trending-hashtags")
public class HashtagsConsumer {
	
	HashtagsRepository repo;


	@Topic("trending")
	public void trendingHashtag(@KafkaKey Hashtag hashtag, Integer numOfLikes) {

	}

	@Topic("delete-hashtag")
	public void deleteHashtag(@KafkaKey Long id, Hashtag hashtag) {

	}

}
