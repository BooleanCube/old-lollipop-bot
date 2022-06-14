package lollipop.listeners;

import lollipop.Constant;
import lollipop.commands.duel.Duel;
import lollipop.commands.duel.models.DGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Responds to player actions for lollipop duels
 */
public class DuelsListener extends ListenerAdapter {

    /**
     * Triggered when a button is pressed
     * @param event
     */
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if(Objects.equals(event.getButton().getId(), "accept")) {
            if(!Duel.memberToGame.containsKey(event.getMember().getIdLong())) return;
            DGame DGame = Duel.memberToGame.get(event.getMember().getIdLong());
            if(event.getMember() != DGame.playerTurn.member) {
                event.replyEmbeds(new EmbedBuilder()
                        .setDescription("This message was not intended towards you!")
                        .setColor(Color.red)
                        .build()
                ).setEphemeral(true).queue();
                return;
            }
            DGame.timeout.cancel(false);
            DGame.deleteDisplayMessagesFull();
            DGame.sendStartSelectMove(event, null);
            DGame.setupTimeout(event.getChannel());
            DGame.switchTurns();
        }
        else if(Duel.memberToGame.containsKey(event.getMember().getIdLong())) {
            DGame DGame = Duel.memberToGame.get(event.getMember().getIdLong());
            if(event.getMessage().getIdLong() != DGame.lastDisplay.get(DGame.lastDisplay.size()-1).getIdLong()) return;
            if(event.getMember() != DGame.playerTurn.member) {
                event.replyEmbeds(new EmbedBuilder()
                        .setDescription("**It is not your turn! Please wait for the other player to finish his turn!**")
                        .setColor(Color.red)
                        .setFooter("If you have not started a duel yet, you can do so by typing " + Constant.PREFIX + "duel")
                        .build()
                ).setEphemeral(true).queue();
                return;
            }
            String move = null;
            if(Objects.requireNonNull(event.getButton().getId()).startsWith("ff")) {
                if(DGame.timeout != null) DGame.timeout.cancel(false);
                if(DGame.editTimeout != null) DGame.editTimeout.cancel(false);
                DGame.surrender(event.getChannel(), DGame.playerTurn);
                DGame.deleteDisplayMessagesFull();
                return;
            }
            else if(event.getButton().getId().startsWith("attack")) {
                if(DGame.playerNotTurn.isDefending) {
                    String name = DGame.playerNotTurn.member != null ? DGame.playerNotTurn.member.getAsMention() : "`Computer`";
                    move = name + " blocked " + DGame.playerTurn.member.getAsMention() + "'s attack!";
                    DGame.playerNotTurn.isDefending = false;
                } else {
                    int damage = (int)(Math.random()*6)+5+ DGame.playerTurn.strengthGain;
                    DGame.playerNotTurn.HP -= damage;
                    move = DGame.playerTurn.member.getAsMention() + " attacked their opponent and did `" + damage + " HP` damage!";
                }
            }
            else if(event.getButton().getId().startsWith("defend")) {
                move = DGame.playerTurn.member.getAsMention() + " is defending himself!";
                DGame.playerTurn.isDefending = true;
            }
            else if(event.getButton().getId().startsWith("heal")) {
                int health = (int)(Math.random()*11)+20;
                DGame.playerTurn.HP += health;
                move = DGame.playerTurn.member.getAsMention() + " healed himself and gained `" + health + " HP`!";
            }
            else if(event.getButton().getId().startsWith("strength")) {
                DGame.playerTurn.strengthGain += (int)(Math.random()*3)+3;
                move = DGame.playerTurn.member.getAsMention() + " took a deep breath and became much stronger! They're attacks will do more damage..";
            }
            else if(event.getButton().getId().startsWith("ora")) {
                if(DGame.playerNotTurn.isDefending) {
                    String name = DGame.playerNotTurn.member != null ? DGame.playerNotTurn.member.getAsMention() : "`Computer`";
                    move = name + " blocked " + DGame.playerTurn.member.getAsMention() + "'s ORA!";
                    DGame.playerNotTurn.isDefending = false;
                } else {
                    int damage = (int)(Math.random()*6)+15+ DGame.playerTurn.strengthGain;
                    DGame.playerNotTurn.HP -= damage;
                    move = "ORA ORA ORA ORA ORRAAAA\n" + DGame.playerTurn.member.getAsMention() + " pounded their opponent and did `" + damage + " HP` damage!";
                }
            }
            else if(event.getButton().getId().startsWith("seriouspunch")) {
                if(DGame.playerNotTurn.isDefending) {
                    int damage = (int)(Math.random()*11)+20+ DGame.playerTurn.strengthGain;
                    DGame.playerNotTurn.HP -= damage;
                    move = "Anybody in my way... gets punched.\n" + DGame.playerTurn.member.getAsMention() + " punched their opponent and did `" + damage + " HP` damage!";
                    DGame.playerNotTurn.isDefending = false;
                } else {
                    int damage = (int)(Math.random()*11)+40+ DGame.playerTurn.strengthGain;
                    DGame.playerNotTurn.HP -= damage;
                    move = "Anybody in my way... gets punched.\n" + DGame.playerTurn.member.getAsMention() + " punched their opponent and did `" + damage + " HP` damage!";
                }
            }
            else if(event.getButton().getId().startsWith("zawarudo")) {
                DGame.playerNotTurn.timeoutStart = System.currentTimeMillis();
                DGame.playerNotTurn.timeoutDuration = Math.random()+6;
                move = "ZA WARUDO!\n" + DGame.playerTurn.member.getAsMention() + " stopped time. Their opponent is frozen for `5 seconds`.";
            }
            else if(event.getButton().getId().startsWith("rasengan")) {
                if(DGame.playerNotTurn.isDefending) {
                    String name = DGame.playerNotTurn.member != null ? DGame.playerNotTurn.member.getAsMention() : "`Computer`";
                    move = name + " blocked " + DGame.playerTurn.member.getAsMention() + "'s Rasengan!";
                    DGame.playerNotTurn.isDefending = false;
                } else {
                    int damage = (int)(Math.random()*6)+14+ DGame.playerTurn.strengthGain;
                    DGame.playerNotTurn.HP -= damage;
                    move = "RASENGAN!\n" + DGame.playerTurn.member.getAsMention() + " hit their opponent with a rasengan and did `" + damage + " HP` damage!";
                }
            }
            else if(event.getButton().getId().startsWith("4thgear")) {
                if(DGame.playerNotTurn.isDefending) {
                    String name = DGame.playerNotTurn.member != null ? DGame.playerNotTurn.member.getAsMention() : "`Computer`";
                    move = name + " blocked " + DGame.playerTurn.member.getAsMention() + "'s Gum-Gum Kong Gun!";
                    DGame.playerNotTurn.isDefending = false;
                } else {
                    int damage = (int)(Math.random()*6)+13+ DGame.playerTurn.strengthGain;
                    DGame.playerNotTurn.HP -= damage;
                    move = "*boing* 4th GEAR *boing*\n" + DGame.playerTurn.member.getAsMention() + " blasted their opponents away with Gum-Gum Kong Gun and did `" + damage + " HP` damage!";
                }
            }
            else if(event.getButton().getId().startsWith("hinokami")) {
                if(DGame.playerNotTurn.isDefending) {
                    String name = DGame.playerNotTurn.member != null ? DGame.playerNotTurn.member.getAsMention() : "`Computer`";
                    move = name + " blocked " + DGame.playerTurn.member.getAsMention() + "'s hinokami attack!";
                    DGame.playerNotTurn.isDefending = false;
                } else {
                    int damage = (int)(Math.random()*6)+13+ DGame.playerTurn.strengthGain;
                    DGame.playerNotTurn.HP -= damage;
                    move = "HINOKAMI KAGURA!\n" + DGame.playerTurn.member.getAsMention() + " sliced the opponent's head off and did `" + damage + " HP` damage!";
                }
            }
            else if(event.getButton().getId().startsWith("yare")) {
                int damage = (int)(Math.random()*6)+15+ DGame.playerTurn.strengthGain;
                DGame.playerNotTurn.HP -= damage;
                DGame.playerNotTurn.strengthGain -= (int)(Math.random()*16)+5;
                if(DGame.playerNotTurn.strengthGain < -5) DGame.playerNotTurn.strengthGain = -5;
                move = "yare yare daze...\nORA! " + DGame.playerTurn.member.getAsMention() + " did `" + damage + " HP` damage and made the opponent weaker! Their attacks will do less damage..";
            }

            DGame.timeout.cancel(false);
            if(DGame.playerNotTurn.isTimedOut()) {
                if(DGame.playerNotTurn.member == null) DGame.setupTimeout(event.getChannel());
                DGame.switchTurns();
                if(DGame.playerTurn.member != null) DGame.setupTimeout(event.getChannel());
                DGame.sendSelectMove(event, move, DGame.gifMap.get(event.getButton().getId()));
            } else {
                DGame.sendSelectMove(event, move, DGame.gifMap.get(event.getButton().getId()));
                if(DGame.playerTurn.member == null) DGame.setupTimeout(event.getChannel());
                DGame.switchTurns();
                if(DGame.playerTurn.member != null) DGame.setupTimeout(event.getChannel());
            }

            if(DGame.playerTurn.isTimedOut()) {
                DGame.switchTurns();
                DGame.timeout.cancel(false);
                DGame.setupTimeout(event.getChannel());
            } else if(DGame.playerTurn.member == null) {
                //AI
                String moveId = DGame.AIMove(DGame.playerTurn, DGame.playerNotTurn);
                String gif = DGame.gifMap.get(moveId);
                StringBuilder aiMove = new StringBuilder("> " + DGame.getMoveString(moveId) + "\n\n");
                DGame.sendSelectMove(event, aiMove.toString(), gif);
                if(DGame.playerNotTurn.isTimedOut()) {
                    int turns = (int)(Math.random()*3)+2;
                    for(int i=0; i<turns; i++) aiMove.append("> ").append(DGame.AIMove(DGame.playerTurn, DGame.playerNotTurn)).append("\n");
                    DGame.sendSelectMove(event, aiMove.toString(), gif);
                    DGame.playerNotTurn.timeoutDuration = 0;
                    DGame.playerTurn.isZaWarudo = false;
                    DGame.switchTurns();
                    int x = (int)(Math.random()*3);
                    int y = x + (int)(Math.random()*3)+1;
                    int z = y + (int)(Math.random()*9)+1;

                    DGame.lastDisplay.get(1).editMessageEmbeds(new EmbedBuilder()
                            .setAuthor(DGame.playerTurn.member.getEffectiveName() + "'s turn", "https://github.com/BooleanCube/lollipop-bot", DGame.playerTurn.member.getEffectiveAvatarUrl())
                            .setDescription("What is your move?")
                            .setFooter("Quick! You have 30 seconds to react!")
                            .build()
                    ).setActionRow(
                            DGame.moveButtons[x].asDisabled(),
                            DGame.moveButtons[y].asDisabled(),
                            DGame.moveButtons[z].asDisabled(),
                            DGame.surrenderButton.asDisabled()
                    ).queue();
                    DGame.lastDisplay.get(1).editMessageComponents().setActionRow(
                            DGame.moveButtons[x],
                            DGame.moveButtons[y],
                            DGame.moveButtons[z],
                            DGame.surrenderButton
                    ).queueAfter(1, TimeUnit.SECONDS);
                } else DGame.switchTurns();
            }

            if(DGame.checkWin(event.getChannel())) {
                DGame.timeout.cancel(false);
                DGame.editTimeout.cancel(false);
            }
        }
    }

}
