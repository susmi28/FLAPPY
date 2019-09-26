package com.example.tappyspaceship01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {

    // Android debug variables
    final static String TAG="TAPPY-SPACESHIP";

    // screen size
    int screenHeight;
    int screenWidth;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;



    // -----------------------------------
    // GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------

    // represent the TOP LEFT CORNER OF THE GRAPHIC
    int playerXPosition;
    int playerYPosition;
    Bitmap playerImage;
    Rect playerHitbox;

    Bitmap ememyImage;
    int enemyXPosition;
    int enemyYPosition;
    Rect enemyHitbox;


    // Add enemy 2
    Bitmap enemy2Image;
    int enemy2XPosition;
    int enemy2YPosition;
    Rect enemy2Hitbox;




    // ----------------------------
    // ## GAME STATS
    // ----------------------------

    int lives = 10;


    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;


        this.printScreenInfo();

        // @TODO: Add your sprites

        // put initial starting postion of enemy
        this.ememyImage = BitmapFactory.decodeResource(this.getContext().getResources(),
                R.drawable.alien_ship2);


        this.enemyXPosition = 1300;
        this.enemyYPosition = 120;
        // 1. create the hitbox
        this.enemyHitbox = new Rect(1300,
                120,
                1300+ememyImage.getWidth(),
                120+ememyImage.getHeight()
        );

        // put the initial starting position of your player

         this.playerImage = BitmapFactory.decodeResource(this.getContext().getResources(),
                R.drawable.player_ship);
        this.playerXPosition = 100;
        this.playerYPosition = 600;

        this.playerHitbox = new Rect(100,
                600,
                100+playerImage.getWidth(),
                600+playerImage.getHeight()
        );


        // CREATE A NEW ENEMY
        // 1. Setup the sprite's graphic image
        // 2. Setup the sprite's starting position
        // 3. Setup the sprite's hitbox
        this.enemy2Image = BitmapFactory.decodeResource(this.getContext().getResources(),
                R.drawable.alien_ship3);

        this.enemy2XPosition = 1500;
        this.enemy2YPosition = 500;

        this.enemy2Hitbox = new Rect(1500,
                500,
                1500+enemy2Image.getWidth(),
                500+enemy2Image.getHeight()
        );

    }


    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }

    private void spawnPlayer() {
        //@TODO: Start the player at the left side of screen
    }
    private void spawnEnemyShips() {
        Random random = new Random();

        //@TODO: Place the enemies in a random location

    }

    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------



    public void updatePositions() {
        // @TODO: Update position of player based on mouse tap
        if (this.fingerAction == "mousedown") {
            // if mousedown, then move player up
            // Make the UP movement > than down movement - this will
            // make it look like the player is moving up alot
            this.playerYPosition = this.playerYPosition - 100;

            this.playerHitbox.left  = this.playerXPosition;
            this.playerHitbox.top = this.playerYPosition;
            this.playerHitbox.right  = this.playerXPosition + this.playerImage.getWidth();
            this.playerHitbox.bottom = this.playerYPosition + this.playerImage.getHeight();
        }

        if (this.fingerAction == "mouseup") {
            // if mouseup, then move player down
            this.playerYPosition = this.playerYPosition + 10;

            this.playerHitbox.left  = this.playerXPosition;
            this.playerHitbox.top = this.playerYPosition;
            this.playerHitbox.right  = this.playerXPosition + this.playerImage.getWidth();
            this.playerHitbox.bottom = this.playerYPosition + this.playerImage.getHeight();
        }

        // MAKE ENEMY MOVE
        // - enemy moves left forever
        // - when enemy touches LEFT wall, respawn on RIGHT SIDE
        this.enemyXPosition = this.enemyXPosition - 25;

        // MOVE THE HITBOX (recalcluate the position of the hitbox)
        this.enemyHitbox.left  = this.enemyXPosition;
        this.enemyHitbox.top = this.enemyYPosition;
        this.enemyHitbox.right  = this.enemyXPosition + this.ememyImage.getWidth();
        this.enemyHitbox.bottom = this.enemyYPosition + this.ememyImage.getHeight();

        if (this.enemyXPosition <= 0) {
            // restart the enemy in the starting position
            this.enemyXPosition = 1300;
            this.enemyYPosition = 120;

            // restart the hitbox in the starting position
            // Anytime you move the enemy, you also need to move the hitbox
            this.enemyHitbox.left  = this.enemyXPosition;
            this.enemyHitbox.top = this.enemyYPosition;
            this.enemyHitbox.right  = this.enemyXPosition + this.ememyImage.getWidth();
            this.enemyHitbox.bottom = this.enemyYPosition + this.ememyImage.getHeight();

        }


        // MAKE ENEMY2 MOVE
        // MAKE ENEMY MOVE
        // - enemy moves left forever
        // - when enemy touches LEFT wall, respawn on RIGHT SIDE
        this.enemy2XPosition = this.enemy2XPosition - 25;

        // MOVE THE HITBOX (recalcluate the position of the hitbox)
        this.enemy2Hitbox.left  = this.enemy2XPosition;
        this.enemy2Hitbox.top = this.enemy2YPosition;
        this.enemy2Hitbox.right  = this.enemy2XPosition + this.enemy2Image.getWidth();
        this.enemy2Hitbox.bottom = this.enemy2YPosition + this.enemy2Image.getHeight();

        if (this.enemy2XPosition <= 0) {
            // restart the enemy in the starting position
            this.enemy2XPosition = 1500;
            this.enemy2YPosition = 500;

            // restart the hitbox in the starting position
            // Anytime you move the enemy, you also need to move the hitbox
            this.enemy2Hitbox.left  = this.enemy2XPosition;
            this.enemy2Hitbox.top = this.enemy2YPosition;
            this.enemy2Hitbox.right  = this.enemy2XPosition + this.enemy2Image.getWidth();
            this.enemy2Hitbox.bottom = this.enemy2YPosition + this.enemy2Image.getHeight();

        }







        // @TODO:  Check collisions between enemy and player
        if (this.playerHitbox.intersect(this.enemyHitbox) == true) {
            // the enemy and player are colliding
            Log.d(TAG, "++++++ENEMY AND PLAYER COLLIDING!");

            // @TODO: What do you want to do next?

            // RESTART THE PLAYER IN ORIGINAL POSITION
            // -------
            // 1. Restart the player
            // 2. Restart the player's hitbox
            this.playerXPosition = 100;
            this.playerYPosition = 600;

            this.playerHitbox = new Rect(100,
                    600,
                    100+playerImage.getWidth(),
                    600+playerImage.getHeight()
            );

            // decrease the lives
            lives = lives - 1;

        }


        // @TODO:  Check collisions between enemy2 and player
        if (this.playerHitbox.intersect(this.enemy2Hitbox) == true) {
            // the enemy and player are colliding
            Log.d(TAG, "++++++ENEMY 2 AND PLAYER COLLIDING!");

            // @TODO: What do you want to do next?

            // RESTART THE PLAYER IN ORIGINAL POSITION
            // -------
            // 1. Restart the player
            // 2. Restart the player's hitbox
            this.playerXPosition = 100;
            this.playerYPosition = 600;

            this.playerHitbox = new Rect(100,
                    600,
                    100+playerImage.getWidth(),
                    600+playerImage.getHeight()
            );

            // decrease the lives
            lives = lives - 1;

        }





    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,255,255,255));
            paintbrush.setColor(Color.WHITE);


            // DRAW THE PLAYER HITBOX
            // ------------------------
            // 1. change the paintbrush settings so we can see the hitbox
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);


            // draw player graphic on screen
            canvas.drawBitmap(playerImage, playerXPosition, playerYPosition, paintbrush);
            // draw the player's hitbox
            canvas.drawRect(this.playerHitbox, paintbrush);

            // draw the enemy graphic on the screen
            canvas.drawBitmap(ememyImage, enemyXPosition, enemyYPosition, paintbrush);
            // 2. draw the enemy's hitbox
            canvas.drawRect(this.enemyHitbox, paintbrush);


            // draw enemy 2 on the screen
            // draw the enemy graphic on the screen
            canvas.drawBitmap(enemy2Image, enemy2XPosition, enemy2YPosition, paintbrush);
            // 2. draw the enemy's hitbox
            canvas.drawRect(this.enemy2Hitbox, paintbrush);




            // DRAW GAME STATS
            // -----------------------------
            paintbrush.setTextSize(60);
            canvas.drawText("Lives remaining: " + lives,
                    1100,
                    800,
                    paintbrush
                    );

            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setFPS() {
        try {
            gameThread.sleep(120);
        }
        catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------


    String fingerAction = "";

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        // Answer - PRESS DOWN ON SCREEN --> PLAYER MOVES UP
        // RELEASE FINGER --> PLAYER MOVES DOWN
        if (userAction == MotionEvent.ACTION_DOWN) {
            //Log.d(TAG, "Person tapped the screen");
            // User is pressing down, so move player up
            fingerAction = "mousedown";
        }
        else if (userAction == MotionEvent.ACTION_UP) {
            //Log.d(TAG, "Person lifted finger");
            // User has released finger, so move player down
            fingerAction = "mouseup";
        }

        return true;
    }
}
