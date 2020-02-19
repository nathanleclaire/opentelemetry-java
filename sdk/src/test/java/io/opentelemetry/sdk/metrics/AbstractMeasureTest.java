/*
 * Copyright 2020, OpenTelemetry Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentelemetry.sdk.metrics;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.testing.EqualsTester;
import io.opentelemetry.sdk.common.InstrumentationLibraryInfo;
import io.opentelemetry.sdk.internal.TestClock;
import io.opentelemetry.sdk.metrics.common.InstrumentValueType;
import io.opentelemetry.sdk.resources.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class AbstractMeasureTest {
  private static final boolean ABSOLUTE = true;
  private static final boolean NON_ABSOLUTE = false;

  @Test
  public void getValues() {
    assertThat(new TestMeasureInstrument(InstrumentValueType.LONG, ABSOLUTE).isAbsolute()).isTrue();
    assertThat(new TestMeasureInstrument(InstrumentValueType.LONG, NON_ABSOLUTE).isAbsolute())
        .isFalse();
  }

  @Test
  public void attributeValue_EqualsAndHashCode() {
    EqualsTester tester = new EqualsTester();
    tester.addEqualityGroup(
        new TestMeasureInstrument(InstrumentValueType.LONG, ABSOLUTE),
        new TestMeasureInstrument(InstrumentValueType.LONG, ABSOLUTE));
    tester.addEqualityGroup(
        new TestMeasureInstrument(InstrumentValueType.LONG, NON_ABSOLUTE),
        new TestMeasureInstrument(InstrumentValueType.LONG, NON_ABSOLUTE));
    tester.addEqualityGroup(
        new TestMeasureInstrument(InstrumentValueType.DOUBLE, ABSOLUTE),
        new TestMeasureInstrument(InstrumentValueType.DOUBLE, ABSOLUTE));
    tester.addEqualityGroup(
        new TestMeasureInstrument(InstrumentValueType.DOUBLE, NON_ABSOLUTE),
        new TestMeasureInstrument(InstrumentValueType.DOUBLE, NON_ABSOLUTE));
    tester.testEquals();
  }

  private static final class TestMeasureInstrument extends AbstractMeasure {
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String UNIT = "1";
    private static final Map<String, String> CONSTANT_LABELS =
        Collections.singletonMap("key_2", "value_2");
    private static final List<String> LABEL_KEY = Collections.singletonList("key");
    private static final MeterProviderSharedState METER_SHARED_STATE =
        MeterProviderSharedState.create(TestClock.create(), Resource.getEmpty());
    private static final InstrumentationLibraryInfo INSTRUMENTATION_LIBRARY_INFO =
        InstrumentationLibraryInfo.create("test_abstract_instrument", "");

    TestMeasureInstrument(InstrumentValueType instrumentValueType, boolean absolute) {
      super(
          NAME,
          DESCRIPTION,
          UNIT,
          CONSTANT_LABELS,
          LABEL_KEY,
          instrumentValueType,
          METER_SHARED_STATE,
          INSTRUMENTATION_LIBRARY_INFO,
          absolute);
    }
  }
}