package dev.kurai.uhc.example.camp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CampColor {

  ChatColor color();

  DyeColor dyeColor();
}
