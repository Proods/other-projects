package eng2.subscriptions;

import eng2.subscriptions.domain.Video;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;

@Client("/")
public interface SubscriptionsClient {
	
	@Put("/subscribe-hashtag/{hashtag_id}/subscribers/{user_id}")
	public HttpResponse<String> subscribe(Long hashtag_id, Long user_id);
	
	@Put("/unsubscribe-hashtag/{hashtag_id}/subscribers/{user_id}")
	public HttpResponse<String> unsubscribe(Long hashtag_id, Long user_id);
	
	@Get("/users/{user_id}/hashtags/{hashtag_id}/recommendations")
	public Iterable<Video> recommend(Long hashtag_id, Long user_id);

}
