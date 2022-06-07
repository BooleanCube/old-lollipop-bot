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
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.BasicConfigurator;
import org.discordbots.api.client.DiscordBotListAPI;
import threading.ThreadManagement;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

/**
 * MAIN CLASS
 */
public class Main {

    /**
     * Executed on application execution
     * @param args string[] args
     * @throws LoginException for ShardMangerBuilder and JDABuilder
     */
    public static void main(String[] args) throws LoginException, SQLException {

        // Configure log4j appenders for debugging
        BasicConfigurator.configure();

        // Build Official Version of Discord Bot Client
        DefaultShardManagerBuilder lollipop = DefaultShardManagerBuilder.createDefault(Secret.TOKEN)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.watching("anime | " + Constant.PREFIX + "help"))
                .setShardsTotal(4)
                .addEventListeners(new Listener())
                .addEventListeners(new DuelsListener())
                .addEventListeners(new PageListener())
                .addEventListeners(new LollipopReaction());
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

        // Set top.gg Statistics and Server Count
        try {
            DiscordBotListAPI topgg = new DiscordBotListAPI.Builder()
                    .token(Secret.TOPGG)
                    .botId(String.valueOf(Constant.BOT_ID))
                    .build();
            topgg.setStats(bot.getGuilds().size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set Discord Bot List Statistics and Server Count
        HttpClient client = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("https://discordbotlist.com/api/v1/bots/919061572649910292/stats");
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", Secret.DBL);
            request.addHeader("Accept", "application/json");
            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
            StringEntity params = new StringEntity("details={\"users\":\"" + bot.getUsers().size() + "\",\"guilds\":\"" + bot.getGuilds().size() + "\"} ");
            request.setEntity(params);
            HttpResponse response = client.execute(request);
            System.out.println(response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // @Deprecated client.getConnectionManager().shutdown();
        }

        // Setup Cache Refresh Cycle
        ThreadManagement.setupCacheRefresh();

        // Setup Databases
        Database.setupDatabases();

    }

}
