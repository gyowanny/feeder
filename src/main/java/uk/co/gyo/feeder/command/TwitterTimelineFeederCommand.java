package uk.co.gyo.feeder.command;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.List;

public class TwitterTimelineFeederCommand extends HystrixObservableCommand<List<Status>> {
    private static final Logger log = LoggerFactory.getLogger(TwitterTimelineFeederCommand.class);

    private final Twitter twitter;
    private final String user;

    public TwitterTimelineFeederCommand(Twitter twitter, String user) {
        super(HystrixCommandGroupKey.Factory.asKey("twitter-timeline"));
        this.twitter = twitter;
        this.user = user;
    }

    @Override
    protected Observable<List<Status>> construct() {
        return Observable.create(subscriber -> {
            Twitter twitter = TwitterFactory.getSingleton();
            try {
                subscriber.onNext(twitter.getUserTimeline());
                subscriber.onCompleted();
            } catch (TwitterException e) {
                log.error("An error occurred while running the twitter feeder", e);
                subscriber.onError(new RuntimeException(e));
            }
        });
    }
}
