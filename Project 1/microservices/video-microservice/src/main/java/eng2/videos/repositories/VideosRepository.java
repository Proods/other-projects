package eng2.videos.repositories;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import eng2.videos.domain.Video;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface VideosRepository extends CrudRepository<Video,Long>{
	
	@Join(value="hashtags", type=Join.Type.LEFT_FETCH)
	@Join(value="likes", type=Join.Type.LEFT_FETCH)
	@Join(value="dislikes", type=Join.Type.LEFT_FETCH)
	@Join(value="viewers", type=Join.Type.LEFT_FETCH)
	@Join(value="creator", type=Join.Type.INNER)
	@Override
	Optional<Video> findById(@NotNull Long id);
}
