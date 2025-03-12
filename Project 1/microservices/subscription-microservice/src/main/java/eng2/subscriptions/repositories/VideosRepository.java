package eng2.subscriptions.repositories;

import eng2.subscriptions.domain.Video;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface VideosRepository extends CrudRepository<Video,Long>{
	
}
