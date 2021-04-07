package io.github.jvcmarcenes.example.util;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class DeserializationHelper {
  
  public static Material material(String name) {
    try {
      return (Material)Material.class.getDeclaredField(name).get(null);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static SoundType sound(String name) {
    try {
      return (SoundType)SoundType.class.getDeclaredField(name).get(null);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

}
