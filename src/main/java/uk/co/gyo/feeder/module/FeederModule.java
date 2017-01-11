package uk.co.gyo.feeder.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import uk.co.gyo.feeder.handler.RssHandler;
import uk.co.gyo.feeder.handler.StatusHandler;

public class FeederModule extends AbstractModule {

    protected void configure() {
        bind(StatusHandler.class).in(Scopes.SINGLETON);
        bind(RssHandler.class).in(Scopes.SINGLETON);
    }
}
