package lollipop.listeners;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Reacts to messages with a lollipop emoji
 */
public class LollipopReaction extends ListenerAdapter {

    /**
     * Triggered when a message is sent
     * @param event
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().toLowerCase().contains("lollipop") &&
                ((event.getMessage().isFromGuild() &&
                event.getGuild().getSelfMember().hasPermission(event.getGuildChannel(), Permission.MESSAGE_ADD_REACTION) &&
                event.getGuild().getSelfMember().hasPermission(event.getGuildChannel(), Permission.MESSAGE_HISTORY)) ||
                !event.getMessage().isFromGuild()))
            event.getMessage().addReaction("\uD83C\uDF6D").queue();
    }

}
