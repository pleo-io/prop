package io.pleo.prop.archaius;

import java.util.function.Function;

import com.google.common.collect.ImmutableMap;
import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.PollResult;
import com.netflix.config.PolledConfigurationSource;
import org.junit.Test;

import static com.google.common.truth.Truth.*;

public class ParsingPropertyTest {

  public static final String PROPERTY_KEY = "ParsingPropertyTest_01";

  private String propertyValue;

  public class TestConfigurationSource implements PolledConfigurationSource {
    @Override
    public PollResult poll(boolean initial, Object checkPoint) throws Exception {
      return PollResult.createFull(ImmutableMap.of(PROPERTY_KEY, propertyValue));
    }
  }

  @Test(expected = UndefinedPropertyException.class)
  public void undefined_property_throws() {
    new ParsingProperty<>(PROPERTY_KEY, Function.identity(), null);
  }

  @Test
  public void can_change_value() throws InterruptedException {
    propertyValue = "value1";
    DynamicConfiguration configuration = new DynamicConfiguration(new TestConfigurationSource(),
                                                                  new FixedDelayPollingScheduler(0, 1, false));
    ((ConcurrentCompositeConfiguration) ConfigurationManager.getConfigInstance()).addConfiguration(configuration);
    ParsingProperty<String> property = new ParsingProperty(PROPERTY_KEY, Function.identity(), null);

    assertThat(property.getValue()).isEqualTo("value1");

    propertyValue = "value2";
    Thread.sleep(100);
    assertThat(property.getValue()).isEqualTo("value2");
  }
}