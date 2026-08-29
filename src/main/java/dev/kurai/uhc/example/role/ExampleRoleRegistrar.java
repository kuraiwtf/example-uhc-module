package dev.kurai.uhc.example.role;

import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.example.role.defaults.solitary.TestRole;
import dev.kurai.uhc.module.role.registrar.AbstractRoleRegistrar;
import dev.kurai.uhc.module.role.repository.RoleRepository;

public final class ExampleRoleRegistrar
    extends AbstractRoleRegistrar<ExampleRole, ExampleRoleData> {

  private final ExampleModule module;

  public ExampleRoleRegistrar(final ExampleModule module) {
    super(new ExampleRoleFactory(), new RoleRepository<>());
    this.module = module;

    this.registerRoles(TestRole.class);
  }

  private void registerRoles(final Class<? extends ExampleRole>... roles) {
    for (final Class<? extends ExampleRole> role : roles) {
      this.registerRole(role);
      this.module.getEnabledRoles().add(this.getRoleData(role).orElseThrow());
    }
  }
}
