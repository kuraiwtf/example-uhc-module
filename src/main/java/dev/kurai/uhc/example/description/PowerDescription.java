package dev.kurai.uhc.example.description;

import net.kyori.adventure.text.Component;

public record PowerDescription(String name, Component description, PowerDescription... children) {}
