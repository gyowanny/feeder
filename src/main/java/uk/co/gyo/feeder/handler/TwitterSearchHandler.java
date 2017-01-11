package uk.co.gyo.feeder.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import twitter4j.Twitter;
import uk.co.gyo.feeder.command.TwitterSearchCommand;
import uk.co.gyo.feeder.error.Error;

import javax.inject.Inject;
import java.util.concurrent.CountDownLatch;

import static ratpack.jackson.Jackson.json;

public class TwitterSearchHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(TwitterSearchCommand.class);

    private Twitter twitter;

    @Inject
    public TwitterSearchHandler(Twitter twitter) {
        this.twitter = twitter;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        final String query = ctx.getPathTokens().get("query");

        Promise.<TwitterSearchCommand>async(down -> down.success(new TwitterSearchCommand(twitter, query)))
                .then(command -> {
                    CountDownLatch countDown = new CountDownLatch(3);
                    command.observe().subscribe(
                            success -> {
                                ctx.getResponse().status(200);
                                ctx.render(json(success));
                            },
                            error -> {
                                log.error("Error searching for {}", query, error);
                                ctx.getResponse().status(404);
                                ctx.render(json(new Error(error.getMessage(), error.getMessage())));
                            },
                            () -> countDown.countDown()
                    );
                });
    }
}
