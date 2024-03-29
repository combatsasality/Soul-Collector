package com.combatsasality.scol.client.renderer;

import com.combatsasality.scol.Main;
import com.combatsasality.scol.client.model.IchigoVizardModel;
import com.combatsasality.scol.entity.IchigoVizard;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IchigoVizardRenderer extends MobRenderer<IchigoVizard, IchigoVizardModel<IchigoVizard>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(Main.MODID, "textures/entity/ichigo_vizard.png");

    public IchigoVizardRenderer(EntityRendererManager rendermanager) {
        super(rendermanager, new IchigoVizardModel<>(), 1.0f);
        this.addLayer(new HeldItemLayer<>(this));
    }
    @Override
    public ResourceLocation getTextureLocation(IchigoVizard p_110775_1_) {
        return TEXTURE;
    }

}
