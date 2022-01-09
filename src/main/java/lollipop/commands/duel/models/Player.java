package lollipop.commands.duel.models;

import net.dv8tion.jda.api.entities.Member;

public class Player {
    public Member member = null;
    public int strengthGain = 0;
    public int HP = 100;
    public boolean isDefending = false;
    public long timeoutStart = 0L;
    public double timeoutDuration = 0;
    public boolean isTimedOut() {
        return (double)(System.currentTimeMillis()-timeoutStart)/1000 <= timeoutDuration;
    }
    public boolean isZaWarudo = false;
}
