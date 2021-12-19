package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.brunocvcunha.jiphy.Jiphy;
import org.brunocvcunha.jiphy.JiphyConstants;
import org.brunocvcunha.jiphy.requests.JiphySearchRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Gif implements Command {
    @Override
    public String getCommand() {
        return "gif";
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Sends a random GIF about anime!\nUsage: `" + CONSTANT.PREFIX + getCommand() + "`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        String[] queries = {"lickilick", "anime", "pokemon", "jjba", "attack%20on%20titan", "demon%20slayer", "tokyo%20ghoul", "jujutsu%20kaisen", "naruto", "black%20clover", "mob%20psycho", "my%20hero%20academia", "darling%20in%20the%20franxx", "konosuba", "deathnote", "evangelion", "fire%20force", "dr%20stone", "your%20lie%20in%20april"};
        String query = queries[(int)(Math.random()*queries.length)];
        Jiphy jiphy = Jiphy.builder()
            .apiKey(JiphyConstants.API_KEY_BETA)
            .build();
        ArrayList<String> gifs = new ArrayList<>();
        try {
            jiphy.sendRequest(new JiphySearchRequest(query)).getData().forEach(g ->
                gifs.add(g.getUrl())
            );
        } catch (IOException e) {}
        if(gifs.size() == 0) return;
        else event.getChannel().sendMessage(gifs.get((int)(Math.random()*gifs.size()))).queue();
    }

}
