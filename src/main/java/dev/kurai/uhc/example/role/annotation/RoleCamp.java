package dev.kurai.uhc.example.role.annotation;

import dev.kurai.uhc.example.camp.ExampleCamp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RoleCamp {

  Class<? extends ExampleCamp> value();
}
