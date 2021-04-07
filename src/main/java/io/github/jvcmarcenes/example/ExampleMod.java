package io.github.jvcmarcenes.example;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
}
