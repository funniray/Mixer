package pro.kdray.funniray.mixer;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import java.util.Arrays;
import java.util.List;

public class MixerConfig {
    public static final ServerConfig CONFIG;
    public static final ForgeConfigSpec CONFIG_SPEC;
    static String token;

    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        CONFIG_SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
        load();
        LogManager.getLogger().debug("Loaded mixer config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.ConfigReloading configEvent) {
        MixerForge.onFileChange();
    }

    public static void load() {
        token = CONFIG.tokenValue.get();
        Config.clientID = CONFIG.clientIDValue.get();
        Config.shareCode = CONFIG.shareCodeValue.get();
        Config.projectID = CONFIG.projectIDValue.get();
        Config.subscribeCommand = CONFIG.subscriberCommandValue.get();
        Config.resubscribeCommand = CONFIG.resubscriberCommandValue.get();
        Config.followCommand = CONFIG.followCommandValue.get();
        Config.bannedWords = CONFIG.bannedWordsValue.get();
    }

    public static class ServerConfig {
        public ForgeConfigSpec.ConfigValue<String> tokenValue;
        public ForgeConfigSpec.ConfigValue<String> clientIDValue;
        public ForgeConfigSpec.ConfigValue<String> shareCodeValue;
        public ForgeConfigSpec.IntValue projectIDValue;
        public ForgeConfigSpec.ConfigValue<String> subscriberCommandValue;
        public ForgeConfigSpec.ConfigValue<String> resubscriberCommandValue;
        public ForgeConfigSpec.ConfigValue<String> followCommandValue;
        public ForgeConfigSpec.ConfigValue<List<String>> bannedWordsValue;

        ServerConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Project");
            tokenValue = builder
                    .comment("Your API token. You can get a valid token at https://bit.ly/2GWQmvV")
                    .define("token","Find your own token, geez");
            clientIDValue = builder
                    .define("clientID", "d04e85fd1cb06e4eb9891fc118fe75893eca399955189926");
            shareCodeValue = builder
                    .define("shareCode", "dbzktlsk");
            projectIDValue = builder
                    .defineInRange("projectID",191773,0,Integer.MAX_VALUE);
            builder.pop();
            builder.push("Constellation");
            subscriberCommandValue = builder
                    .comment("Command to be ran when someone subscribes. Replaces %user% with the subscriber's username")
                    .define("subscribeCommand","say %user% has subscribed");
            resubscriberCommandValue = builder
                    .comment("Command to be ran when someone resubscribes. Replaces %user% with the subscriber's " +
                            "username. Replaces %totalMonths% with the months they have been subscribed for")
                    .define("resubscriberCommand","say %user% has resubscribed for %totalMonths%");
            followCommandValue = builder
                    .comment("Command to be ran when someone follows. Replaces %user% with the follower's username")
                    .define("followCommand","say %user% has subscribed");
            builder.pop();
            builder.push("Chat");
            bannedWordsValue = builder
                    .comment("If a message has any of these words in it, it won't be shown in chat")
                    .define("bannedWords", Arrays.asList("Put bad words", "here"));
            builder.pop();
        }
    }
}
