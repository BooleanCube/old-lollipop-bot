package lollipop;

import lollipop.listeners.DuelsListener;
import lollipop.listeners.LollipopReaction;
import lollipop.listeners.PageListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.apache.log4j.BasicConfigurator;
import threading.ThreadManagement;

import javax.security.auth.login.LoginException;

/**
 * MAIN CLASS
 */
public class Main {

    /**
     * Runs both discord bot clients
     * For testing and the real product
     * @param args string[] args
     * @throws LoginException for ShardMangerBuilder and JDABuilder
     */
    public static void main(String[] args) throws LoginException {

        // Configure log4j appenders for debugging
        BasicConfigurator.configure();

        // Build Official Version of Discord Bot Client
        DefaultShardManagerBuilder lollipop = DefaultShardManagerBuilder.createDefault(Secret.TOKEN)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.streaming("anime | " + Constant.PREFIX + "help", "https://www.twitch.tv/lollipopbot1"))
                .setShardsTotal(4)
                .addEventListeners(new Listener())
                .addEventListeners(new DuelsListener())
                .addEventListeners(new PageListener())
                .addEventListeners(new LollipopReaction())
                .addEventListeners(new BotStatistics());
        ShardManager bot = lollipop.build();

        // Build the testing model for this Discord Bot with a test client
        JDABuilder testClient = JDABuilder.createDefault(Secret.TESTTOKEN)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.watching("anime | " + Constant.PREFIX + "help"))
                .addEventListeners(new Listener())
                .addEventListeners(new DuelsListener())
                .addEventListeners(new PageListener())
                .addEventListeners(new LollipopReaction());
        //JDA test = testClient.build();

        // Setup Cache Refresh Cycle
        ThreadManagement.setupCacheRefresh();

        // Setup Databases
        Database.setupDatabases();

    }

}
