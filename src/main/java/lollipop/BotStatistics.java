package lollipop;

import lollipop.Constant;
import lollipop.Secret;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.discordbots.api.client.DiscordBotListAPI;
import org.jetbrains.annotations.NotNull;
import threading.ThreadManagement;

import java.io.IOException;

public class BotStatistics extends ListenerAdapter {

    private static ShardManager bot;
    private static DiscordBotListAPI topgg;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        if(event.getJDA().getShardInfo().getShardId() != event.getJDA().getShardInfo().getShardTotal()-1) return;

        bot = event.getJDA().getShardManager();

        setStatistics();

        // Start statistics setting cycle
        ThreadManagement.setupStatisticsCycle();
    }

    public static void setStatistics() {
        int guilds = bot.getGuilds().size();
        int users = bot.getUsers().size();
        int shards = bot.getShardsTotal();

        // Set top.gg Statistics and Server Count
        try {
            topgg = new DiscordBotListAPI.Builder()
                    .token(Secret.TOPGG)
                    .botId(String.valueOf(Constant.BOT_ID))
                    .build();
            topgg.setStats(guilds);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        // Set Discord Bot List Statistics and Server Count
        HttpClient client = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("https://discordbotlist.com/api/v1/bots/" + Constant.BOT_ID + "/stats");
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", Secret.DBL);
            request.addHeader("Accept", "application/json");
            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
            StringEntity params = new StringEntity("{\"users\":\"" + users + "\",\"guilds\":\"" + guilds + "\"}");
            request.setEntity(params);
            HttpResponse response = client.execute(request);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            // @Deprecated client.getConnectionManager().shutdown();
        }

        // Setup Infinity Bot List Statistics and Server Count
        client = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("https://api.infinitybotlist.com/bots/stats");
            request.addHeader("content-type", "application/json");
            request.addHeader("authorization", Secret.INFINITYBOTLIST);
            request.addHeader("Accept", "application/json");
            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
            StringEntity params = new StringEntity("{\"servers\":\"" + guilds + "\",\"shards\":\"" + shards + "\"}");
            request.setEntity(params);
            HttpResponse response = client.execute(request);
            System.out.println(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            // @Deprecated client.getConnectionManager().shutdown();
        }
    }

    /**
     * Checks if the user has voted for lollipop on top.gg
     * @param id user id
     */
    public static void sendMultiplier(String id, Runnable success, Runnable failure) {
        topgg.hasVoted(id).whenComplete((voted, e) -> {
            if(voted == null || !voted) failure.run();
            else success.run();
        });
    }

}
