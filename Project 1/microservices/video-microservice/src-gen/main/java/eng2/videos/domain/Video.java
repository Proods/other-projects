package eng2.videos.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.micronaut.serde.annotation.Serdeable;


@Entity
@Serdeable
public class Video {
	
	@Id
	private Long id;

	private User creator;

	private String title;

	private Set<Hashtag> hashtags;

	private Set<User> likes;

	private User dislikes;

	private Set<User> viewers;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Hashtag> getHashtags() {
		return hashtags;
	}

	public void setHashtags(Set<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}

	public Set<User> getLikes() {
		return likes;
	}

	public void setLikes(Set<User> likes) {
		this.likes = likes;
	}

	public User getDislikes() {
		return dislikes;
	}

	public void setDislikes(User dislikes) {
		this.dislikes = dislikes;
	}

	public Set<User> getViewers() {
		return viewers;
	}

	public void setViewers(Set<User> viewers) {
		this.viewers = viewers;
	}

}
