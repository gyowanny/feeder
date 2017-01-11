package uk.co.gyo.feeder.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import uk.co.gyo.feeder.command.RssFeederCommand;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static ratpack.jackson.Jackson.json;

public class RssHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(RssHandler.class);

    public RssHandler() {
    }

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.getRequest().getBody()
                .map(body -> {
                    RssFeederCommand rssFeederCommand = new RssFeederCommand();
                    rssFeederCommand.setUrl(body.getText());
                    return rssFeederCommand;
                })
                .route(command -> !command.isValidUrl(), command -> ctx.getResponse().status(400).send())
                .then(command -> {
                    CountDownLatch count = new CountDownLatch(3);
                    command.observe().subscribe(
                            success -> {
                                List<RssEntry> result = success.getEntries().stream().map(
                                        syndEntry -> new RssEntry(syndEntry.getLink(), syndEntry.getTitle())
                                    ).collect(Collectors.toList());
                                ctx.getResponse().status(200);
                                ctx.render(json(result));
                            },
                            error -> {
                                log.error("Error while calling rss feeder command", error);
                                ctx.getResponse().status(400);
                                ctx.render(json(new Error(error.getMessage(), error.getMessage())));
                            },
                            () -> count.countDown()

                    );
                });
    }

    private static class RssEntry {
        private final String link;
        private final String title;

        public RssEntry(String link, String title) {
            this.link = link;
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public String getTitle() {
            return title;
        }
    }

    private static class Error {
        private final String message;
        private final String cause;

        public Error(String message, String cause) {
            this.message = message;
            this.cause = cause;
        }

        public String getMessage() {
            return message;
        }

        public String getCause() {
            return cause;
        }
    }

}
