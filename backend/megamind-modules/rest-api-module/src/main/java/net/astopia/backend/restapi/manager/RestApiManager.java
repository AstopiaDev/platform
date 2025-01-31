package net.astopia.backend.restapi.manager;

import lombok.SneakyThrows;
import net.astopia.api.protocol.platform.Platform;
import net.astopia.backend.restapi.endpoints.EndpointsManager;
import net.astopia.backend.restapi.events.EventsManager;
import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public final class RestApiManager {

    // ----------------------------------------------------------------
    public static final RestApiManager INSTANCE = new RestApiManager();
    // ----------------------------------------------------------------

    @SneakyThrows
    public void initialize(final int port) {
        port(port);

        // adding endpoints
        EndpointsManager.INSTANCE.initialize();

        Platform.logger.info("[RestAPI] Listening on port {}", port);
        enableCORS();
        awaitInitialization();
    }

    private static void enableCORS() {
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", request.host());
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });
    }

    public void shutdown() {
        EndpointsManager.INSTANCE.shutdown();
        stop();
    }

}
