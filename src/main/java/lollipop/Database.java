package lollipop;


import discorddb.DatabaseManager;
import discorddb.DatabaseObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import javax.naming.LimitExceededException;
import java.io.FileNotFoundException;
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
     * @return integer rank
     */
    public static int getUserRank(String id, Guild guild) {
        ArrayList<Integer> lps = new ArrayList<>();
        for(Member member : guild.getMembers()) {
            int lp = getUserBalance(member.getId());
            lps.add(lp);
        }
        lps.sort(Collections.reverseOrder());
        int userLp = getUserBalance(id);
        return lps.indexOf(userLp)+1;
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
            if(guild.getMemberById(id).getUser().isBot()) continue;
            if(rank == 1)
                result.add("\uD83E\uDD47 " + guild.getMemberById(id).getUser().getAsTag() + " (" + userToLollipops.get(id) + " \uD83C\uDF6D)");
            else if(rank == 2)
                result.add("\uD83E\uDD48 " + guild.getMemberById(id).getUser().getAsTag() + " (" + userToLollipops.get(id) + " \uD83C\uDF6D)");
            else if(rank == 3)
                result.add("\uD83E\uDD49 " + guild.getMemberById(id).getUser().getAsTag() + " (" + userToLollipops.get(id) + " \uD83C\uDF6D)");
            else
                result.add("\uD83C\uDF96️" + guild.getMemberById(id).getUser().getAsTag() + " (" + userToLollipops.get(id) + " \uD83C\uDF6D)");
            rank++;
        }
        return result;
    }

}
