package lollipop.commands;

import lollipop.API;
import lollipop.Constant;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.io.IOException;

public class Gif implements Command {

    @Override
    public String[] getAliases() {
        return new String[] {"gif"};
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Sends a random GIF about anime!\nUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        String[] types = {"alarm", "amazing", "ask", "baka", "bite", "blush", "blyat", "boop", "clap", "coffee", "confused", "cry", "cuddle", "cute", "dance", "destroy", "die", "disappear", "dodge", "error", "facedesk", "facepalm", "fbi", "fight", "happy", "hide", "highfive", "hug", "kill", "kiss", "laugh", "lick", "lonely", "love", "mad", "money", "nom", "nosebleed", "ok", "party", "pat", "peek", "poke", "pout", "protect", "puke", "punch", "purr", "pusheen", "run", "salute", "scared", "scream", "shame", "shocked", "shoot", "shrug", "sip", "sit", "slap", "sleepy", "smile", "smoke", "smug", "spin", "stare", "stomp", "tickle", "trap", "triggered", "uwu", "wasted", "wave", "wiggle", "wink", "yeet"};
        String type = types[(int)(Math.random()*types.length)];
        API api = new API();
        try {
            event.replyEmbeds(new EmbedBuilder().setImage(api.randomGIF(type)).build()).queue();
        } catch (IOException e) { throw new RuntimeException(e); }
    }

}
