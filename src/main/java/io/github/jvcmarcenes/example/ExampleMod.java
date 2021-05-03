package io.github.jvcmarcenes.example;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.google.common.base.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.github.jvcmarcenes.example.init.ModBlocks;
import io.github.jvcmarcenes.example.init.ModItems;

@Mod(ExampleMod.MOD_ID)
public class ExampleMod
{
  public static final String MOD_ID = "examplemod";
  public static final Logger LOGGER = LogManager.getLogger();
  
  public ExampleMod() {
    
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    ModBlocks.BLOCKS.register(modEventBus);
    ModItems.ITEMS.register(modEventBus);

    MinecraftForge.EVENT_BUS.register(this);
  }

  public static final HashMap<String, ItemGroup> ITEM_GROUPS = new HashMap<>();

  @SuppressWarnings("unchecked")
  private static void loadItemGroups() {
    JSONParser parser = new JSONParser();

    try {
      InputStreamReader reader = new InputStreamReader(ExampleMod.class.getResourceAsStream("/content/config.json"));

      JSONObject json = (JSONObject)parser.parse(reader);

      JSONArray itemGroups = (JSONArray)json.get("item_groups");

      itemGroups.iterator().forEachRemaining(obj -> {
        JSONObject jsonTab = (JSONObject)obj;

        String name = (String)jsonTab.get("name");

        Supplier<Supplier<IItemProvider>> supplierSupplier = () -> {
          switch ((String)jsonTab.get("icon_type")) {
            case "item":
              return null;
            case "block":
              String rl = (String)jsonTab.get("icon_location");
              return () -> ModBlocks.MAP.get(rl).get();
            default:
              return null;
          }
        };

        final Supplier<IItemProvider> iconSupplier = supplierSupplier.get();

        ITEM_GROUPS.put(name, new ItemGroup(MOD_ID) {

          private final Supplier<IItemProvider> sup = iconSupplier;

          @Override
          public ItemStack makeIcon() {
            return new ItemStack(sup.get());
          }
          
        });
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
    loadItemGroups();
  }
}
