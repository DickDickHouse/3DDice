package com.example.threedice.input;

import android.view.MotionEvent;
import com.example.threedice.config.UIConfig;
import com.example.threedice.model.Vector3;

/**
 * Input Handler - handles touch input and energy charging
 */
public class InputHandler {
    private boolean isTouching = false;
    private float touchStartX = 0;
    private float touchStartY = 0;
    private float currentTouchX = 0;
    private float currentTouchY = 0;
    private float energyLevel = 0; // 0 to 1
    private long touchStartTime = 0;
    private long lastEnergyRefreshTime = 0;
    private boolean energyRefreshed = false;
    
    // Constants
    private static final float TOUCH_MOVE_THRESHOLD = 5f; // pixels
    private static final long ENERGY_CYCLE_TIME = UIConfig.ENERGY_CHARGE_DURATION; // 2 seconds for full cycle
    
    public InputHandler() {
    }
    
    /**
     * Handle touch events
     */
    public void onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp(event);
                break;
        }
    }
    
    /**
     * Handle touch down event
     */
    private void onTouchDown(MotionEvent event) {
        isTouching = true;
        touchStartX = event.getX();
        touchStartY = event.getY();
        currentTouchX = touchStartX;
        currentTouchY = touchStartY;
        touchStartTime = System.currentTimeMillis();
        lastEnergyRefreshTime = touchStartTime;
        energyRefreshed = false;
        energyLevel = 0;
    }
    
    /**
     * Handle touch move event
     */
    private void onTouchMove(MotionEvent event) {
        if (isTouching) {
            currentTouchX = event.getX();
            currentTouchY = event.getY();
            updateEnergy();
        }
    }
    
    /**
     * Handle touch up event
     */
    private void onTouchUp(MotionEvent event) {
        isTouching = false;
    }
    
    /**
     * Update energy level based on elapsed time
     * Energy charges from 0% to 100% over ENERGY_CYCLE_TIME
     * If it reaches 100% and user is still holding, it resets to 0% and starts again
     */
    private void updateEnergy() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastEnergyRefreshTime;
        
        // Energy increases from 0 to 1 over ENERGY_CYCLE_TIME milliseconds
        energyLevel = (float) (elapsedTime % ENERGY_CYCLE_TIME) / ENERGY_CYCLE_TIME;
    }
    
    /**
     * Get the throwing direction based on touch movement
     * Returns a normalized vector indicating the direction
     */
    public Vector3 getThrowDirection() {
        float deltaX = currentTouchX - touchStartX;
        float deltaY = currentTouchY - touchStartY;
        
        // Create direction vector (X and Y from touch, Z from vertical component for upward throw)
        Vector3 direction = new Vector3(deltaX, deltaY, 0);
        float length = direction.length();
        
        if (length > 0) {
            direction.normalize();
        }
        
        return direction;
    }
    
    /**
     * Get the throw magnitude (speed) based on current energy level
     */
    public float getThrowMagnitude() {
        return energyLevel;
    }
    
    /**
     * Get throw force vector combining direction and magnitude
     */
    public Vector3 getThrowForce(float maxForce) {
        Vector3 direction = getThrowDirection();
        float magnitude = getThrowMagnitude();
        direction.multiply(magnitude * maxForce);
        return direction;
    }
    
    // Getters
    public boolean isTouching() {
        return isTouching;
    }
    
    public float getEnergyLevel() {
        return energyLevel;
    }
    
    public float getTouchStartX() {
        return touchStartX;
    }
    
    public float getTouchStartY() {
        return touchStartY;
    }
    
    public float getCurrentTouchX() {
        return currentTouchX;
    }
    
    public float getCurrentTouchY() {
        return currentTouchY;
    }
    
    public float getTouchDeltaX() {
        return currentTouchX - touchStartX;
    }
    
    public float getTouchDeltaY() {
        return currentTouchY - touchStartY;
    }
}
