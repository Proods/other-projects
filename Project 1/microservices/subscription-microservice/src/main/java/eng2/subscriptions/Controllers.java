package eng2.subscriptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import eng2.subscriptions.domain.Hashtag;
import eng2.subscriptions.domain.User;
import eng2.subscriptions.domain.Video;
import eng2.subscriptions.events.SubscriptionsProducer;
import eng2.subscriptions.repositories.HashtagsRepository;
import eng2.subscriptions.repositories.UsersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;

@Controller("/")
public class Controllers {
	
	@Inject
	UsersRepository usersRepo;
	
	@Inject
	HashtagsRepository hashtagsRepo;
	
	@Inject
	SubscriptionsProducer producer;
	
	
	@Put("/subscribe-hashtag/{hashtag_id}/subscribers/{user_id}")
	@Transactional
	public HttpResponse<String> subscribe(Long hashtag_id, Long user_id){
		
		Optional<User> user = usersRepo.findById(user_id);
		if (user.isEmpty()) {
			return HttpResponse.notFound(String.format("User %d not found!", user_id));
		}
		
		Optional<Hashtag> hashtag = hashtagsRepo.findById(hashtag_id);
		if (hashtag.isEmpty()) {
			return HttpResponse.notFound(String.format("Hashtag %d not found!", hashtag_id));
		}
		
		User u = user.get();
		Hashtag h = hashtag.get();
		
		if (u.getSubscriptions().contains(h)) {
			return HttpResponse.ok(String.format("User %d is already subscribed to Hashtag %d!", user_id, hashtag_id));
		}
		
		if (u.getSubscriptions().add(h)) {
			usersRepo.update(u);
			producer.subscribe(u, h);
		}
		
		return HttpResponse.ok(String.format("User %d has subscribed to Hashtag %d!", user_id, hashtag_id));
	}
	
	@Put("/unsubscribe-hashtag/{hashtag_id}/subscribers/{user_id}")
	@Transactional
	public HttpResponse<String> unsubscribe(Long hashtag_id, Long user_id){
		
		Optional<User> user = usersRepo.findById(user_id);
		if (user.isEmpty()) {
			return HttpResponse.notFound(String.format("User %d not found!", user_id));
		}
		
		Optional<Hashtag> hashtag = hashtagsRepo.findById(hashtag_id);
		if (hashtag.isEmpty()) {
			return HttpResponse.notFound(String.format("Hashtag %d not found!", hashtag_id));
		}
		
		User u = user.get();
		Hashtag h = hashtag.get();
		
		if (!u.getSubscriptions().contains(h)) {
			return HttpResponse.ok(String.format("User %d is not subscribed to Hashtag %d!", user_id, hashtag_id));
		}
		
		if (u.getSubscriptions().remove(h) && h.getSubscribers().remove(u)) {
			usersRepo.update(u);
			hashtagsRepo.update(h);
			producer.unsubscribe(u, h);
		}
		
		return HttpResponse.ok(String.format("User %d has unsubscribed to Hashtag %d!", user_id, hashtag_id));
	}
	
	@Get("/users/{user_id}/hashtags/{hashtag_id}/recommendations")
	@Transactional
	public Iterable<Video> recommend(Long hashtag_id, Long user_id){
		
		Optional<User> user = usersRepo.findById(user_id);
		Optional<Hashtag> hashtag = hashtagsRepo.findById(hashtag_id);
		
		if (user.isEmpty() || hashtag.isEmpty()) {
			return null;
		}
		
		User u = user.get();
		Hashtag h = hashtag.get();
		
		if (!u.getSubscriptions().contains(h)) {
			return null;
		}
		
		List<Video> recommendations = iterableToList(h.getVideos());
		
		Collections.sort(recommendations, (a,b) -> {
			return b.getLikes().compareTo(a.getLikes());
		});
		
		if (recommendations.size() > 10) {
			return recommendations.subList(0, 10);
		}
		
		return recommendations;
	}
	
	private <T> List<T> iterableToList(Iterable<T> iterable) {
		List<T> l = new ArrayList<>();
		iterable.forEach(l::add);
		return l;
	}
	
}
