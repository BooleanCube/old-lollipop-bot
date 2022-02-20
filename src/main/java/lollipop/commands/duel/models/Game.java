package lollipop.commands.duel.models;

import lollipop.Constant;
import lollipop.commands.duel.Duel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;


import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Game {
    public Player homePlayer = new Player();
    public Player opposingPlayer = new Player();
    public Player playerTurn = new Player();
    public Player playerNotTurn = new Player();
    public ScheduledFuture<?> timeout = null;
    public ScheduledFuture<?> editTimeout = null;
    public ArrayList<Message> lastDisplay = new ArrayList<>();
    public static Button[] moveButtons = {
            Button.secondary("attack1", "punch"),
            Button.secondary("attack2", "kick"),
            Button.secondary("attack3", "headbutt"),
            Button.secondary("attack4", "chop"),
            Button.secondary("heal", "eat"),
            Button.secondary("strength", "breathe"),
            Button.secondary("defend1", "shield"),
            Button.secondary("defend2", "block"),
            Button.primary("4thgear", "4th gear"),
            Button.primary("hinokami", "hinokami"),
            Button.primary("rasengan", "rasengan"),
            Button.primary("ora", "ora"),
            Button.primary("seriouspunch", "serious punch"),
            Button.primary("zawarudo", "za warudo"),
            Button.primary("yare", "yare yare daze")
    };
    public static Button surrenderButton = Button.danger("ff", "surrender");

    public static String getAvailableMoves() {
        StringBuilder sb = new StringBuilder();
        for(Button b : moveButtons) sb.append("`").append(b.getLabel()).append("`, ");
        return sb.substring(0, sb.length()-2);
    }

    public static String moveDescription(String name) {
        if(name.equalsIgnoreCase("punch")) return "**Punch your opponent... *unseriously*!**\n(Does `5-10 HP` damage to the opponent by default)\n> `Type`: Regular Attack\n> `Blockable`: True";
        if(name.equalsIgnoreCase("kick")) return "**Kick your opponent with power!**\n(Does `5-10 HP` damage to the opponent by default)\n> `Type`: Regular Attack\n> `Blockable`: True";
        if(name.equalsIgnoreCase("headbutt")) return "**Headbutt your opponent like Tanjiro!**\n(Does `5-10 HP` damage to the opponent by default)\n> `Type`: Regular Attack\n> `Blockable`: True";
        if(name.equalsIgnoreCase("chop")) return "**Chop your opponent with a knifehand!**\n(Does `5-10 HP` damage to the opponent by default)\n> `Type`: Regular Attack\n> `Blockable`: True";
        if(name.equalsIgnoreCase("eat")) return "**Eat some food to regenerate your health!**\n(Regenerates your health by `20-30 HP`)\n> `Type`: Regenerate\n> `Blockable`: False";
        if(name.equalsIgnoreCase("breathe")) return "**Take in a deep breath and become stronger!**\n(Increases your attack strength by `3-5 HP`)\n> `Type`: Strength Gain\n> `Blockable`: False";
        if(name.equalsIgnoreCase("shield")) return "**Use a shield to cover yourself!**\n(Lasts until the opponent uses a blockable move)\n> `Type`: Defense\n> `Blockable`: False";
        if(name.equalsIgnoreCase("block")) return "**Dodge the opponent's next attack!**\n(Lasts until the opponent uses a blockable move)\n> `Type`: Defense\n> `Blockable`: False";
        if(name.equalsIgnoreCase("4th gear") || name.equalsIgnoreCase("4thgear")) return "**Bounce your opponent back to hell...**\n(Does `13-18 HP` to the opponent by default)\n> `Type`: Ultimate\n> `Blockable`: True";
        if(name.equalsIgnoreCase("hinokami")) return "**Slice your opponent's head off**\n(Does `13-18 HP` to the opponent by default)\n> `Type`: Ultimate\n> `Blockable`: True";
        if(name.equalsIgnoreCase("rasengan")) return "**Blast your opponents with some chakra!**\n(Does `14-19 HP` to the opponent by default)\n> `Type`: Ultimate\n> `Blockable`: True";
        if(name.equalsIgnoreCase("ora")) return "**Pound your opponents with multiple powerful shots in a very short amount of time!**\n(Does `15-20 HP` to the opponent by default)\n> `Type`: Ultimate\n> `Blockable`: True";
        if(name.equalsIgnoreCase("serious punch") || name.equalsIgnoreCase("seriouspunch")) return "**Punch your opponent... *seriously*!**\n(Does `40-50 HP` to the opponent by default but does 20-30 HP when the opponent is blocking (not inclusive of strength gain))\n> `Type`: Ultimate\n> `Blockable`: False";
        if(name.equalsIgnoreCase("za warudo") || name.equalsIgnoreCase("zawarudo") || name.equalsIgnoreCase("the world")) return "**Freeze your opponent in time!**\n(This freezes the opponent for `5 seconds` allowing for the attacker to use as many moves as they can in 5 seconds!)\n> `Type`: Ultimate\n> `Blockable`: False";
        if(name.equalsIgnoreCase("yare yare daze") || name.equalsIgnoreCase("yare") || name.equalsIgnoreCase("yareyaredaze")) return "**yare yare daze**\n(Say *yare yare daze*, pound your opponent with an ora and make them weaker. Their attacks will `5-15 HP` weaker and take `15-20 HP` damage by default)\n> `Type`: Ultimate\n> `Blockable`: False";
        return null;
    }

    public void sendStartSelectMove(SlashCommandInteractionEvent event, String move) {
        TextChannel c = event.getTextChannel();
        if(homePlayer.HP < 0) homePlayer.HP = 0;
        if(opposingPlayer.HP < 0) opposingPlayer.HP = 0;
        if(playerTurn.member == null) {
            String homePName = homePlayer.member.getEffectiveName();
            EmbedBuilder e = new EmbedBuilder()
                    .setDescription("**" + move + "**")
                    .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                    .addField("Computer", "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                    .setAuthor("Computer", "https://github.com/BooleanCube/lollipop-bot", "https://www.pngkey.com/png/full/0-8970_open-my-computer-icon-circle.png");
            lastDisplay.add(c.sendMessageEmbeds(e.build()).complete());
        } else {
            String homePName = homePlayer.member.getEffectiveName();
            if(move == null) {
                move = "I will accept this challenge and become your opponent! Let the duel begin!";
                if(playerNotTurn.member == null) {
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                            .addField("Computer", "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                            .setAuthor("Computer", "https://github.com/BooleanCube/lollipop-bot", "https://www.pngkey.com/png/full/0-8970_open-my-computer-icon-circle.png");
                    lastDisplay.add(event.replyEmbeds(e.build()).complete().retrieveOriginal().complete());
                    int x = (int)(Math.random()*3);
                    int y = x + (int)(Math.random()*3)+1;
                    int z = y + (int)(Math.random()*9)+1;
                    lastDisplay.add(c.sendMessageEmbeds(new EmbedBuilder()
                            .setAuthor(playerTurn.member.getEffectiveName() + "'s turn", "https://github.com/BooleanCube/lollipop-bot", playerTurn.member.getEffectiveAvatarUrl())
                            .setDescription("What is your move?")
                            .setFooter("Quick! You have 30 seconds to react!")
                            .build()
                    ).complete().editMessageComponents().setActionRow(
                            moveButtons[x],
                            moveButtons[y],
                            moveButtons[z],
                            surrenderButton
                    ).complete());
                } else {
                    String oppoPName = opposingPlayer.member.getEffectiveName();
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                            .addField(oppoPName, "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                            .setAuthor(playerTurn.member.getEffectiveName(), "https://github.com/BooleanCube/lollipop-bot", playerTurn.member.getEffectiveAvatarUrl());
                    lastDisplay.add(event.replyEmbeds(e.build()).complete().retrieveOriginal().complete());
                }
            } else {
                if(opposingPlayer.member == null) {
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .addField(homePName, "Health: `" + homePlayer.HP + " HP`\nStrength Gain: `" + homePlayer.strengthGain + " HP`", true)
                            .addField("Computer", "Health: `" + opposingPlayer.HP + " HP`\nStrength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                            .setAuthor(playerTurn.member.getEffectiveName(), playerTurn.member.getUser().getAvatarUrl(), playerTurn.member.getUser().getEffectiveAvatarUrl());
                    lastDisplay.add(c.sendMessageEmbeds(e.build()).complete());
                } else {
                    String oppoPName = opposingPlayer.member.getEffectiveName();
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                            .addField(oppoPName, "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                            .setAuthor(playerTurn.member.getEffectiveName(), playerTurn.member.getUser().getAvatarUrl(), playerTurn.member.getUser().getEffectiveAvatarUrl());
                    lastDisplay.add(c.sendMessageEmbeds(e.build()).complete());
                }
            }
        }
        if(playerNotTurn.member != null && !playerNotTurn.isTimedOut()) {
            int x = (int)(Math.random()*3);
            int y = x + (int)(Math.random()*3)+1;
            int z = y + (int)(Math.random()*9)+1;
            lastDisplay.add(c.sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor(playerNotTurn.member.getEffectiveName() + "'s turn", "https://github.com/BooleanCube/lollipop-bot", playerNotTurn.member.getEffectiveAvatarUrl())
                    .setDescription("What is your move?")
                    .setFooter("Quick! You have 30 seconds to react!")
                    .build()
            ).complete().editMessageComponents().setActionRow(
                    moveButtons[x],
                    moveButtons[y],
                    moveButtons[z],
                    surrenderButton
            ).complete());
        }
        if(!event.isAcknowledged()) event.deferReply().queue();
    }

    public void sendStartSelectMove(ButtonInteractionEvent event, String move) {
        TextChannel c = event.getTextChannel();
        if(homePlayer.HP < 0) homePlayer.HP = 0;
        if(opposingPlayer.HP < 0) opposingPlayer.HP = 0;
        if(playerTurn.member == null) {
            String homePName = homePlayer.member.getEffectiveName();
            EmbedBuilder e = new EmbedBuilder()
                    .setDescription("**" + move + "**")
                    .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                    .addField("Computer", "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                    .setAuthor("Computer", "https://github.com/BooleanCube/lollipop-bot", "https://www.pngkey.com/png/full/0-8970_open-my-computer-icon-circle.png");
            lastDisplay.add(c.sendMessageEmbeds(e.build()).complete());
        } else {
            String homePName = homePlayer.member.getEffectiveName();
            if(move == null) {
                move = "I will accept this challenge and become your opponent! Let the duel begin!";
                if(playerNotTurn.member == null) {
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                            .addField("Computer", "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                            .setAuthor("Computer", "https://github.com/BooleanCube/lollipop-bot", "https://www.pngkey.com/png/full/0-8970_open-my-computer-icon-circle.png");
                    lastDisplay.add(event.replyEmbeds(e.build()).complete().retrieveOriginal().complete());
                    int x = (int)(Math.random()*3);
                    int y = x + (int)(Math.random()*3)+1;
                    int z = y + (int)(Math.random()*9)+1;
                    lastDisplay.add(c.sendMessageEmbeds(new EmbedBuilder()
                            .setAuthor(playerTurn.member.getEffectiveName() + "'s turn", "https://github.com/BooleanCube/lollipop-bot", playerTurn.member.getEffectiveAvatarUrl())
                            .setDescription("What is your move?")
                            .setFooter("Quick! You have 30 seconds to react!")
                            .build()
                    ).complete().editMessageComponents().setActionRow(
                            moveButtons[x],
                            moveButtons[y],
                            moveButtons[z],
                            surrenderButton
                    ).complete());
                } else {
                    String oppoPName = opposingPlayer.member.getEffectiveName();
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                            .addField(oppoPName, "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                            .setAuthor(playerTurn.member.getEffectiveName(), "https://github.com/BooleanCube/lollipop-bot", playerTurn.member.getEffectiveAvatarUrl());
                    lastDisplay.add(event.replyEmbeds(e.build()).complete().retrieveOriginal().complete());
                }
            } else {
                if(opposingPlayer.member == null) {
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .addField(homePName, "Health: `" + homePlayer.HP + " HP`\nStrength Gain: `" + homePlayer.strengthGain + " HP`", true)
                            .addField("Computer", "Health: `" + opposingPlayer.HP + " HP`\nStrength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                            .setAuthor(playerTurn.member.getEffectiveName(), playerTurn.member.getUser().getAvatarUrl(), playerTurn.member.getUser().getEffectiveAvatarUrl());
                    lastDisplay.add(c.sendMessageEmbeds(e.build()).complete());
                } else {
                    String oppoPName = opposingPlayer.member.getEffectiveName();
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                            .addField(oppoPName, "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                            .setAuthor(playerTurn.member.getEffectiveName(), playerTurn.member.getUser().getAvatarUrl(), playerTurn.member.getUser().getEffectiveAvatarUrl());
                    lastDisplay.add(c.sendMessageEmbeds(e.build()).complete());
                }
            }
        }
        if(playerNotTurn.member != null && !playerNotTurn.isTimedOut()) {
            int x = (int)(Math.random()*3);
            int y = x + (int)(Math.random()*3)+1;
            int z = y + (int)(Math.random()*9)+1;
            lastDisplay.add(c.sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor(playerNotTurn.member.getEffectiveName() + "'s turn", "https://github.com/BooleanCube/lollipop-bot", playerNotTurn.member.getEffectiveAvatarUrl())
                    .setDescription("What is your move?")
                    .setFooter("Quick! You have 30 seconds to react!")
                    .build()
            ).complete().editMessageComponents().setActionRow(
                    moveButtons[x],
                    moveButtons[y],
                    moveButtons[z],
                    surrenderButton
            ).complete());
        }
    }

    public void sendSelectMove(ButtonInteractionEvent event, String move) {
        if(homePlayer.HP < 0) homePlayer.HP = 0;
        if(opposingPlayer.HP < 0) opposingPlayer.HP = 0;
        if(playerTurn.member == null) {
            String homePName = homePlayer.member.getEffectiveName();
            EmbedBuilder e = new EmbedBuilder()
                    .setDescription("**" + move + "**")
                    .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                    .addField("Computer", "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                    .setAuthor("Computer", "https://github.com/BooleanCube/lollipop-bot", "https://www.pngkey.com/png/full/0-8970_open-my-computer-icon-circle.png");
            lastDisplay.get(0).editMessageEmbeds(e.build()).queue();
        } else {
            String homePName = homePlayer.member.getEffectiveName();
            if(move == null) {
                move = "I will accept this challenge and become your opponent! Let the duel begin!";
                if(playerNotTurn.member == null) {
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                            .addField("Computer", "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                            .setAuthor("Computer", "https://github.com/BooleanCube/lollipop-bot", "https://www.pngkey.com/png/full/0-8970_open-my-computer-icon-circle.png");
                    lastDisplay.get(0).editMessageEmbeds(e.build()).queue();
                    int x = (int)(Math.random()*3);
                    int y = x + (int)(Math.random()*3)+1;
                    int z = y + (int)(Math.random()*9)+1;
                    lastDisplay.get(1).editMessageEmbeds(new EmbedBuilder()
                            .setAuthor(playerNotTurn.member.getEffectiveName() + "'s turn", "https://github.com/BooleanCube/lollipop-bot", playerNotTurn.member.getEffectiveAvatarUrl())
                            .setDescription("What is your move?")
                            .setFooter("Quick! You have 30 seconds to react!")
                            .build()
                    ).setActionRow(
                            moveButtons[x].asDisabled(),
                            moveButtons[y].asDisabled(),
                            moveButtons[z].asDisabled(),
                            surrenderButton.asDisabled()
                    ).queue();
                    editTimeout = lastDisplay.get(1).editMessageComponents().setActionRow(
                            moveButtons[x],
                            moveButtons[y],
                            moveButtons[z],
                            surrenderButton
                    ).queueAfter(1, TimeUnit.SECONDS);
                } else {
                    String oppoPName = opposingPlayer.member.getEffectiveName();
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                            .addField(oppoPName, "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                            .setAuthor(playerTurn.member.getEffectiveName(), "https://github.com/BooleanCube/lollipop-bot", playerTurn.member.getEffectiveAvatarUrl());
                    lastDisplay.get(0).editMessageEmbeds(e.build()).queue();
                }
            } else {
                if(opposingPlayer.member == null) {
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .addField(homePName, "Health: `" + homePlayer.HP + " HP`\nStrength Gain: `" + homePlayer.strengthGain + " HP`", true)
                            .addField("Computer", "Health: `" + opposingPlayer.HP + " HP`\nStrength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                            .setAuthor(playerTurn.member.getEffectiveName(), playerTurn.member.getUser().getAvatarUrl(), playerTurn.member.getUser().getEffectiveAvatarUrl());
                    lastDisplay.get(0).editMessageEmbeds(e.build()).queue();
                } else {
                    String oppoPName = opposingPlayer.member.getEffectiveName();
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                            .addField(oppoPName, "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                            .setAuthor(playerTurn.member.getEffectiveName(), playerTurn.member.getUser().getAvatarUrl(), playerTurn.member.getUser().getEffectiveAvatarUrl());
                    lastDisplay.get(0).editMessageEmbeds(e.build()).queue();
                }
            }
        }
        if(playerNotTurn.member != null && playerTurn.isTimedOut()) {
            int x = (int)(Math.random()*3);
            int y = x + (int)(Math.random()*3)+1;
            int z = y + (int)(Math.random()*9)+1;
            try {
                lastDisplay.get(1).editMessageEmbeds(new EmbedBuilder()
                        .setAuthor(playerNotTurn.member.getEffectiveName() + "'s turn", "https://github.com/BooleanCube/lollipop-bot", playerNotTurn.member.getEffectiveAvatarUrl())
                        .setDescription("What is your move?")
                        .setFooter("Quick! You have 30 seconds to react!")
                        .build()
                ).setActionRow(
                        moveButtons[x].asDisabled(),
                        moveButtons[y].asDisabled(),
                        moveButtons[z].asDisabled(),
                        surrenderButton.asDisabled()
                ).queue();
                editTimeout = lastDisplay.get(1).editMessageComponents().setActionRow(
                        moveButtons[x],
                        moveButtons[y],
                        moveButtons[z],
                        surrenderButton
                ).queueAfter(1, TimeUnit.SECONDS);
            } catch(Exception ignored) {}
        }
        else if(playerNotTurn.member != null && !playerNotTurn.isTimedOut()) {
            int x = (int)(Math.random()*3);
            int y = x + (int)(Math.random()*3)+1;
            int z = y + (int)(Math.random()*9)+1;
            try {
                lastDisplay.get(1).editMessageEmbeds(new EmbedBuilder()
                        .setAuthor(playerNotTurn.member.getEffectiveName() + "'s turn", "https://github.com/BooleanCube/lollipop-bot", playerNotTurn.member.getEffectiveAvatarUrl())
                        .setDescription("What is your move?")
                        .setFooter("Quick! You have 30 seconds to react!")
                        .build()
                ).setActionRow(
                        moveButtons[x].asDisabled(),
                        moveButtons[y].asDisabled(),
                        moveButtons[z].asDisabled(),
                        surrenderButton.asDisabled()
                ).queue();
                editTimeout = lastDisplay.get(1).editMessageComponents().setActionRow(
                        moveButtons[x],
                        moveButtons[y],
                        moveButtons[z],
                        surrenderButton
                ).queueAfter(3, TimeUnit.SECONDS);
            } catch(Exception ignored) {}
        }
        if(!event.isAcknowledged()) event.deferEdit().queue();
    }

    public void switchTurns() {
        Player temp = playerTurn;
        playerTurn = playerNotTurn;
        playerNotTurn = temp;
    }

    public void deleteDisplayMessagesFull() {
        lastDisplay.forEach(msg -> {
            msg.delete().queue();
        });
        lastDisplay = new ArrayList<>();
    }

    //spares the last display message
    public void deleteDisplayMessages() {
        for(int i=0; i<lastDisplay.size()-1; i++) {
            lastDisplay.get(i).delete().queue();
            lastDisplay.remove(i);
        }
    }

    public String AIMove(Player h, Player o) {

        //Decides move
        String move;
        if(!h.isDefending && ((h.HP <= 30 && Math.random()*8<6) || Math.random()*6<3)) move = "defend";
        else if((h.HP <= 50 && Math.random()*5<3) || Math.random()*6<2) move = "heal";
        else if(!h.isZaWarudo && h.HP <= 35 && Math.random()*18<4+Math.random()*2) move = "zawarudo";
        else if((o.isDefending && Math.random()*11<6) || Math.random()*11<3) move = "strength";
        else if(Math.random()*16<3) move = "4thgear";
        else if(Math.random()*16<3) move = "hinokami";
        else if(Math.random()*16<3) move = "rasengan";
        else if(Math.random()*16<3) move = "ora";
        else if((o.isDefending && Math.random()*16<6) || Math.random()*16<3) move = "seriouspunch";
        else if(!h.isZaWarudo && Math.random()*16<3) move = "zawarudo";
        else if((o.strengthGain >= 12 && Math.random()*16<8) || Math.random()*16<3) move = "yare";
        else move = "attack";

        //Implements Move
        if(move.startsWith("attack")) {
            if(playerNotTurn.isDefending) {
                playerNotTurn.isDefending = false;
                return playerNotTurn.member.getAsMention() + " blocked `Computer`'s attack!\n";
            } else {
                int damage = (int)(Math.random()*6)+5+playerTurn.strengthGain;
                playerNotTurn.HP -= damage;
                return "`Computer` attacked their opponent and did `" + damage + " HP` damage!\n";
            }
        } else if(move.startsWith("defend")) {
            playerTurn.isDefending = true;
            return "`Computer` is defending himself!\n";
        } else if(move.startsWith("heal")) {
            int health = (int)(Math.random()*11)+20;
            playerTurn.HP += health;
            return "`Computer` healed himself and gained `" + health + " HP`!\n";
        } else if(move.startsWith("strength")) {
            playerTurn.strengthGain += (int)(Math.random()*3)+3;
            return "`Computer` took a deep breath and became much stronger! Their attacks will do more damage..\n";
        } else if(move.startsWith("ora")) {
            if(playerNotTurn.isDefending) {
                String name = playerNotTurn.member != null ? playerNotTurn.member.getAsMention() : "`Computer`";
                playerNotTurn.isDefending = false;
                return name + " blocked `Computer`'s ORA attack!";
            } else {
                int damage = (int)(Math.random()*6)+15+playerTurn.strengthGain;
                playerNotTurn.HP -= damage;
                return "ORA ORA ORA ORA ORRAAAA\n`Computer` pounded their opponent and did `" + damage + " HP` damage!";
            }
        } else if(move.startsWith("seriouspunch")) {
            if(playerNotTurn.isDefending) {
                int damage = (int)(Math.random()*11)+20+playerTurn.strengthGain;
                playerNotTurn.HP -= damage;
                playerNotTurn.isDefending = false;
                return "Anybody in my way... gets punched.\n`Computer` punched their opponent and did `" + damage + " HP` damage!";
            } else {
                int damage = (int)(Math.random()*11)+40+playerTurn.strengthGain;
                playerNotTurn.HP -= damage;
                return "Anybody in my way... gets punched.\n`Computer` punched their opponent and did `" + damage + " HP` damage!";
            }
        } else if(move.startsWith("rasengan")) {
            if(playerNotTurn.isDefending) {
                String name = playerNotTurn.member != null ? playerNotTurn.member.getAsMention() : "`Computer`";
                playerNotTurn.isDefending = false;
                return name + " blocked `Computer`'s Rasengan!";
            } else {
                int damage = (int)(Math.random()*6)+14+playerTurn.strengthGain;
                playerNotTurn.HP -= damage;
                return "RASENGAN!\n`Computer` hit their opponent with a rasengan and did `" + damage + " HP` damage!";
            }
        } else if(move.startsWith("4thgear")) {
            if(playerNotTurn.isDefending) {
                String name = playerNotTurn.member != null ? playerNotTurn.member.getAsMention() : "`Computer`";
                playerNotTurn.isDefending = false;
                return name + " blocked `Computer`'s Gum-Gum Kong Gun!";
            } else {
                int damage = (int)(Math.random()*6)+13+playerTurn.strengthGain;
                playerNotTurn.HP -= damage;
                return "*boing* 4th GEAR *boing*\n`Computer` blasted their opponents away with Gum-Gum Kong Gun and did `" + damage + " HP` damage!";
            }
        } else if(move.startsWith("hinokami")) {
            if(playerNotTurn.isDefending) {
                String name = playerNotTurn.member != null ? playerNotTurn.member.getAsMention() : "`Computer`";
                playerNotTurn.isDefending = false;
                return name + " blocked `Computer`'s hinokami attack!";
            } else {
                int damage = (int)(Math.random()*6)+13+playerTurn.strengthGain;
                playerNotTurn.HP -= damage;
                return "HINOKAMI KAGURA!\n`Computer` sliced the opponent's head off and did `" + damage + " HP` damage!";
            }
        } else if(move.startsWith("yare")) {
            int damage = (int)(Math.random()*6)+15+playerTurn.strengthGain;
            playerNotTurn.HP -= damage;
            playerNotTurn.strengthGain -= (int)(Math.random()*3)+3;
            if(playerNotTurn.strengthGain < -10) playerNotTurn.strengthGain = -10;
            return "yare yare daze...\nORA! `Copmputer` did `" + damage + " HP` damage and made the opponent weaker! Their attacks will do less damage..";
        } else if(move.startsWith("zawarudo")) {
            playerNotTurn.timeoutStart = System.currentTimeMillis();
            playerNotTurn.timeoutDuration = Math.random()+5;
            playerTurn.isZaWarudo = true;
            return "ZA WARUDO!\n`Computer` stopped time. Their opponent is frozen for `5 seconds`.";
        }

        //Duel closes if anything above doesn't work for some reason
        Duel.memberToGame.remove(homePlayer.member.getIdLong());
        Duel.memberToGame.remove(opposingPlayer.member.getIdLong());
        return "Error! This duel match will be closing...";
    }

    public void setupTimeout(MessageChannel c) {
        String[] victoryMsg = {"You are too strong...", "That kind of power should be illegal!", "You are a god amongst men!", "How did you get so much power?", "Nobody dares to duel with you!"};
        EmbedBuilder e = new EmbedBuilder().setColor(Color.green).setFooter("Type " + Constant.PREFIX + "duel to start another duel with me!");
        if(playerNotTurn.member == null) e.setAuthor("Computer won the duel!", "https://github.com/BooleanCube/lollipop-bot", "https://www.pngkey.com/png/full/0-8970_open-my-computer-icon-circle.png");
        else e.setAuthor(playerNotTurn.member.getEffectiveName() + " won the duel!", playerNotTurn.member.getUser().getAvatarUrl(), playerNotTurn.member.getUser().getEffectiveAvatarUrl());
        e.setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)]);
        e.setDescription(playerTurn.member.getAsMention() + " didn't react fast enough, so I assumed they surrendered!");
        timeout = c.sendMessageEmbeds(e.build()).queueAfter(30, TimeUnit.SECONDS, me -> {
            deleteDisplayMessagesFull();
            Duel.memberToGame.remove(playerTurn.member.getIdLong());
            if(playerNotTurn.member != null) Duel.memberToGame.remove(playerNotTurn.member.getIdLong());
        });
    }

    public boolean checkWin(MessageChannel c) {
        String[] victoryMsg = {"You are too strong...", "That kind of power should be illegal!", "You are a god amongst men!", "How did you get so much power?", "Nobody dares to duel with you!"};
        if(homePlayer.HP <= 0) {
            editTimeout.cancel(false);
            deleteDisplayMessagesFull();
            Duel.memberToGame.remove(homePlayer.member.getIdLong());
            EmbedBuilder e = new EmbedBuilder().setColor(Color.green).setFooter("Type l!duel to start another duel with me!");
            if(opposingPlayer.member == null) {
                e.setAuthor("Computer won the duel!", "https://github.com/BooleanCube/lollipop-bot", "https://www.pngkey.com/png/full/0-8970_open-my-computer-icon-circle.png");
                e.setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)]);
                e.setDescription("> " + homePlayer.member.getEffectiveName() + "'s HP: `0`\n" +
                        "> Computer's HP: `" + opposingPlayer.HP + "`");
            } else {
                e.setAuthor(opposingPlayer.member.getEffectiveName() + " won the duel!", opposingPlayer.member.getUser().getAvatarUrl(), opposingPlayer.member.getUser().getEffectiveAvatarUrl());
                e.setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)]);
                e.setDescription("> " + homePlayer.member.getEffectiveName() + "'s HP: `0`\n" +
                        "> " + opposingPlayer.member.getEffectiveName() + "'s HP: `" + opposingPlayer.HP + "`");
                Duel.memberToGame.remove(opposingPlayer.member.getIdLong());
            }
            c.sendMessageEmbeds(e.build()).queue();
            return true;
        } else if(opposingPlayer.HP <= 0) {
            Duel.memberToGame.remove(homePlayer.member.getIdLong());
            if(opposingPlayer.member != null) {
                Duel.memberToGame.remove(opposingPlayer.member.getIdLong());
                editTimeout.cancel(false);
                deleteDisplayMessagesFull();
                EmbedBuilder e = new EmbedBuilder()
                        .setColor(Color.green)
                        .setFooter("Type " + Constant.PREFIX + "duel to start another duel with me!")
                        .setAuthor(homePlayer.member.getEffectiveName() + " won the duel!", homePlayer.member.getUser().getAvatarUrl(), homePlayer.member.getUser().getEffectiveAvatarUrl())
                        .setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)])
                        .setDescription("> " + homePlayer.member.getEffectiveName() + "'s HP: `" + homePlayer.HP + "`\n" +
                                "> " + opposingPlayer.member.getEffectiveName() + "'s HP: `0`");
                c.sendMessageEmbeds(e.build()).queue();
            } else {
                editTimeout.cancel(false);

                deleteDisplayMessagesFull();
                EmbedBuilder e = new EmbedBuilder()
                        .setColor(Color.green)
                        .setFooter("Type " + Constant.PREFIX + "duel to start another duel with me!")
                        .setAuthor(homePlayer.member.getEffectiveName() + " won the duel!", homePlayer.member.getUser().getAvatarUrl(), homePlayer.member.getUser().getEffectiveAvatarUrl())
                        .setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)])
                        .setDescription("> " + homePlayer.member.getEffectiveName() + "'s HP: `" + homePlayer.HP + "`\n" +
                                "> Computer's HP: `0`");
                c.sendMessageEmbeds(e.build()).queue();
            }
            return true;
        }
        return false;
    }

    public void surrender(MessageChannel c, Player p) {
        String[] victoryMsg = {"You are too strong...", "That kind of power should be illegal!", "He is a god amongst men!", "How did you get so much power?", "Nobody dares to duel with you!"};
        Duel.memberToGame.remove(playerTurn.member.getIdLong());
        EmbedBuilder e = new EmbedBuilder().setColor(Color.green).setFooter("Type " + Constant.PREFIX + "duel to start another duel with me!");
        if(playerNotTurn.member == null) {
            e.setAuthor("Computer won the duel!", "https://github.com/BooleanCube/lollipop-bot", "https://www.pngkey.com/png/full/0-8970_open-my-computer-icon-circle.png");
            e.setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)]);
            e.setDescription(p.member.getEffectiveName() + " ran away from the duel!");
        } else {
            e.setAuthor(playerNotTurn.member.getEffectiveName() + " won the duel!", playerNotTurn.member.getUser().getAvatarUrl(), playerNotTurn.member.getUser().getEffectiveAvatarUrl());
            e.setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)]);
            e.setDescription(p.member.getAsMention() + " ran away from the duel!");
            Duel.memberToGame.remove(playerNotTurn.member.getIdLong());
        }
        c.sendMessageEmbeds(e.build()).queue();
    }

}
