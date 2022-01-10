package lollipop.listeners;

import lollipop.commands.duel.Duel;
import lollipop.commands.duel.models.Game;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class DuelsListener extends ListenerAdapter {

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        if(Objects.equals(Objects.requireNonNull(event.getButton()).getId(), "accept")) {
            if(!Duel.memberToGame.containsKey(Objects.requireNonNull(event.getMember()).getIdLong())) return;
            Game game = Duel.memberToGame.get(event.getMember().getIdLong());
            if(event.getMember() != game.playerTurn.member) {
                event.replyEmbeds(new EmbedBuilder()
                        .setDescription("This message was not intended towards you!")
                        .setColor(Color.red)
                        .build()
                ).setEphemeral(true).queue();
                return;
            }
            game.timeout.cancel(false);
            game.sendSelectMove(event.getTextChannel(), null);
            game.setupTimeout(event.getChannel());
            game.switchTurns();
        }
        else if(Duel.memberToGame.containsKey(Objects.requireNonNull(event.getMember()).getIdLong())) {
            Game game = Duel.memberToGame.get(event.getMember().getIdLong());
            if(event.getMember() != game.playerTurn.member) {
                event.replyEmbeds(new EmbedBuilder()
                        .setDescription("**It is not your turn! Please wait for the other player to finish his turn!**")
                        .setColor(Color.red)
                        .setFooter("If you have not started a duel yet, you can do so by typing l!duel")
                        .build()
                ).setEphemeral(true).queue();
                return;
            }
            if(event.getMessage().getIdLong() != game.lastDisplay.get(game.lastDisplay.size()-1).getIdLong()) return;
            if(game.lastDisplay != null) game.deleteDisplayMessagesFull();
            String move = null;
            if(Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId()).startsWith("ff")) {
                game.surrender(event.getChannel(), game.playerTurn);
                game.timeout.cancel(false);
                return;
            }
            else if(Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId()).startsWith("attack")) {
                if(game.playerNotTurn.isDefending) {
                    String name = game.playerNotTurn.member != null ? game.playerNotTurn.member.getAsMention() : "`Computer`";
                    move = name + " blocked " + game.playerTurn.member.getAsMention() + "'s attack!";
                    game.playerNotTurn.isDefending = false;
                } else {
                    int damage = (int)(Math.random()*6)+5+game.playerTurn.strengthGain;
                    game.playerNotTurn.HP -= damage;
                    move = game.playerTurn.member.getAsMention() + " attacked their opponent and did `" + damage + " HP` damage!";
                }
            }
            else if(Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId()).startsWith("defend")) {
                move = game.playerTurn.member.getAsMention() + " is defending himself!";
                game.playerTurn.isDefending = true;
            }
            else if(Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId()).startsWith("heal")) {
                int health = (int)(Math.random()*11)+20;
                game.playerTurn.HP += health;
                move = game.playerTurn.member.getAsMention() + " healed himself and gained `" + health + " HP`!";
            }
            else if(Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId()).startsWith("strength")) {
                game.playerTurn.strengthGain += (int)(Math.random()*3)+3;
                move = game.playerTurn.member.getAsMention() + " took a deep breath and became much stronger! They're attacks will do more damage..";
            }
            else if(Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId()).startsWith("ora")) {
                if(game.playerNotTurn.isDefending) {
                    String name = game.playerNotTurn.member != null ? game.playerNotTurn.member.getAsMention() : "`Computer`";
                    move = name + " blocked " + game.playerTurn.member.getAsMention() + "'s ORA!";
                    game.playerNotTurn.isDefending = false;
                } else {
                    int damage = (int)(Math.random()*6)+15+game.playerTurn.strengthGain;
                    game.playerNotTurn.HP -= damage;
                    move = "ORA ORA ORA ORA ORRAAAA\n" + game.playerTurn.member.getAsMention() + " pounded their opponent and did `" + damage + " HP` damage!";
                }
            }
            else if(Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId()).startsWith("seriouspunch")) {
                if(game.playerNotTurn.isDefending) {
                    String name = game.playerNotTurn.member != null ? game.playerNotTurn.member.getAsMention() : "`Computer`";
                    move = name + " blocked " + game.playerTurn.member.getAsMention() + "'s serious punch!";
                    game.playerNotTurn.isDefending = false;
                } else {
                    int damage = (int)(Math.random()*6)+20+game.playerTurn.strengthGain;
                    game.playerNotTurn.HP -= damage;
                    move = "Anybody in my way... gets punched.\n" + game.playerTurn.member.getAsMention() + " punched their opponent and did `" + damage + " HP` damage!";
                }
            }
            else if(Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId()).startsWith("zawarudo")) {
                game.playerNotTurn.timeoutStart = System.currentTimeMillis();
                game.playerNotTurn.timeoutDuration = Math.random()+5;
                move = "ZA WARUDO!\n" + game.playerTurn.member.getAsMention() + " stopped time. Their opponent is frozen for `5 seconds`.";
            }
            else if(Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId()).startsWith("rasengan")) {
                if(game.playerNotTurn.isDefending) {
                    String name = game.playerNotTurn.member != null ? game.playerNotTurn.member.getAsMention() : "`Computer`";
                    move = name + " blocked " + game.playerTurn.member.getAsMention() + "'s Rasengan!";
                    game.playerNotTurn.isDefending = false;
                } else {
                    int damage = (int)(Math.random()*6)+14+game.playerTurn.strengthGain;
                    game.playerNotTurn.HP -= damage;
                    move = "RASENGAN!\n" + game.playerTurn.member.getAsMention() + " hit their opponent with a rasengan and did `" + damage + " HP` damage!";
                }
            }
            else if(Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId()).startsWith("4thgear")) {
                if(game.playerNotTurn.isDefending) {
                    String name = game.playerNotTurn.member != null ? game.playerNotTurn.member.getAsMention() : "`Computer`";
                    move = name + " blocked " + game.playerTurn.member.getAsMention() + "'s Gum-Gum Kong Gun!";
                    game.playerNotTurn.isDefending = false;
                } else {
                    int damage = (int)(Math.random()*6)+13+game.playerTurn.strengthGain;
                    game.playerNotTurn.HP -= damage;
                    move = "*boing* 4th GEAR *boing*\n" + game.playerTurn.member.getAsMention() + " blasted their opponents away with Gum-Gum Kong Gun and did `" + damage + " HP` damage!";
                }
            }
            else if(Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId()).startsWith("hinokami")) {
                if(game.playerNotTurn.isDefending) {
                    String name = game.playerNotTurn.member != null ? game.playerNotTurn.member.getAsMention() : "`Computer`";
                    move = name + " blocked " + game.playerTurn.member.getAsMention() + "'s hinokami attack!";
                    game.playerNotTurn.isDefending = false;
                } else {
                    int damage = (int)(Math.random()*6)+13+game.playerTurn.strengthGain;
                    game.playerNotTurn.HP -= damage;
                    move = "HINOKAMI KAGURA!\n" + game.playerTurn.member.getAsMention() + " sliced the opponent's head off and did `" + damage + " HP` damage!";
                }
            }
            else if(Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId()).startsWith("yare")) {
                int damage = (int)(Math.random()*6)+15+game.playerTurn.strengthGain;
                game.playerNotTurn.HP -= damage;
                game.playerNotTurn.strengthGain -= (int)(Math.random()*16)+5;
                if(game.playerNotTurn.strengthGain < -5) game.playerNotTurn.strengthGain = -5;
                move = "yare yare daze...\nORA! " + game.playerTurn.member.getAsMention() + " did `" + damage + " HP` damage and made the opponent weaker! Their attacks will do less damage..";
            }
            game.timeout.cancel(false);
            game.sendSelectMove(event.getTextChannel(), move);
            game.switchTurns();
            game.setupTimeout(event.getChannel());
            if(game.checkWin(event.getChannel())) {
                game.timeout.cancel(false);
                return;
            }
            if(game.playerTurn.isTimedOut()) {
                game.switchTurns();
                int x = (int)(Math.random()*5);
                int y = x + (int)(Math.random()*5)+1;
                int z = y + (int)(Math.random()*5)+1;
                if(z == 13) --z;
                game.lastDisplay.add(event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setAuthor(game.playerTurn.member.getEffectiveName() + "'s turn", "https://github.com/BooleanCube/lollipop-bot", game.playerTurn.member.getEffectiveAvatarUrl())
                        .setDescription("What is your move?")
                        .setFooter("Quick! You have 15 seconds to react!")
                        .build()
                ).setActionRow(
                        Game.moveButtons[x],
                        Game.moveButtons[y],
                        Game.moveButtons[z],
                        game.surrenderButton
                ).complete());
                game.timeout.cancel(false);
                game.setupTimeout(event.getChannel());
            } else if(game.playerTurn.member == null) {
                //AI
                String aiMove = game.AIMove(game.playerTurn, game.playerNotTurn);
                event.getChannel().sendTyping().complete();
                game.sendSelectMove(event.getTextChannel(), aiMove);
                if(game.playerNotTurn.isTimedOut()) {
                    int turns = (int)(Math.random()*2)+2;
                    for(int i=0; i<turns; i++) {
                        aiMove = game.AIMove(game.playerTurn, game.playerNotTurn);
                        event.getChannel().sendTyping().complete();
                        game.sendSelectMove(event.getTextChannel(), aiMove);
                    }
                    game.playerNotTurn.timeoutDuration = 0;
                    game.playerTurn.isZaWarudo = false;
                    game.switchTurns();
                    int x = (int)(Math.random()*5);
                    int y = x + (int)(Math.random()*5)+1;
                    int z = y + (int)(Math.random()*5)+1;
                    game.lastDisplay.add(event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                            .setAuthor(game.playerTurn.member.getEffectiveName() + "'s turn", "https://github.com/BooleanCube/lollipop-bot", game.playerTurn.member.getEffectiveAvatarUrl())
                            .setDescription("What is your move?")
                            .setFooter("Quick! You have 15 seconds to react!")
                            .build()
                    ).setActionRow(
                            Game.moveButtons[x],
                            Game.moveButtons[y],
                            Game.moveButtons[z],
                            game.surrenderButton
                    ).complete());
                } else game.switchTurns();
            }
            game.checkWin(event.getChannel());
        }
    }
}
