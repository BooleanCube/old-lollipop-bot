package lollipop.listeners;

import awatch.models.Anime;
import lollipop.Newspaper;
import lollipop.SearchPage;
import lollipop.Tools;
import lollipop.commands.News;
import lollipop.commands.Search;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PageListener extends ListenerAdapter {

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        if(Objects.requireNonNull(event.getUser()).isBot()) return;
        long id = event.getMessageIdLong();
        if(News.messageToPage.containsKey(id)) {
            Newspaper page = News.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't move the pages because you didn't use this command! Use the `news` command to be able to move pages!").setEphemeral(false).queue();
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
            SearchPage page = Search.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't move the pages because you didn't use this command! Use the `news` command to be able to move pages!").setEphemeral(false).queue();
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
                    if(page.pageNumber<page.animes.size())
                        event.editMessageEmbeds(
                                Tools.animeToEmbed(page.animes.get(++page.pageNumber-1)).setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    else
                        event.editMessageEmbeds(
                                Tools.animeToEmbed(page.animes.get(page.animes.size()-1)).setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                } else if(Objects.equals(event.getButton().getId(), "trailer") && event.getButton().getStyle() != ButtonStyle.SUCCESS) {
                    Anime a = page.animes.get(page.pageNumber-1);
                    event.reply(a.trailer.equals("Unkown") ? "I could not find a trailer for this manga!" : a.trailer).queue();
                    event.editButton(Button.success("trailer", Emoji.fromUnicode("▶"))).queue();
                }
            } else if(page.animes == null) {
                if(Objects.equals(Objects.requireNonNull(event.getButton()).getId(), "left")) {
                    if(page.pageNumber>1)
                        event.editMessageEmbeds(
                                Tools.mangaToEmbed(page.mangas.get(--page.pageNumber-1)).setFooter("Page " + page.pageNumber + "/" + page.mangas.size()).build()
                        ).queue();
                    else
                        event.editMessageEmbeds(
                                Tools.mangaToEmbed(page.mangas.get(0)).setFooter("Page " + page.pageNumber + "/" + page.mangas.size()).build()
                        ).queue();
                } else if(Objects.equals(Objects.requireNonNull(event.getButton()).getId(), "right")) {
                    if(page.pageNumber<page.animes.size())
                        event.editMessageEmbeds(
                                Tools.mangaToEmbed(page.mangas.get(++page.pageNumber-1)).setFooter("Page " + page.pageNumber + "/" + page.mangas.size()).build()
                        ).queue();
                    else
                        event.editMessageEmbeds(
                                Tools.mangaToEmbed(page.mangas.get(page.mangas.size()-1)).setFooter("Page " + page.pageNumber + "/" + page.mangas.size()).build()
                        ).queue();
                } else if(Objects.equals(event.getButton().getId(), "trailer") && event.getButton().getStyle() != ButtonStyle.SUCCESS) {
                    Anime a = page.animes.get(page.pageNumber-1);
                    event.reply(a.trailer.equals("Unkown") ? "I could not find a trailer for this manga!" : a.trailer).queue();
                    event.editButton(Button.success("trailer", Emoji.fromUnicode("▶"))).queue();
                }
            }
        }
    }
}
