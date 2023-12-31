package co.com.bancolombia.factory.upgrades.actions;

import static co.com.bancolombia.factory.upgrades.actions.UpdateDependencies.FILES_TO_UPDATE;

import co.com.bancolombia.factory.ModuleBuilder;
import co.com.bancolombia.factory.commons.GenericModule;
import co.com.bancolombia.factory.upgrades.UpgradeAction;
import co.com.bancolombia.utils.Utils;
import java.util.List;
import lombok.SneakyThrows;

public class UpgradeY2022M05D03 implements UpgradeAction {
  @Override
  @SuppressWarnings("unchecked")
  @SneakyThrows
  public boolean up(ModuleBuilder builder) {
    List<String> gradleFiles = (List<String>) builder.getParam(FILES_TO_UPDATE);
    boolean applied =
        gradleFiles.stream()
            .map(file -> applyUpdate(builder, file))
            .reduce(false, (app, current) -> app || current);
    if (applied) {
      GenericModule.addAwsBom(builder);
    }
    return applied;
  }

  @Override
  public String name() {
    return "Apply aws bom dependency";
  }

  @Override
  public String description() {
    return "Manages aws dependencies version though bom dependency";
  }

  @SneakyThrows
  private boolean applyUpdate(ModuleBuilder builder, String file) {
    String regex = "['\\\"](software\\.amazon\\.awssdk:)((?!(bom)).*)(:.+)['\\\"]";
    return builder.updateFile(file, content -> Utils.replaceExpression(content, regex, "'$1$2'"));
  }
}
