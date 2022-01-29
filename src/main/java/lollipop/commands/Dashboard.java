package lollipop.commands;

import lollipop.Constant;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.lang.management.RuntimeMXBean;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dashboard implements Command {

    public Runtime runtime = Runtime.getRuntime();

    @Override
    public String[] getAliases() {
        return new String[] {"dash", "statinfo"};
    }

    @Override
    public String getCategory() {
        return "Owner";
    }

    @Override
    public String getHelp() {
        return "Shows you the statistics and resource usage of the bot!\nUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        if(event.getUser().getIdLong() != Constant.OWNER_ID) return;
        if(event.getJDA().getSelfUser().getIdLong() == Constant.TEST_ID) return;
        EmbedBuilder msg = new EmbedBuilder()
                .setTitle("Lollipop Dashboard")
                .setFooter("lollipop v6.2")
                .addField("System", osInfo(), true)
                .addField("Memory", memInfo(), true)
                .addField("CPU", cpuInfo(), false)
                .addField("Shards", shardInfo(event), false)
                .addField("Discord", botInfo(event), true)
                .addField("Uptime", uptimeInfo(), true);
        event.replyEmbeds(msg.build()).queue();
    }

    public String memInfo() {
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        sb.append("Free memory: `").append(format.format(freeMemory / 1024)).append("`\nAllocated memory: `")
        .append(format.format(allocatedMemory / 1024)).append("`\nMax memory: `").append(format.format(maxMemory / 1024))
        .append("`\nTotal free memory: `").append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024)).append("`");
        return sb.toString();
    }
    public String osInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("OS: `").append(System.getProperty("os.name")).append("`\nVersion: `").append(System.getProperty("os.version"))
        .append("`\nArchitecture: `").append(System.getProperty("os.arch")).append("`");
        return sb.toString();
    }
    public String cpuInfo() {
        OperatingSystemMXBean operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        int availableProcessors = operatingSystemMXBean.getAvailableProcessors();
        long processCpuTime = operatingSystemMXBean.getProcessCpuTime();
        double cpuProcLoad = operatingSystemMXBean.getProcessCpuLoad();
        double cpuSysLoad = operatingSystemMXBean.getSystemCpuLoad();
        double systemLoad = operatingSystemMXBean.getSystemLoadAverage();
        StringBuilder sb = new StringBuilder();
        sb.append("CPU Process Time: `").append(processCpuTime).append("`\nProcess CPU Load: `").append(cpuProcLoad)
        .append("`\nSystem Load Average: `").append(systemLoad).append("`\nSystem CPU Load: `").append(cpuSysLoad).append("`\n")
        .append("Available processors (cores): `").append(runtime.availableProcessors()).append("`");
        return sb.toString();
    }
    public String uptimeInfo() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeMXBean.getUptime();
        long uptimeInSeconds = uptime / 1000;
        long numberOfHours = uptimeInSeconds / (60 * 60);
        long numberOfMinutes = (uptimeInSeconds / 60) - (numberOfHours * 60);
        long numberOfSeconds = uptimeInSeconds % 60;
        StringBuilder sb = new StringBuilder();
        sb.append(numberOfHours).append(" hour(s), ").append(numberOfMinutes).append(" minute(s), ").append(numberOfSeconds).append(" second(s)");
        return sb.toString();
    }
    public String botInfo(SlashCommandEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("Server Count `").append(event.getJDA().getShardManager().getGuilds().size()).append("`\nUser Count: `").append(event.getJDA().getUsers().size()).append("`\nPing: `")
                .append(event.getJDA().getGatewayPing()).append("`");
        return sb.toString();
    }
    public String shardInfo(SlashCommandEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("Average Ping: `").append(event.getJDA().getShardManager().getAverageGatewayPing()).append("`\nCurrent Shard ID: `").append(event.getJDA().getShardInfo().getShardId()).append("`\nTotal Shards: `")
        .append(event.getJDA().getShardInfo().getShardTotal()).append("`\nShard Statuses: \n");
        ArrayList<JDA> shards = new ArrayList<>(event.getJDA().getShardManager().getShards());
        Collections.reverse(shards);
        for(JDA shard : shards)
            sb.append("> Shard ID **").append(shard.getShardInfo().getShardId()).append("** Status = `")
                    .append(shard.getShardManager().getStatus(shard.getShardInfo().getShardId())).append("`\n");
        return sb.toString();
    }

}
