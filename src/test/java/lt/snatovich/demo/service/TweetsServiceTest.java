package lt.snatovich.demo.service;


import lt.snatovich.demo.dto.CreateUpdateTweetDto;
import lt.snatovich.demo.model.Tweet;
import lt.snatovich.demo.model.User;
import lt.snatovich.demo.repository.TweetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TweetsServiceTest {

    @InjectMocks
    private TweetsServiceImpl tweetsService;

    @Mock
    private UsersServiceImpl usersService;

    @Mock
    private TweetRepository tweetRepository;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(tweetsService, "modelMapper", new ModelMapper());
    }

    @Test
    public void createTweetTest() {
        final UUID userId = UUID.randomUUID();
        final User user = new User(userId);
        final String tweetText = "test";

        when(usersService.getUserCreateIfNone(userId.toString())).thenReturn(user);
        when(tweetRepository.save(any(Tweet.class))).thenReturn(Tweet.builder()
                .user(user)
                .id(1L)
                .likes(0L)
                .text(tweetText)
                .publishedAt(LocalDateTime.now(ZoneOffset.UTC))
                .build());

        final Tweet created = tweetsService.createTweet(new CreateUpdateTweetDto(tweetText), userId.toString());

        assertNotNull("Tweet should be created", created);
    }
}
