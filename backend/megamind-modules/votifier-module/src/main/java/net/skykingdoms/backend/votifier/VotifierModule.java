package net.skykingdoms.backend.votifier;

import lombok.val;
import net.skykingdoms.api.backend.util.config.Configuration;
import net.skykingdoms.api.backend.util.config.ConfigurationProvider;
import net.skykingdoms.api.backend.util.config.YamlConfiguration;
import net.skykingdoms.api.protocol.platform.Platform;
import net.skykingdoms.backend.megamind.data.gamer.Gamer;
import net.skykingdoms.backend.megamind.manager.ServerManager;
import net.skykingdoms.backend.megamind.plugin.Plugin;
import net.skykingdoms.backend.protocol.packet.play.S33VoteReceived;
import net.skykingdoms.backend.votifier.model.Vote;
import net.skykingdoms.backend.votifier.net.VotifierServerBootstrap;
import net.skykingdoms.backend.votifier.net.VotifierSession;
import net.skykingdoms.backend.votifier.net.protocol.v1crypto.RSAIO;
import net.skykingdoms.backend.votifier.net.protocol.v1crypto.RSAKeygen;
import net.skykingdoms.backend.votifier.platform.JavaUtilLogger;
import net.skykingdoms.backend.votifier.platform.LoggingAdapter;
import net.skykingdoms.backend.votifier.platform.VotifierPlugin;
import net.skykingdoms.backend.votifier.platform.scheduler.VotifierScheduler;
import net.skykingdoms.backend.votifier.support.forwarding.ForwardedVoteListener;
import net.skykingdoms.backend.votifier.util.KeyCreator;
import net.skykingdoms.backend.votifier.util.TokenUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import static org.fusesource.jansi.Ansi.setEnabled;

public class VotifierModule extends Plugin implements VoteHandler, VotifierPlugin, ForwardedVoteListener {
    private final Random random = new Random();

    /**
     * The server bootstrap.
     */
    private VotifierServerBootstrap bootstrap;

    /**
     * The RSA key pair.
     */
    private KeyPair keyPair;

    /**
     * Debug mode flag
     */
    private boolean debug;

    /**
     * Keys used for websites.
     */
    private Map<String, Key> tokens = new HashMap<>();

/*    private ForwardingVoteSink forwardingMethod;*/
    private VotifierScheduler scheduler;
    private LoggingAdapter pluginLogger;
    private boolean isFolia = false;

    private boolean loadAndBind() {

        pluginLogger = new JavaUtilLogger(Logger.getGlobal());

        scheduler = new MegamindScheduler();
        if (!getDataFolder().exists()) {
            if (!getDataFolder().mkdir()) {
                throw new RuntimeException("Unable to create the plugin data folder " + getDataFolder());
            }
        }



        // Handle configuration.
        File config = new File(getDataFolder(), "config.yml");

        /*
         * Use IP address from server.properties as a default for
         * configurations. Do not use InetAddress.getLocalHost() as it most
         * likely will return the main server address instead of the address
         * assigned to the server.
         */
        String hostAddr = "0.0.0.0";
        /*
         * Create configuration file if it does not exists; otherwise, load it
         */
        if (!config.exists()) {
            try {
                // First time run - do some initialization.
                Platform.logger.info("Configuring Votifier for the first time...");

                // Initialize the configuration file.

                val provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
                String token = TokenUtil.newToken();

                Configuration defaultConfiguration = new Configuration();
                defaultConfiguration.set("host", hostAddr);
                defaultConfiguration.set("port", 8192);
                defaultConfiguration.set("disable-v1-protocol", false);

  /*              Configuration tokensSection = new Configuration();
                tokensSection.set("default", token);*/

                defaultConfiguration.set("tokens.default", token);


           /*     defaultConfiguration.set("servers", new ArrayList<>());*/

                if (!config.createNewFile()) {
                    throw new IOException("Unable to create the config file at " + config);
                }


                provider.save(defaultConfiguration, config);



                /*
                 * Remind hosted server admins to be sure they have the right
                 * port number.
                 */
                Platform.logger.info("------------------------------------------------------------------------------");
                Platform.logger.info("Assigning NuVotifier to listen on port 8192. If you are hosting Craftbukkit on a");
                Platform.logger.info("shared server please check with your hosting provider to verify that this port");
                Platform.logger.info("is available for your use. Chances are that your hosting provider will assign");
                Platform.logger.info("a different port, which you need to specify in config.yml");
                Platform.logger.info("------------------------------------------------------------------------------");
                Platform.logger.info("Your default NuVotifier token is " + token + ".");
                Platform.logger.info("You will need to provide this token when you submit your server to a voting");
                Platform.logger.info("list.");
                Platform.logger.info("------------------------------------------------------------------------------");
            } catch (Exception ex) {
                Platform.logger.error("Error creating configuration file", ex);
                return false;
            }
        }

        Configuration cfg = null;
        File rsaDirectory = new File(getDataFolder(), "rsa");

        val provider = ConfigurationProvider.getProvider(YamlConfiguration.class);

        // Load configuration.
        try {
            cfg = provider.load(config);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cfg == null) {
            Platform.logger.info("Loading configuration...");
            return false;
        }

        /*
         * Create RSA directory and keys if it does not exist; otherwise, read
         * keys.
         */
        try {
            if (!rsaDirectory.exists()) {
                if (!rsaDirectory.mkdir()) {
                    throw new RuntimeException("Unable to create the RSA key folder " + rsaDirectory);
                }
                keyPair = RSAKeygen.generate(2048);
                RSAIO.save(rsaDirectory, keyPair);
            } else {
                keyPair = RSAIO.load(rsaDirectory);
            }
        } catch (Exception ex) {
            Platform.logger.error(
                    "Error reading configuration file or RSA tokens {}", ex);
            return false;
        }

        // the quiet flag always runs priority to the debug flag
        debug = true;

        // Load Votifier tokens.
        Configuration tokenSection = cfg.getSection("tokens");

        if (tokenSection != null) {
            Collection<String> websites = tokenSection.getKeys();
            for (String website : websites) {
                tokens.put(website, KeyCreator.createKeyFrom(tokenSection.getString(website)));
                Platform.logger.info("Loaded token for website: " + website);
            }
        } else {
            String token = TokenUtil.newToken();
            tokenSection = new Configuration();
            tokenSection.set("default", token);
            cfg.set("tokens", tokenSection);
            tokens.put("default", KeyCreator.createKeyFrom(token));
            try {
                provider.save(cfg, config);
            } catch (IOException e) {
                Platform.logger.error("Error generating Votifier token", e);
                return false;
            }
            Platform.logger.info("------------------------------------------------------------------------------");
            Platform.logger.info("No tokens were found in your configuration, so we've generated one for you.");
            Platform.logger.info("Your default Votifier token is " + token + ".");
            Platform.logger.info("You will need to provide this token when you submit your server to a voting");
            Platform.logger.info("list.");
            Platform.logger.info("------------------------------------------------------------------------------");
        }

        // Initialize the receiver.
        final String host = cfg.getString("host", hostAddr);
        final int port = cfg.getInt("port", 8192);
        if (!debug)
            Platform.logger.info("QUIET mode enabled!");

        if (port >= 0) {
            final boolean disablev1 = cfg.getBoolean("disable-v1-protocol");
            if (disablev1) {
                Platform.logger.info("------------------------------------------------------------------------------");
                Platform.logger.info("Votifier protocol v1 parsing has been disabled. Most voting websites do not");
                Platform.logger.info("currently support the modern Votifier protocol in NuVotifier.");
                Platform.logger.info("------------------------------------------------------------------------------");
            }

            this.bootstrap = new VotifierServerBootstrap(host, port, this, disablev1);
            this.bootstrap.start(error -> {});
        } else {
            Platform.logger.info("------------------------------------------------------------------------------");
            Platform.logger.info("Your Votifier port is less than 0, so we assume you do NOT want to start the");
            Platform.logger.info("votifier port server! Votifier will not listen for votes over any port, and");
            Platform.logger.info("will only listen for pluginMessaging forwarded votes!");
            Platform.logger.info("------------------------------------------------------------------------------");
        }

/*        Configuration forwardingConfig = cfg.getSection("forwarding");
        if (forwardingConfig != null) {
            String method = forwardingConfig.getString("method", "none").toLowerCase(); //Default to lower case for case-insensitive searches
            if ("none".equals(method)) {
                Logging.IMP.info("Method none selected for vote forwarding: Votes will not be received from a forwarder.");
            } else if ("pluginmessaging".equals(method)) {
                String channel = forwardingConfig.getString("pluginMessaging.channel", "NuVotifier");
                try {
                    forwardingMethod = new BukkitPluginMessagingForwardingSink(this, channel, this);
                    Logging.IMP.info("Receiving votes over PluginMessaging channel '" + channel + "'.");
                } catch (RuntimeException e) {
                    Logging.IMP.log(Level.SEVERE, "NuVotifier could not set up PluginMessaging for vote forwarding!", e);
                }
            } else {
                Logging.IMP.severe("No vote forwarding method '" + method + "' known. Defaulting to noop implementation.");
            }
        }*/
        return true;
    }

    private void halt() {
        // Shut down the network handlers.
        if (bootstrap != null) {
            bootstrap.shutdown();
            bootstrap = null;
        }

/*        if (forwardingMethod != null) {
            forwardingMethod.halt();
            forwardingMethod = null;
        }*/
    }

    @Override
    public void onEnable() {
/*        getCommand("nvreload").setExecutor(new NVReloadCmd(this));
        getCommand("testvote").setExecutor(new TestVoteCmd(this));*/

        if (!loadAndBind()) {
            gracefulExit();
            setEnabled(false); // safer to just bomb out
        }
    }

    @Override
    public void onDisable() {
        halt();
        Platform.logger.info("Votifier disabled.");
    }

    public boolean reload() {
        try {
            halt();
        } catch (Exception ex) {
            Platform.logger.error("On halt, an exception was thrown. This may be fine!", ex);
        }

        if (loadAndBind()) {
            Platform.logger.info("Reload was successful.");
            return true;
        } else {
            try {
                halt();
                Platform.logger.error("On reload, there was a problem with the configuration. Votifier currently does nothing!");
            } catch (Exception ex) {
                Platform.logger.error("On reload, there was a problem loading, and we could not re-halt the server. Votifier is in an unstable state!", ex);
            }
            return false;
        }
    }

    private void gracefulExit() {
        Platform.logger.error("Votifier did not initialize properly!");
    }

    @Override
    public LoggingAdapter getPluginLogger() {
        return pluginLogger;
    }

    @Override
    public VotifierScheduler getScheduler() {
        return scheduler;
    }

    public boolean isDebug() {
        return debug;
    }

    @Override
    public Map<String, Key> getTokens() {
        return tokens;
    }

    @Override
    public KeyPair getProtocolV1Key() {
        return keyPair;
    }

    @Override
    public void onVoteReceived(final Vote vote, VotifierSession.ProtocolVersion protocolVersion, String remoteAddress) {
        if (debug) {
            Platform.logger.info("Got a " + protocolVersion.humanReadable + " vote record from " + remoteAddress + " -> " + vote);
        }
/*        ServerManager.INSTANCE.sendToServers(

        );*/
        fireVotifierEvent(vote);
    }

    @Override
    public void onError(Throwable throwable, boolean alreadyHandledVote, String remoteAddress) {
        if (debug) {
            if (alreadyHandledVote) {
                Platform.logger.warn("Vote processed, however an exception " +
                        "occurred with a vote from " + remoteAddress, throwable);
            } else {
                Platform.logger.warn("Unable to process vote from " + remoteAddress, throwable);
            }
        } else if (!alreadyHandledVote) {
            Platform.logger.warn("Unable to process vote from " + remoteAddress);
        }
    }

    @Override
    public void onForward(final Vote v) {
        if (debug) {
            Platform.logger.info("Got a forwarded vote -> " + v);
        }
        fireVotifierEvent(v);
    }

    private void fireVotifierEvent(Vote vote) {
        // TODO: SEND PACKET TO EVER SERVER

        byte[] rawData = vote.serialize().toString().getBytes(StandardCharsets.UTF_8);
        ServerManager.INSTANCE.sendToServers(new S33VoteReceived(rawData));

        Gamer gamer = Gamer.getGamer(vote.getUsername());

        if (gamer == null) {
            Platform.logger.warn("Gamer not found for vote: " + vote);
            return;
        }

        Platform.logger.info("Processing vote for " + vote.getUsername());

        int points = random.nextInt(6);
        points = points == 0 ? 1 : points;

        gamer.addProperty("player_points", points);
        gamer.sendLocalizedMessage("vote_received_player_points", "%points%", points);

        Platform.logger.info("The vote has been processed for " + vote.getUsername() + " and they have been awarded " + points + " points.");
/*        if (VotifierEvent.getHandlerList().getRegisteredListeners().length == 0) {
            Logging.IMP.log(Level.SEVERE, "A vote was received, but you don't have any listeners available to listen for it.");
            Logging.IMP.log(Level.SEVERE, "See https://github.com/NuVotifier/NuVotifier/wiki/Setup-Guide#vote-listeners for");
            Logging.IMP.log(Level.SEVERE, "a list of listeners you can configure.");
        }

        if (!isFolia) {
            getServer().getScheduler().runTask(
                    this, () -> getServer().getPluginManager().callEvent(new VotifierEvent(vote))
            );
        } else {
            getServer().getScheduler().runTaskAsynchronously(
                    this, () -> getServer().getPluginManager().callEvent(new VotifierEvent(vote, true))
            );
        }*/
    }
}
