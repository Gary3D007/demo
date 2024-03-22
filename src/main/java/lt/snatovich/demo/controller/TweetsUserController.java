package lt.snatovich.demo.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lt.snatovich.demo.dto.CreateUpdateTweetDto;
import lt.snatovich.demo.exception.EntityNotFoundException;
import lt.snatovich.demo.model.Tweet;
import lt.snatovich.demo.service.TweetsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user/tweets")
@RequiredArgsConstructor
@Validated
public class TweetsUserController {

    private final TweetsService tweetsService;


    @PostMapping
    public ResponseEntity<Tweet> createTweet(@RequestBody CreateUpdateTweetDto tweet, Principal principal) {
        final var created = tweetsService.createTweet(tweet, principal.getName());
        return ResponseEntity.ok(created);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Tweet> getUserTweetById(@PathVariable @Positive Long id, Principal principal) {
        return tweetsService.getUserTweet(id, principal.getName())
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(Tweet.class, id));
    }

    @GetMapping
    public ResponseEntity<Page<Tweet>> getAllUserTweets(Pageable pageable, Principal principal) {
        return ResponseEntity.ok(tweetsService.getAllUserTweetsPaginated(principal.getName(), pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Tweet> updateUserTweet(@PathVariable Long id,
                                                 @RequestBody CreateUpdateTweetDto tweetDto,
                                                 Principal principal) {
        return tweetsService.updateTweet(principal.getName(), id, tweetDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(Tweet.class, id));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUserTweet(@PathVariable @Positive Long id, Principal principal) {
        final int deletedCount = tweetsService.deleteUsersTweet(id, principal.getName());

        if (deletedCount == 0) {
            throw new EntityNotFoundException(Tweet.class, id);
        }

        return ResponseEntity.ok().build();
    }
}
