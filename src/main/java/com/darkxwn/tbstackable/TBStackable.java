package com.darkxwn.tbstackable;

import com.darkxwn.tbstackable.init.ModComponents;
import com.darkxwn.tbstackable.init.ModItems;
import net.fabricmc.api.ModInitializer;

public class TBStackable implements ModInitializer {
    public static final String MOD_ID = "tbstackable";

    @Override
    public void onInitialize() {
        ModComponents.init();
        ModItems.init();
    }
}
