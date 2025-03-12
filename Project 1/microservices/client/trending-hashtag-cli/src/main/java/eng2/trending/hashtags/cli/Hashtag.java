package eng2.trending.hashtags.cli;

import io.micronaut.serde.annotation.Serdeable;


@Serdeable
public class Hashtag {
	
	private Long id;
	private String name;
	private Integer numOfLikes;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumOfLikes() {
		return numOfLikes;
	}

	public void setNumOfLikes(Integer numOfLikes) {
		this.numOfLikes = numOfLikes;
	}

	@Override
	public String toString() {
		return "Hashtag [id=" + id + ", name=" + name + ", numOfLikes=" + numOfLikes + "]";
	}

}
