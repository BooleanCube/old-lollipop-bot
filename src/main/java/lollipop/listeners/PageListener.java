package lollipop.listeners;

import awatch.model.Anime;
import lollipop.API;
import lollipop.BotStatistics;
import lollipop.Constant;
import lollipop.Database;
import lollipop.commands.*;
import lollipop.commands.search.*;
import lollipop.commands.search.animecomps.*;
import lollipop.commands.search.mangacomps.Chapters;
import lollipop.commands.trivia.TGame;
import lollipop.commands.trivia.Trivia;
import lollipop.pages.*;
import mread.model.Chapter;
import mread.model.Manga;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Responds to page interactions for commands which include pagination
 */
public class PageListener extends ListenerAdapter {

    static API api = new API();

    /**
     * Triggered when a button is pressed
     * @param event button interaction event
     */
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if(Objects.requireNonNull(event.getUser()).isBot()) return;
        long id = event.getMessageIdLong();
        if(News.messageToPage.containsKey(id)) {
            Newspaper page = News.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `search` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(event.getButton().getId(), "left")) {
                if(page.pageNumber>1)
                    event.editMessageEmbeds(
                            page.articles.get(--page.pageNumber-1).toEmbed()
                                    .setFooter("Page " + page.pageNumber + "/" + page.articles.size())
                                    .build()
                    ).queue();
                else {
                    int loop = page.articles.size()-1;
                    page.pageNumber = loop+1;
                    event.editMessageEmbeds(
                            page.articles.get(loop).toEmbed()
                                    .setFooter("Page " + page.pageNumber + "/" + page.articles.size())
                                    .build()
                    ).queue();
                }
            } else if(Objects.equals(event.getButton().getId(), "right")) {
                if(page.pageNumber<page.articles.size())
                    event.editMessageEmbeds(
                            page.articles.get(++page.pageNumber-1).toEmbed()
                                    .setFooter("Page " + page.pageNumber + "/" + page.articles.size())
                                    .build()
                    ).queue();
                else {
                    page.pageNumber = 1;
                    event.editMessageEmbeds(
                            page.articles.get(0).toEmbed()
                                    .setFooter("Page " + page.pageNumber + "/" + page.articles.size())
                                    .build()
                    ).queue();
                }
            }
        }
        if(Episodes.messageToPage.containsKey(id)) {
            EpisodeList page = Episodes.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `search` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(event.getButton().getId(), "left")) {
                if(page.pageNumber>1)
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Episode List")
                                    .setDescription(page.pages.get(--page.pageNumber-1))
                                    .setFooter("Page " + page.pageNumber + "/" + page.pages.size())
                                    .build()
                    ).queue();
                else {
                    int loop = page.pages.size()-1;
                    page.pageNumber = loop+1;
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Episode List")
                                    .setDescription(page.pages.get(loop))
                                    .setFooter("Page " + page.pageNumber + "/" + page.pages.size())

                                    .build()
                    ).queue();
                }
            } else if(Objects.equals(event.getButton().getId(), "right")) {
                if(page.pageNumber<page.pages.size())
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Episode List")
                                    .setDescription(page.pages.get(++page.pageNumber-1))
                                    .setFooter("Page " + page.pageNumber + "/" + page.pages.size())
                                    .build()
                    ).queue();
                else {
                    page.pageNumber = 1;
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Episode List")
                                    .setDescription(page.pages.get(0))
                                    .setFooter("Page " + page.pageNumber + "/" + page.pages.size())
                                    .build()
                    ).queue();
                }
            }
        }
        if(Characters.messageToPage.containsKey(id)) {
            CharacterList page = Characters.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `search` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(event.getButton().getId(), "left")) {
                if(page.pageNumber>1)
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Character List")
                                    .setDescription(page.pages.get(--page.pageNumber-1))
                                    .setFooter("Page " + page.pageNumber + "/" + page.pages.size())
                                    .build()
                    ).queue();
                else {
                    int loop = page.pages.size()-1;
                    page.pageNumber = loop+1;
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Character List")
                                    .setDescription(page.pages.get(loop))
                                    .setFooter("Page " + page.pageNumber + "/" + page.pages.size())

                                    .build()
                    ).queue();
                }
            } else if(Objects.equals(event.getButton().getId(), "right")) {
                if(page.pageNumber<page.pages.size())
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Character List")
                                    .setDescription(page.pages.get(++page.pageNumber-1))
                                    .setFooter("Page " + page.pageNumber + "/" + page.pages.size())
                                    .build()
                    ).queue();
                else {
                    page.pageNumber = 1;
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Character List")
                                    .setDescription(page.pages.get(0))
                                    .setFooter("Page " + page.pageNumber + "/" + page.pages.size())
                                    .build()
                    ).queue();
                }
            }
        }
        if(Search.messageToAnimePage.containsKey(id)) {
            AnimePage page = Search.messageToAnimePage.get(id);
            if (event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `search` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if (Objects.equals(event.getButton().getId(), "left")) {
                if (page.pageNumber > 1)
                    event.editMessageEmbeds(
                            page.animes.get(--page.pageNumber - 1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                else {
                    int loop = page.animes.size()-1;
                    page.pageNumber = loop+1;
                    event.editMessageEmbeds(
                            page.animes.get(loop).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                }
            } else if (Objects.equals(event.getButton().getId(), "right")) {
                if (page.pageNumber < page.animes.size()) {
                    event.editMessageEmbeds(
                            page.animes.get(++page.pageNumber - 1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                } else {
                    page.pageNumber = 1;
                    event.editMessageEmbeds(
                            page.animes.get(0).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                }
            }
        }
        if(Search.messageToMangaPage.containsKey(id)) {
            MangaPage page = Search.messageToMangaPage.get(id);
            if (event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `search` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if (Objects.equals(event.getButton().getId(), "left")) {
                if (page.pageNumber > 1)
                    event.editMessageEmbeds(
                            page.mangas.get(--page.pageNumber - 1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.mangas.size()).build()
                    ).queue();
                else {
                    int loop = page.mangas.size()-1;
                    page.pageNumber = loop+1;
                    event.editMessageEmbeds(
                            page.mangas.get(loop).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.mangas.size()).build()
                    ).queue();
                }
            } else if (Objects.equals(event.getButton().getId(), "right")) {
                if (page.pageNumber < page.mangas.size()) {
                    event.editMessageEmbeds(
                            page.mangas.get(++page.pageNumber - 1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.mangas.size()).build()
                    ).queue();
                } else {
                    page.pageNumber = 1;
                    event.editMessageEmbeds(
                            page.mangas.get(0).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.mangas.size()).build()
                    ).queue();
                }
            }
        }
        if(Top.messageToPage.containsKey(id)) {
            AnimePage page = Top.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `top` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(event.getButton().getId(), "left")) {
                if(page.pageNumber>1)
                    event.editMessageEmbeds(
                            page.animes.get(--page.pageNumber-1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                else {
                    int loop = page.animes.size()-1;
                    page.pageNumber = loop+1;
                    event.editMessageEmbeds(
                            page.animes.get(loop).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                }
            } else if(Objects.equals(event.getButton().getId(), "right")) {
                if(page.pageNumber<page.animes.size()) {
                    event.editMessageEmbeds(
                            page.animes.get(++page.pageNumber - 1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                }
                else {
                    page.pageNumber = 1;
                    event.editMessageEmbeds(
                            page.animes.get(0).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                }
            } else if(Objects.equals(event.getButton().getId(), "trailer")) {
                Anime a = page.animes.get(page.pageNumber-1);
                event.reply(a.trailer.equals("Unkown") || a.trailer.trim().equals("") ? "I could not find a trailer for this anime!" : a.trailer).setEphemeral(true).complete();
            }
        }
        if(Popular.messageToPage.containsKey(id)) {
            AnimePage page = Popular.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `popular` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(event.getButton().getId(), "left")) {
                if(page.pageNumber>1)
                    event.editMessageEmbeds(
                            page.animes.get(--page.pageNumber-1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                else {
                    int loop = page.animes.size()-1;
                    page.pageNumber = loop+1;
                    event.editMessageEmbeds(
                            page.animes.get(loop).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                }
            } else if(Objects.equals(event.getButton().getId(), "right")) {
                if(page.pageNumber<page.animes.size()) {
                    event.editMessageEmbeds(
                            page.animes.get(++page.pageNumber - 1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                }
                else {
                    page.pageNumber = 1;
                    event.editMessageEmbeds(
                            page.animes.get(0).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                }
            } else if(Objects.equals(event.getButton().getId(), "trailer")) {
                Anime a = page.animes.get(page.pageNumber-1);
                event.reply(a.trailer.equals("Unkown") || a.trailer.trim().equals("") ? "I could not find a trailer for this anime!" : a.trailer).setEphemeral(true).complete();
            }
        }
        if(Latest.messageToPage.containsKey(id)) {
            AnimePage page = Latest.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `latest` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(event.getButton().getId(), "left")) {
                if(page.pageNumber>1)
                    event.editMessageEmbeds(
                            page.animes.get(--page.pageNumber-1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                else {
                    int loop = page.animes.size()-1;
                    page.pageNumber = loop+1;
                    event.editMessageEmbeds(
                            page.animes.get(loop).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                }
            } else if(Objects.equals(event.getButton().getId(), "right")) {
                if(page.pageNumber<page.animes.size()) {
                    event.editMessageEmbeds(
                            page.animes.get(++page.pageNumber - 1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                }
                else {
                    page.pageNumber = 1;
                    event.editMessageEmbeds(
                            page.animes.get(0).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                    ).queue();
                }
            } else if(Objects.equals(event.getButton().getId(), "trailer")) {
                Anime a = page.animes.get(page.pageNumber-1);
                event.reply(a.trailer.equals("Unkown") || a.trailer.trim().equals("") ? "I could not find a trailer for this anime!" : a.trailer).setEphemeral(true).complete();
            }
        }
        if(ChapterList.messageToChapter.containsKey(id)) {
            Chapter chapter = ChapterList.messageToChapter.get(id);
            if(event.getUser() != chapter.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `latest` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(event.getButton().getId(), "left")) {
                if(chapter.pageNumber>1)
                    event.editMessageEmbeds(
                            chapter.embedPage(--chapter.pageNumber-1).build()
                    ).queue();
                else {
                    int loop = chapter.pages.size()-1;
                    chapter.pageNumber = loop+1;
                    event.editMessageEmbeds(
                            chapter.embedPage(loop).build()
                    ).queue();
                }
            } else if(Objects.equals(event.getButton().getId(), "right")) {
                if(chapter.pageNumber<chapter.pages.size()) {
                    event.editMessageEmbeds(
                            chapter.embedPage(++chapter.pageNumber - 1).build()
                    ).queue();
                }
                else {
                    chapter.pageNumber = 1;
                    event.editMessageEmbeds(
                            chapter.embedPage(0).build()
                    ).queue();
                }
            }
        }
        if(RandomAnime.messageToPage.containsKey(id)) {
            AnimePage page = RandomAnime.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `top` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(event.getButton().getId(), "trailer")) {
                Anime a = page.animes.get(0);
                event.reply(a.trailer.equals("Unkown") || a.trailer.trim().equals("") ? "I could not find a trailer for this anime!" : a.trailer).setEphemeral(true).complete();
            }
        }
        if(Chapters.messageToPage.containsKey(id)) {
            ChapterList page = Chapters.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `top` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            MessageEmbed embed = event.getMessage().getEmbeds().get(0);
            if(Objects.equals(event.getButton().getId(), "left")) {
                if(page.pageNumber>1) {
                    int startIndex = --page.pageNumber;
                    startIndex--;
                    startIndex *= 4;
                    List<SelectMenu> menus = page.menus.subList(startIndex, Math.min(startIndex+4, page.menus.size()));
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle(embed.getTitle())
                                    .setDescription(embed.getDescription())
                                    .setFooter("Page " + page.pageNumber + "/" + page.pages)
                                    .build()
                    ).setActionRows(
                            ActionRow.of(menus.get(0)),
                            ActionRow.of(
                                    1 >= menus.size() ?
                                            SelectMenu.create("disabled1")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            menus.get(1)
                            ),
                            ActionRow.of(
                                    2 >= menus.size() ?
                                            SelectMenu.create("disabled2")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            menus.get(2)
                            ),
                            ActionRow.of(
                                    3 >= menus.size() ?
                                            SelectMenu.create("disabled3")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            menus.get(3)
                            ),
                            ActionRow.of(
                                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                                    Button.secondary("right", Emoji.fromUnicode("➡"))
                            )
                    ).queue();
                }
                else {
                    int loop = page.pages-1;
                    page.pageNumber = loop+1;
                    int startIndex = (page.pageNumber-1) * 4;
                    List<SelectMenu> menus = page.menus.subList(startIndex, Math.min(startIndex+4, page.menus.size()));
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle(embed.getTitle())
                                    .setDescription(embed.getDescription())
                                    .setFooter("Page " + page.pageNumber + "/" + page.pages)
                                    .build()
                    ).setActionRows(
                            ActionRow.of(menus.get(0)),
                            ActionRow.of(
                                    1 >= menus.size() ?
                                            SelectMenu.create("disabled1")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            menus.get(1)
                            ),
                            ActionRow.of(
                                    2 >= menus.size() ?
                                            SelectMenu.create("disabled2")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            menus.get(2)
                            ),
                            ActionRow.of(
                                    3 >= menus.size() ?
                                            SelectMenu.create("disabled3")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            menus.get(3)
                            ),
                            ActionRow.of(
                                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                                    Button.secondary("right", Emoji.fromUnicode("➡"))
                            )
                    ).queue();
                }
            } else if(Objects.equals(event.getButton().getId(), "right")) {
                if(page.pageNumber<page.pages) {
                    int startIndex = page.pageNumber++;
                    startIndex *= 4;
                    List<SelectMenu> menus = page.menus.subList(startIndex, Math.min(startIndex+4, page.menus.size()));
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle(embed.getTitle())
                                    .setDescription(embed.getDescription())
                                    .setFooter("Page " + page.pageNumber + "/" + page.pages)
                                    .build()
                    ).setActionRows(
                            ActionRow.of(menus.get(0)),
                            ActionRow.of(
                                    1 >= menus.size() ?
                                            SelectMenu.create("disabled1")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            menus.get(1)
                            ),
                            ActionRow.of(
                                    2 >= menus.size() ?
                                            SelectMenu.create("disabled2")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            menus.get(2)
                            ),
                            ActionRow.of(
                                    3 >= menus.size() ?
                                            SelectMenu.create("disabled3")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            menus.get(3)
                            ),
                            ActionRow.of(
                                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                                    Button.secondary("right", Emoji.fromUnicode("➡"))
                            )
                    ).queue();
                }
                else {
                    page.pageNumber = 1;
                    int startIndex = 0;
                    List<SelectMenu> menus = page.menus.subList(startIndex, Math.min(startIndex+4, page.menus.size()));
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle(embed.getTitle())
                                    .setDescription(embed.getDescription())
                                    .setFooter("Page " + page.pageNumber + "/" + page.pages)
                                    .build()
                    ).setActionRows(
                            ActionRow.of(menus.get(0)),
                            ActionRow.of(
                                    1 >= menus.size() ?
                                            SelectMenu.create("disabled1")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            menus.get(1)
                            ),
                            ActionRow.of(
                                    2 >= menus.size() ?
                                            SelectMenu.create("disabled2")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            menus.get(2)
                            ),
                            ActionRow.of(
                                    3 >= menus.size() ?
                                            SelectMenu.create("disabled3")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            menus.get(3)
                            ),
                            ActionRow.of(
                                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                                    Button.secondary("right", Emoji.fromUnicode("➡"))
                            )
                    ).queue();
                }
            }
        }
        if(Trivia.openGames.containsKey(id)) {
            TGame game = Trivia.openGames.get(id);
            game.gameTimeout.cancel(false);
            if(event.getUser() != game.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `top` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(event.getButton().getId(), "right")) {
                Runnable success = () -> {
                    int xp = (int)(Math.random()*21)+40;
                    xp = (int)(xp* Constant.MULTIPLIER);
                    Database.addToUserBalance(event.getUser().getId(), xp);
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Correct Answer!")
                                    .setColor(Color.green)
                                    .setDescription("You guessed the correct anime!")
                                    .setThumbnail("https://cdn.discordapp.com/emojis/738541796174594108.webp?size=80&quality=lossless")
                                    .setFooter("You won " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png")
                                    .build()
                    ).setActionRows(Collections.emptyList()).queue();
                };
                Runnable failure = () -> {
                    int xp = (int)(Math.random()*21)+40;
                    Database.addToUserBalance(event.getUser().getId(), xp);
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Correct Answer!")
                                    .setColor(Color.green)
                                    .setDescription("You guessed the correct anime!")
                                    .setThumbnail("https://cdn.discordapp.com/emojis/738541796174594108.webp?size=80&quality=lossless")
                                    .setFooter("You won " + xp + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png")
                                    .build()
                    ).setActionRows(Collections.emptyList()).queue();
                };
                BotStatistics.sendMultiplier(game.user.getId(), success, failure);
            } else {
                Runnable success = () -> {
                    int xp = (int)(Math.random()*11)-20;
                    xp = (int)(xp/Constant.MULTIPLIER);
                    Database.addToUserBalance(event.getUser().getId(), xp);
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Wrong Answer!")
                                    .setColor(Color.red)
                                    .setDescription("You guessed the wrong anime!")
                                    .setThumbnail("https://cdn.discordapp.com/emojis/886080067195772970.webp?size=80&quality=lossless")
                                    .setFooter("You lost " + (-1*xp) + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png")
                                    .build()
                    ).setActionRows(Collections.emptyList()).queue();
                };
                Runnable failure = () -> {
                    int xp = (int)(Math.random()*11)-20;
                    Database.addToUserBalance(event.getUser().getId(), xp);
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Wrong Answer!")
                                    .setColor(Color.red)
                                    .setDescription("You guessed the wrong anime!")
                                    .setThumbnail("https://cdn.discordapp.com/emojis/886080067195772970.webp?size=80&quality=lossless")
                                    .setFooter("You lost " + (-1*xp) + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png")
                                    .build()
                    ).setActionRows(Collections.emptyList()).queue();
                };
                BotStatistics.sendMultiplier(game.user.getId(), success, failure);
            }
            Trivia.openGames.remove(id);
        }
    }

    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        if(event.getUser().isBot()) return;
        long id = event.getMessageIdLong();
        if(Search.messageToAnimePage.containsKey(id)) {
            AnimePage page = Search.messageToAnimePage.get(id);
            if(event.getInteraction().getValues().get(0).equals("popularity")) {
                page.animes.sort(Comparator.comparingInt(a -> a.popularity));
                page.currentPlaceholder = "Sort by popularity";
                page.pageNumber = 1;
                event.editMessageEmbeds(
                        page.animes.get(0).toEmbed().setFooter("Page 1/" + page.animes.size()).build()
                ).setActionRows(
                        ActionRow.of(
                                SelectMenu.create("order")
                                        .setPlaceholder("Sort by popularity")
                                        .addOption("Sort by popularity", "popularity")
                                        .addOption("Sort by ranks", "ranks")
                                        .addOption("Sort by latest", "time")
                                        .build()
                        ),
                        ActionRow.of(
                                SelectMenu.create("components")
                                        .setPlaceholder("Show component")
                                        .addOption("Trailer", "Trailer")
                                        .addOption("Episodes", "Episodes")
                                        .addOption("Characters", "Characters")
                                        .addOption("Themes", "Themes")
                                        .addOption("Recommendations", "Recommendations")
                                        .addOption("News", "News")
                                        .addOption("Review", "Review")
                                        .addOption("Statistics", "Statistics")
                                        .build()
                        ),
                        ActionRow.of(
                                Button.secondary("left", Emoji.fromUnicode("⬅")),
                                Button.secondary("right", Emoji.fromUnicode("➡"))
                        )
                ).queue();
            } else if(event.getInteraction().getValues().get(0).equals("ranks")) {
                page.animes.sort(Comparator.comparingInt(a -> a.rank));
                page.currentPlaceholder = "Sort by ranks";
                page.pageNumber = 1;
                event.editMessageEmbeds(
                        page.animes.get(0).toEmbed().setFooter("Page 1/" + page.animes.size()).build()
                ).setActionRows(
                        ActionRow.of(
                                SelectMenu.create("order")
                                        .setPlaceholder("Sort by ranks")
                                        .addOption("Sort by popularity", "popularity")
                                        .addOption("Sort by ranks", "ranks")
                                        .addOption("Sort by latest", "time")
                                        .build()
                        ),
                        ActionRow.of(
                                SelectMenu.create("components")
                                        .setPlaceholder("Show component")
                                        .addOption("Trailer", "Trailer")
                                        .addOption("Episodes", "Episodes")
                                        .addOption("Characters", "Characters")
                                        .addOption("Themes", "Themes")
                                        .addOption("Recommendations", "Recommendations")
                                        .addOption("News", "News")
                                        .addOption("Review", "Review")
                                        .addOption("Statistics", "Statistics")
                                        .build()
                        ),
                        ActionRow.of(
                                Button.secondary("left", Emoji.fromUnicode("⬅")),
                                Button.secondary("right", Emoji.fromUnicode("➡"))
                        )
                ).queue();
            } else if(event.getInteraction().getValues().get(0).equals("time")) {
                page.animes.sort((a,b) -> b.lastAired.compareTo(a.lastAired));
                page.currentPlaceholder = "Sort by latest";
                page.pageNumber = 1;
                event.editMessageEmbeds(
                        page.animes.get(0).toEmbed().setFooter("Page 1/" + page.animes.size()).build()
                ).setActionRows(
                        ActionRow.of(
                                SelectMenu.create("order")
                                        .setPlaceholder("Sort by latest")
                                        .addOption("Sort by popularity", "popularity")
                                        .addOption("Sort by ranks", "ranks")
                                        .addOption("Sort by latest", "time")
                                        .build()
                        ),
                        ActionRow.of(
                                SelectMenu.create("components")
                                        .setPlaceholder("Show component")
                                        .addOption("Trailer", "Trailer")
                                        .addOption("Episodes", "Episodes")
                                        .addOption("Characters", "Characters")
                                        .addOption("Themes", "Themes")
                                        .addOption("Recommendations", "Recommendations")
                                        .addOption("News", "News")
                                        .addOption("Review", "Review")
                                        .addOption("Statistics", "Statistics")
                                        .build()
                        ),
                        ActionRow.of(
                                Button.secondary("left", Emoji.fromUnicode("⬅")),
                                Button.secondary("right", Emoji.fromUnicode("➡"))
                        )
                ).queue();
            } else if(event.getInteraction().getValues().get(0).equals("Episodes")) {
                    if (page.animes.get(page.pageNumber-1).episodeList == null)
                        Episodes.run(event, page);
                    else {
                        EpisodeList episodes = page.animes.get(page.pageNumber-1).episodeList;
                        episodes.pageNumber = 1;
                        InteractionHook msg = event.replyEmbeds(
                                new EmbedBuilder()
                                        .setTitle("Episode List")
                                        .setDescription(episodes.pages.get(0))
                                        .setFooter("Page 1/" + episodes.pages.size())
                                        .build()
                        ).addActionRow(
                                Button.secondary("left", Emoji.fromUnicode("⬅")),
                                Button.secondary("right", Emoji.fromUnicode("➡"))
                        ).setEphemeral(true).complete();
                        Message m = msg.retrieveOriginal().complete();
                        event.getMessage().editMessageComponents().setActionRows(
                                ActionRow.of(
                                        SelectMenu.create("order")
                                                .setPlaceholder(page.currentPlaceholder)
                                                .addOption("Sort by popularity", "popularity")
                                                .addOption("Sort by ranks", "ranks")
                                                .addOption("Sort by latest", "time")
                                                .build()
                                ),
                                ActionRow.of(
                                        SelectMenu.create("components")
                                                .setPlaceholder("Show component")
                                                .addOption("Trailer", "Trailer")
                                                .addOption("Episodes", "Episodes")
                                                .addOption("Characters", "Characters")
                                                .addOption("Themes", "Themes")
                                                .addOption("Recommendations", "Recommendations")
                                                .addOption("News", "News")
                                                .addOption("Review", "Review")
                                                .addOption("Statistics", "Statistics")
                                                .build()
                                ),
                                ActionRow.of(
                                        Button.secondary("left", Emoji.fromUnicode("⬅")),
                                        Button.secondary("right", Emoji.fromUnicode("➡"))
                                )
                        ).queue();
                        Episodes.messageToPage.put(m.getIdLong(), episodes);
                        msg.editOriginalComponents()
                                .queueAfter(3, TimeUnit.MINUTES, me -> Episodes.messageToPage.remove(m.getIdLong()));
                    }
            } else if(event.getInteraction().getValues().get(0).equals("Characters")) {
                if (page.animes.get(page.pageNumber-1).characterList == null)
                    Characters.run(event, page);
                else {
                    CharacterList characters = page.animes.get(page.pageNumber-1).characterList;
                    characters.pageNumber = 1;
                    InteractionHook msg = event.replyEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Character List")
                                    .setDescription(characters.pages.get(0))
                                    .setFooter("Page 1/" + characters.pages.size())
                                    .build()
                    ).addActionRow(
                            Button.secondary("left", Emoji.fromUnicode("⬅")),
                            Button.secondary("right", Emoji.fromUnicode("➡"))
                    ).setEphemeral(true).complete();
                    Message m = msg.retrieveOriginal().complete();
                    event.getMessage().editMessageComponents().setActionRows(
                            ActionRow.of(
                                    SelectMenu.create("order")
                                            .setPlaceholder(page.currentPlaceholder)
                                            .addOption("Sort by popularity", "popularity")
                                            .addOption("Sort by ranks", "ranks")
                                            .addOption("Sort by latest", "time")
                                            .build()
                            ),
                            ActionRow.of(
                                    SelectMenu.create("components")
                                            .setPlaceholder("Show component")
                                            .addOption("Trailer", "Trailer")
                                            .addOption("Episodes", "Episodes")
                                            .addOption("Characters", "Characters")
                                            .addOption("Themes", "Themes")
                                            .addOption("Recommendations", "Recommendations")
                                            .addOption("News", "News")
                                            .addOption("Review", "Review")
                                            .addOption("Statistics", "Statistics")
                                            .build()
                            ),
                            ActionRow.of(
                                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                                    Button.secondary("right", Emoji.fromUnicode("➡"))
                            )
                    ).queue();
                    Characters.messageToPage.put(m.getIdLong(), characters);
                    msg.editOriginalComponents()
                            .queueAfter(3, TimeUnit.MINUTES, me -> Characters.messageToPage.remove(m.getIdLong()));
                }
            } else if(event.getInteraction().getValues().get(0).equals("Trailer")) {
                Anime a = page.animes.get(page.pageNumber - 1);
                event.reply(a.trailer.equals("Unkown") || a.trailer.trim().equals("") ? "I could not find a trailer for this anime!" : a.trailer).setEphemeral(true).complete();
                editDefaultSearchComp(event, page);
            } else if(event.getInteraction().getValues().get(0).equals("Recommendations")) {
                if(page.animes.get(page.pageNumber-1).recommendations == null)
                    Recommendation.run(event, page);
                else {
                    event.replyEmbeds(page.animes.get(page.pageNumber-1).recommendations).setEphemeral(true).queue();
                    editDefaultSearchComp(event, page);
                }
            } else if(event.getInteraction().getValues().get(0).equals("News")) {
                if(page.animes.get(page.pageNumber-1).news == null)
                    News.run(event, page);
                else {
                    Newspaper news = page.animes.get(page.pageNumber-1).news;
                    news.pageNumber = 1;
                    InteractionHook msg = event.replyEmbeds(
                            news.articles.get(0).toEmbed()
                                    .setFooter("Page 1/" + news.articles.size())
                                    .build()
                    ).addActionRow(
                            Button.secondary("left", Emoji.fromUnicode("⬅")),
                            Button.secondary("right", Emoji.fromUnicode("➡"))
                    ).setEphemeral(true).complete();
                    editDefaultSearchComp(event, page);
                    Message m = msg.retrieveOriginal().complete();
                    News.messageToPage.put(m.getIdLong(), news);
                    m.editMessageComponents()
                            .queueAfter(3, TimeUnit.MINUTES, me -> News.messageToPage.remove(m.getIdLong()));
                }
            } else if(event.getInteraction().getValues().get(0).equals("Statistics")) {
                if(page.animes.get(page.pageNumber-1).stats == null)
                    Statistics.run(event, page);
                else {
                    event.replyEmbeds(page.animes.get(page.pageNumber - 1).stats).setEphemeral(true).queue();
                    editDefaultSearchComp(event, page);
                }
            } else if(event.getInteraction().getValues().get(0).equals("Themes")) {
                if(page.animes.get(page.pageNumber-1).themes == null)
                    Themes.run(event, page);
                else {
                    event.replyEmbeds(page.animes.get(page.pageNumber - 1).themes).setEphemeral(true).queue();
                    editDefaultSearchComp(event, page);
                }
            } else if(event.getInteraction().getValues().get(0).equals("Review")) {
                if(page.animes.get(page.pageNumber-1).review == null)
                    Reviews.run(event, page);
                else {
                    event.replyEmbeds(page.animes.get(page.pageNumber - 1).review).setEphemeral(true).queue();
                    editDefaultSearchComp(event, page);
                }
            }
        }
        if(Chapters.messageToPage.containsKey(id)) {
            if(event.getInteraction().getValues().get(0).chars().allMatch(Character::isDigit)) {
                ChapterList list = Chapters.messageToPage.get(id);
                int chapterNum = Integer.parseInt(event.getInteraction().getValues().get(0));
                Chapter chapter = list.chapters.get(chapterNum);
                api.getPages(event, chapter);
            }
        }
        if(Search.messageToMangaPage.containsKey(id)) {
            MangaPage page = Search.messageToMangaPage.get(id);
            if(event.getInteraction().getValues().get(0).equals("views")) {
                page.mangas.sort(Comparator.comparingInt(a -> -a.views-(a.subscibers*10)));
                page.currentPlaceholder = "Sort by popularity";
                page.pageNumber = 1;
                event.editMessageEmbeds(
                        page.mangas.get(0).toEmbed().setFooter("Page 1/" + page.mangas.size()).build()
                ).setActionRows(
                        ActionRow.of(
                                SelectMenu.create("order")
                                        .setPlaceholder("Sort by popularity")
                                        .addOption("Sort by popularity", "views")
                                        .addOption("Sort by ranks", "ranks")
                                        .build()
                        ),
                        ActionRow.of(
                                SelectMenu.create("components")
                                        .setPlaceholder("Show component")
                                        .addOption("Chapters", "Chapters")
                                        .build()
                        ),
                        ActionRow.of(
                                Button.secondary("left", Emoji.fromUnicode("⬅")),
                                Button.secondary("right", Emoji.fromUnicode("➡"))
                        )
                ).queue();
            } else if(event.getInteraction().getValues().get(0).equals("score")) {
                page.mangas.sort(Comparator.comparingDouble(a -> -a.score));
                page.currentPlaceholder = "Sort by score";
                page.pageNumber = 1;
                event.editMessageEmbeds(
                        page.mangas.get(0).toEmbed().setFooter("Page 1/" + page.mangas.size()).build()
                ).setActionRows(
                        ActionRow.of(
                                SelectMenu.create("order")
                                        .setPlaceholder("Sort by score")
                                        .addOption("Sort by popularity", "views")
                                        .addOption("Sort by score", "score")
                                        .build()
                        ),
                        ActionRow.of(
                                SelectMenu.create("components")
                                        .setPlaceholder("Show component")
                                        .addOption("Chapters", "Chapters")
                                        .build()
                        ),
                        ActionRow.of(
                                Button.secondary("left", Emoji.fromUnicode("⬅")),
                                Button.secondary("right", Emoji.fromUnicode("➡"))
                        )
                ).queue();
            } else if(event.getInteraction().getValues().get(0).equals("Chapters")) {
                if(page.mangas.get(page.pageNumber-1).chapters == null)
                    Chapters.run(event, page);
                else {
                    Manga manga = page.mangas.get(page.pageNumber - 1);
                    ChapterList chapterList = manga.chapters;
                    InteractionHook msg = event.replyEmbeds(
                            new EmbedBuilder()
                                    .setTitle(manga.title + " Chapter List")
                                    .setFooter("Page 1/" + chapterList.pages)
                                    .build()
                    ).setEphemeral(true).addActionRows(
                            ActionRow.of(chapterList.menus.get(0)),
                            ActionRow.of(
                                    1 >= chapterList.menus.size() ?
                                            SelectMenu.create("disabled1")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            chapterList.menus.get(1)
                            ),
                            ActionRow.of(
                                    2 >= chapterList.menus.size() ?
                                            SelectMenu.create("disabled2")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            chapterList.menus.get(2)
                            ),
                            ActionRow.of(
                                    3 >= chapterList.menus.size() ?
                                            SelectMenu.create("disabled3")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh","really")
                                                    .setDisabled(true)
                                                    .build() :
                                            chapterList.menus.get(3)
                            ),
                            ActionRow.of(
                                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                                    Button.secondary("right", Emoji.fromUnicode("➡"))
                            )
                    ).complete();

                    Message message = msg.retrieveOriginal().complete();
                    editDefaultSearchComp(event, page);

                    Chapters.messageToPage.put(message.getIdLong(), manga.chapters);
                    msg.editOriginalComponents()
                            .setActionRows(
                                    ActionRow.of(
                                            SelectMenu.create("disabled1")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh", "really")
                                                    .setDisabled(true)
                                                    .build()
                                    ),
                                    ActionRow.of(
                                            SelectMenu.create("disabled2")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh", "really")
                                                    .setDisabled(true)
                                                    .build()
                                    ),
                                    ActionRow.of(
                                            SelectMenu.create("disabled3")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh", "really")
                                                    .setDisabled(true)
                                                    .build()
                                    ),
                                    ActionRow.of(
                                            SelectMenu.create("disabled4")
                                                    .setPlaceholder("Disabled")
                                                    .addOption("bruh", "really")
                                                    .setDisabled(true)
                                                    .build()
                                    ),
                                    ActionRow.of(
                                            Button.secondary("left", Emoji.fromUnicode("⬅")).asDisabled(),
                                            Button.secondary("right", Emoji.fromUnicode("➡")).asDisabled()
                                    )
                            ).queueAfter(25, TimeUnit.MINUTES, i -> Chapters.messageToPage.remove(message.getIdLong()));
                }
            }
        }
    }

    private static void editDefaultSearchComp(SelectMenuInteractionEvent event, AnimePage page) {
        event.getMessage().editMessageComponents().setActionRows(
                ActionRow.of(
                        SelectMenu.create("order")
                                .setPlaceholder(page.currentPlaceholder)
                                .addOption("Sort by popularity", "popularity")
                                .addOption("Sort by ranks", "ranks")
                                .addOption("Sort by latest", "time")
                                .build()
                ),
                ActionRow.of(
                        SelectMenu.create("components")
                                .setPlaceholder("Show component")
                                .addOption("Trailer", "Trailer")
                                .addOption("Episodes", "Episodes")
                                .addOption("Characters", "Characters")
                                .addOption("Themes", "Themes")
                                .addOption("Recommendations", "Recommendations")
                                .addOption("News", "News")
                                .addOption("Review", "Review")
                                .addOption("Statistics", "Statistics")
                                .build()
                ),
                ActionRow.of(
                        Button.secondary("left", Emoji.fromUnicode("⬅")),
                        Button.secondary("right", Emoji.fromUnicode("➡"))
                )
        ).queue();
    }

    private static void editDefaultSearchComp(SelectMenuInteractionEvent event, MangaPage page) {
        event.getMessage().editMessageComponents().setActionRows(
                ActionRow.of(
                        SelectMenu.create("order")
                                .setPlaceholder(page.currentPlaceholder)
                                .addOption("Sort by popularity", "views")
                                .addOption("Sort by ranks", "ranks")
                                .build()
                ),
                ActionRow.of(
                        SelectMenu.create("components")
                                .setPlaceholder("Show component")
                                .addOption("Chapters", "Chapters")
                                .build()
                ),
                ActionRow.of(
                        Button.secondary("left", Emoji.fromUnicode("⬅")),
                        Button.secondary("right", Emoji.fromUnicode("➡"))
                )
        ).queue();
    }

}
