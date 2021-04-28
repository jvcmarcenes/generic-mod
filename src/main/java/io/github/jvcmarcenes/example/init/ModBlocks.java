package io.github.jvcmarcenes.example.init;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.function.Supplier;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.github.jvcmarcenes.example.ExampleMod;
import io.github.jvcmarcenes.example.util.DeserializationHelper;
import io.github.jvcmarcenes.example.util.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.AbstractBlock.IExtendedPositionPredicate;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExampleMod.MOD_ID);

  private static final Hashtable<String, RegistryObject<Block>> MAP = new Hashtable<>();

  @SuppressWarnings("unchecked")
  private static void registerBlocks() {
    JSONParser parser = new JSONParser();

    try {
      InputStreamReader reader = new InputStreamReader(ExampleMod.class.getResourceAsStream("/content/blocks.json"));

      JSONObject json = (JSONObject)parser.parse(reader);

      JSONArray blocks = (JSONArray)json.get("blocks");

      blocks.iterator().forEachRemaining(obj -> {
        JSONObject jsonBlock = (JSONObject) obj;

        if (!jsonBlock.containsKey("registry_name")) 
          throw new IllegalStateException("Block without a registry name");

        String registryName = (String)jsonBlock.get("registry_name");

        Material mat = DeserializationHelper.material((String)jsonBlock.get("material"));
        if (mat == null) throw new IllegalStateException("Invalid material for block " + registryName);
        Properties props = Properties.of(mat);

        props.strength((float)(double)jsonBlock.get("hardness"), (float)(double)jsonBlock.get("resistance"));
        if (jsonBlock.containsKey("sound_type")) {
          SoundType sound = DeserializationHelper.sound((String)jsonBlock.get("sound_type"));
          if (sound == null) throw new IllegalStateException("Invalid sound type for block " + registryName);
          props.sound(sound);
        };

        if (jsonBlock.containsKey("harvest_level")) props.harvestLevel((int)(long)jsonBlock.get("harvest_level"));
        if (jsonBlock.containsKey("harvest_tool")) props.harvestTool(ToolType.get(((String)jsonBlock.get("harvest_tool")).toLowerCase()));
        if (jsonBlock.containsKey("requires_tool") && (boolean)jsonBlock.get("requires_tool")) props.requiresCorrectToolForDrops();

        if (jsonBlock.containsKey("can_spawn_mobs")) props.isValidSpawn(new IExtendedPositionPredicate<EntityType<?>>(){
          @Override
          public boolean test(BlockState state, IBlockReader world, BlockPos pos, EntityType<?> entity) {
            return (boolean)jsonBlock.get("can_spawn_mobs");
          }
        });

        if (jsonBlock.containsKey("light_level")) props.lightLevel(state -> MathHelper.clamp(0, (int)(long)jsonBlock.get("light_level"), 15));

        MAP.put(registryName, register(registryName, () -> new Block(props)));
      });

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  static {
    registerBlocks();
  }

  private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> supplier) {
    return BLOCKS.register(name, supplier);
  }

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier) {
    RegistryObject<T> ret = registerNoItem(name, supplier);
    ModItems.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    return ret;
  }
  
}
