package uk.co.gyo.feeder.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import twitter4j.Twitter;
import uk.co.gyo.feeder.command.TwitterTimelineFeederCommand;
import uk.co.gyo.feeder.error.Error;

import javax.inject.Inject;
import java.util.concurrent.CountDownLatch;

import static ratpack.jackson.Jackson.json;

public class TwitterTimelineHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(TwitterTimelineHandler.class);

    private final Twitter twitter;

    @Inject
    public TwitterTimelineHandler(Twitter twitter) {
        this.twitter = twitter;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        Promise.<TwitterTimelineFeederCommand>async(down ->
                    down.success(new TwitterTimelineFeederCommand(twitter, ctx.getPathTokens().get("user"))))
                .then(command -> {
                    CountDownLatch countDown = new CountDownLatch(3);
                    command.observe().subscribe(
                            success -> {
                                ctx.getResponse().status(200);
                                ctx.render(json(success));
                            },
                            error -> {
                                log.error("Error listing twitter ", error);
                                ctx.getResponse().status(400);
                                ctx.render(json(new Error(error.getMessage(), error.getMessage())));
                            },
                            () -> countDown.countDown()
                    );
                });
    }

}
