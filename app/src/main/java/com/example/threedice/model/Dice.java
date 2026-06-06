package com.example.threedice.model;

import com.example.threedice.config.DiceConfig;

/**
 * Dice model - represents a 3D dice with position, rotation, velocity, and angular velocity
 */
public class Dice {
    // Position (center of dice)
    private Vector3 position;
    
    // Velocity (linear motion)
    private Vector3 velocity;
    
    // Rotation (as quaternion or Euler angles - using Euler for simplicity)
    private Vector3 rotation; // x, y, z angles in radians
    
    // Angular velocity
    private Vector3 angularVelocity;
    
    // Dimensions
    private float width;
    private float height;
    private float depth;
    
    // State
    private boolean isMoving;
    
    public Dice() {
        this.position = new Vector3(DiceConfig.INITIAL_POSITION);
        this.velocity = new Vector3(0, 0, 0);
        this.rotation = new Vector3(0, 0, 0);
        this.angularVelocity = new Vector3(0, 0, 0);
        this.width = DiceConfig.DICE_WIDTH;
        this.height = DiceConfig.DICE_HEIGHT;
        this.depth = DiceConfig.DICE_DEPTH;
        this.isMoving = false;
    }
    
    // Getters
    public Vector3 getPosition() {
        return position;
    }
    
    public Vector3 getVelocity() {
        return velocity;
    }
    
    public Vector3 getRotation() {
        return rotation;
    }
    
    public Vector3 getAngularVelocity() {
        return angularVelocity;
    }
    
    public float getWidth() {
        return width;
    }
    
    public float getHeight() {
        return height;
    }
    
    public float getDepth() {
        return depth;
    }
    
    public boolean isMoving() {
        return isMoving;
    }
    
    // Setters
    public void setPosition(Vector3 position) {
        this.position = position;
    }
    
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }
    
    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }
    
    public void setVelocity(float x, float y, float z) {
        this.velocity.x = x;
        this.velocity.y = y;
        this.velocity.z = z;
    }
    
    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
    }
    
    public void setAngularVelocity(Vector3 angularVelocity) {
        this.angularVelocity = angularVelocity;
    }
    
    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }
    
    // Update position and rotation based on velocity and angular velocity
    public void update(float deltaTime) {
        // Update position
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
        position.z += velocity.z * deltaTime;
        
        // Update rotation (convert angular velocity to rotation change)
        rotation.x += angularVelocity.x * deltaTime;
        rotation.y += angularVelocity.y * deltaTime;
        rotation.z += angularVelocity.z * deltaTime;
    }
    
    // Reset dice to initial state
    public void reset() {
        position.x = DiceConfig.INITIAL_POSITION[0];
        position.y = DiceConfig.INITIAL_POSITION[1];
        position.z = DiceConfig.INITIAL_POSITION[2];
        velocity.x = 0;
        velocity.y = 0;
        velocity.z = 0;
        rotation.x = 0;
        rotation.y = 0;
        rotation.z = 0;
        angularVelocity.x = 0;
        angularVelocity.y = 0;
        angularVelocity.z = 0;
        isMoving = false;
    }
    
    // Get corners of dice for collision detection
    public Vector3[] getCorners() {
        Vector3[] corners = new Vector3[8];
        float halfW = width / 2;
        float halfH = height / 2;
        float halfD = depth / 2;
        
        // Unrotated corners (relative to center)
        float[][] offsets = {
            {-halfW, -halfH, -halfD},
            {halfW, -halfH, -halfD},
            {halfW, halfH, -halfD},
            {-halfW, halfH, -halfD},
            {-halfW, -halfH, halfD},
            {halfW, -halfH, halfD},
            {halfW, halfH, halfD},
            {-halfW, halfH, halfD}
        };
        
        for (int i = 0; i < 8; i++) {
            Vector3 corner = new Vector3(offsets[i][0], offsets[i][1], offsets[i][2]);
            // Apply rotation (simplified - no actual rotation applied for now)
            corner.x += position.x;
            corner.y += position.y;
            corner.z += position.z;
            corners[i] = corner;
        }
        
        return corners;
    }
    
    // Get the minimum and maximum coordinates of dice bounding box
    public float getMinX() {
        return position.x - width / 2;
    }
    
    public float getMaxX() {
        return position.x + width / 2;
    }
    
    public float getMinY() {
        return position.y - height / 2;
    }
    
    public float getMaxY() {
        return position.y + height / 2;
    }
    
    public float getMinZ() {
        return position.z - depth / 2;
    }
    
    public float getMaxZ() {
        return position.z + depth / 2;
    }
}
