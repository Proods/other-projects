package eng2.videos.controllers;

import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import eng2.videos.domain.Hashtag;
import eng2.videos.domain.User;
import eng2.videos.domain.Video;
import eng2.videos.dto.UserDTO;
import eng2.videos.dto.VideoDTO;
import eng2.videos.events.HashtagsProducer;
import eng2.videos.events.UsersProducer;
import eng2.videos.events.VideosProducer;
import eng2.videos.repositories.HashtagsRepository;
import eng2.videos.repositories.UsersRepository;
import eng2.videos.repositories.VideosRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;

@Controller("/users")
public class UsersController {
	
	@Inject
	UsersRepository usersRepo;
	
	@Inject
	VideosRepository videosRepo;
	
	@Inject
	HashtagsRepository hashtagRepo;
	
	@Inject
	UsersProducer usersProducer;
	
	@Inject
	VideosProducer videosProducer;
	
	@Inject
	HashtagsProducer hashtagsProducer;
	
	
	@Get("/")
	public Iterable<User> list(){
		return usersRepo.findAll();
	}
	
	@Post("/")
	public HttpResponse<Void> createUser(@Body UserDTO userDetails){
		User user = new User();
		user.setName(userDetails.getName());
		if (usersRepo.save(user)!=null) {
			usersProducer.createUser(user.getId(), user);
		}
		return HttpResponse.created(URI.create("/users/" + user.getId()));
	}
	
	@Get("/{id}")
	public User getUser(Long id) {
		return usersRepo.findById(id).orElse(null);
	}
	
	@Put("/{id}")
	@Transactional
	public HttpResponse<String> updateUser(Long id, @Body UserDTO userDetails) {
		
		Optional<User> user = usersRepo.findById(id);
		if (user.isEmpty()) {
			return HttpResponse.notFound(String.format("User %d not found!", id));
		}
		
		User u = user.get();
		if (userDetails.getName()!=null) {
			u.setName(userDetails.getName());
		}
		usersRepo.update(u);
		return HttpResponse.ok(String.format("User %d has been updated!", id));
	}
	
	@Delete("/{id}")
	@Transactional
	public HttpResponse<String> deleteUser(Long id){
		
		Optional<User> user = usersRepo.findById(id);
		if (user.isEmpty()) {
			return HttpResponse.notFound(String.format("User %d not found!", id));
		}
		
		User u = user.get();
		
		for (Video v: u.getUploads()) {
			v.setCreator(null);
			for (Hashtag h: v.getHashtags()) {
				h.getVideos().remove(v);
				hashtagsProducer.topHashtags(h, v.getLikes().size() * -1);
				hashtagRepo.update(h);
				if (h.getVideos()==null || h.getVideos().isEmpty()) {
					hashtagsProducer.deleteHashtag(h.getId(), h);
					hashtagRepo.delete(h);
				}
			}
			videosProducer.deleteVideo(u, v);
		}
		
		for (Video v: u.getLikedVids()) {
			v.getLikes().remove(u);
		}
		
		for (Video v: u.getDislikedVids()) {
			v.getDislikes().remove(u);
		}
		
		for (Video v: u.getViews()) {
			v.getViewers().remove(u);
		}
		
		usersRepo.delete(u);
		usersProducer.deleteUser(u.getId(), u);
		return HttpResponse.ok(String.format("User %d has been deleted!", id));
	}
	
	@Get("/{id}/uploads")
	public Iterable<Video> getUploads(Long id){
		Optional<User> user = usersRepo.findById(id);
		if (user.isEmpty()) {
			return null;
		}
		return user.get().getUploads();
	}
	
	@Get("/{id}/views")
	public Iterable<Video> getViewedVideos(Long id){
		Optional<User> user = usersRepo.findById(id);
		if (user.isEmpty()) {
			return null;
		}
		return user.get().getViews();
	}
	
	@Get("/{id}/likes")
	public Iterable<Video> getLikedVideos(Long id){
		Optional<User> user = usersRepo.findById(id);
		if (user.isEmpty()) {
			return null;
		}
		return user.get().getLikedVids();
	}
	
	@Get("/{id}/dislikes")
	public Iterable<Video> getDislikedVideos(Long id){
		Optional<User> user = usersRepo.findById(id);
		if (user.isEmpty()) {
			return null;
		}
		return user.get().getDislikedVids();
	}
	
	@Post("/{id}/videos")
	@Transactional
	public HttpResponse<Void> createVideo(Long id, @Body VideoDTO videoDetails){
		
		Optional<User> user = usersRepo.findById(id);
		if (user.isEmpty()) {
			return HttpResponse.notFound();
		}
		
		User u = user.get();
		Video video = new Video();
		
		video.setCreator(u);
		video.setTitle(videoDetails.getTitle());
		
		if (videosRepo.save(video)!=null) {
			videosProducer.postVideo(u, video);
		}
		
		if (videoDetails.getHashtags()!=null) {
			Set<Hashtag> hashtags = new HashSet<>();
			for (String h : videoDetails.getHashtags()) {
				if (!checkIfHashtagExists(h)) {
					Hashtag hashtag = new Hashtag();
					hashtag.setName(h);
					if (hashtagRepo.save(hashtag)!=null) {
						hashtagsProducer.createHashtag(hashtag.getId(), hashtag);
					}
					if (hashtags.add(hashtag)) {
						hashtagsProducer.addHashtagVideo(hashtag, video);
					}
				} else {
					Hashtag temp = findHashtagByName(h);
					if (hashtags.add(temp)) {
						hashtagsProducer.addHashtagVideo(temp, video);
					}
				}
			}
			video.setHashtags(hashtags);
		}
				
		videosRepo.update(video);
		
		return HttpResponse.created(URI.create("/users/" + id + "/videos/" + video.getId()));
	}
	
	@Put("/{creator_id}/videos/{video_id}")
	@Transactional
	public HttpResponse<String> updateVideo(Long creator_id, Long video_id, @Body VideoDTO videoDetails){
		
		Optional<User> user = usersRepo.findById(creator_id);
		if (user.isEmpty()) {
			return HttpResponse.notFound(String.format("User %d not found!", creator_id));
		}
		
		Optional<Video> video = videosRepo.findById(video_id);
		if (video.isEmpty()) {
			return HttpResponse.notFound(String.format("Video %d not found!", video_id));
		}
		
		Video v = video.get();
		if (!v.getCreator().equals(user.get())) {
			return HttpResponse.notFound(String.format("User %d has no permission to update Video %d", creator_id , video_id));
		}
		
		if (videoDetails.getTitle()!=null) {
			v.setTitle(videoDetails.getTitle());
		}
		
		if (videoDetails.getHashtags()!=null) {
			Set<String> hashtags = new HashSet<>();
			
			for (Hashtag h : v.getHashtags()) {
				hashtags.add(h.getName());
				if (!videoDetails.getHashtags().contains(h.getName())) {
					v.getHashtags().remove(h);
					videosRepo.update(v);
					if (h.getVideos().remove(v)) {
						hashtagsProducer.removeHashtagVideo(h, v);
					}
					hashtagsProducer.topHashtags(h, v.getLikes().size() * -1);
					hashtagRepo.update(h);
					if (h.getVideos()==null || h.getVideos().isEmpty()) {
						hashtagsProducer.deleteHashtag(h.getId(), h);
						hashtagRepo.delete(h);
					}
				}
			}
	
			for (String h : videoDetails.getHashtags()) {
				if (!hashtags.contains(h)) {
					if (!checkIfHashtagExists(h)) {
						Hashtag hashtag = new Hashtag();
						hashtag.setName(h);
						if (hashtagRepo.save(hashtag)!=null) {
							hashtagsProducer.createHashtag(hashtag.getId(), hashtag);
						}
						if (v.getHashtags().add(hashtag)) {
							hashtagsProducer.addHashtagVideo(hashtag, v);
						}
					} else {
						Hashtag temp = findHashtagByName(h);
						if (v.getHashtags().add(temp)) {
							hashtagsProducer.addHashtagVideo(temp, v);
						};
					}
				}
			}
			
		} else {
			v.setHashtags(null);
		}
		
		if (videosRepo.update(v)!=null) {
			videosProducer.updateVideo(user.get(), v);
		}
		
		return HttpResponse.ok(String.format("Video %d has been updated!", video_id));
	}
	
	@Delete("/{creator_id}/videos/{video_id}")
	@Transactional
	public HttpResponse<String> deleteVideo(Long creator_id, Long video_id){
		
		Optional<User> user = usersRepo.findById(creator_id);
		if (user.isEmpty()) {
			return HttpResponse.notFound(String.format("User %d not found!", creator_id));
		}
		
		Optional<Video> video = videosRepo.findById(video_id);
		if (video.isEmpty()) {
			return HttpResponse.notFound(String.format("Video %d not found!", video_id));
		}
		
		Video v = video.get();
		if (!v.getCreator().equals(user.get())) {
			return HttpResponse.notFound(String.format("User %d has no permission to delete Video %d", creator_id , video_id));
		}
		
		User u = user.get();
		u.getUploads().remove(v);
		u.getLikedVids().remove(v);
		u.getDislikedVids().remove(v);
		u.getViews().remove(v);
		
		for (Hashtag h: v.getHashtags()) {
			v.getHashtags().remove(h);
			videosRepo.update(v);
			h.getVideos().remove(v);
			hashtagsProducer.topHashtags(h, v.getLikes().size() * -1);
			hashtagRepo.update(h);
			if (h.getVideos()==null || h.getVideos().isEmpty()) {
				hashtagsProducer.deleteHashtag(h.getId(), h);
				hashtagRepo.delete(h);
			}
		}
		
		videosRepo.delete(v);
		videosProducer.deleteVideo(u, v);
		return HttpResponse.ok(String.format("Video %d has been deleted!", video_id));
	}
	
	private Hashtag findHashtagByName(String hashtag) {
		for (Hashtag h : hashtagRepo.findAll()) {
			if (hashtag.equals(h.getName())) {
				return h;
			}
		}
		return null;
	}

	private boolean checkIfHashtagExists(String hashtag) {
		for (Hashtag h : hashtagRepo.findAll()) {
			if (hashtag.equals(h.getName())) {
				return true;
			}
		}
		return false;
	}
	
}
