package net.astopia.backend.restapi.endpoints.impl;

import lombok.val;
import net.astopia.backend.megamind.MegamindApp;
import net.astopia.backend.megamind.manager.ServerManager;
import net.astopia.backend.protocol.packet.play.S11ServerCommand;
import net.astopia.backend.protocol.server.IConnectable;
import net.astopia.backend.restapi.endpoints.*;
import spark.Request;
import spark.Response;

@ApiMethod(RequestType.POST)
@Endpoint("restart")
@AuthorizedOnly
public class RestartEndpoint implements IEndpoint {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        val token = request.queryParams("token");
        if (token == null) {
            return terminate(response, 400, "No token param found!");
        }

        if (!token.equals("dolbaeb-is-cooking")) {
            return terminate(response, 400, "Invalid token!");
        }

        val serverName = request.queryParams("serverName");
        if (serverName == null) {
            return terminate(response, 400, "No serverName param found!");
        }

        IConnectable server = ServerManager.INSTANCE.getBukkitServer(serverName);
        if (server == null) {
            return terminate(response, 400, "Server not found");
        }

        server.getChannel().sendPacket(new S11ServerCommand("stop"));
        response.status(200);
        return "Restarted successfully.";
    }
}
