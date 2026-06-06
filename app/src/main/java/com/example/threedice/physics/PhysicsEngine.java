package com.example.threedice.physics;

import com.example.threedice.config.DiceConfig;
import com.example.threedice.config.PhysicsConfig;
import com.example.threedice.config.SceneConfig;
import com.example.threedice.model.Dice;
import com.example.threedice.model.Vector3;

/**
 * Physics Engine - handles gravity, collisions, and physical simulation
 */
public class PhysicsEngine {
    private Dice dice;
    private Vector3 gravity;
    
    public PhysicsEngine(Dice dice) {
        this.dice = dice;
        this.gravity = new Vector3(0, 0, -PhysicsConfig.GRAVITY); // Gravity pulls down (negative Z)
    }
    
    /**
     * Update physics simulation for one frame
     */
    public void update(float deltaTime) {
        if (!dice.isMoving()) {
            return;
        }
        
        // Clamp deltaTime to prevent large jumps
        if (deltaTime > PhysicsConfig.DELTA_TIME_MAX) {
            deltaTime = PhysicsConfig.DELTA_TIME_MAX;
        }
        
        // Apply gravity
        applyGravity(deltaTime);
        
        // Apply damping (air resistance)
        applyDamping();
        
        // Apply angular damping (rotation resistance)
        applyAngularDamping();
        
        // Update position and rotation
        dice.update(deltaTime);
        
        // Check collisions
        checkCollisions();
        
        // Check if dice has stopped
        checkIfStopped();
    }
    
    /**
     * Apply gravity force to velocity
     */
    private void applyGravity(float deltaTime) {
        Vector3 vel = dice.getVelocity();
        vel.z += gravity.z * deltaTime; // Gravity only affects Z axis
    }
    
    /**
     * Apply damping (air resistance) to velocity
     */
    private void applyDamping() {
        Vector3 vel = dice.getVelocity();
        float dampingFactor = 1.0f - PhysicsConfig.DAMPING;
        vel.multiply(dampingFactor);
    }
    
    /**
     * Apply damping to angular velocity (rotation resistance)
     */
    private void applyAngularDamping() {
        Vector3 angVel = dice.getAngularVelocity();
        float angularDampingFactor = 1.0f - PhysicsConfig.ANGULAR_DAMPING;
        angVel.multiply(angularDampingFactor);
    }
    
    /**
     * Check collisions with walls and ground
     */
    private void checkCollisions() {
        Vector3 pos = dice.getPosition();
        Vector3 vel = dice.getVelocity();
        Vector3 angVel = dice.getAngularVelocity();
        
        float minX = dice.getMinX();
        float maxX = dice.getMaxX();
        float minY = dice.getMinY();
        float maxY = dice.getMaxY();
        float minZ = dice.getMinZ();
        float maxZ = dice.getMaxZ();
        
        // Ground collision (Z = 0)
        if (minZ <= SceneConfig.GROUND_LEVEL + PhysicsConfig.COLLISION_EPSILON) {
            pos.z = SceneConfig.GROUND_LEVEL + DiceConfig.GROUND_CONTACT_DISTANCE;
            vel.z *= -PhysicsConfig.BOUNCE_COEFFICIENT; // Bounce
            angVel.multiply(0.9f); // Reduce rotation on ground contact
        }
        
        // Left wall collision (X = 0)
        if (minX <= SceneConfig.LEFT_PLANE_X + PhysicsConfig.COLLISION_EPSILON) {
            pos.x = SceneConfig.LEFT_PLANE_X + DiceConfig.DICE_WIDTH / 2;
            vel.x *= -PhysicsConfig.BOUNCE_COEFFICIENT;
        }
        
        // Right wall collision (X = 2000)
        if (maxX >= SceneConfig.SPACE_WIDTH - PhysicsConfig.COLLISION_EPSILON) {
            pos.x = SceneConfig.SPACE_WIDTH - DiceConfig.DICE_WIDTH / 2;
            vel.x *= -PhysicsConfig.BOUNCE_COEFFICIENT;
        }
        
        // Front wall collision (Y = 0)
        if (minY <= SceneConfig.TOP_PLANE_Y + PhysicsConfig.COLLISION_EPSILON) {
            pos.y = SceneConfig.TOP_PLANE_Y + DiceConfig.DICE_HEIGHT / 2;
            vel.y *= -PhysicsConfig.BOUNCE_COEFFICIENT;
        }
        
        // Back wall collision (Y = 2000)
        if (maxY >= SceneConfig.SPACE_HEIGHT - PhysicsConfig.COLLISION_EPSILON) {
            pos.y = SceneConfig.SPACE_HEIGHT - DiceConfig.DICE_HEIGHT / 2;
            vel.y *= -PhysicsConfig.BOUNCE_COEFFICIENT;
        }
        
        // Limit maximum velocity
        float velocityMagnitude = vel.length();
        if (velocityMagnitude > PhysicsConfig.MAX_VELOCITY) {
            vel.normalize().multiply(PhysicsConfig.MAX_VELOCITY);
        }
    }
    
    /**
     * Check if dice has stopped moving
     */
    private void checkIfStopped() {
        Vector3 vel = dice.getVelocity();
        Vector3 angVel = dice.getAngularVelocity();
        
        float velocityMagnitude = vel.length();
        float angularVelocityMagnitude = angVel.length();
        
        if (velocityMagnitude < PhysicsConfig.VELOCITY_THRESHOLD &&
            angularVelocityMagnitude < PhysicsConfig.ANGULAR_VELOCITY_THRESHOLD) {
            dice.setMoving(false);
            // Stop completely
            vel.x = 0;
            vel.y = 0;
            vel.z = 0;
            angVel.x = 0;
            angVel.y = 0;
            angVel.z = 0;
        }
    }
    
    /**
     * Apply force to dice (used for throwing)
     */
    public void applyForce(Vector3 force) {
        Vector3 vel = dice.getVelocity();
        vel.add(force);
        dice.setMoving(true);
    }
    
    /**
     * Apply torque to dice (used for spinning)
     */
    public void applyTorque(Vector3 torque) {
        Vector3 angVel = dice.getAngularVelocity();
        angVel.add(torque.copy().multiply(PhysicsConfig.INERTIA_FACTOR));
    }
}
