package com.example.threedice.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import com.example.threedice.config.DiceConfig;
import com.example.threedice.config.SceneConfig;
import com.example.threedice.config.UIConfig;
import com.example.threedice.model.Dice;
import com.example.threedice.model.Vector3;

/**
 * Renderer - handles 3D to 2D projection and drawing
 */
public class Renderer {
    private Paint paint;
    private Dice dice;
    private int screenWidth;
    private int screenHeight;
    
    // Projection parameters
    private float viewDistance = 3000f; // Distance from viewer to scene
    
    public Renderer(Dice dice, int screenWidth, int screenHeight) {
        this.dice = dice;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
    }
    
    /**
     * Render the entire scene
     */
    public void render(Canvas canvas, float energyLevel) {
        // Draw background
        paint.setColor(SceneConfig.BACKGROUND_COLOR);
        canvas.drawRect(0, 0, screenWidth, screenHeight, paint);
        
        // Draw reference grid lines
        drawReferenceGrid(canvas);
        
        // Draw dice
        drawDice(canvas);
        
        // Draw UI elements
        drawEnergyBar(canvas, energyLevel);
    }
    
    /**
     * Draw reference grid lines for 3D visualization
     */
    private void drawReferenceGrid(Canvas canvas) {
        paint.setColor(SceneConfig.GRID_LINE_COLOR);
        paint.setStrokeWidth(SceneConfig.GRID_LINE_WIDTH);
        
        // Draw grid on ground (Z = 0 plane)
        float spacing = SceneConfig.GRID_SPACING;
        for (int i = 0; i <= SceneConfig.SPACE_WIDTH; i += spacing) {
            // Lines parallel to Y axis
            Vector3 start = project3DTo2D(new Vector3(i, 0, 0));
            Vector3 end = project3DTo2D(new Vector3(i, SceneConfig.SPACE_HEIGHT, 0));
            canvas.drawLine(start.x, start.y, end.x, end.y, paint);
            
            // Lines parallel to X axis
            start = project3DTo2D(new Vector3(0, i, 0));
            end = project3DTo2D(new Vector3(SceneConfig.SPACE_WIDTH, i, 0));
            canvas.drawLine(start.x, start.y, end.x, end.y, paint);
        }
        
        // Draw reference axes from origin
        paint.setColor(0xFF0000FF); // Blue for Z axis
        paint.setStrokeWidth(2);
        Vector3 originProj = project3DTo2D(new Vector3(0, 0, 0));
        Vector3 zAxisProj = project3DTo2D(new Vector3(0, 0, 500));
        canvas.drawLine(originProj.x, originProj.y, zAxisProj.x, zAxisProj.y, paint);
    }
    
    /**
     * Draw the dice
     */
    private void drawDice(Canvas canvas) {
        Vector3 dicePos = dice.getPosition();
        Vector3 rotation = dice.getRotation();
        
        // For now, draw dice as a simple 3D box projection
        // TODO: Implement proper 3D rendering with face visibility
        
        // Get 8 corners of dice
        Vector3[] corners = dice.getCorners();
        Vector3[] projectedCorners = new Vector3[8];
        
        for (int i = 0; i < 8; i++) {
            projectedCorners[i] = project3DTo2D(corners[i]);
        }
        
        // Draw edges of dice
        paint.setColor(DiceConfig.DICE_COLOR);
        paint.setStrokeWidth(2);
        
        // Draw bottom face
        drawQuad(canvas, projectedCorners[0], projectedCorners[1], 
                 projectedCorners[5], projectedCorners[4]);
        
        // Draw top face
        drawQuad(canvas, projectedCorners[3], projectedCorners[2], 
                 projectedCorners[6], projectedCorners[7]);
        
        // Draw front face
        drawQuad(canvas, projectedCorners[0], projectedCorners[3], 
                 projectedCorners[7], projectedCorners[4]);
        
        // Draw back face
        drawQuad(canvas, projectedCorners[1], projectedCorners[2], 
                 projectedCorners[6], projectedCorners[5]);
        
        // Draw left face
        drawQuad(canvas, projectedCorners[0], projectedCorners[4], 
                 projectedCorners[7], projectedCorners[3]);
        
        // Draw right face
        drawQuad(canvas, projectedCorners[1], projectedCorners[5], 
                 projectedCorners[6], projectedCorners[2]);
        
        // Draw dice dots
        drawDiceDots(canvas, dicePos, rotation);
    }
    
    /**
     * Draw a quadrilateral on canvas
     */
    private void drawQuad(Canvas canvas, Vector3 p1, Vector3 p2, Vector3 p3, Vector3 p4) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xFF808080); // Gray color for wireframe
        paint.setStrokeWidth(2);
        
        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
        canvas.drawLine(p2.x, p2.y, p3.x, p3.y, paint);
        canvas.drawLine(p3.x, p3.y, p4.x, p4.y, paint);
        canvas.drawLine(p4.x, p4.y, p1.x, p1.y, paint);
    }
    
    /**
     * Draw dice dots based on current orientation
     */
    private void drawDiceDots(Canvas canvas, Vector3 dicePos, Vector3 rotation) {
        paint.setStyle(Paint.Style.FILL);
        
        // Determine which face is pointing up based on rotation
        // This is simplified - in reality would need proper rotation matrix
        int topFaceNumber = determineTopFace(rotation);
        
        // Draw dots on the top face
        drawDotsOnFace(canvas, dicePos, topFaceNumber);
    }
    
    /**
     * Determine which face is pointing up (1-6) based on rotation
     * Simplified version - maps rotation to face
     */
    private int determineTopFace(Vector3 rotation) {
        // Normalize angles to 0-2*PI
        float x = ((rotation.x % (float)Math.PI) + (float)Math.PI) % (float)Math.PI;
        float y = ((rotation.y % (float)Math.PI) + (float)Math.PI) % (float)Math.PI;
        float z = ((rotation.z % (float)Math.PI) + (float)Math.PI) % (float)Math.PI;
        
        // Simplified logic - in reality need proper orientation calculation
        if (x < Math.PI / 4) return 1;
        if (x < Math.PI / 2) return 2;
        if (y < Math.PI / 4) return 3;
        if (y < Math.PI / 2) return 4;
        if (z < Math.PI / 4) return 5;
        return 6;
    }
    
    /**
     * Draw dots on the top face of dice
     */
    private void drawDotsOnFace(Canvas canvas, Vector3 dicePos, int faceNumber) {
        Vector3 faceCenter = project3DTo2D(dicePos);
        float dotRadius = DiceConfig.DOT_RADIUS;
        int dotColor;
        int dotCount;
        
        // Determine dot color and count based on face number
        switch (faceNumber) {
            case 1:
                dotCount = DiceConfig.FACE_1_DOTS;
                dotColor = DiceConfig.FACE_1_COLOR;
                break;
            case 2:
                dotCount = DiceConfig.FACE_2_DOTS;
                dotColor = DiceConfig.FACE_2_COLOR;
                break;
            case 3:
                dotCount = DiceConfig.FACE_3_DOTS;
                dotColor = DiceConfig.FACE_3_COLOR;
                break;
            case 4:
                dotCount = DiceConfig.FACE_4_DOTS;
                dotColor = DiceConfig.FACE_4_COLOR;
                break;
            case 5:
                dotCount = DiceConfig.FACE_5_DOTS;
                dotColor = DiceConfig.FACE_5_COLOR;
                break;
            case 6:
                dotCount = DiceConfig.FACE_6_DOTS;
                dotColor = DiceConfig.FACE_6_COLOR;
                break;
            default:
                return;
        }
        
        paint.setColor(dotColor);
        
        // Draw dots in appropriate pattern
        float spacing = 40f;
        switch (dotCount) {
            case 1: // Center dot
                canvas.drawCircle(faceCenter.x, faceCenter.y, dotRadius, paint);
                break;
            case 2: // Diagonal dots
                canvas.drawCircle(faceCenter.x - spacing, faceCenter.y - spacing, dotRadius, paint);
                canvas.drawCircle(faceCenter.x + spacing, faceCenter.y + spacing, dotRadius, paint);
                break;
            case 3: // Diagonal line
                canvas.drawCircle(faceCenter.x - spacing, faceCenter.y - spacing, dotRadius, paint);
                canvas.drawCircle(faceCenter.x, faceCenter.y, dotRadius, paint);
                canvas.drawCircle(faceCenter.x + spacing, faceCenter.y + spacing, dotRadius, paint);
                break;
            case 4: // Four corners
                canvas.drawCircle(faceCenter.x - spacing, faceCenter.y - spacing, dotRadius, paint);
                canvas.drawCircle(faceCenter.x + spacing, faceCenter.y - spacing, dotRadius, paint);
                canvas.drawCircle(faceCenter.x - spacing, faceCenter.y + spacing, dotRadius, paint);
                canvas.drawCircle(faceCenter.x + spacing, faceCenter.y + spacing, dotRadius, paint);
                break;
            case 5: // Four corners + center
                canvas.drawCircle(faceCenter.x - spacing, faceCenter.y - spacing, dotRadius, paint);
                canvas.drawCircle(faceCenter.x + spacing, faceCenter.y - spacing, dotRadius, paint);
                canvas.drawCircle(faceCenter.x - spacing, faceCenter.y + spacing, dotRadius, paint);
                canvas.drawCircle(faceCenter.x + spacing, faceCenter.y + spacing, dotRadius, paint);
                canvas.drawCircle(faceCenter.x, faceCenter.y, dotRadius, paint);
                break;
            case 6: // Two columns of 3
                for (int i = -1; i <= 1; i++) {
                    canvas.drawCircle(faceCenter.x - spacing, faceCenter.y + i * spacing, dotRadius, paint);
                    canvas.drawCircle(faceCenter.x + spacing, faceCenter.y + i * spacing, dotRadius, paint);
                }
                break;
        }
    }
    
    /**
     * Draw energy bar UI element
     */
    private void drawEnergyBar(Canvas canvas, float energyLevel) {
        float barX = UIConfig.ENERGY_BAR_MARGIN_LEFT;
        float barY = UIConfig.ENERGY_BAR_MARGIN_TOP;
        float barLength = UIConfig.ENERGY_BAR_LENGTH;
        float barHeight = UIConfig.ENERGY_BAR_HEIGHT;
        float border = UIConfig.ENERGY_BAR_BORDER;
        
        // Draw background
        paint.setColor(UIConfig.ENERGY_BAR_BACKGROUND);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(barX, barY, barX + barLength, barY + barHeight, paint);
        
        // Draw border
        paint.setColor(UIConfig.ENERGY_BAR_BORDER_COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(border);
        canvas.drawRect(barX, barY, barX + barLength, barY + barHeight, paint);
        
        // Draw energy fill
        if (energyLevel > 0) {
            paint.setColor(UIConfig.ENERGY_BAR_FILL_COLOR);
            paint.setStyle(Paint.Style.FILL);
            float fillWidth = (UIConfig.ENERGY_BAR_FULL_LENGTH) * energyLevel;
            canvas.drawRect(barX + border, barY + border, 
                          barX + border + fillWidth, barY + barHeight - border, paint);
        }
    }
    
    /**
     * Project 3D coordinates to 2D screen coordinates using isometric-like projection
     */
    private Vector3 project3DTo2D(Vector3 point3D) {
        // Simple isometric-like projection
        // Convert 3D space to 2D screen
        
        // Center the scene
        float centerX = screenWidth / 2f;
        float centerY = screenHeight / 2f;
        
        // Scale factor for projection
        float scale = 0.3f;
        
        // Isometric projection (simplified)
        float screenX = centerX + (point3D.x - point3D.y) * scale;
        float screenY = centerY + (point3D.z - (point3D.x + point3D.y) * 0.5f) * scale;
        
        return new Vector3(screenX, screenY, 0);
    }
}
