package eng2.trending.hashtags.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.micronaut.serde.annotation.Serdeable;


@Entity
@Serdeable
public class Hashtag {
	
	@Id
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

}
