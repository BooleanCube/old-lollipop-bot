package lollipop.commands.search.charactercomps;

import lollipop.API;
import lollipop.pages.CharacterPage;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public class VoiceActors {

    static API api = new API();

    public static void run(SelectMenuInteractionEvent event, CharacterPage page) {
        api.getCharacterVoices(event, page);
    }

}
