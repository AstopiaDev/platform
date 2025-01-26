package net.astopia.backend.restapi.endpoints.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.val;
import net.astopia.backend.megamind.data.gamer.Gamer;
import net.astopia.backend.restapi.endpoints.ApiMethod;
import net.astopia.backend.restapi.endpoints.Endpoint;
import net.astopia.backend.restapi.endpoints.IEndpoint;
import net.astopia.backend.restapi.endpoints.RequestType;
import spark.Request;
import spark.Response;

@ApiMethod(RequestType.GET)
@Endpoint("list")
public final class PlayerListEndpoint implements IEndpoint {

    @Override
    public Object handle(Request request, Response response) {
        val jsonArray = new JsonArray();
        Gamer.getGamers().forEach((gamer) -> {
            val playerObject = new JsonObject();

            val rank = gamer.getRanks().getPriorityRank();

            playerObject.addProperty("name", gamer.getNameAsString());
            playerObject.addProperty("rank", rank.getDisplayName());
            playerObject.addProperty("color", rank.getGroup().getPrefixColor());

            jsonArray.add(playerObject);
        });

        response.type("application/json");

        return jsonArray.toString();
    }
}
