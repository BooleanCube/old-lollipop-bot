package lollipop.listeners;

import awatch.models.Anime;
import lollipop.models.Newspaper;
import lollipop.models.AnimePage;
import lollipop.Tools;
import lollipop.commands.News;
import lollipop.commands.Random;
import lollipop.commands.Search;
import lollipop.commands.Top;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class PageListener extends ListenerAdapter {

    public HashMap<Long, InteractionHook> trailerMessage = new HashMap<>();
    public HashMap<Long, Long> trailerToUser = new HashMap<>();

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        if(Objects.requireNonNull(event.getUser()).isBot()) return;
        long id = event.getMessageIdLong();
        if(News.messageToPage.containsKey(id)) {
            Newspaper page = News.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `news` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(Objects.requireNonNull(event.getButton()).getId(), "left")) {
                if(page.pageNumber>1)
                    event.editMessageEmbeds(
                            Tools.newsEmbed(page.articles.get(--page.pageNumber-1)).setFooter("Page " + page.pageNumber + "/" + page.articles.size()).build()
                    ).queue();
                else
                    event.editMessageEmbeds(
                            Tools.newsEmbed(page.articles.get(0)).setFooter("Page " + page.pageNumber + "/" + page.articles.size()).build()
                    ).queue();
            } else if(Objects.equals(Objects.requireNonNull(event.getButton()).getId(), "right")) {
                if(page.pageNumber<page.articles.size())
                    event.editMessageEmbeds(
                            Tools.newsEmbed(page.articles.get(++page.pageNumber-1)).setFooter("Page " + page.pageNumber + "/" + page.articles.size()).build()
                    ).queue();
                else
                    event.editMessageEmbeds(
                            Tools.newsEmbed(page.articles.get(page.articles.size()-1)).setFooter("Page " + page.pageNumber + "/" + page.articles.size()).build()
                    ).queue();
            }
        }
        if(Search.messageToPage.containsKey(id)) {
            AnimePage page = Search.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `search` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(page.mangas == null) {
                if(Objects.equals(Objects.requireNonNull(event.getButton()).getId(), "left")) {
                    if(page.pageNumber>1)
                        event.editMessageEmbeds(
                                Tools.animeToEmbed(page.animes.get(--page.pageNumber-1)).setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    else
                        event.editMessageEmbeds(
                                Tools.animeToEmbed(page.animes.get(0)).setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                } else if(Objects.equals(Objects.requireNonNull(event.getButton()).getId(), "right")) {
                    if(page.pageNumber<page.animes.size()) {
                        event.editMessageEmbeds(
                                Tools.animeToEmbed(page.animes.get(++page.pageNumber - 1)).setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    }
                    else {
                        event.editMessageEmbeds(
                                Tools.animeToEmbed(page.animes.get(page.animes.size()-1)).setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    }
                } else if(Objects.equals(event.getButton().getId(), "trailer")) {
                    Anime a = page.animes.get(page.pageNumber-1);
                    if(trailerMessage.containsKey(id))
                        try { trailerMessage.get(id).deleteOriginal().complete(); trailerMessage.remove(id); } catch(ErrorResponseException ignored) {}
                    InteractionHook m = event.reply(a.trailer.equals("Unkown") ? "I could not find a trailer for this anime!" : a.trailer).complete();
                    m.editOriginalComponents().setActionRow(
                            Button.danger("delete", Emoji.fromUnicode("\uD83D\uDDD1"))
                    ).queue();
                    trailerMessage.put(id, m);
                    trailerToUser.put(m.retrieveOriginal().complete().getIdLong(), page.user.getIdLong());
                }
            } else if(page.animes == null) {
                if(Objects.equals(Objects.requireNonNull(event.getButton()).getId(), "left")) {
                    if(page.pageNumber>1) {
                        event.editMessageEmbeds(
                                Tools.mangaToEmbed(page.mangas.get(--page.pageNumber - 1)).setFooter("Page " + page.pageNumber + "/" + page.mangas.size()).build()
                        ).queue();
                    }
                    else {
                        event.editMessageEmbeds(
                                Tools.mangaToEmbed(page.mangas.get(0)).setFooter("Page " + page.pageNumber + "/" + page.mangas.size()).build()
                        ).queue();
                    }
                } else if(Objects.equals(Objects.requireNonNull(event.getButton()).getId(), "right")) {
                    if(page.pageNumber<page.animes.size()) {
                        event.editMessageEmbeds(
                                Tools.mangaToEmbed(page.mangas.get(++page.pageNumber - 1)).setFooter("Page " + page.pageNumber + "/" + page.mangas.size()).build()
                        ).queue();
                    }
                    else {
                        event.editMessageEmbeds(
                                Tools.mangaToEmbed(page.mangas.get(page.mangas.size()-1)).setFooter("Page " + page.pageNumber + "/" + page.mangas.size()).build()
                        ).queue();
                    }
                }
            }
        }
        if(Top.messageToPage.containsKey(id)) {
            AnimePage page = Top.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `top` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(page.mangas == null) {
                if(Objects.equals(Objects.requireNonNull(event.getButton()).getId(), "left")) {
                    if(page.pageNumber>1)
                        event.editMessageEmbeds(
                                Tools.animeToEmbed(page.animes.get(--page.pageNumber-1)).setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    else
                        event.editMessageEmbeds(
                                Tools.animeToEmbed(page.animes.get(0)).setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                } else if(Objects.equals(Objects.requireNonNull(event.getButton()).getId(), "right")) {
                    if(page.pageNumber<page.animes.size()) {
                        event.editMessageEmbeds(
                                Tools.animeToEmbed(page.animes.get(++page.pageNumber - 1)).setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    }
                    else {
                        event.editMessageEmbeds(
                                Tools.animeToEmbed(page.animes.get(page.animes.size()-1)).setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    }
                } else if(Objects.equals(event.getButton().getId(), "trailer")) {
                    Anime a = page.animes.get(page.pageNumber-1);
                    if(trailerMessage.containsKey(id))
                        try { trailerMessage.get(id).deleteOriginal().complete(); trailerMessage.remove(id); } catch(ErrorResponseException ignored) {}
                    InteractionHook m = event.reply(a.trailer.equals("Unkown") ? "I could not find a trailer for this manga!" : a.trailer).complete();
                    m.editOriginalComponents().setActionRow(
                            Button.danger("delete", Emoji.fromUnicode("\uD83D\uDDD1"))
                    ).queue();
                    trailerMessage.put(id, m);
                    trailerToUser.put(m.retrieveOriginal().complete().getIdLong(), page.user.getIdLong());
                }
            }
        }
        if(Random.messageToPage.containsKey(id)) {
            AnimePage page = Random.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `top` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(Objects.requireNonNull(event.getButton()).getId(), "trailer")) {
                Anime a = page.animes.get(0);
                InteractionHook m = event.reply(a.trailer.equals("Unkown") ? "I could not find a trailer for this anime!" : a.trailer).complete();
                m.editOriginalComponents().setActionRow(
                        Button.danger("delete", Emoji.fromUnicode("\uD83D\uDDD1"))
                ).queue();
                trailerMessage.put(id, m);
                trailerToUser.put(m.retrieveOriginal().complete().getIdLong(), page.user.getIdLong());
                event.editButton(event.getButton().asDisabled()).queue();
            }
        }
        if(Objects.equals(Objects.requireNonNull(event.getButton()).getId(), "delete")) {
            if(trailerToUser.get(event.getMessageIdLong()) != event.getUser().getIdLong()) {
                event.reply("You can't use the delete button because you didn't use this command! Type `l!help` for a list of commands!").setEphemeral(true).queue();
                return;
            }
            event.getMessage().delete().queue();
            trailerToUser.remove(event.getMessageIdLong());
        }
    }
}
