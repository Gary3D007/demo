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

@RestController
@RequestMapping("/admin/tweets")
@RequiredArgsConstructor
@Validated
public class TweetsAdminController {

    private final TweetsService tweetsService;

    @GetMapping
    public ResponseEntity<Page<Tweet>> getAllTweets(Pageable pageable) {
        return ResponseEntity.ok(tweetsService.getAllTweetsPaginated(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tweet> getTweetById(@PathVariable @Positive Long id) {
        return tweetsService.getTweetById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(Tweet.class, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Tweet> updateTweet(@PathVariable Long id, @RequestBody CreateUpdateTweetDto tweetDto) {
        return tweetsService.updateTweet(id, tweetDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(Tweet.class, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTweet(@PathVariable @Positive Long id) {
        final int deletedCount = tweetsService.deleteById(id);

        if (deletedCount == 0) {
            throw new EntityNotFoundException(Tweet.class, id);
        }

        return ResponseEntity.ok().build();
    }
}
