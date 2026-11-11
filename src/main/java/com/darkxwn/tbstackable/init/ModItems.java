package com.darkxwn.tbstackable.init;

import com.darkxwn.tbstackable.StackTier;
import com.darkxwn.tbstackable.TBStackable;
import com.darkxwn.tbstackable.item.StackUpgradeItem;
import com.tiviacz.travelersbackpack.init.ModItemGroups;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.EnumMap;
import java.util.Map;

public class ModItems {
    public static final Map<StackTier, StackUpgradeItem> STACK_UPGRADES = new EnumMap<>(StackTier.class);

    public static void init() {
        for (StackTier tier : StackTier.values()) {
            Identifier id = Identifier.fromNamespaceAndPath(TBStackable.MOD_ID, tier.itemName());
            ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
            StackUpgradeItem item = new StackUpgradeItem(new Item.Properties().stacksTo(1).setId(key), tier);
            Registry.register(BuiltInRegistries.ITEM, id, item);
            STACK_UPGRADES.put(tier, item);
        }

        // Кладём улучшения во вкладку самого Traveler's Backpack
        CreativeModeTabEvents.modifyOutputEvent(ModItemGroups.TRAVELERS_BACKPACK)
                .register(output -> STACK_UPGRADES.values().forEach(output::accept));
    }
}
