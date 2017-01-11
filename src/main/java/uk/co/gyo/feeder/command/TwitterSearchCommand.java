package uk.co.gyo.feeder.command;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;

public class TwitterSearchCommand extends HystrixObservableCommand<List<Status>>{
    private static final Logger log = LoggerFactory.getLogger(TwitterSearchCommand.class);

    private final Twitter twitter;
    private final String query;

    public TwitterSearchCommand(Twitter twitter, String query) {
        super(HystrixCommandGroupKey.Factory.asKey("twitter-search"));
        this.twitter = twitter;
        this.query = query;
    }

    @Override
    protected Observable<List<Status>> construct() {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(twitter.search(new Query(query)).getTweets());
                subscriber.onCompleted();
            } catch (TwitterException e) {
                log.error("Error while searching tweets by {}", query, e);
                subscriber.onError(e);
            }
        });
    }
}
