package uk.co.gyo.feeder.rss;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.net.URL;

public class RomeFeeder {

    public RomeFeeder() {
    }

    public synchronized SyndFeed loadFeedFor(String url) throws Exception {
        return new SyndFeedInput().build(new XmlReader(new URL(url)));
    }

}
