package uk.co.gyo.feeder.handler;

import ratpack.handling.Context;
import ratpack.handling.Handler;

import static ratpack.jackson.Jackson.json;

public class StatusHandler implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.getResponse().status(200);
        ctx.render(json(new StatusResponse("OK")));
    }

    private static class StatusResponse {
        private String status;

        public StatusResponse(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
}
