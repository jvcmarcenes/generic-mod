package io.github.jvcmarcenes.example.util;

public class MathHelper {
  
  public static int clamp(int min, int value, int max) {
    return value < min ? min : value > max ? max : value;
  }

}
