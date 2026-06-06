package com.example.threedice.game;

import android.view.MotionEvent;
import com.example.threedice.config.PhysicsConfig;
import com.example.threedice.input.InputHandler;
import com.example.threedice.model.Dice;
import com.example.threedice.model.Vector3;
import com.example.threedice.physics.PhysicsEngine;

/**
 * Game Manager - coordinates all game systems (physics, input, rendering)
 */
public class GameManager {
    private Dice dice;
    private PhysicsEngine physicsEngine;
    private InputHandler inputHandler;
    private long lastFrameTime = 0;
    private boolean gameRunning = false;
    
    // Constants for force scaling
    private static final float MAX_THROW_FORCE = 3000f;
    private static final float MAX_TORQUE = 500f;
    
    public GameManager() {
        this.dice = new Dice();
        this.physicsEngine = new PhysicsEngine(dice);
        this.inputHandler = new InputHandler();
        this.lastFrameTime = System.currentTimeMillis();
    }
    
    /**
     * Start the game
     */
    public void start() {
        gameRunning = true;
        lastFrameTime = System.currentTimeMillis();
    }
    
    /**
     * Stop the game
     */
    public void stop() {
        gameRunning = false;
    }
    
    /**
     * Update game state (called every frame)
     */
    public void update() {
        if (!gameRunning) {
            return;
        }
        
        // Calculate delta time
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastFrameTime) / 1000f;
        lastFrameTime = currentTime;
        
        // Clamp delta time to prevent large jumps
        if (deltaTime > 0.1f) {
            deltaTime = 0.1f;
        }
        
        // Update physics
        physicsEngine.update(deltaTime);
        
        // Check if user released touch to throw dice
        if (!inputHandler.isTouching() && inputHandler.getEnergyLevel() > 0) {
            throwDice();
        }
    }
    
    /**
     * Handle touch input
     */
    public void onTouchEvent(MotionEvent event) {
        inputHandler.onTouchEvent(event);
        
        // If user taps and dice is not moving, prepare for throw
        if (event.getAction() == MotionEvent.ACTION_DOWN && !dice.isMoving()) {
            // Ready to throw
        }
        
        // If dice is stopped and user taps, reset dice
        if (event.getAction() == MotionEvent.ACTION_DOWN && !dice.isMoving() && 
            inputHandler.getEnergyLevel() == 0) {
            // Tapping anywhere when dice is stopped will reset it in next update
        }
    }
    
    /**
     * Throw the dice based on current input
     */
    private void throwDice() {
        Vector3 throwDirection = inputHandler.getThrowDirection();
        float throwMagnitude = inputHandler.getEnergyLevel();
        
        // Calculate throw force with upward component
        Vector3 throwForce = new Vector3(
            throwDirection.x * throwMagnitude * MAX_THROW_FORCE,
            throwDirection.y * throwMagnitude * MAX_THROW_FORCE,
            Math.abs(throwDirection.x) * throwMagnitude * MAX_THROW_FORCE * 0.5f // Add upward component
        );
        
        // Apply force to dice
        physicsEngine.applyForce(throwForce);
        
        // Apply torque for rotation (perpendicular to throw direction)
        Vector3 torque = new Vector3(
            throwDirection.y * throwMagnitude * MAX_TORQUE,
            -throwDirection.x * throwMagnitude * MAX_TORQUE,
            throwMagnitude * MAX_TORQUE * 0.5f
        );
        
        physicsEngine.applyTorque(torque);
        
        // Reset input energy
        inputHandler.getEnergyLevel(); // Mark as used
    }
    
    /**
     * Reset the game (dice back to initial position)
     */
    public void resetGame() {
        dice.reset();
    }
    
    // Getters
    public Dice getDice() {
        return dice;
    }
    
    public InputHandler getInputHandler() {
        return inputHandler;
    }
    
    public boolean isGameRunning() {
        return gameRunning;
    }
    
    public float getEnergyLevel() {
        return inputHandler.getEnergyLevel();
    }
}
