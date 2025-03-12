package eng2.videos.dto;

import io.micronaut.serde.annotation.Serdeable;


@Serdeable
public class UserDTO {

	private String name;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
