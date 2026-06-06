package com.example.threedice;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.example.threedice.game.GameManager;
import com.example.threedice.render.Renderer;

/**
 * Main Activity for 3D Dice Application
 */
public class MainActivity extends Activity implements SurfaceHolder.Callback {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private GameManager gameManager;
    private Renderer renderer;
    private RenderThread renderThread;
    private boolean isRunning = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize SurfaceView
        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        
        // Initialize game systems
        gameManager = new GameManager();
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (renderer == null) {
            renderer = new Renderer(
                gameManager.getDice(), 
                surfaceView.getWidth(), 
                surfaceView.getHeight()
            );
        }
        
        gameManager.start();
        isRunning = true;
        renderThread = new RenderThread();
        renderThread.start();
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (renderer != null) {
            renderer = new Renderer(gameManager.getDice(), width, height);
        }
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
        gameManager.stop();
        try {
            if (renderThread != null) {
                renderThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gameManager.onTouchEvent(event);
        
        if (event.getAction() == MotionEvent.ACTION_DOWN && 
            !gameManager.getDice().isMoving()) {
            if (gameManager.getInputHandler().getEnergyLevel() < 0.01f) {
                gameManager.resetGame();
            }
        }
        
        return true;
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        gameManager.stop();
        isRunning = false;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (surfaceView != null && surfaceView.getHolder().getSurface().isValid()) {
            gameManager.start();
            isRunning = true;
        }
    }
    
    private class RenderThread extends Thread {
        @Override
        public void run() {
            while (isRunning) {
                gameManager.update();
                
                if (surfaceHolder.getSurface().isValid()) {
                    Canvas canvas = null;
                    try {
                        canvas = surfaceHolder.lockCanvas();
                        if (canvas != null && renderer != null) {
                            synchronized (surfaceHolder) {
                                renderer.render(canvas, gameManager.getEnergyLevel());
                            }
                        }
                    } finally {
                        if (canvas != null) {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
                
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
