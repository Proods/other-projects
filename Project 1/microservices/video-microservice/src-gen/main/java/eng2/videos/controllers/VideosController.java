package eng2.videos.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;


@Controller("/videos")
public class VideosController {
	
	private VideosRepository videosRepo;

	private UsersRepository usersRepo;

	private VideosProducer videosProducer;

	private HashtagsProducer hashtagsProducer;

	
	@Get("/")
	public Iterable<Video> list() {

	}

	@Get("/{id}")
	public Video getVideo(Long id) {

	}

	@Get("/{id}/viewers")
	public Iterable<User> getViewers(Long id) {

	}

	@Get("/{id}/likes")
	public Iterable<User> getLikes(Long id) {

	}

	@Get("/{id}/dislikes")
	public Iterable<User> getDislikes(Long id) {

	}

	@Put("/{video_id}/viewers/{viewer_id}")
	public HttpResponse<String> addView(Long video_id, Long viewer_id) {

	}

	@Put("/{video_id}/likes/{viewer_id}")
	public HttpResponse<String> addLike(Long video_id, Long viewer_id) {

	}

	@Put("/{video_id}/dislikes/{viewer_id}")
	public HttpResponse<String> addDislike(Long video_id, Long viewer_id) {

	}

	@Delete("/{video_id}/likes/{viewer_id}")
	public HttpResponse<String> removeLike(Long video_id, Long viewer_id) {

	}

	@Delete("/{video_id}/dislikes/{viewer_id}")
	public HttpResponse<String> removeDislike(Long video_id, Long viewer_id) {

	}

	
}
