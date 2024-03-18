package lt.snatovich.demo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.snatovich.demo.dto.CreateUpdateTweetDto;
import lt.snatovich.demo.model.Tweet;
import lt.snatovich.demo.model.User;
import lt.snatovich.demo.repository.TweetRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TweetsServiceImpl implements TweetsService {

    private final TweetRepository tweetRepository;
    private final UsersService usersService;
    private final ModelMapper modelMapper;

    @Override
    public Tweet createTweet(CreateUpdateTweetDto tweetDto, String userId) {
        final User user = usersService.getUserCreateIfNone(userId);
        Tweet tweet = modelMapper.map(tweetDto, Tweet.class);
        tweet.setUser(user);
        tweet.setPublishedAt(LocalDateTime.now(ZoneOffset.UTC));
        final Tweet created = tweetRepository.save(tweet);
        log.debug("Created tweet with id '{}'", tweet.getId());
        return created;
    }

    @Override
    public Optional<Tweet> getUserTweet(Long id, String userId) {
        return tweetRepository.findByIdAndUser(id, new User(userId));
    }

    @Override
    public Optional<Tweet> getTweetById(Long id) {
        return tweetRepository.findById(id);
    }

    @Override
    public Page<Tweet> getAllUserTweetsPaginated(String userId, Pageable pageable) {
        return tweetRepository.findAllByUser(new User(userId), pageable);
    }

    @Override
    public Page<Tweet> getAllTweetsPaginated(Pageable pageable) {
        return tweetRepository.findAll(pageable);
    }

    @Override
    public Optional<Tweet> updateTweet(String userId, Long id, CreateUpdateTweetDto tweetDto) {
        return tweetRepository.findByIdAndUser(id, new User(userId))
                .map((tweet) -> {
                    tweet.setText(tweetDto.getText());
                    return tweetRepository.save(tweet);
                });
    }

    @Override
    public Optional<Tweet> updateTweet(Long id, CreateUpdateTweetDto tweetDto) {
        return tweetRepository.findById(id).map((tweet) -> {
            tweet.setText(tweetDto.getText());
            return tweetRepository.save(tweet);
        });
    }

    @Transactional
    @Override
    public int deleteUsersTweet(Long id, String userId) {
        return tweetRepository.removeByIdAndUser(id, new User(userId));
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        return tweetRepository.removeById(id);
    }
}
