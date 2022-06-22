package lollipop.commands.search.animecomps;

import lollipop.API;
import lollipop.pages.AnimePage;
import lollipop.pages.CharacterList;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

import java.util.HashMap;

public class Characters {

    static API api = new API();
    public static HashMap<Long, CharacterList> messageToPage = new HashMap<>();

    /**
     * Runs a request to get anime characters
     * @param event select menu interaction event
     * @param page anime page
     */
    public static void run(SelectMenuInteractionEvent event, AnimePage page) {
        api.getCharacters(event, page);
    }

}
