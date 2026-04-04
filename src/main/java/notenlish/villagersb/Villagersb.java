package notenlish.villagersb;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Villagersb implements ModInitializer {
    public static final String MOD_ID = "villagersb";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        LOGGER.info("AHAHAHAHAH");

        LOGGER.info("Hello Fabric world! This is VILLAGERSB mod talking!!!!!!");


        DoStuff();
    }

    private void DoStuff() {

    }
}