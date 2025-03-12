package eng2.subscriptions.cli;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class Video {
	
	private Long id;
	private String title;
	private Integer likes;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	@Override
	public String toString() {
		return "Video [id=" + id + ", title=" + title + ", likes=" + likes + "]";
	}

}
