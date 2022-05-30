package lollipop.commands.trivia;

import awatch.model.Question;
import net.dv8tion.jda.api.entities.User;

import java.util.concurrent.ScheduledFuture;

/**
 * Trivia Game Model
 */
public class TGame {

    public Question question;
    public User user;
    public ScheduledFuture<?> startTimeout;
    public ScheduledFuture<?> gameTimeout;

    /**
     * Constructer to initialize all the variables
     * @param u user / player
     * @param t start timeout for generating question
     */
    public TGame(User u, ScheduledFuture<?> t) {
        this.user = u;
        this.startTimeout = t;
    }

}
