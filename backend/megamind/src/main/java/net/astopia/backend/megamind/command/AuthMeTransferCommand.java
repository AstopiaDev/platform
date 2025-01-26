package net.astopia.backend.megamind.command;

import net.astopia.api.backend.command.NamedCommand;
import net.astopia.api.backend.command.condition.WithCondition;
import net.astopia.api.backend.command.execution.Executor;
import net.astopia.api.protocol.platform.Platform;
import net.astopia.backend.base.data.auth.AuthSqlHandler;
import net.astopia.backend.base.data.network.NetworkSqlHandler;
import net.astopia.backend.base.language.Language;
import net.astopia.backend.base.mysql.BufferedRow;
import net.astopia.backend.base.mysql.DatabaseCredentials;
import net.astopia.backend.base.mysql.impl.MySQL;
import net.astopia.backend.base.util.hash.HashType;
import net.astopia.backend.base.util.optionals.Optional;
import net.astopia.backend.megamind.api.command.CoreExecutionSession;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;

@NamedCommand("authmetransfer")
public class AuthMeTransferCommand {

    @Executor
    @WithCondition("consoleOnly")
    public static void execute(CoreExecutionSession session) {
        session.sendMessage("Transfering all authme rows..");

        MySQL authmeDatabase = new MySQL(DatabaseCredentials.MAIN.withDatabase("authme"));
        MySQL fastLoginDatabase = new MySQL(DatabaseCredentials.MAIN.withDatabase("fastlogin"));

        authmeDatabase.executeQuery("SELECT * FROM authme").thenAccept(rows -> {
            if (rows.isEmpty()) {
                session.sendMessage("No rows!");
            }

            for (BufferedRow row : rows) {
                String userName = row.getRequiredString("realname");
                List<BufferedRow> rows2 = fastLoginDatabase.executeQuery("SELECT * FROM premium WHERE Name = ?", userName).join();
                boolean premium = false;
                UUID uuid = UUID.randomUUID();

                if (!rows2.isEmpty()) {
                    Optional<String> uuidString = rows2.stream().findFirst().get().getString("UUID");
                    if (uuidString.isPresent()) {
                        uuid = UUID.fromString(uuidString.get().replaceFirst( "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5" ));
                    };
                    premium = rows2.stream().findFirst().get().getBoolean("Premium");
                }

                String password = row.getRequiredString("password");
                HashType hashType = HashType.SHA_256;

                int gamerId = NetworkSqlHandler.INSTANCE.getPlayerId(userName);

                if (gamerId == -1) {
                    gamerId = NetworkSqlHandler.INSTANCE.getPlayerId(uuid);
                    if (gamerId == -1) {
                        NetworkSqlHandler.INSTANCE.createUserRecord(uuid, userName, Language.DEFAULT, new InetSocketAddress("127.0.0.1", 25565).getAddress());
                    }
                }

                if (gamerId == -1) {
                    continue;
                }

                String name = NetworkSqlHandler.INSTANCE.getPlayerName(gamerId);
                if (name != null && name.equals("Unknown")) {
                    NetworkSqlHandler.INSTANCE.updatePlayerName(gamerId, userName);
                }

                AuthSqlHandler.INSTANCE.insertAuthRecord(gamerId, premium, password, hashType);
            }

            Platform.logger.info("Done transferring all authme data");
        }).join();
    }
}
