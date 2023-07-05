package xyz.tuxinal.silkyHands.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.loader.api.FabricLoader;

public class ConfigParser {
    private static Config config;
    private static Logger LOGGER = LogManager.getLogger();

    public static void init() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        File configFile = getConfigPath().toFile();
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
                FileWriter writer = new FileWriter(configFile);
                Config configTemplate = new Config();
                configTemplate.ignoredBlocks = new String[] {};
                configTemplate.tag = "silkyhands";
                writer.write(gson.toJson(configTemplate));
                writer.close();
            }
            FileReader reader = new FileReader(configFile);
            config = gson.fromJson(IOUtils.toString(reader), Config.class);
        } catch (IOException e) {
            LOGGER.catching(e);
        } catch (JsonSyntaxException e) {
            LOGGER.catching(e);
        }
    }

    public static String[] getIgnoredBlocks() {
        return config.ignoredBlocks;
    }

    public static String getTag() {
        return config.tag;
    }

    private static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("silky-hands.json");
    }
}

class Config {
    String tag;
    String[] ignoredBlocks;
}