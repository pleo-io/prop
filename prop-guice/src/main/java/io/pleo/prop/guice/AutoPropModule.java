package io.pleo.prop.guice;

import java.util.Map;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.spi.Elements;

import io.pleo.prop.core.Prop;
import io.pleo.prop.core.internal.ParserFactory;
import io.pleo.prop.core.internal.PropFactory;
import io.pleo.prop.guice.internal.PropMappingVisitor;
import io.pleo.prop.guice.internal.PropResult;

public class AutoPropModule implements Module {

  private final String packagePrefix;
  private final Iterable<? extends Module> modulesToScan;
  private final PropFactory propFactory;
  private final ParserFactory parserFactory;

  public AutoPropModule(String packagePrefix,
                        Iterable<? extends Module> modulesToScan,
                        PropFactory propFactory,
                        ParserFactory parserFactory) {
    this.packagePrefix = packagePrefix;
    this.modulesToScan = modulesToScan;
    this.propFactory = propFactory;
    this.parserFactory = parserFactory;
  }

  @Override
  public void configure(Binder binder) {
    PropMappingVisitor visitor = new PropMappingVisitor(type -> type
      .getRawType()
      .getPackage()
      .getName()
      .startsWith(packagePrefix), propFactory, parserFactory);

    Map<Key<Prop<?>>, PropResult> keyToPropMappings = visitor.visit(Elements.getElements(modulesToScan));
    keyToPropMappings.entrySet().stream().forEach(kvp -> {
      if (kvp.getValue().isError()) {
        binder.addError(kvp.getValue().getError());
      } else {
        binder.bind(kvp.getKey()).toInstance(kvp.getValue().getProp());
      }
    });
  }
}
