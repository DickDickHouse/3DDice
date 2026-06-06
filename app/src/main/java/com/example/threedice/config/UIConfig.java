package com.example.threedice.config;

/**
 * UI configuration - all parameters related to user interface elements
 */
public class UIConfig {
    // Energy Bar dimensions and position
    public static final float ENERGY_BAR_LENGTH = 1000f;
    public static final float ENERGY_BAR_HEIGHT = 50f;
    public static final float ENERGY_BAR_BORDER = 5f;

    // Energy Bar colors
    public static final int ENERGY_BAR_BACKGROUND = 0xFF000000; // Black
    public static final int ENERGY_BAR_BORDER_COLOR = 0xFFC0C0C0; // Silver
    public static final int ENERGY_BAR_FILL_COLOR = 0xFF00FF00; // Neon green

    // Energy Bar position on screen (top-left corner)
    public static final float ENERGY_BAR_MARGIN_LEFT = 50f;
    public static final float ENERGY_BAR_MARGIN_TOP = 50f;

    // Full energy bar dimensions (when at 100%)
    public static final float ENERGY_BAR_FULL_LENGTH = ENERGY_BAR_LENGTH - (ENERGY_BAR_BORDER * 2);
    public static final float ENERGY_BAR_FULL_HEIGHT = ENERGY_BAR_HEIGHT - (ENERGY_BAR_BORDER * 2);

    // Screen overlay
    public static final int OVERLAY_COLOR_DARK = 0x99000000; // Semi-transparent black
    public static final int OVERLAY_COLOR_HIGHLIGHT = 0x33FFFFFF; // Semi-transparent white

    // Text rendering
    public static final int TEXT_COLOR = 0xFFFFFFFF; // White
    public static final float TEXT_SIZE = 40f;

    // Touch feedback
    public static final int TOUCH_INDICATOR_RADIUS = 100;
    public static final int TOUCH_INDICATOR_COLOR = 0x66FFFFFF;

    // Animation durations (milliseconds)
    public static final long ENERGY_CHARGE_DURATION = 2000; // 2 seconds to reach 100%
    public static final long RESET_ANIMATION_DURATION = 500; // 0.5 seconds to reset dice
}
