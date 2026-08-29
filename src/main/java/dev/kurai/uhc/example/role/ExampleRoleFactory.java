package dev.kurai.uhc.example.role;

import dev.kurai.uhc.module.role.factory.RoleFactory;

final class ExampleRoleFactory implements RoleFactory<ExampleRole, ExampleRoleData> {

  @Override
  public ExampleRoleData provideNewInstance(final Class<? extends ExampleRole> identifier) {
    return new ExampleRoleData(identifier);
  }
}
