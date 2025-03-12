package eng2.subscriptions.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;


@Controller("/")
public class Controllers {
	
	private UsersRepository usersRepo;

	private HashtagsRepository hashtagsRepo;

	private SubscriptionsProducer producer;

	
	@Put("/subscribe-hashtag/{hashtag_id}/subscribers/{user_id}")
	public HttpResponse<String> subscribe(Hashtag hashtag_id, User user_id) {

	}

	@Put("/unsubscribe-hashtag/{hashtag_id}/subscribers/{user_id}")
	public HttpResponse<String> unsubscribe(Hashtag hashtag_id, User user_id) {

	}

	@Get("/users/{user_id}/hashtags/{hashtag_id}/recommendations")
	public Iterable<Video> recommend(Hashtag hashtag_id, User user_id) {

	}

	
	private <T> List<T> iterableToList(Iterable<T> iterable) {

	}

}
