package eng2.trending.hashtags.cli;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;


@Client("${hashtags.url:`http://localhost:8081/trending-hashtags`}")
public interface HashtagsClient {
	
	@Get("/")
	public Iterable<Hashtag> getTrendingHashtags();

}
