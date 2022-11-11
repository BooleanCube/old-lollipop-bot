package lollipop;


import discorddb.DatabaseManager;
import discorddb.DatabaseObject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import javax.naming.LimitExceededException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Static database class to manage all of lollipop's databases
 */
public class Database {

    private static DatabaseObject currency;

    /**
     * Setup and Initialize all the databases
     */
    public static void setupDatabases() {
        try {
            DatabaseManager.createDatabase("currency");
            currency = DatabaseManager.getDatabase("currency");
        } catch (LimitExceededException | FileAlreadyExistsException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get user's current balance
     * @param id user id
     * @return balance amount
     */
    public static int getUserBalance(String id) {
        if(currency.getValue(id) == null) {
            currency.addKey(id, "0");
            return 0;
        }
        return currency.getValueInt(id);
    }

    /**
     * Gets a users ranking in terms of lollipops in the specified guild
     * @param id user id
     * @param guild guild to rank
     * @return int array: 1st element = guild rank, 2nd element = global rank
     */
    public static int[] getUserRank(String id, Guild guild) {
        ArrayList<Integer> guildRank = new ArrayList<>();
        for(Member member : guild.getMembers()) {
            int lp = getUserBalance(member.getId());
            guildRank.add(lp);
        }
        guildRank.sort(Collections.reverseOrder());
        ArrayList<Integer> globalRank = new ArrayList<>();
        Collections.addAll(globalRank, currency.getValues().stream().mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new));
        globalRank.sort(Collections.reverseOrder());
        int userLp = getUserBalance(id);
        return new int[]{guildRank.indexOf(userLp)+1, globalRank.indexOf(userLp)+1};
    }

    /**
     * Gets a users ranking in terms of lollipops in the specified guild
     * @param id user id
     * @param guild guild to rank
     * @return int array: 1st element = guild rank, 2nd element = global rank
     */
    public static int getUserGuildRank(String id, Guild guild) {
        ArrayList<Integer> guildRank = new ArrayList<>();
        for(Member member : guild.getMembers()) {
            int lp = getUserBalance(member.getId());
            guildRank.add(lp);
        }
        guildRank.sort(Collections.reverseOrder());
        int userLp = getUserBalance(id);
        return guildRank.indexOf(userLp)+1;
    }

    /**
     * Gets a users ranking in terms of lollipops in the specified guild
     * @param id user id
     * @return int array: 1st element = guild rank, 2nd element = global rank
     */
    public static int getUserGlobalRank(String id) {
        ArrayList<Integer> globalRank = new ArrayList<>();
        Collections.addAll(globalRank, currency.getValues().stream().mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new));
        globalRank.sort(Collections.reverseOrder());
        int userLp = getUserBalance(id);
        return globalRank.indexOf(userLp)+1;
    }

    /**
     * Increment user's balance by specified amount
     * @param id user id
     * @param increment increment amount
     */
    public static void addToUserBalance(String id, int increment) {
        int balance = getUserBalance(id) + increment;
        currency.updateValue(id, String.valueOf(Math.max(0, balance)));
    }

    /**
     * Gets the top 10 ranked members in a guild
     * @param guild mentioned guild
     * @return arraylist of String for leaderboard text
     */
    public static ArrayList<String> getLeaderboard(Guild guild) {
        ArrayList<String> result = new ArrayList<>();
        HashMap<String, Integer> userToLollipops = new HashMap<>();
        for(Member member : guild.getMembers()) userToLollipops.put(member.getId(), getUserBalance(member.getId()));
        userToLollipops = Tools.sortByValue(userToLollipops);
        int rank = 1;
        for(String id : userToLollipops.keySet()) {
            if(rank == 11) break;
            User user = guild.getJDA().getShardManager().getUserById(id);
            if(user == null || user.isBot()) continue;
            if(rank == 1)
                result.add("\uD83E\uDD47 " + user.getAsTag() + " (" + userToLollipops.get(id) + " \uD83C\uDF6D)");
            else if(rank == 2)
                result.add("\uD83E\uDD48 " + user.getAsTag() + " (" + userToLollipops.get(id) + " \uD83C\uDF6D)");
            else if(rank == 3)
                result.add("\uD83E\uDD49 " + user.getAsTag() + " (" + userToLollipops.get(id) + " \uD83C\uDF6D)");
            else
                result.add("\uD83C\uDF96️" + user.getAsTag() + " (" + userToLollipops.get(id) + " \uD83C\uDF6D)");
            rank++;
        }
        return result;
    }

    /**
     * Gets the top 10 ranked members globally
     * @return arraylist of String for leaderboard text
     */
    public static ArrayList<String> getLeaderboard(JDA jda) {
        ArrayList<String> result = new ArrayList<>();
        HashMap<String, Integer> userToLollipops = new HashMap<>();
        for(String id : currency.getKeys()) userToLollipops.put(id, getUserBalance(id));
        userToLollipops = Tools.sortByValue(userToLollipops);
        int rank = 1;
        for(String id : userToLollipops.keySet()) {
            if(rank == 11) break;
            User user = jda.getShardManager().getUserById(id);
            if(user == null || user.isBot()) continue;
            if(rank == 1)
                result.add("\uD83E\uDD47 " + user.getAsTag() + " (" + userToLollipops.get(id) + " \uD83C\uDF6D)");
            else if(rank == 2)
                result.add("\uD83E\uDD48 " + user.getAsTag() + " (" + userToLollipops.get(id) + " \uD83C\uDF6D)");
            else if(rank == 3)
                result.add("\uD83E\uDD49 " + user.getAsTag() + " (" + userToLollipops.get(id) + " \uD83C\uDF6D)");
            else
                result.add("\uD83C\uDF96️" + user.getAsTag() + " (" + userToLollipops.get(id) + " \uD83C\uDF6D)");
            rank++;
        }
        return result;
    }

    /**
     * Gets the amount of keys in the currency datbase
     * @return integer for number of users in currency datbase
     */
    public static int getCurrencyUserCount() {
        return currency.getKeys().size();
    }

}
