package com.darkxwn.tbstackable.init;

import com.darkxwn.tbstackable.TBStackable;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class ModComponents {
    /**
     * Сколько единиц стака не поместилось в обычный лимит предмета при сохранении.
     * Нужен только внутри сохранённого содержимого рюкзака: стандартный кодек ItemStack принимает count не больше 99.
     */
    public static final DataComponentType<Integer> EXTRA_COUNT = DataComponentType.<Integer>builder()
            .persistent(ExtraCodecs.POSITIVE_INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .build();

    public static void init() {
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                Identifier.fromNamespaceAndPath(TBStackable.MOD_ID, "extra_count"), EXTRA_COUNT);
    }
}
