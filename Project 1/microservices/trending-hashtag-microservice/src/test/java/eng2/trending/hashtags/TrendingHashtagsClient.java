package eng2.trending.hashtags;

import eng2.trending.hashtags.domain.Hashtag;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;


@Client("/trending-hashtags")
public interface TrendingHashtagsClient {
	
	@Get("/")
	public Iterable<Hashtag> getTrendingHashtags();

}
