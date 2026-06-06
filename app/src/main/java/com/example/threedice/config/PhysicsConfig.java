package com.example.threedice.config;

/**
 * Physics configuration - all parameters related to physics simulation
 */
public class PhysicsConfig {
    // Gravity acceleration (m/s²)
    public static final float GRAVITY = 1500f;

    // Bounce/restitution coefficient (energy loss on collision)
    // 0.7 means 70% energy retained, 30% energy lost
    public static final float BOUNCE_COEFFICIENT = 0.7f;

    // Damping coefficient (friction in air)
    // Applied to velocity each frame: velocity *= (1 - DAMPING)
    public static final float DAMPING = 0.02f;

    // Angular damping (rotation friction)
    public static final float ANGULAR_DAMPING = 0.05f;

    // Minimum velocity threshold to consider dice as stopped
    public static final float VELOCITY_THRESHOLD = 5f;

    // Minimum angular velocity threshold
    public static final float ANGULAR_VELOCITY_THRESHOLD = 0.1f;

    // Maximum velocity allowed
    public static final float MAX_VELOCITY = 3000f;

    // Rotation inertia factor (affects how easily dice rotates)
    public static final float INERTIA_FACTOR = 1.0f;

    // Collision detection epsilon (for wall/ground collisions)
    public static final float COLLISION_EPSILON = 1f;

    // Time step for physics simulation (seconds per frame)
    // This is typically set by rendering frame rate
    public static final float DELTA_TIME_MAX = 0.033f; // ~30 FPS
}
