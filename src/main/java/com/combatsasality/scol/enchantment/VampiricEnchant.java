package com.combatsasality.scol.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class VampiricEnchant extends Enchantment {

    public VampiricEnchant() {
        super(Rarity.RARE, EnchantmentType.WEAPON, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
