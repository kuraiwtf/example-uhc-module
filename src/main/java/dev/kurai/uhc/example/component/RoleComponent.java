package dev.kurai.uhc.example.component;

import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.example.role.ExampleRole;

public record RoleComponent(ExampleRole role) implements Component {}
