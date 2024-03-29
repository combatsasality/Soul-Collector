package com.combatsasality.scol.items;

import com.combatsasality.scol.Main;
import com.combatsasality.scol.ScolCapabality;
import com.combatsasality.scol.handlers.CheckVersionHandler;
import com.combatsasality.scol.packets.server.PacketGetCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;

public class TestItem extends Item {
    public TestItem() {
        super(new Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.item.test_item.0"));
        if (Screen.hasShiftDown()) {
            tooltip.add(new StringTextComponent(Minecraft.getInstance().player.getCapability(ScolCapabality.NeedVariables).map(capa -> capa.getNBT()).orElse(null).toString()));
        }
        Main.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketGetCapability(true));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void inventoryTick(ItemStack p_77663_1_, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
//            Main.packetInstance.send(PacketDistributor.PLAYER.with(() -> player), new PacketCapa(entity.getCapability(scolCapability.NeedVariables).map(capa -> capa.getNBT()).orElse(null)));
            player.setHealth(player.getMaxHealth());
            player.addEffect(new EffectInstance(Effects.SATURATION, 1000, 1, true, false));
            player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 1000, 1, true, false));
        } else {
//            world.addParticle(ParticleTypes.ENCHANT, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
        }

        super.inventoryTick(p_77663_1_, world, entity, p_77663_4_, p_77663_5_);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        CheckVersionHandler.getNewestVersion();
        return super.use(world, player, hand);
    }

}
