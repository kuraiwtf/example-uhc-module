package dev.kurai.uhc.example.component;

import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.example.camp.ExampleCamp;
import dev.kurai.uhc.example.role.ExampleRole;

public record CampComponent(ExampleCamp camp) implements Component {}
