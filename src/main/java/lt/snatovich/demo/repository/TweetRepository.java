package lt.snatovich.demo.repository;

import lt.snatovich.demo.model.Tweet;
import lt.snatovich.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface TweetRepository extends CrudRepository<Tweet, Long>, PagingAndSortingRepository<Tweet, Long> {

    Page<Tweet> findAllByUser(User user, Pageable pageable);

    Optional<Tweet> findByIdAndUser(Long id, User user);

    @Transactional
    int removeById(Long id);

    @Transactional
    int removeByIdAndUser(Long id, User user);
}
