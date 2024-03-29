package com.combatsasality.scol.handlers;

import com.combatsasality.scol.Main;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HelpHandler {
    public static int getRandomInt(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static double getRandomDouble(Random random, int min, int max) {
        return random.nextDouble() * (max - min) + min;
    }

    private static <F extends Structure<?>> void setupMapSpacingAndLand(
            F structure,
            StructureSeparationSettings structureSeparationSettings,
            boolean transformSurroundingLand)
    {
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);
        if(transformSurroundingLand){
            Structure.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<Structure<?>>builder()
                            .addAll(Structure.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }
        DimensionStructuresSettings.DEFAULTS =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.DEFAULTS)
                        .put(structure, structureSeparationSettings)
                        .build();
        WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();
            if(structureMap instanceof ImmutableMap){
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureSeparationSettings);
                settings.getValue().structureSettings().structureConfig = tempMap;
            }
            else{
                structureMap.put(structure, structureSeparationSettings);
            }
        });
    }
    private static <T extends IForgeRegistryEntry<T>> T registerStructureReg(IForgeRegistry<T> registry, T entry, String registryKey) {
        entry.setRegistryName(new ResourceLocation(Main.MODID, registryKey));
        registry.register(entry);
        return entry;
    }

    public static void registerStructure(String registryKey, Structure<?> structure, StructureFeature<?, ?> configuredStructure, int average, int minAverage) {
        HelpHandler.setupMapSpacingAndLand(
                structure,
                new StructureSeparationSettings(
                        average, /* average distance apart in chunks between spawn attempts */
                        minAverage, /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE*/
                        123456890 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */
                ),
                true
        );
        Registry<StructureFeature<?, ?>> registryConfigured = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registryConfigured, new ResourceLocation(Main.MODID, "configured_"+registryKey), configuredStructure);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(structure, configuredStructure);
    }

    public static void createVisualLighting(BlockPos pos, World world) {
        LightningBoltEntity ent = EntityType.LIGHTNING_BOLT.create(world);
        ent.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        ent.setVisualOnly(true);
        world.addFreshEntity(ent);
    }
}
