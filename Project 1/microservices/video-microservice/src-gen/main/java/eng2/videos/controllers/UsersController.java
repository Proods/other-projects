package eng2.videos.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;


@Controller("/users")
public class UsersController {
	
	private UsersRepository usersRepo;

	private VideosRepository videosRepo;

	private HashtagsRepository hashtagRepo;

	private UsersProducer usersProducer;

	private VideosProducer videosProducer;

	private HashtagsProducer hashtagsProducer;

	
	@Get("/")
	public Iterable<User> list() {

	}

	@Post("/")
	public HttpResponse<Void> createUser(UserDTO userDetails) {

	}

	@Get("/{id}")
	public User getUser(Long id) {

	}

	@Put("/{id}")
	public HttpResponse<String> updateUser(Long id, UserDTO userDetails) {

	}

	@Delete("/{id}")
	public HttpResponse<String> deleteUser(Long id) {

	}

	@Get("/{id}/uploads")
	public Iterable<Video> getUploads(Long id) {

	}

	@Get("/{id}/views")
	public Iterable<Video> getViewedVideos(Long id) {

	}

	@Get("/{id}/likes")
	public Iterable<Video> getLikedVideos(Long id) {

	}

	@Get("/{id}/dislikes")
	public Iterable<Video> getDislikedVideos(Long id) {

	}

	@Post("/{id}/videos")
	public HttpResponse<Void> createVideo(Long id, VideoDTO videoDetails) {

	}

	@Put("/{creator_id}/videos/{video_id}")
	public HttpResponse<String> updateVideo(Long creator_id, Long video_id, VideoDTO videoDetails) {

	}

	@Delete("/{creator_id}/videos/{video_id}")
	public HttpResponse<String> deleteVideo(Long creator_id, Long video_id) {

	}

	
	private Hashtag findHashtagByName(String hashtag) {

	}

	private boolean checkIfHashtagExists(String hashtag) {

	}

}
