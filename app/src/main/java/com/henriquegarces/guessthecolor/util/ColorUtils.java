package com.henriquegarces.guessthecolor.util;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by henrique on 18/05/15.
 */
public class ColorUtils {

    private static final double GOLDEN_RATIO = 0.618033988749895;
    private Random rand;

    public ColorUtils() {
        rand = new Random();
    }

    public int[] getRandomColor(int n) {
        int[] colors = new int[n];

        for(int i = 0; i < n; i++) {
            double h = rand.nextDouble();
            h += GOLDEN_RATIO;
            h %= 1;
            double s = rand.nextDouble();
            double v = (rand.nextInt((100-40)) + 40);
            double[] rgb = hsvToRgb(h, s, v/100);
            int red = (int) (rgb[0] * 256.0f);
            int green = (int) (rgb[1] * 256.0f);
            int blue = (int) (rgb[2] * 256.0f);
            colors[i] = Color.rgb(red, green, blue);

        }

        return colors;
    }

    private double[] hsvToRgb(double h, double s, double v) {
        int h_i = (int) Math.floor(h * 6);
        double f = h * 6 - h_i;
        double p = v * (1 - s);
        double q = v * (1 - f * s);
        double t = v * (1 - (1 - f) * s);
        double[] rgb = new double[3];

        switch (h_i) {
            case 0:
                rgb[0] = v; rgb[1] = t; rgb[2] = p;
                break;
            case  1:
                rgb[0] = q; rgb[1] = v; rgb[2] = p;
                break;
            case 2:
                rgb[0] = p; rgb[1] = v; rgb[2] = t;
                break;
            case 3:
                rgb[0] = p; rgb[1] = q; rgb[2] = v;
                break;
            case 4:
                rgb[0] = t; rgb[1] = p; rgb[2] = v;
                break;
            case 5:
                rgb[0] = v; rgb[1] = p; rgb[2] = q;
                break;
        }

        return rgb;
    }

    public int getRandomColor() {
        int red = (int)Math.floor(Math.random() * 256);
        int green = (int)Math.floor(Math.random() * 256);
        int blue = (int)Math.floor(Math.random() * 256);

        return Color.rgb(red, green, blue);
    }
}