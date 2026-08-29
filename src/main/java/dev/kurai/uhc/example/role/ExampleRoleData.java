package dev.kurai.uhc.example.role;

import dev.kurai.uhc.example.camp.ExampleCamp;
import dev.kurai.uhc.example.role.annotation.RoleCamp;
import dev.kurai.uhc.module.role.AbstractRoleData;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;

public final class ExampleRoleData extends AbstractRoleData<ExampleRole> {

  private final Class<? extends ExampleCamp> campClass;
  private final String identifier;
  private final String name;

  public ExampleRoleData(final Class<? extends ExampleRole> role) {
    super(role);
    this.campClass = role.getAnnotation(RoleCamp.class).value();
    this.identifier = role.getAnnotation(Identifier.class).value();
    this.name = role.getAnnotation(Name.class).value();
  }

  public Class<? extends ExampleCamp> getCampClass() {
    return this.campClass;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public String getName() {
    return this.name;
  }
}
