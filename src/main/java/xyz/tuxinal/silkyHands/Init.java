package xyz.tuxinal.silkyHands;

import net.fabricmc.api.ModInitializer;
import xyz.tuxinal.silkyHands.utils.ConfigParser;

public class Init implements ModInitializer {

    @Override
    public void onInitialize() {
        ConfigParser.init();
    }
    
}
