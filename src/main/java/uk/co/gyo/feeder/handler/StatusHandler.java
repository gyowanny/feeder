package uk.co.gyo.feeder.handler;

import ratpack.handling.Context;
import ratpack.handling.Handler;

import static ratpack.jackson.Jackson.json;

public class StatusHandler implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        StatusResponse status = new StatusResponse("OK");
        ctx.getResponse().status(200);
        ctx.render(json(status));
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
