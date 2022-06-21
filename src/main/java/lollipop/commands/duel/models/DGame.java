package lollipop.commands.duel.models;

import lollipop.BotStatistics;
import lollipop.Constant;
import lollipop.Database;
import lollipop.commands.duel.Duel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;


import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Game Model for duel games
 */
public class DGame {
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
    public static HashMap<String, String> gifMap;
    public static Button surrenderButton = Button.danger("ff", "surrender");
    public static boolean hasEnded = false;

    static {
        gifMap = new HashMap<>();
        gifMap.put("attack1", "https://c.tenor.com/6a42QlkVsCEAAAAd/anime-punch.gif");
        gifMap.put("attack2", "https://c.tenor.com/1sTe1w12WHwAAAAC/nezuko-kamado-tanjiro-kamado.gif");
        gifMap.put("attack3", "https://c.tenor.com/4AvIBPKxbOwAAAAd/demonslayer-headbutt.gif");
        gifMap.put("attack4", "https://c.tenor.com/FMO5562dLt4AAAAd/one-punch-man2-saitama-v-garou.gif");
        gifMap.put("heal", "https://c.tenor.com/NUt8vwChgIcAAAAC/luffy-eating.gif");
        gifMap.put("strength", "https://c.tenor.com/WGGJBAiyhxQAAAAC/demon-slayer-kimetsu-no-yaiba.gif");
        gifMap.put("defend1", "https://c.tenor.com/Z_0BQslObuIAAAAd/dragon-ball-barrier.gif");
        gifMap.put("defend2", "https://c.tenor.com/Z8Q13fWVtQkAAAAC/block-deflect.gif");
        gifMap.put("4thgear", "https://c.tenor.com/Z6xWNeyumJMAAAAC/one-piece-fight.gif");
        gifMap.put("hinokami", "https://c.tenor.com/LUAKGZSLoD8AAAAd/demon-slayer-tanjiro.gif");
        gifMap.put("rasengan", "https://c.tenor.com/_zEr-rdppKMAAAAC/minato-naruto.gif");
        gifMap.put("ora", "https://c.tenor.com/LytxJSf81m4AAAAC/ora-beatdown-oraoraora.gif");
        gifMap.put("seriouspunch", "https://c.tenor.com/vlsvbgqYz5QAAAAd/carnage-kabuto-saitama.gif");
        gifMap.put("zawarudo", "https://c.tenor.com/ETlOjJ8aU7EAAAAC/za-warudo-jojo-bizarre-adventure.gif");
        gifMap.put("yare", "https://c.tenor.com/Wtn31Gl1CpYAAAAC/jotaro-ora.gif");
    }

    /**
     * Get a list of all moves possible
     * @return string
     */
    public static String getAvailableMoves() {
        StringBuilder sb = new StringBuilder();
        for(Button b : moveButtons) sb.append("`").append(b.getLabel()).append("`, ");
        return sb.substring(0, sb.length()-2);
    }

    /**
     * Get a move description for each move in a duel game
     * @param name move name
     * @return string move description
     */
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

    /**
     * Send the starting select move once the duel has been accepted by both sides
     * @param event triggered after slash command usage
     * @param move move phrase
     */
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
                move = "I will accept this challenge and become your opponent! Bring it on!";
                if(playerNotTurn.member == null) {
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .setImage("https://c.tenor.com/2C1HHrXGzbMAAAAC/anime-fight.gif")
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
                            .setImage("https://c.tenor.com/2C1HHrXGzbMAAAAC/anime-fight.gif")
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
        if(!event.isAcknowledged()) event.deferReply().complete();
    }

    /**
     * Send the starting select move once the duel has been accepted by both sides
     * @param event triggered when a button is pressed
     * @param move move phrase
     */
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
                move = "I will accept this challenge and become your opponent! Bring it on!";
                if(playerNotTurn.member == null) {
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .setImage("https://c.tenor.com/2C1HHrXGzbMAAAAC/anime-fight.gif")
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
                            .setImage("https://c.tenor.com/2C1HHrXGzbMAAAAC/anime-fight.gif")
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

    /**
     * Send the select move message for the player to select their next move
     * @param event triggered when a button is pressed
     * @param move move phrase
     */
    public void sendSelectMove(ButtonInteractionEvent event, String move, String gif) {
        if(homePlayer.HP < 0) homePlayer.HP = 0;
        if(opposingPlayer.HP < 0) opposingPlayer.HP = 0;
        if(playerTurn.member == null) {
            String homePName = homePlayer.member.getEffectiveName();
            EmbedBuilder e = new EmbedBuilder()
                    .setDescription("**" + move + "**")
                    .setImage(gif)
                    .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                    .addField("Computer", "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                    .setAuthor("Computer", "https://github.com/BooleanCube/lollipop-bot", "https://www.pngkey.com/png/full/0-8970_open-my-computer-icon-circle.png");
            lastDisplay.get(0).editMessageEmbeds(e.build()).queue();
        } else {
            String homePName = homePlayer.member.getEffectiveName();
            if(move == null) {
                move = "I will accept this challenge and become your opponent! Bring it on!";
                if(playerNotTurn.member == null) {
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .setImage(gif)
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
                    ).complete();
                    editTimeout = lastDisplay.get(1).editMessageComponents().setActionRow(
                            moveButtons[x],
                            moveButtons[y],
                            moveButtons[z],
                            surrenderButton
                    ).queueAfter(3, TimeUnit.SECONDS);
                } else {
                    String oppoPName = opposingPlayer.member.getEffectiveName();
                    EmbedBuilder e = new EmbedBuilder()
                            .setDescription("**" + move + "**")
                            .setImage(gif)
                            .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                            .addField(oppoPName, "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                            .setAuthor(playerTurn.member.getEffectiveName(), "https://github.com/BooleanCube/lollipop-bot", playerTurn.member.getEffectiveAvatarUrl());
                    lastDisplay.get(0).editMessageEmbeds(e.build()).queue();
                }
            } else if(opposingPlayer.member != null) {
                String oppoPName = opposingPlayer.member.getEffectiveName();
                EmbedBuilder e = new EmbedBuilder()
                        .setDescription("**" + move + "**")
                        .setImage(gif)
                        .addField(homePName, "> Health: `" + homePlayer.HP + " HP`\n> Strength Gain: `" + homePlayer.strengthGain + " HP`", true)
                        .addField(oppoPName, "> Health: `" + opposingPlayer.HP + " HP`\n> Strength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                        .setAuthor(playerTurn.member.getEffectiveName(), playerTurn.member.getUser().getAvatarUrl(), playerTurn.member.getUser().getEffectiveAvatarUrl());
                lastDisplay.get(0).editMessageEmbeds(e.build()).queue();
            } else if(opposingPlayer.isTimedOut()) {
                EmbedBuilder e = new EmbedBuilder()
                        .setDescription("**" + move + "**")
                        .setImage(gif)
                        .addField(homePName, "Health: `" + homePlayer.HP + " HP`\nStrength Gain: `" + homePlayer.strengthGain + " HP`", true)
                        .addField("Computer", "Health: `" + opposingPlayer.HP + " HP`\nStrength Gain: `" + opposingPlayer.strengthGain + " HP`", true)
                        .setAuthor(playerTurn.member.getEffectiveName(), playerTurn.member.getUser().getAvatarUrl(), playerTurn.member.getUser().getEffectiveAvatarUrl());
                lastDisplay.get(0).editMessageEmbeds(e.build()).queue();
            }
        }
        if(playerTurn.isTimedOut()) {
            int x = (int)(Math.random()*3);
            int y = x + (int)(Math.random()*3)+1;
            int z = y + (int)(Math.random()*9)+1;
            if(lastDisplay.size() >= 2) {
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
                ).complete();
                editTimeout = lastDisplay.get(1).editMessageComponents().setActionRow(
                        moveButtons[x],
                        moveButtons[y],
                        moveButtons[z],
                        surrenderButton
                ).queueAfter(1, TimeUnit.SECONDS);
            }
        }
        else if(playerNotTurn.member != null && !playerNotTurn.isTimedOut()) {
            int x = (int)(Math.random()*3);
            int y = x + (int)(Math.random()*3)+1;
            int z = y + (int)(Math.random()*9)+1;
            if(lastDisplay.size() >= 2) {
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
                ).complete();
                editTimeout = lastDisplay.get(1).editMessageComponents().setActionRow(
                        moveButtons[x],
                        moveButtons[y],
                        moveButtons[z],
                        surrenderButton
                ).queueAfter(3, TimeUnit.SECONDS);
            }
        }
        if(!event.isAcknowledged()) event.deferEdit().queue();
    }

    /**
     * Switch the players turns
     */
    public void switchTurns() {
        Player temp = playerTurn;
        playerTurn = playerNotTurn;
        playerNotTurn = temp;
    }

    /**
     * Deletes ALL of the display messages for the duels game
     */
    public void deleteDisplayMessagesFull() {
        lastDisplay.forEach(msg -> msg.delete().queue());
        lastDisplay = new ArrayList<>();
    }

    /**
     * Deletes all of the display messages except the final display message
     * (unused since we're editing the messages instead now)
     */
    public void deleteDisplayMessages() {
        for(int i=0; i<lastDisplay.size()-1; i++) {
            lastDisplay.get(i).delete().queue();
            lastDisplay.remove(i);
        }
    }

    /**
     * Runs AI to calculate best move with arbitrarily chosen moves
     * @param h home player (player)
     * @param o opponent player (bot)
     * @return {@link String} move id
     */
    public String AIMove(Player h, Player o) {

        int x = (int)(Math.random()*3);
        int y = x + (int)(Math.random()*3)+1;
        int z = y + (int)(Math.random()*9)+1;

        String first = moveButtons[x].getId();
        String second = moveButtons[y].getId();
        String third = moveButtons[z].getId();

        // Decides move
        if(h.HP < 15-o.strengthGain) return first;
        if(h.HP-o.HP > 40 && (second.startsWith("heal") || (second.startsWith("defend") && !o.isDefending))) return second;
        if(!o.isDefending && Math.random()<0.5) return second;
        if(o.HP > h.HP && second.startsWith("strength")) return second;
        if(h.isDefending && Math.random()<0.5) return first;
        return third;

    }

    /**
     * Implements move for AI bot
     * @param move move to display
     * @return {@link String} move description display text
     */
    public String getMoveString(String move) {
        // Implements move
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
            if(playerNotTurn.strengthGain < -5) playerNotTurn.strengthGain = -5;
            return "yare yare daze...\nORA! `Copmputer` did `" + damage + " HP` damage and made the opponent weaker! Their attacks will do less damage..";
        } else if(move.startsWith("zawarudo")) {
            playerNotTurn.timeoutStart = System.currentTimeMillis();
            playerNotTurn.timeoutDuration = Math.random()+5;
            playerTurn.isZaWarudo = true;
            return "ZA WARUDO!\n`Computer` stopped time. Their opponent is frozen for `5 seconds`.";
        }
        // Duel closes if anything above doesn't work for some reason
        Duel.memberToGame.remove(homePlayer.member.getIdLong());
        Duel.memberToGame.remove(opposingPlayer.member.getIdLong());
        return "Error! This duel will be ending with no victor...";
    }

    /**
     * Setup 30 second timeout for people who go AFK
     * @param c channel
     */
    public void setupTimeout(MessageChannel c) {
        String[] victoryMsg = {"You are too strong...", "That kind of power should be illegal!", "You are a god amongst men!", "How did you get so much power?", "Nobody dares to duel with you!"};
        EmbedBuilder e = new EmbedBuilder().setColor(Color.green).setFooter("Type " + Constant.PREFIX + "duel to start another duel with me!");
        if(playerNotTurn.member == null) {
            Runnable success = () -> {
                int xp = (int)((int)(Math.random()*11)-20/Constant.MULTIPLIER);
                e.setFooter(playerTurn.member.getEffectiveName() + " lost " + (-1*xp) + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                e.setAuthor("Computer won the duel!", "https://github.com/BooleanCube/lollipop-bot", "https://www.pngkey.com/png/full/0-8970_open-my-computer-icon-circle.png");
                e.setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)]);
                e.setImage("https://c.tenor.com/1WSdEj1xWfAAAAAd/one-punch-man-anime.gif");
                e.setDescription(playerTurn.member.getAsMention() + " didn't react fast enough, so I assumed they surrendered!");
                this.timeout = c.sendMessageEmbeds(e.build()).queueAfter(30, TimeUnit.SECONDS, me -> {
                    Database.addToUserBalance(playerTurn.member.getId(), xp);
                    Duel.memberToGame.remove(playerTurn.member.getIdLong());
                    Duel.occupiedShards.remove(Integer.valueOf(c.getJDA().getShardInfo().getShardId()));
                    if(playerNotTurn.member != null) Duel.memberToGame.remove(playerNotTurn.member.getIdLong());
                    deleteDisplayMessagesFull();
                });
                if(hasEnded) timeout.cancel(false);
            };
            Runnable failure = () -> {
                int xp = (int)(Math.random()*11)-20;
                e.setFooter(playerTurn.member.getEffectiveName() + " lost " + (-1*xp) + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                e.setAuthor("Computer won the duel!", "https://github.com/BooleanCube/lollipop-bot", "https://www.pngkey.com/png/full/0-8970_open-my-computer-icon-circle.png");
                e.setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)]);
                e.setImage("https://c.tenor.com/1WSdEj1xWfAAAAAd/one-punch-man-anime.gif");
                e.setDescription(playerTurn.member.getAsMention() + " didn't react fast enough, so I assumed they surrendered!");
                this.timeout = c.sendMessageEmbeds(e.build()).queueAfter(30, TimeUnit.SECONDS, me -> {
                    Database.addToUserBalance(playerTurn.member.getId(), xp);
                    Duel.memberToGame.remove(playerTurn.member.getIdLong());
                    Duel.occupiedShards.remove(Integer.valueOf(c.getJDA().getShardInfo().getShardId()));
                    if(playerNotTurn.member != null) Duel.memberToGame.remove(playerNotTurn.member.getIdLong());
                    deleteDisplayMessagesFull();
                });
                if(hasEnded) timeout.cancel(false);
            };
            BotStatistics.sendMultiplier(playerTurn.member.getId(), success, failure);
        } else {
            Runnable success = () -> {
                int xp = (int)((int)(Math.random()*31)+70*Constant.MULTIPLIER);
                e.setFooter(playerNotTurn.member.getEffectiveName() + " won " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                e.setAuthor(playerNotTurn.member.getEffectiveName() + " won the duel!", playerNotTurn.member.getUser().getAvatarUrl(), playerNotTurn.member.getUser().getEffectiveAvatarUrl());
                e.setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)]);
                e.setImage("https://c.tenor.com/1WSdEj1xWfAAAAAd/one-punch-man-anime.gif");
                e.setDescription(playerTurn.member.getAsMention() + " didn't react fast enough, so I assumed they surrendered!");
                if(timeout != null && timeout.isCancelled()) {
                    this.timeout = c.sendMessageEmbeds(e.build()).queueAfter(30, TimeUnit.SECONDS, me -> {
                        Database.addToUserBalance(playerNotTurn.member.getId(), xp);
                        Duel.memberToGame.remove(playerTurn.member.getIdLong());
                        Duel.occupiedShards.remove(Integer.valueOf(c.getJDA().getShardInfo().getShardId()));
                        if(playerNotTurn.member != null) Duel.memberToGame.remove(playerNotTurn.member.getIdLong());
                        deleteDisplayMessagesFull();
                    });
                }
                if(hasEnded) timeout.cancel(false);
            };
            Runnable failure = () -> {
                int xp = (int)(Math.random()*31)+70;
                e.setFooter(playerNotTurn.member.getEffectiveName() + " won " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                e.setAuthor(playerNotTurn.member.getEffectiveName() + " won the duel!", playerNotTurn.member.getUser().getAvatarUrl(), playerNotTurn.member.getUser().getEffectiveAvatarUrl());
                e.setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)]);
                e.setImage("https://c.tenor.com/1WSdEj1xWfAAAAAd/one-punch-man-anime.gif");
                e.setDescription(playerTurn.member.getAsMention() + " didn't react fast enough, so I assumed they surrendered!");
                if(timeout != null && timeout.isCancelled()) {
                    this.timeout = c.sendMessageEmbeds(e.build()).queueAfter(30, TimeUnit.SECONDS, me -> {
                        Database.addToUserBalance(playerNotTurn.member.getId(), xp);
                        Duel.memberToGame.remove(playerTurn.member.getIdLong());
                        Duel.occupiedShards.remove(Integer.valueOf(c.getJDA().getShardInfo().getShardId()));
                        if(playerNotTurn.member != null) Duel.memberToGame.remove(playerNotTurn.member.getIdLong());
                        deleteDisplayMessagesFull();
                    });
                }
                if(hasEnded) timeout.cancel(false);
            };
            BotStatistics.sendMultiplier(playerNotTurn.member.getId(), success, failure);
        }
    }

    /**
     * Checks if a player in the duel has won and does the following procedures
     * @param c channel
     * @return boolean indicating whether a player won or not
     */
    public boolean checkWin(MessageChannel c) {
        String[] victoryMsg = {"You are too strong...", "That kind of power should be illegal!", "You are a god amongst men!", "How did you get so much power?", "Nobody dares to duel with you!"};
        if(homePlayer.HP <= 0) {
            editTimeout.cancel(false);
            hasEnded = true;
            if(!timeout.isCancelled()) timeout.cancel(false);
            deleteDisplayMessagesFull();
            Duel.memberToGame.remove(homePlayer.member.getIdLong());
            EmbedBuilder e = new EmbedBuilder().setColor(Color.green).setFooter("Type /duel to start another duel with me!");
            if(opposingPlayer.member == null) {
                e.setAuthor("Computer won the duel!", "https://github.com/BooleanCube/lollipop-bot", "https://www.pngkey.com/png/full/0-8970_open-my-computer-icon-circle.png");
                e.setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)]);
                e.setImage("https://c.tenor.com/1WSdEj1xWfAAAAAd/one-punch-man-anime.gif");
                e.setDescription("> " + homePlayer.member.getEffectiveName() + "'s HP: `0`\n" +
                        "> Computer's HP: `" + opposingPlayer.HP + "`");
                Runnable success = () -> {
                    int xp = (int)(Math.random()*11)-20;
                    xp = (int)(xp/Constant.MULTIPLIER);
                    Database.addToUserBalance(homePlayer.member.getId(), xp);
                    e.setFooter(homePlayer.member.getEffectiveName() + " lost " + (-1*xp) + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    c.sendMessageEmbeds(e.build()).queue();
                };
                Runnable failure = () -> {
                    int xp = (int)(Math.random()*11)-20;
                    Database.addToUserBalance(homePlayer.member.getId(), xp);
                    e.setFooter(homePlayer.member.getEffectiveName() + " lost " + (-1*xp) + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    c.sendMessageEmbeds(e.build()).queue();
                };
                BotStatistics.sendMultiplier(homePlayer.member.getId(), success, failure);
            } else {
                e.setAuthor(opposingPlayer.member.getEffectiveName() + " won the duel!", opposingPlayer.member.getUser().getAvatarUrl(), opposingPlayer.member.getUser().getEffectiveAvatarUrl());
                e.setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)]);
                e.setImage("https://c.tenor.com/1WSdEj1xWfAAAAAd/one-punch-man-anime.gif");
                e.setDescription("> " + homePlayer.member.getEffectiveName() + "'s HP: `0`\n" +
                        "> " + opposingPlayer.member.getEffectiveName() + "'s HP: `" + opposingPlayer.HP + "`");
                Duel.memberToGame.remove(opposingPlayer.member.getIdLong());
                Runnable success = () -> {
                    int xp = (int)(Math.random()*31)+70;
                    xp = (int)(xp*Constant.MULTIPLIER);
                    Database.addToUserBalance(opposingPlayer.member.getId(), xp);
                    e.setFooter(opposingPlayer.member.getEffectiveName() + " gained " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    c.sendMessageEmbeds(e.build()).queue();
                };
                Runnable failure = () -> {
                    int xp = (int)(Math.random()*31)+70;
                    Database.addToUserBalance(opposingPlayer.member.getId(), xp);
                    e.setFooter(opposingPlayer.member.getEffectiveName() + " gained " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    c.sendMessageEmbeds(e.build()).queue();
                };
                BotStatistics.sendMultiplier(opposingPlayer.member.getId(), success, failure);
            }
            Duel.occupiedShards.remove(Integer.valueOf(c.getJDA().getShardInfo().getShardId()));
            return true;
        } else if(opposingPlayer.HP <= 0) {
            editTimeout.cancel(false);
            if(!timeout.isCancelled()) timeout.cancel(false);
            hasEnded = true;
            Duel.memberToGame.remove(homePlayer.member.getIdLong());
            if(opposingPlayer.member != null) {
                Duel.memberToGame.remove(opposingPlayer.member.getIdLong());
                deleteDisplayMessagesFull();
                EmbedBuilder e = new EmbedBuilder()
                        .setColor(Color.green)
                        .setFooter("Type " + Constant.PREFIX + "duel to start another duel with me!")
                        .setAuthor(homePlayer.member.getEffectiveName() + " won the duel!", homePlayer.member.getUser().getAvatarUrl(), homePlayer.member.getUser().getEffectiveAvatarUrl())
                        .setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)])
                        .setImage("https://c.tenor.com/1WSdEj1xWfAAAAAd/one-punch-man-anime.gif")
                        .setDescription("> " + homePlayer.member.getEffectiveName() + "'s HP: `" + homePlayer.HP + "`\n" +
                                "> " + opposingPlayer.member.getEffectiveName() + "'s HP: `0`");
                Runnable success = () -> {
                    int xp = (int)(Math.random()*31)+70;
                    xp = (int)(xp*Constant.MULTIPLIER);
                    Database.addToUserBalance(homePlayer.member.getId(), xp);
                    e.setFooter(homePlayer.member.getEffectiveName() + " gained " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    c.sendMessageEmbeds(e.build()).queue();
                };
                Runnable failure = () -> {
                    int xp = (int)(Math.random()*31)+70;
                    Database.addToUserBalance(homePlayer.member.getId(), xp);
                    e.setFooter(homePlayer.member.getEffectiveName() + " gained " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    c.sendMessageEmbeds(e.build()).queue();
                };
                BotStatistics.sendMultiplier(homePlayer.member.getId(), success, failure);
            } else {
                deleteDisplayMessagesFull();
                EmbedBuilder e = new EmbedBuilder()
                        .setColor(Color.green)
                        .setFooter("Type " + Constant.PREFIX + "duel to start another duel with me!")
                        .setAuthor(homePlayer.member.getEffectiveName() + " won the duel!", homePlayer.member.getUser().getAvatarUrl(), homePlayer.member.getUser().getEffectiveAvatarUrl())
                        .setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)])
                        .setImage("https://c.tenor.com/1WSdEj1xWfAAAAAd/one-punch-man-anime.gif")
                        .setDescription("> " + homePlayer.member.getEffectiveName() + "'s HP: `" + homePlayer.HP + "`\n" +
                                "> Computer's HP: `0`");
                Runnable success = () -> {
                    int xp = (int)(Math.random()*31)+70;
                    xp = (int)(xp*Constant.MULTIPLIER);
                    Database.addToUserBalance(homePlayer.member.getId(), xp);
                    e.setFooter(homePlayer.member.getEffectiveName() + " gained " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    c.sendMessageEmbeds(e.build()).queue();
                };
                Runnable failure = () -> {
                    int xp = (int)(Math.random()*31)+70;
                    Database.addToUserBalance(homePlayer.member.getId(), xp);
                    e.setFooter(homePlayer.member.getEffectiveName() + " gained " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                    c.sendMessageEmbeds(e.build()).queue();
                };
                BotStatistics.sendMultiplier(homePlayer.member.getId(), success, failure);
            }
            Duel.occupiedShards.remove(Integer.valueOf(c.getJDA().getShardInfo().getShardId()));
            return true;
        }
        return false;
    }

    /**
     * Runs the surrender procedures when a player surrenders the duel
     * @param c channel
     * @param p player
     */
    public void surrender(MessageChannel c, Player p) {
        String[] victoryMsg = {"You are too strong...", "That kind of power should be illegal!", "He is a god amongst men!", "How did you get so much power?", "Nobody dares to duel with you!"};
        Duel.memberToGame.remove(playerTurn.member.getIdLong());
        Duel.occupiedShards.remove(Integer.valueOf(c.getJDA().getShardInfo().getShardId()));
        EmbedBuilder e = new EmbedBuilder().setColor(Color.green).setFooter("Type " + Constant.PREFIX + "duel to start another duel with me!");
        if(playerNotTurn.member == null) {
            e.setAuthor("Computer won the duel!", "https://github.com/BooleanCube/lollipop-bot", "https://www.pngkey.com/png/full/0-8970_open-my-computer-icon-circle.png");
            e.setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)]);
            e.setImage("https://c.tenor.com/1WSdEj1xWfAAAAAd/one-punch-man-anime.gif");
            e.setDescription(p.member.getEffectiveName() + " ran away from the duel!");
            Runnable success = () -> {
                int xp = (int)(Math.random()*11)-20;
                xp = (int)(xp/Constant.MULTIPLIER);
                Database.addToUserBalance(playerTurn.member.getId(), xp);
                e.setFooter(playerTurn.member.getEffectiveName() + " lost " + (-1*xp) + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                c.sendMessageEmbeds(e.build()).queue();
            };
            Runnable failure = () -> {
                int xp = (int)(Math.random()*11)-20;
                Database.addToUserBalance(playerTurn.member.getId(), xp);
                e.setFooter(playerTurn.member.getEffectiveName() + " lost " + (-1*xp) + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                c.sendMessageEmbeds(e.build()).queue();
            };
            BotStatistics.sendMultiplier(playerTurn.member.getId(), success, failure);
        } else {
            e.setAuthor(playerNotTurn.member.getEffectiveName() + " won the duel!", playerNotTurn.member.getUser().getAvatarUrl(), playerNotTurn.member.getUser().getEffectiveAvatarUrl());
            e.setTitle(victoryMsg[(int)(Math.random()*victoryMsg.length)]);
            e.setImage("https://c.tenor.com/1WSdEj1xWfAAAAAd/one-punch-man-anime.gif");
            e.setDescription(p.member.getAsMention() + " ran away from the duel!");
            Duel.memberToGame.remove(playerNotTurn.member.getIdLong());
            Runnable success = () -> {
                int xp = (int)(Math.random()*31)+70;
                xp = (int)(xp*Constant.MULTIPLIER);
                Database.addToUserBalance(playerNotTurn.member.getId(), xp);
                e.setFooter(playerNotTurn.member.getEffectiveName() + " gained " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                c.sendMessageEmbeds(e.build()).queue();
            };
            Runnable failure = () -> {
                int xp = (int)(Math.random()*31)+70;
                Database.addToUserBalance(playerNotTurn.member.getId(), xp);
                e.setFooter(playerNotTurn.member.getEffectiveName() + " gained " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png");
                c.sendMessageEmbeds(e.build()).queue();
            };
            BotStatistics.sendMultiplier(playerNotTurn.member.getId(), success, failure);
        }
    }

}
