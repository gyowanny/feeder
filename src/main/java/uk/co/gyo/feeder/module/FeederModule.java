package uk.co.gyo.feeder.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import uk.co.gyo.feeder.handler.RssHandler;
import uk.co.gyo.feeder.handler.StatusHandler;
import uk.co.gyo.feeder.handler.TwitterSearchHandler;
import uk.co.gyo.feeder.handler.TwitterTimelineHandler;

public class FeederModule extends AbstractModule {

    protected void configure() {
        bind(StatusHandler.class).in(Scopes.SINGLETON);
        bind(RssHandler.class).in(Scopes.SINGLETON);
        bind(TwitterTimelineHandler.class).in(Scopes.SINGLETON);
        bind(TwitterSearchHandler.class).in(Scopes.SINGLETON);

        bind(Twitter.class).toProvider(TwitterProvider.class).in(Scopes.SINGLETON);
    }

    private static class TwitterProvider implements Provider<Twitter> {
        @Override
        public Twitter get() {
            return TwitterFactory.getSingleton();
        }
    }
}
