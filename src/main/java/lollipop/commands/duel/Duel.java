package lollipop.commands.duel;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import lollipop.commands.duel.models.Game;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Duel implements Command {
    @Override
    public String[] getAliases() {
        return new String[]{"duel"};
    }

    @Override
    public String getCategory() {
        return "Fun";
    }

    @Override
    public String getHelp() {
        return "Duel somebody (or an AI) in a small fun and competitive battle game!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [user(optional)]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "mention a user", false);
    }

    public static HashMap<Long, Game> memberToGame = new HashMap<>();

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        if(memberToGame.containsKey(Objects.requireNonNull(event.getMember()).getIdLong())) {
            event.replyEmbeds(new EmbedBuilder()
                    .setDescription("You are already in a duel! Finish your current duel to be able to start a new one...")
                    .setColor(Color.red)
                    .build()
            ).queue(m -> m.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
            return;
        }
        if(args.isEmpty()) {
            Game game = new Game();
            game.homePlayer.member = event.getMember();
            game.opposingPlayer.member = null; //AI
            game.playerTurn = game.homePlayer;
            game.playerNotTurn = game.opposingPlayer;
            game.sendSelectMove(event.getTextChannel(), null);
            game.setupTimeout(event.getChannel());
            memberToGame.put(Objects.requireNonNull(event.getMember()).getIdLong(), game);
        } else if(args.size() == 1) {
            Game game = new Game();
            Member target = Tools.getEffectiveMember(event.getGuild(), args.get(0));
            if(target == event.getGuild().getSelfMember()) {
                game.homePlayer.member = event.getMember();
                game.opposingPlayer.member = null; //AI
                game.playerTurn = game.homePlayer;
                game.playerNotTurn = game.opposingPlayer;
                game.sendSelectMove(event.getTextChannel(), null);
                game.setupTimeout(event.getChannel());
                memberToGame.put(Objects.requireNonNull(event.getMember()).getIdLong(), game);
            } else {
                game.homePlayer.member = event.getMember();
                game.opposingPlayer.member = target;
                game.playerTurn = game.opposingPlayer;
                game.playerNotTurn = game.homePlayer;
                if(target == null) {
                    event.replyEmbeds(new EmbedBuilder()
                            .setDescription("I couldn't find the specified member! Try mentioning them in the command...\n> Example: `l!duel @bool`")
                            .setColor(Color.red)
                            .build()
                    ).queue();
                    return;
                }
                event.getChannel().sendMessage(target.getAsMention()).queue(m -> game.lastDisplay.add(m));
                event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setDescription(event.getMember().getAsMention() + " requested to duel you! Do you accept their duel request?")
                        .build()
                ).setActionRow(
                        Button.primary("accept", "accept")
                ).queue(m -> game.lastDisplay.add(m));
                game.timeout = event.replyEmbeds(new EmbedBuilder()
                        .setDescription(target.getAsMention() + " didn't arrive in time! The duel request expired...")
                        .setColor(Color.red)
                        .build()
                ).queueAfter(30, TimeUnit.SECONDS, m -> {
                    game.deleteDisplayMessagesFull();
                    Duel.memberToGame.remove(Objects.requireNonNull(event.getMember()).getIdLong());
                    Duel.memberToGame.remove(Objects.requireNonNull(target).getIdLong());
                });
                Duel.memberToGame.put(Objects.requireNonNull(event.getMember()).getIdLong(), game);
                Duel.memberToGame.put(Objects.requireNonNull(target).getIdLong(), game);
            }
        }
    }
}
