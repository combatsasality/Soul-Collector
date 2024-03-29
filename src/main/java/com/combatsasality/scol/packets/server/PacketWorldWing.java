package com.combatsasality.scol.packets.server;

import com.combatsasality.scol.items.WorldWing;
import com.combatsasality.scol.registries.ScolItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class PacketWorldWing {
    public static UUID uuidSend = UUID.fromString("427d8cdc-1e87-4484-b785-279ec4721cd5");
    private final boolean pressed;

    public PacketWorldWing(boolean pressed) {
        this.pressed = pressed;
    }

    public static void encode(PacketWorldWing msg, PacketBuffer buf) {
        buf.writeBoolean(msg.pressed);
    }

    public static PacketWorldWing decode(PacketBuffer buf) {
        return new PacketWorldWing(buf.readBoolean());
    }

    public static void handle(PacketWorldWing msg, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity playerServ = ctx.get().getSender();
            Optional<SlotResult> slot = CuriosApi.getCuriosHelper().findFirstCurio(playerServ, ScolItems.WORLD_WING);
            if (slot.isPresent()) {
                ItemStack stack = slot.get().getStack();
                if (WorldWing.getFlySpeedInt(stack) == 4) {
                    WorldWing.setFlySpeed(stack, 0);
                } else {
                    WorldWing.setFlySpeed(stack, WorldWing.getFlySpeedInt(stack)+1);
                }

            }
        });
        ctx.get().setPacketHandled(true);
    }

}

