package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.List;

public class Janken implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"janken"};
    }

    @Override
    public String getCategory() {
        return "Fun";
    }

    @Override
    public String getHelp() {
        return "Play jankenpon with me!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [rock/paper/scissors]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.STRING, "hand", "rock / paper / scissors", true);
    }

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        if(args.size() == 1) {
            String choice = args.get(0);
            int num = 0;
            String[] mychoices = {"rock", "paper", "scissors"};
            if(!choice.equalsIgnoreCase("rock") && !choice.equalsIgnoreCase("paper") && !choice.equalsIgnoreCase("scissors")) {
                Tools.wrongUsage(event.getTextChannel(), this);
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
                e.setDescription(tie[(int)(Math.random()*tie.length)]);
                e.addField("Your Hand", choice, true);
                e.addField("My Hand", myChoice, true);
                e.setColor(Color.yellow);
            } else if(random == 0 && num == 2) {
                e.setDescription(loss[(int)(Math.random()*loss.length)]);
                e.addField("Your Hand", choice, true);
                e.addField("My Hand", myChoice, true);
                e.setColor(Color.red);
            } else if(random == 2 && num == 0) {
                e.setDescription(victory[(int)(Math.random()*victory.length)]);
                e.addField("Your Hand", choice, true);
                e.addField("My Hand", myChoice, true);
                e.setColor(Color.green);
            } else if(random > num) {
                e.setDescription(loss[(int)(Math.random()*loss.length)]);
                e.addField("Your Hand", choice, true);
                e.addField("My Hand", myChoice, true);
                e.setColor(Color.red);
            } else if(num > random) {
                e.setDescription(victory[(int)(Math.random()*victory.length)]);
                e.addField("Your Hand", choice, true);
                e.addField("My Hand", myChoice, true);
                e.setColor(Color.green);
            }
            event.replyEmbeds(e.build()).queue();
        } else Tools.wrongUsage(event.getTextChannel(), this);
    }
}
