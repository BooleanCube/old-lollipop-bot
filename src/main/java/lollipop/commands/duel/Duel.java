package lollipop.commands.duel;

import lollipop.Constant;
import lollipop.Command;
import lollipop.Tools;
import lollipop.commands.duel.models.DGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;


import java.awt.*;
import java.util.ArrayList;
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
        return "Duel somebody (or an AI) in a small fun and competitive battle game!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [user*]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "mention a user", false);
    }

    // Game Settings and Occupancy
    public static HashMap<Long, DGame> memberToGame = new HashMap<>();
    public static ArrayList<Integer> occupiedShards = new ArrayList<>();

    @Override
    public void run(SlashCommandInteractionEvent event) {
        if(!event.getInteraction().isFromGuild()) {
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("This command can only be used in guilds!")
                            .build()
            ).queue();
            return;
        }
        final List<OptionMapping> options = event.getOptions();
        if(memberToGame.containsKey(event.getMember().getIdLong())) {
            event.replyEmbeds(new EmbedBuilder()
                    .setDescription("You are already in a duel! Finish your current duel to be able to start a new one...")
                    .setColor(Color.red)
                    .build()
            ).setEphemeral(true).queue();
            return;
        }
        int shardId = event.getJDA().getShardInfo().getShardId();
        if(occupiedShards.contains(shardId)) {
            event.replyEmbeds(new EmbedBuilder()
                            .setDescription("There is a concurrent duel in this shard! Please wait until the current duel ends...")
                            .setFooter("There is a limit of 1 duel per shard to prevent the players from having a laggy experience. Please be patient!")
                            .setColor(Color.red)
                            .build()
            ).setEphemeral(true).queue();
            return;
        }
        else occupiedShards.add(shardId);
        if(options.isEmpty()) {
            DGame DGame = new DGame();
            DGame.homePlayer.member = event.getMember();
            DGame.opposingPlayer.member = null; //AI
            DGame.playerTurn = DGame.homePlayer;
            DGame.playerNotTurn = DGame.opposingPlayer;
            DGame.sendStartSelectMove(event, null);
            DGame.setupTimeout(event.getChannel());
            memberToGame.put(Objects.requireNonNull(event.getMember()).getIdLong(), DGame);
        } else if(options.size() == 1) {
            DGame DGame = new DGame();
            Member target = options.get(0).getAsMember();
            if(target == event.getGuild().getSelfMember()) {
                DGame.homePlayer.member = event.getMember();
                DGame.opposingPlayer.member = null; //AI
                DGame.playerTurn = DGame.homePlayer;
                DGame.playerNotTurn = DGame.opposingPlayer;
                DGame.sendStartSelectMove(event, null);
                DGame.setupTimeout(event.getChannel());
                memberToGame.put(Objects.requireNonNull(event.getMember()).getIdLong(), DGame);
            } else {
                if(target == null) {
                    event.replyEmbeds(new EmbedBuilder()
                            .setDescription("I couldn't find the specified member! Try mentioning them in the command...\n> Example: `" + Constant.PREFIX + "duel @bool`")
                            .setColor(Color.red)
                            .build()
                    ).queue();
                    return;
                }
                if(target.getUser().isBot()) {
                    event.replyEmbeds(new EmbedBuilder()
                            .setDescription("You can't request a duel with other bots!\n> Example: `" + Constant.PREFIX + "duel @bool`")
                            .setColor(Color.red)
                            .build()
                    ).queue();
                    return;
                }
                if(target == event.getMember()) {
                    event.replyEmbeds(new EmbedBuilder()
                            .setDescription("You can't request a duel with yourself!\n> Example: `" + Constant.PREFIX + "duel @bool`")
                            .setColor(Color.red)
                            .build()
                    ).queue();
                    return;
                }
                DGame.homePlayer.member = event.getMember();
                DGame.opposingPlayer.member = target;
                DGame.playerTurn = DGame.opposingPlayer;
                DGame.playerNotTurn = DGame.homePlayer;
                DGame.lastDisplay.add(event.reply(target.getAsMention()).complete().retrieveOriginal().complete());
                event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setDescription(event.getMember().getAsMention() + " requested to duel you! Do you accept their duel request?")
                        .setFooter("Quick! You have 30 seconds to accept!")
                        .build()
                ).setActionRow(
                        Button.primary("accept", "accept")
                ).queue(m -> DGame.lastDisplay.add(m));
                DGame.timeout = event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setDescription(target.getAsMention() + " didn't arrive in time! The duel request expired...")
                        .setColor(Color.red)
                        .build()
                ).queueAfter(30, TimeUnit.SECONDS, m -> {
                    DGame.deleteDisplayMessagesFull();
                    Duel.memberToGame.remove(event.getMember().getIdLong());
                    Duel.memberToGame.remove(target.getIdLong());
                    occupiedShards.remove(Integer.valueOf(event.getJDA().getShardInfo().getShardId()));
                });
                Duel.memberToGame.put(event.getMember().getIdLong(), DGame);
                Duel.memberToGame.put(target.getIdLong(), DGame);
            }
        }
    }

}
