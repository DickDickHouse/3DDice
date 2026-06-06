package com.example.threedice.config;

/**
 * Dice configuration - all parameters related to the dice model and appearance
 */
public class DiceConfig {
    // Dice dimensions (200x200x200)
    public static final float DICE_WIDTH = 200f;
    public static final float DICE_HEIGHT = 200f;
    public static final float DICE_DEPTH = 200f;

    // Dice initial position (center of dice in simulated coordinates)
    public static final float[] INITIAL_POSITION = {1000f, 1000f, 1600f};

    // Dice color (cream/milk white)
    public static final int DICE_COLOR = 0xFFFFFFF0;

    // Dot colors
    public static final int DOT_COLOR_RED = 0xFFFF0000;
    public static final int DOT_COLOR_BLACK = 0xFF000000;

    // Dot radius (relative to face size)
    public static final float DOT_RADIUS = 12f;

    // Face configurations
    // Face 1: 1 red dot (center)
    public static final int FACE_1_DOTS = 1;
    public static final int FACE_1_COLOR = DOT_COLOR_RED;

    // Face 2: 2 black dots (diagonal)
    public static final int FACE_2_DOTS = 2;
    public static final int FACE_2_COLOR = DOT_COLOR_BLACK;

    // Face 3: 3 black dots (diagonal line)
    public static final int FACE_3_DOTS = 3;
    public static final int FACE_3_COLOR = DOT_COLOR_BLACK;

    // Face 4: 4 black dots (corners)
    public static final int FACE_4_DOTS = 4;
    public static final int FACE_4_COLOR = DOT_COLOR_BLACK;

    // Face 5: 5 black dots (four corners + center)
    public static final int FACE_5_DOTS = 5;
    public static final int FACE_5_COLOR = DOT_COLOR_BLACK;

    // Face 6: 6 black dots (two columns of 3)
    public static final int FACE_6_DOTS = 6;
    public static final int FACE_6_COLOR = DOT_COLOR_BLACK;

    // Dice ground contact distance (half of dice size = radius)
    public static final float DICE_RADIUS = DICE_HEIGHT / 2f;
    public static final float GROUND_CONTACT_DISTANCE = DICE_RADIUS;
}
