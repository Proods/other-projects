package eng2.videos.controllers;

import java.util.Optional;

import javax.transaction.Transactional;

import eng2.videos.domain.Hashtag;
import eng2.videos.domain.User;
import eng2.videos.domain.Video;
import eng2.videos.events.HashtagsProducer;
import eng2.videos.events.VideosProducer;
//import eng2.videos.events.VideosProducer;
import eng2.videos.repositories.UsersRepository;
import eng2.videos.repositories.VideosRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;

@Controller("/videos")
public class VideosController {
	
	@Inject
	VideosRepository videosRepo;
	
	@Inject
	UsersRepository usersRepo;
	
	@Inject
	VideosProducer videosProducer;
	
	@Inject
	HashtagsProducer hashtagsProducer;
	
	
	@Get("/")
	public Iterable<Video> list(){
		return videosRepo.findAll();
	}
	
	@Get("/{id}")
	public Video getVideo(Long id) {
		return videosRepo.findById(id).orElse(null);
	}
	
	@Get("/{id}/viewers")
	public Iterable<User> getViewers(Long id){
		Optional<Video> video = videosRepo.findById(id);
		if (video.isEmpty()) {
			return null;
		}
		return video.get().getViewers();
	}
	
	@Get("/{id}/likes")
	public Iterable<User> getLikes(Long id){
		Optional<Video> video = videosRepo.findById(id);
		if (video.isEmpty()) {
			return null;
		}
		return video.get().getLikes();
	}
	
	@Get("/{id}/dislikes")
	public Iterable<User> getDislikes(Long id){
		Optional<Video> video = videosRepo.findById(id);
		if (video.isEmpty()) {
			return null;
		}
		return video.get().getDislikes();
	}
		
	@Put("/{video_id}/viewers/{viewer_id}")
	@Transactional
	public HttpResponse<String> addView(Long video_id, Long viewer_id){
		
		Optional<Video> video = videosRepo.findById(video_id);
		if (video.isEmpty()) {
			return HttpResponse.notFound(String.format("Video %d not found!", video_id));
		}

		Optional<User> user = usersRepo.findById(viewer_id);
		if (user.isEmpty()) {
			return HttpResponse.notFound(String.format("User %d not found!", viewer_id));
		}
		
		User u = user.get();
		Video v = video.get();
		
		if (v.getViewers().add(u)) {
			videosRepo.update(v);
			videosProducer.watchVideo(v,u);
		}		
		return HttpResponse.ok(String.format("User %d has viewed Video %d", viewer_id, video_id));
	}
	
	@Put("/{video_id}/likes/{viewer_id}")
	@Transactional
	public HttpResponse<String> addLike(Long video_id, Long viewer_id){
		
		Optional<Video> video = videosRepo.findById(video_id);
		if (video.isEmpty()) {
			return HttpResponse.notFound(String.format("Video %d not found!", video_id));
		}
		
		Optional<User> user = usersRepo.findById(viewer_id);
		if (user.isEmpty()) {
			return HttpResponse.notFound(String.format("User %d not found!", viewer_id));
		}
		
		User u = user.get();
		Video v = video.get();	
		
		if (!v.getViewers().contains(u)) {
			v.getViewers().add(u);
			videosProducer.watchVideo(v,u);
		}
		
		if (v.getDislikes().contains(u)) {
			v.getDislikes().remove(u);
			videosProducer.removeDislike(v, u);
		}
		
		if (v.getLikes().add(u)) {
			videosRepo.update(v);
			videosProducer.likeVideo(v,u);
			for (Hashtag h : v.getHashtags()) {
				hashtagsProducer.topHashtags(h, 1);
			}
		}
		
		return HttpResponse.ok(String.format("User %d has liked Video %d", viewer_id, video_id));
	}
	
	@Put("/{video_id}/dislikes/{viewer_id}")
	@Transactional
	public HttpResponse<String> addDislike(Long video_id, Long viewer_id){
		
		Optional<Video> video = videosRepo.findById(video_id);
		if (video.isEmpty()) {
			return HttpResponse.notFound(String.format("Video %d not found!", video_id));
		}
		
		Optional<User> user = usersRepo.findById(viewer_id);
		if (user.isEmpty()) {
			return HttpResponse.notFound(String.format("User %d not found!", viewer_id));
		}
		
		User u = user.get();
		Video v = video.get();		
		
		if (!v.getViewers().contains(u)) {
			v.getViewers().add(u);
			videosProducer.watchVideo(v,u);
		}
		
		if (v.getLikes().contains(u)) {
			v.getLikes().remove(u);
			videosProducer.removeLike(v, u);
			for (Hashtag h : v.getHashtags()) {
				hashtagsProducer.topHashtags(h, -1);
			}
		}
		
		if (v.getDislikes().add(u)) {
			videosRepo.update(v);
			videosProducer.dislikeVideo(v,u);
		}
		
		return HttpResponse.ok(String.format("User %d has disliked Video %d", viewer_id, video_id));
	}
	
	@Delete("/{video_id}/likes/{viewer_id}")
	@Transactional
	public HttpResponse<String> removeLike(Long video_id, Long viewer_id){
		
		Optional<Video> video = videosRepo.findById(video_id);
		if (video.isEmpty()) {
			return HttpResponse.notFound(String.format("Video %d not found!", video_id));
		}
		
		Optional<User> user = usersRepo.findById(viewer_id);
		if (user.isEmpty()) {
			return HttpResponse.notFound(String.format("User %d not found!", viewer_id));
		}
		
		User u = user.get();
		Video v = video.get();
		
		if (v.getLikes().remove(u)) {
			videosRepo.update(v);
			videosProducer.removeLike(v,u);
			for (Hashtag h : v.getHashtags()) {
				hashtagsProducer.topHashtags(h, -1);
			}
		}
		
		return HttpResponse.ok(String.format("User %d has undoed like from Video %d", viewer_id, video_id));
	}
	
	@Delete("/{video_id}/dislikes/{viewer_id}")
	@Transactional
	public HttpResponse<String> removeDislike(Long video_id, Long viewer_id){
		
		Optional<Video> video = videosRepo.findById(video_id);
		if (video.isEmpty()) {
			return HttpResponse.notFound(String.format("Video %d not found!", video_id));
		}
		
		Optional<User> user = usersRepo.findById(viewer_id);
		if (user.isEmpty()) {
			return HttpResponse.notFound(String.format("User %d not found!", viewer_id));
		}
		
		User u = user.get();
		Video v = video.get();
		
		if (v.getDislikes().remove(u)) {
			videosRepo.update(v);
			videosProducer.removeDislike(v,u);
		}
		
		return HttpResponse.ok(String.format("User %d has undoed dislike from Video %d", viewer_id, video_id));
	}

}
