package eng2.trending.hashtags.repositories;

import eng2.trending.hashtags.domain.Hashtag;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;


@Repository
public interface HashtagsRepository extends CrudRepository<Hashtag,Long>{

}
