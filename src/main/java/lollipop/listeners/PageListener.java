package lollipop.listeners;

import awatch.model.Anime;
import lollipop.commands.*;
import lollipop.commands.search.*;
import lollipop.commands.search.infos.*;
import lollipop.pages.EpisodeList;
import lollipop.pages.Newspaper;
import lollipop.pages.AnimePage;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Responds to page interactions for commands which include pagination
 */
public class PageListener extends ListenerAdapter {

    /**
     * Triggered when a button is pressed
     * @param event
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
                else
                    event.editMessageEmbeds(
                            page.articles.get(0).toEmbed()
                                    .setFooter("Page " + page.pageNumber + "/" + page.articles.size())
                                    .build()
                    ).queue();
            } else if(Objects.equals(event.getButton().getId(), "right")) {
                if(page.pageNumber<page.articles.size())
                    event.editMessageEmbeds(
                            page.articles.get(++page.pageNumber-1).toEmbed()
                                    .setFooter("Page " + page.pageNumber + "/" + page.articles.size())
                                    .build()
                    ).queue();
                else
                    event.editMessageEmbeds(
                            page.articles.get(page.articles.size()-1).toEmbed()
                                    .setFooter("Page " + page.pageNumber + "/" + page.articles.size())
                                    .build()
                    ).queue();
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
                else
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Episode List")
                                    .setDescription(page.pages.get(0))
                                    .setFooter("Page 1/" + page.pages.size())
                                    .build()
                    ).queue();
            } else if(Objects.equals(event.getButton().getId(), "right")) {
                if(page.pageNumber<page.pages.size())
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Episode List")
                                    .setDescription(page.pages.get(++page.pageNumber-1))
                                    .setFooter("Page " + page.pageNumber + "/" + page.pages.size())
                                    .build()
                    ).queue();
                else
                    event.editMessageEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Episode List")
                                    .setDescription(page.pages.get(page.pages.size()-1))
                                    .setFooter("Page " + page.pageNumber + "/" + page.pages.size())
                                    .build()
                    ).queue();
            }
        }
        if(MoreInfo.messageToPage.containsKey(id)) {
            AnimePage page = MoreInfo.messageToPage.get(id);
            if(Objects.equals(event.getButton().getId(), "recommendations")) {
                if(!page.recommendations.containsKey(page.pageNumber))
                    Recommendation.run(event, page);
                else
                    event.replyEmbeds(page.recommendations.get(page.pageNumber)).setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(event.getButton().getId(), "news")) {
                if(!page.news.containsKey(page.pageNumber))
                    News.run(event, page);
                else {
                    Newspaper news = page.news.get(page.pageNumber);
                    news.pageNumber = 1;
                    InteractionHook msg = event.replyEmbeds(
                            new EmbedBuilder()
                                    .setDescription("Searching for news...")
                                    .build()
                    ).setEphemeral(true).complete();
                    Message m = msg.editOriginalEmbeds(
                            news.articles.get(0).toEmbed()
                                    .setFooter("Page 1/" + news.articles.size())
                                    .build()
                    ).setActionRow(
                            Button.secondary("left", Emoji.fromUnicode("⬅")),
                            Button.secondary("right", Emoji.fromUnicode("➡"))
                    ).complete();
                    News.messageToPage.put(m.getIdLong(), news);
                    m.editMessageComponents()
                            .queueAfter(3, TimeUnit.MINUTES, me -> News.messageToPage.remove(m.getIdLong()));
                }
                return;
            }
            if(Objects.equals(event.getButton().getId(), "stats")) {
                if(!page.stats.containsKey(page.pageNumber))
                    Statistics.run(event, page);
                else
                    event.replyEmbeds(page.stats.get(page.pageNumber)).setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(event.getButton().getId(), "theme")) {
                if(!page.themes.containsKey(page.pageNumber))
                    Themes.run(event, page);
                else
                    event.replyEmbeds(page.themes.get(page.pageNumber)).setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(event.getButton().getId(), "review")) {
                if(!page.review.containsKey(page.pageNumber))
                    Reviews.run(event, page);
                else
                    event.replyEmbeds(page.review.get(page.pageNumber)).setEphemeral(true).queue();
                return;
            }
        }
        if(Search.messageToPage.containsKey(id)) {
            AnimePage page = Search.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `search` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(event.getButton().getId(), "episodes")) {
                if(!page.episodes.containsKey(page.pageNumber))
                    Episodes.run(event, page);
                else {
                    EpisodeList episodes = page.episodes.get(page.pageNumber);
                    episodes.pageNumber = 1;
                    InteractionHook msg = event.replyEmbeds(
                            new EmbedBuilder()
                                    .setDescription("Searching for episodes...")
                                    .build()
                    ).setEphemeral(true).complete();
                    Message m = msg.editOriginalEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Episode List")
                                    .setDescription(episodes.pages.get(0))
                                    .setFooter("Page 1/" + episodes.pages.size())
                                    .build()
                    ).setActionRow(
                            Button.secondary("left", Emoji.fromUnicode("⬅")),
                            Button.secondary("right", Emoji.fromUnicode("➡"))
                    ).complete();
                    Episodes.messageToPage.put(m.getIdLong(), episodes);
                    m.editMessageComponents()
                            .queueAfter(3, TimeUnit.MINUTES, me -> Episodes.messageToPage.remove(m.getIdLong()));
                }
                return;
            }
            if(Objects.equals(event.getButton().getId(), "more")) {
                MoreInfo.run(event, page);
                return;
            }
            if(page.mangas == null) {
                if(Objects.equals(event.getButton().getId(), "left")) {
                    if(page.pageNumber>1)
                        event.editMessageEmbeds(
                                page.animes.get(--page.pageNumber-1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    else
                        event.editMessageEmbeds(
                                page.animes.get(0).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                } else if(Objects.equals(event.getButton().getId(), "right")) {
                    if(page.pageNumber<page.animes.size()) {
                        event.editMessageEmbeds(
                                page.animes.get(++page.pageNumber - 1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    }
                    else {
                        event.editMessageEmbeds(
                                page.animes.get(page.animes.size()-1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    }
                } else if(Objects.equals(event.getButton().getId(), "trailer")) {
                    Anime a = page.animes.get(page.pageNumber-1);
                    event.reply(a.trailer.equals("Unkown") || a.trailer.trim().equals("") ? "I could not find a trailer for this anime!" : a.trailer).setEphemeral(true).complete();
                }
            } else if(page.animes == null) {
                if(Objects.equals(event.getButton().getId(), "left")) {
                    if(page.pageNumber>1) {
                        event.editMessageEmbeds(
                                page.mangas.get(--page.pageNumber - 1).toEmbed()
                                        .setFooter("Page " + page.pageNumber + "/" + page.mangas.size())
                                        .build()
                        ).queue();
                    }
                    else {
                        event.editMessageEmbeds(
                                page.mangas.get(0).toEmbed()
                                        .setFooter("Page " + page.pageNumber + "/" + page.mangas.size())
                                        .build()
                        ).queue();
                    }
                } else if(Objects.equals(event.getButton().getId(), "right")) {
                    if(page.pageNumber<page.animes.size()) {
                        event.editMessageEmbeds(
                                page.mangas.get(++page.pageNumber - 1).toEmbed()
                                        .setFooter("Page " + page.pageNumber + "/" + page.mangas.size())
                                        .build()
                        ).queue();
                    }
                    else {
                        event.editMessageEmbeds(
                                page.mangas.get(page.mangas.size()-1).toEmbed()
                                        .setFooter("Page " + page.pageNumber + "/" + page.mangas.size())
                                        .build()
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
            if(Objects.equals(event.getButton().getId(), "news")) {
                if(!page.news.containsKey(page.pageNumber))
                    News.run(event, page);
                else {
                    Newspaper news = page.news.get(page.pageNumber);
                    news.pageNumber = 1;
                    InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Searching for news...").build()).setEphemeral(true).complete();
                    Message m = msg.editOriginalEmbeds(news.articles.get(0).toEmbed().setFooter("Page 1/" + news.articles.size()).build()).setActionRow(
                            Button.secondary("left", Emoji.fromUnicode("⬅")),
                            Button.secondary("right", Emoji.fromUnicode("➡"))
                    ).complete();
                    News.messageToPage.put(m.getIdLong(), news);
                    m.editMessageComponents()
                            .queueAfter(3, TimeUnit.MINUTES, me -> News.messageToPage.remove(m.getIdLong()));
                }
                return;
            }
            if(Objects.equals(event.getButton().getId(), "stats")) {
                if(!page.stats.containsKey(page.pageNumber))
                    Statistics.run(event, page);
                else
                    event.replyEmbeds(page.stats.get(page.pageNumber)).setEphemeral(true).queue();
                return;
            }
            if(page.mangas == null) {
                if(Objects.equals(event.getButton().getId(), "left")) {
                    if(page.pageNumber>1)
                        event.editMessageEmbeds(
                                page.animes.get(--page.pageNumber-1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    else
                        event.editMessageEmbeds(
                                page.animes.get(0).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                } else if(Objects.equals(event.getButton().getId(), "right")) {
                    if(page.pageNumber<page.animes.size()) {
                        event.editMessageEmbeds(
                                page.animes.get(++page.pageNumber - 1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    }
                    else {
                        event.editMessageEmbeds(
                                page.animes.get(page.animes.size()-1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    }
                } else if(Objects.equals(event.getButton().getId(), "trailer")) {
                    Anime a = page.animes.get(page.pageNumber-1);
                    event.reply(a.trailer.equals("Unkown") || a.trailer.trim().equals("") ? "I could not find a trailer for this anime!" : a.trailer).setEphemeral(true).complete();
                }
            }
        }
        if(Latest.messageToPage.containsKey(id)) {
            AnimePage page = Latest.messageToPage.get(id);
            if(event.getUser() != page.user) {
                event.reply("You can't use the buttons because you didn't use this command! Use the `latest` command to be able to use buttons!").setEphemeral(true).queue();
                return;
            }
            if(Objects.equals(event.getButton().getId(), "news")) {
                if(!page.news.containsKey(page.pageNumber))
                    News.run(event, page);
                else {
                    Newspaper news = page.news.get(page.pageNumber);
                    news.pageNumber = 1;
                    InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Searching for news...").build()).setEphemeral(true).complete();
                    Message m = msg.editOriginalEmbeds(news.articles.get(0).toEmbed().setFooter("Page 1/" + news.articles.size()).build()).setActionRow(
                            Button.secondary("left", Emoji.fromUnicode("⬅")),
                            Button.secondary("right", Emoji.fromUnicode("➡"))
                    ).complete();
                    News.messageToPage.put(m.getIdLong(), news);
                    m.editMessageComponents()
                            .queueAfter(3, TimeUnit.MINUTES, me -> News.messageToPage.remove(m.getIdLong()));
                }
                return;
            }
            if(Objects.equals(event.getButton().getId(), "stats")) {
                if(!page.stats.containsKey(page.pageNumber))
                    Statistics.run(event, page);
                else
                    event.replyEmbeds(page.stats.get(page.pageNumber)).setEphemeral(true).queue();
                return;
            }
            if(page.mangas == null) {
                if(Objects.equals(event.getButton().getId(), "left")) {
                    if(page.pageNumber>1)
                        event.editMessageEmbeds(
                                page.animes.get(--page.pageNumber-1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    else
                        event.editMessageEmbeds(
                                page.animes.get(0).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                } else if(Objects.equals(event.getButton().getId(), "right")) {
                    if(page.pageNumber<page.animes.size()) {
                        event.editMessageEmbeds(
                                page.animes.get(++page.pageNumber - 1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    }
                    else {
                        event.editMessageEmbeds(
                                page.animes.get(page.animes.size()-1).toEmbed().setFooter("Page " + page.pageNumber + "/" + page.animes.size()).build()
                        ).queue();
                    }
                } else if(Objects.equals(event.getButton().getId(), "trailer")) {
                    Anime a = page.animes.get(page.pageNumber-1);
                    event.reply(a.trailer.equals("Unkown") || a.trailer.trim().equals("") ? "I could not find a trailer for this anime!" : a.trailer).setEphemeral(true).complete();
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
    }

}
