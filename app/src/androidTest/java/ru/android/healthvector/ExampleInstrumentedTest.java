package ru.android.healthvector;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Context testContext = InstrumentationRegistry.getContext();

        assertEquals("ru.android.healthvector", appContext.getPackageName());
        assertEquals("ru.android.healthvector.test", testContext.getPackageName());
    }
}
