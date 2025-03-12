package eng2.videos.repositories;

import eng2.videos.domain.Hashtag;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface HashtagsRepository extends CrudRepository<Hashtag,Long>{

}
