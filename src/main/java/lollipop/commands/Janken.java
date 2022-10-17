package lollipop.commands;

import lollipop.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Janken implements Command {

    @Override
    public String[] getAliases() {
        return new String[] {"janken"};
    }

    @Override
    public CommandType getCategory() {
        return CommandType.FUN;
    }

    @Override
    public String getHelp() {
        return "Play jankenpon with me!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [rock/paper/scissors]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this).addOptions(
                new OptionData(OptionType.STRING, "hand", "rock / paper / scissors", true)
                        .addChoice("rock", "rock")
                        .addChoice("paper", "paper")
                        .addChoice("scissors", "scissors")
        );
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        final List<String> args = options.stream().map(OptionMapping::getAsString).collect(Collectors.toList());
        if(args.size() == 1) {
            String choice = args.get(0);
            int num = 0;
            String[] mychoices = {"rock", "paper", "scissors"};
            if(!choice.equalsIgnoreCase("rock") && !choice.equalsIgnoreCase("paper") && !choice.equalsIgnoreCase("scissors")) {
                Tools.wrongUsage(event, this);
                return;
            }
            for(String str : mychoices) {
                if(str.equalsIgnoreCase(choice)) break;
                else num++;
            }
            int random = (int)(Math.random()*10)%mychoices.length;
            String myChoice = mychoices[random];
            String[] victory = {"I admit defeat! You are too powerful...", "How are you this strong?", "I can not match this power!", "I surrender! You are too powerful!", "I might need some backup."};
            String[] loss = {"Pathetic!", "Mudae!", "Piece of trash!", "Practice is key...", "Come at me with your full strength!", "Is that all you got?"};
            String[] tie = {"A worthy opponent!", "You are indeed powerful, but can you match my next move...", "Kuso! That was close!", "Try your hardest on me!", "Don't get afraid now..."};
            EmbedBuilder e = new EmbedBuilder();
            if(num == random) {
                e.setTitle(tie[(int)(Math.random()*tie.length)]);
                e.addField("Your Hand", choice, true);
                e.addField("My Hand", myChoice, true);
                e.setFooter("You didn't get any lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                e.setColor(Color.yellow);
                event.replyEmbeds(e.build()).queue();
            } else if(random == 0 && num == 2) {
                Runnable success = () -> {
                    int xp = (int)(Math.random()*6)-10;
                    xp = (int)(xp/Constant.MULTIPLIER);
                    Database.addToUserBalance(event.getUser().getId(), xp);
                    e.setTitle(loss[(int)(Math.random()*loss.length)]);
                    e.addField("Your Hand", choice, true);
                    e.addField("My Hand", myChoice, true);
                    e.setFooter("You lost " + (-1*xp) + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    e.setColor(Color.red);
                    event.replyEmbeds(e.build()).queue();
                };
                Runnable failure = () -> {
                    int xp = (int)(Math.random()*6)-10;
                    Database.addToUserBalance(event.getUser().getId(), xp);
                    e.setTitle(loss[(int)(Math.random()*loss.length)]);
                    e.addField("Your Hand", choice, true);
                    e.addField("My Hand", myChoice, true);
                    e.setFooter("You lost " + (-1*xp) + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    e.setColor(Color.red);
                    event.replyEmbeds(e.build()).queue();
                };
                BotStatistics.sendMultiplier(event.getUser().getId(), success, failure);
            } else if(random == 2 && num == 0) {
                Runnable success = () -> {
                    int xp = (int)(Math.random()*6)+10;
                    xp = (int)(xp*Constant.MULTIPLIER);
                    Database.addToUserBalance(event.getUser().getId(), xp);
                    e.setTitle(victory[(int)(Math.random()*victory.length)]);
                    e.addField("Your Hand", choice, true);
                    e.addField("My Hand", myChoice, true);
                    e.setFooter("You won " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    e.setColor(Color.green);
                    event.replyEmbeds(e.build()).queue();
                };
                Runnable failure = () -> {
                    int xp = (int)(Math.random()*6)+10;
                    Database.addToUserBalance(event.getUser().getId(), xp);
                    e.setTitle(victory[(int)(Math.random()*victory.length)]);
                    e.addField("Your Hand", choice, true);
                    e.addField("My Hand", myChoice, true);
                    e.setFooter("You won " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    e.setColor(Color.green);
                    event.replyEmbeds(e.build()).queue();
                };
                BotStatistics.sendMultiplier(event.getUser().getId(), success, failure);
            } else if(random > num) {
                Runnable success = () -> {
                    int xp = (int)(Math.random()*6)-10;
                    xp = (int)(xp/Constant.MULTIPLIER);
                    Database.addToUserBalance(event.getUser().getId(), xp);
                    e.setTitle(loss[(int)(Math.random()*loss.length)]);
                    e.addField("Your Hand", choice, true);
                    e.addField("My Hand", myChoice, true);
                    e.setFooter("You lost " + (-1*xp) + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    e.setColor(Color.red);
                    event.replyEmbeds(e.build()).queue();
                };
                Runnable failure = () -> {
                    int xp = (int)(Math.random()*6)-10;
                    Database.addToUserBalance(event.getUser().getId(), xp);
                    e.setTitle(loss[(int)(Math.random()*loss.length)]);
                    e.addField("Your Hand", choice, true);
                    e.addField("My Hand", myChoice, true);
                    e.setFooter("You lost " + (-1*xp) + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    e.setColor(Color.red);
                    event.replyEmbeds(e.build()).queue();
                };
                BotStatistics.sendMultiplier(event.getUser().getId(), success, failure);
            } else {
                Runnable success = () -> {
                    int xp = (int)(Math.random()*6)+10;
                    xp = (int)(xp*Constant.MULTIPLIER);
                    Database.addToUserBalance(event.getUser().getId(), xp);
                    e.setTitle(victory[(int)(Math.random()*victory.length)]);
                    e.addField("Your Hand", choice, true);
                    e.addField("My Hand", myChoice, true);
                    e.setFooter("You won " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    e.setColor(Color.green);
                    event.replyEmbeds(e.build()).queue();
                };
                Runnable failure = () -> {
                    int xp = (int)(Math.random()*6)+10;
                    Database.addToUserBalance(event.getUser().getId(), xp);
                    e.setTitle(victory[(int)(Math.random()*victory.length)]);
                    e.addField("Your Hand", choice, true);
                    e.addField("My Hand", myChoice, true);
                    e.setFooter("You won " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    e.setColor(Color.green);
                    event.replyEmbeds(e.build()).queue();
                };
                BotStatistics.sendMultiplier(event.getUser().getId(), success, failure);
            }
        } else Tools.wrongUsage(event, this);
    }

}
