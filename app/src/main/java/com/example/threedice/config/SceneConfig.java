package com.example.threedice.config;

/**
 * Scene configuration - all parameters related to the 3D space and environment
 */
public class SceneConfig {
    // 3D Space dimensions (simulated coordinates)
    public static final float SPACE_WIDTH = 2000f;
    public static final float SPACE_HEIGHT = 2000f;
    public static final float SPACE_DEPTH = 2000f;

    // Space corners
    public static final float[] TOP_LEFT_FRONT = {0f, 0f, 2000f};
    public static final float[] BOTTOM_RIGHT_BACK = {2000f, 2000f, 0f};

    // Scene background color (black)
    public static final int BACKGROUND_COLOR = 0xFF000000;

    // Reference planes for 3D visualization
    public static final float FRONT_PLANE_Z = 2000f;
    public static final float LEFT_PLANE_X = 0f;
    public static final float TOP_PLANE_Y = 0f;

    // Ground level
    public static final float GROUND_LEVEL = 0f;

    // Walls configuration
    public static final float WALL_THICKNESS = 20f;
    public static final int WALL_COLOR = 0xFF808080; // Gray

    // Reference grid lines
    public static final int GRID_LINE_COLOR = 0xFF404040; // Dark gray
    public static final float GRID_SPACING = 200f;
    public static final int GRID_LINE_WIDTH = 1;
}
