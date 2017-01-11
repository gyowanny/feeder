package uk.co.gyo.feeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import ratpack.guice.Guice;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;
import uk.co.gyo.feeder.handler.RssHandler;
import uk.co.gyo.feeder.handler.StatusHandler;
import uk.co.gyo.feeder.module.FeederModule;

/*
 * TODO: Twitter Feeder
 */
public class Main {

    public static void main( String[] args ) throws Exception {
        RatpackServer.start(server -> server
            .serverConfig(config -> ServerConfig.embedded())
                .registryOf(registry -> registry.add(ObjectMapper.class, new ObjectMapper().registerModule(new Jdk8Module())))
                .registry(Guice.registry(b -> b.module(FeederModule.class)))
            .handlers(chain -> {
                chain.get("private/status", StatusHandler.class);
                chain.post("rss", RssHandler.class);
            })
        );
    }

}
