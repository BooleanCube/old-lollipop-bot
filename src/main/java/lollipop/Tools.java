package lollipop;

import net.dv8tion.jda.api.entities.TextChannel;

public class Tools {

    public static void wrongUsage(TextChannel tc, Command c) {
        tc.sendMessage("Wrong Command Usage!\n" + c.getHelp()).queue();
    }

}
