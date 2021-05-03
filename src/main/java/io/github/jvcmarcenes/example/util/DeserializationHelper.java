package io.github.jvcmarcenes.example.util;

import io.github.jvcmarcenes.example.ExampleMod;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;

public class DeserializationHelper {
  
  public static Material material(String name) {
    try {
      return (Material)Material.class.getDeclaredField(name.toUpperCase()).get(null);
    } catch (NoSuchFieldException e) {
      throw new IllegalStateException("Invalid material type: " + name);
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static SoundType sound(String name) {
    try {
      return (SoundType)SoundType.class.getDeclaredField(name.toUpperCase()).get(null);
    } catch (NoSuchFieldException e) {
      throw new IllegalStateException("Invalid sound type: " + name);
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static ItemGroup tab(String name) {
    switch (name) {
      case "buildingBlocks":
        return ItemGroup.TAB_BUILDING_BLOCKS;
      case "decorations":
        return ItemGroup.TAB_DECORATIONS;
      case "redstone": 
        return ItemGroup.TAB_REDSTONE;
      case "transportation":
        return ItemGroup.TAB_TRANSPORTATION;
      case "misc":
        return ItemGroup.TAB_MISC;
      case "food":
        return ItemGroup.TAB_FOOD;
      case "tools":
        return ItemGroup.TAB_TOOLS;
      case "combat":
        return ItemGroup.TAB_COMBAT;
      case "brewing":
        return ItemGroup.TAB_BREWING;
      default:
        if (ExampleMod.ITEM_GROUPS.containsKey(name)) return ExampleMod.ITEM_GROUPS.get(name);
        else throw new IllegalStateException("Invalid creative tab: " + name);
    }
  }

}
