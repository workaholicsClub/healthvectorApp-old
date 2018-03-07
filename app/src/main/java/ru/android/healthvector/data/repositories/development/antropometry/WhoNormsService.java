package ru.android.healthvector.data.repositories.development.antropometry;

import android.content.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Cleanup;

@Singleton
public class WhoNormsService {
    private static final String WEIGHT_LOW_BOY = "boy_w_low.txt";
    private static final String WEIGHT_HIGH_BOY = "boy_w_high.txt";
    private static final String HEIGHT_LOW_BOY = "boy_h_low.txt";
    private static final String HEIGHT_HIGH_BOY = "boy_h_high.txt";
    private static final String WEIGHT_LOW_GIRL = "girl_w_low.txt";
    private static final String WEIGHT_HIGH_GIRL = "girl_w_high.txt";
    private static final String HEIGHT_LOW_GIRL = "girl_h_low.txt";
    private static final String HEIGHT_HIGH_GIRL = "girl_h_high.txt";

    private final Logger logger = LoggerFactory.getLogger(toString());
    private final Context context;

    private final Object weightLowBoyLock = new Object();
    private final Object weightHighBoyLock = new Object();
    private final Object heightLowBoyLock = new Object();
    private final Object heightHighBoyLock = new Object();
    private final Object weightLowGirlLock = new Object();
    private final Object weightHighGirlLock = new Object();
    private final Object heightLowGirlLock = new Object();
    private final Object heightHighGirlLock = new Object();

    private List<Double> weightLowBoy;
    private List<Double> weightHighBoy;
    private List<Double> heightLowBoy;
    private List<Double> heightHighBoy;
    private List<Double> weightLowGirl;
    private List<Double> weightHighGirl;
    private List<Double> heightLowGirl;
    private List<Double> heightHighGirl;

    @Inject
    public WhoNormsService(Context context) {
        this.context = context;
    }

    public List<Double> readWeightLowBoy() {
        synchronized (weightLowBoyLock) {
            if (weightLowBoy == null) {
                weightLowBoy = readDoubles(WEIGHT_LOW_BOY);
            }
            return weightLowBoy;
        }
    }

    public List<Double> readWeightHighBoy() {
        synchronized (weightHighBoyLock) {
            if (weightHighBoy == null) {
                weightHighBoy = readDoubles(WEIGHT_HIGH_BOY);
            }
            return weightHighBoy;
        }
    }

    public List<Double> readHeightLowBoy() {
        synchronized (heightLowBoyLock) {
            if (heightLowBoy == null) {
                heightLowBoy = readDoubles(HEIGHT_LOW_BOY);
            }
            return heightLowBoy;
        }
    }

    public List<Double> readHeightHighBoy() {
        synchronized (heightHighBoyLock) {
            if (heightHighBoy == null) {
                heightHighBoy = readDoubles(HEIGHT_HIGH_BOY);
            }
            return heightHighBoy;
        }
    }

    public List<Double> readWeightLowGirl() {
        synchronized (weightLowGirlLock) {
            if (weightLowGirl == null) {
                weightLowGirl = readDoubles(WEIGHT_LOW_GIRL);
            }
            return weightLowGirl;
        }
    }

    public List<Double> readWeightHighGirl() {
        synchronized (weightHighGirlLock) {
            if (weightHighGirl == null) {
                weightHighGirl = readDoubles(WEIGHT_HIGH_GIRL);
            }
            return weightHighGirl;
        }
    }

    public List<Double> readHeightLowGirl() {
        synchronized (heightLowGirlLock) {
            if (heightLowGirl == null) {
                heightLowGirl = readDoubles(HEIGHT_LOW_GIRL);
            }
            return heightLowGirl;
        }
    }

    public List<Double> readHeightHighGirl() {
        synchronized (heightHighGirlLock) {
            if (heightHighGirl == null) {
                heightHighGirl = readDoubles(HEIGHT_HIGH_GIRL);
            }
            return heightHighGirl;
        }
    }

    private List<Double> readDoubles(String fileName) {
        List<Double> result = new ArrayList<>();
        try {
            @Cleanup InputStreamReader inputStream = new InputStreamReader(context.getAssets().open(fileName));
            @Cleanup BufferedReader reader = new BufferedReader(inputStream);
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    double value = Double.parseDouble(line);
                    result.add(value);
                } catch (NumberFormatException e) {
                    logger.warn("failed to parse item '" + line + "' in file " + fileName, e);
                }
            }
        } catch (IOException e) {
            logger.error("failed to read " + fileName, e);
        }
        return result;
    }
}
