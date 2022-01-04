package lollipop;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class Listener extends ListenerAdapter {

    public final Manager m = new Manager();
    public final TestCM testM = new TestCM();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println(event.getJDA().getSelfUser().getName() + " is online!");
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().equalsIgnoreCase(CONSTANT.PREFIX+"shutdown") && (event.getAuthor().getIdLong()==CONSTANT.OWNERID)) {
            event.getJDA().shutdown();
            System.exit(0);
        }
        if(event.getJDA().getSelfUser().getIdLong() == CONSTANT.TESTID) testM.run(event);
        else m.run(event);
    }

}
