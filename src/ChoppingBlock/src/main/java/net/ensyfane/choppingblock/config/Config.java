package net.ensyfane.choppingblock.config;

import net.ensyfane.choppingblock.ChoppingBlockMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ChoppingBlockMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> ChoppableBlocks;

    private static final Map<String, String> DefaultChoppableBlocks = new HashMap<>();

    static {
        AddDefaultChoppables();
        BUILDER.push("Config for Chopping Block Mod by EnsyFane");

        ChoppableBlocks = BUILDER.comment("A map containing information about which blocks turn into which items when chopped on the copping block.")
                .define("Choppable Blocks To Items", SerializeMap(DefaultChoppableBlocks));

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    private static void AddDefaultChoppables() {
        DefaultChoppableBlocks.put("TestBlock", "minecraft:stone");
    }

    public static Map<String, String> GetChoppableBlocks() {
        return DeserializeMap(ChoppableBlocks.get());
    }

    private static String SerializeMap(Map<String, String> map) {
        var sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("->").append(entry.getValue()).append(";");
        }
        return sb.toString();
    }

    private static Map<String, String> DeserializeMap(String serialized) {
        var map = new HashMap<String, String>();
        var pairs = serialized.split(";");
        for (var pair : pairs) {
            var parts = pair.split("->");
            map.put(parts[0], parts[1]);
        }
        return map;
    }
}
