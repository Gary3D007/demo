package lt.snatovich.demo.service;

import lt.snatovich.demo.dto.CreateUpdateTweetDto;
import lt.snatovich.demo.model.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TweetsService {
    Tweet createTweet(CreateUpdateTweetDto tweet, String userId);

    Optional<Tweet> getUserTweet(Long id, String userId);

    Optional<Tweet> getTweetById(Long id);

    Page<Tweet> getAllTweetsPaginated(Pageable pageable);

    Page<Tweet> getAllUserTweetsPaginated(String userId, Pageable pageable);

    Optional<Tweet> updateTweet(String userId, Long id, CreateUpdateTweetDto tweetDto);

    Optional<Tweet> updateTweet(Long id, CreateUpdateTweetDto tweetDto);

    int deleteUsersTweet(Long id, String userId);

    int deleteById(Long id);
}
