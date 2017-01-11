package uk.co.gyo.feeder.command;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import com.rometools.rome.feed.synd.SyndFeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import uk.co.gyo.feeder.rss.RomeFeeder;

public class RssFeederCommand extends HystrixObservableCommand<SyndFeed> {
    private static final Logger log = LoggerFactory.getLogger(RssFeederCommand.class);

    private final RomeFeeder feeder;

    private String url;

    public RssFeederCommand() {
        super(HystrixCommandGroupKey.Factory.asKey("rome"));
        this.feeder = new RomeFeeder();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isValidUrl() {
        return url != null && !url.isEmpty();
    }

    @Override
    protected Observable<SyndFeed> construct() {
        return Observable.create( subscriber -> {
                    try {
                        subscriber.onNext(feeder.loadFeedFor(url));
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        log.error("An error occurred while running the rss feeder", e);
                        Observable.error(new RuntimeException(e));
                    }
                }
        );
    }

}
