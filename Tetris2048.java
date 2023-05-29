import java.awt.Color; // the color type used in StdDraw
import java.awt.Font; // the font type used in StdDraw
import java.awt.event.KeyEvent; // for the key codes used in StdDraw
import java.util.Random;

// The main class to run the Tetris 2048 game
public class Tetris2048 {
   static boolean isRestarted = false;
   public static void main(String[] args) throws InterruptedException {
      // set the size of the game grid
      int gridH = 18, gridW = 12;
      // set the size of the drawing canvas
      int canvasH = 40 * gridH, canvasW = (40 * gridW) + (40 * gridW/3);
      StdDraw.setCanvasSize(canvasW, canvasH);
      // set the scale of the coordinate system
      StdDraw.setXscale(-0.5, gridW + ((double) gridW / 3) - 0.5); // "((double) gridW / 3)"for right side bar
      StdDraw.setYscale(-0.5, gridH - 0.5);
      // double buffering enables computer animations, creating an illusion of
      // motion by repeating four steps: clear, draw, show and pause
      StdDraw.enableDoubleBuffering();

      // set the dimension values stored and used in the Tetromino class
      Tetromino.gridHeight = gridH;
      Tetromino.gridWidth = gridW;

      // create the game grid
      GameGrid grid = new GameGrid(gridH, gridW);
      // create the first tetromino to enter the game grid
      // by using the createTetromino method defined below
      Tetromino currentTetromino = randomTetromino();
      grid.setCurrentTetromino(currentTetromino);
      // for next Tetromino
      Tetromino nextTetromino = randomTetromino();
      grid.setNextTetromino(nextTetromino);
      // display a simple menu before opening the game
      // by using the displayGameMenu method defined below
      // for restart
      if(isRestarted == false){
         displayGameMenu(gridH, gridW);
      }
      // the main game loop (using some keyboard keys for moving the tetromino)
      // -----------------------------------------------------------------------
      int iterationCount = 0; // used for the speed of the game
      boolean isPaused = false;
      while (true) {
         // check user interactions via the keyboard
         // --------------------------------------------------------------------
         // if the left arrow key is being pressed
         if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
            // move the active tetromino left by one
            currentTetromino.move("left", grid);
            // for the delay
            StdDraw.pause(25);
         }
         // if the right arrow key is being pressed
         else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
            // move the active tetromino right by one
            currentTetromino.move("right", grid);
            // for the delay
            StdDraw.pause(25);
         }
         // if the down arrow key is being pressed
         else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
            // move the active tetromino down by one
            currentTetromino.move("down", grid);
            // for the delay
            StdDraw.pause(25);
         }
         
         // if the space key is being pressed
         else if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
            // move the active tetromino to the very bottom
            currentTetromino.move("fulldown", grid);
            // for the delay
            StdDraw.pause(50);
         }
         // if it can be possible rotate clockwise
         else if (StdDraw.isKeyPressed(KeyEvent.VK_D)) {
            if (currentTetromino.canBeRotated(grid)) {
               currentTetromino.rotateRight();
               // for the delay
               StdDraw.pause(75);
            }
         }
         // if it can be possible rotate anticlockwise
         else if (StdDraw.isKeyPressed(KeyEvent.VK_A)) {
            if (currentTetromino.canBeRotated(grid)) {
               currentTetromino.rotateLeft();
               // for the delay
               StdDraw.pause(75);
            }
         }
         // open pause menu
         else if (StdDraw.isKeyPressed(KeyEvent.VK_P)){
            StdDraw.pause(50);
            // for the delay
            displayPauseMenu(gridH, gridW);
         }

         // move the active tetromino down by 1 once in 10 iterations (auto fall)
         boolean success = true;
         if (iterationCount % 10 == 0)
            success = currentTetromino.move("down", grid);
         iterationCount++;

         // place the active tetromino on the grid when it cannot go down anymore
         if (!success) {
            // get the tile matrix of the tetromino without empty rows and columns
            currentTetromino.createMinBoundedTileMatrix();
            Tile[][] tiles = currentTetromino.getMinBoundedTileMatrix();
            Point pos = currentTetromino.getMinBoundedTileMatrixPosition();
            // update the game grid by locking the tiles of the landed tetromino
            boolean gameOver = grid.updateGrid(tiles, pos);
            // end the main game loop if the game is over
            if (gameOver){
               System.out.println("You lose!");
               displayGameOver(gridH, gridW, false);
               break;
            }
            if (grid.requiredNumberToWin() >= 2048) {
               System.out.println("You win!");
               displayGameOver(gridH, gridW, true);
            }
            // create the next tetromino to enter the game grid
            // by using the createTetromino function defined below
            currentTetromino = nextTetromino;
            grid.setCurrentTetromino(nextTetromino);
            // for the next tetromino
            nextTetromino = randomTetromino();
            grid.setNextTetromino(nextTetromino);
            // If the line is full, it deletes it, adds the score, and moves it to the next line.
            grid.clearLines();
         }

         // display the game grid and the current tetromino
         grid.display();

      }

      // print a message on the console that the game is over
      System.out.println("Game over!");
   }

   // A method for creating a random shaped tetromino to enter the game grid
   public static Tetromino randomTetromino() {
      // the type (shape) of the tetromino is determined randomly
      char[] tetrominoTypes = { 'I', 'O', 'Z', 'S', 'J', 'L', 'T' };
      Random random = new Random();
      int randomIndex = random.nextInt(tetrominoTypes.length);
      char randomType = tetrominoTypes[randomIndex];
      // create and return the tetromino
      Tetromino tetromino = new Tetromino(randomType);
      return tetromino;
   }

   // A method for displaying a simple menu before starting the game
   public static void displayGameMenu(int gridHeight, int gridWidth) {
      // colors used for the menu
      Color backgroundColor = new Color(42, 69, 99);
      Color buttonColor = new Color(25, 255, 228);
      Color textColor = new Color(31, 160, 239);
      // clear the background canvas to background_color
      StdDraw.clear(backgroundColor);
      // the relative path of the image file
      String imgFile = "images/menu_image.png";
      // center coordinates to display the image
      gridWidth = gridWidth + gridWidth / 4;
      double imgCenterX = (gridWidth - 1) / 2.0, imgCenterY = gridHeight - 7;
      // display the image
      StdDraw.picture(imgCenterX, imgCenterY, imgFile);
      // the width and the height of the start game button
      double buttonW = gridWidth - 1.5, buttonH = 2;
      // the center point coordinates of the start game button
      double buttonX = imgCenterX, buttonY = 5;
      // display the start game button as a filled rectangle
      StdDraw.setPenColor(buttonColor);
      StdDraw.filledRectangle(buttonX, buttonY, buttonW / 2, buttonH / 2);
      // display the text on the start game button
      Font font = new Font("Arial", Font.PLAIN, 25);
      StdDraw.setFont(font);
      StdDraw.setPenColor(textColor);
      String textToDisplay = "Start New Game";
      StdDraw.text(buttonX, buttonY, textToDisplay);
      // menu interaction loop
      while (true) {
         // display the menu and wait for a short time (50 ms)
         StdDraw.show();
         StdDraw.pause(50);
         // check if the mouse is being pressed on the button
         if (StdDraw.isMousePressed()) {
            // get the x and y coordinates of the position of the mouse
            double mouseX = StdDraw.mouseX(), mouseY = StdDraw.mouseY();
            // check if these coordinates are inside the button
            if (mouseX > buttonX - buttonW / 2 && mouseX < buttonX + buttonW / 2)
               if (mouseY > buttonY - buttonH / 2 && mouseY < buttonY + buttonH / 2)
                  break; // break the loop to end the method and start the game
         }
      }
   }

   public static void displayPauseMenu(int gridHeight, int gridWidth) throws InterruptedException {
      Color backgroundColor = new Color(72, 72, 93);
      Color buttonColor = new Color(250, 250, 250);
      Color textColor = new Color(45, 45, 45);
      StdDraw.clear(backgroundColor);
      String pauseImage = "images/pause.png";
      gridWidth = gridWidth + gridWidth / 4;
      double pauseX = (gridWidth + 1) / 2.0;
      double pauseY = gridHeight - 5;
      StdDraw.picture(pauseX - 0.5, pauseY -2, pauseImage);
      double width = gridWidth - 1, height = 1;
      double y = 5;
      StdDraw.setPenColor(buttonColor);
      StdDraw.filledRectangle(pauseX - 4.5, y + 0, width / 8, height + 0);
      StdDraw.filledRectangle(pauseX - 0.5 , y + 0, width / 8, height + 0);
      StdDraw.filledRectangle(pauseX + 3.5, y + 0, width / 8, height + 0);

      Font font = new Font("Arial", Font.BOLD, 25);
      StdDraw.setFont(font);
      StdDraw.setPenColor(textColor);

      String textToDisplay = "Resume";
      String resumeImage = "images/resume.png";
      StdDraw.picture(pauseX -4.5, y, resumeImage);
      StdDraw.text(pauseX -4.5, y - 1.5, textToDisplay);

      textToDisplay = "Restart";
      String restartImage = "images/restart.png";
      StdDraw.picture(pauseX - 0.5, y, restartImage);
      StdDraw.text(pauseX -0.5, y - 1.5, textToDisplay);

      textToDisplay = "Exit";
      String exitImage = "images/exit.png";
      StdDraw.picture(pauseX + 3.5, y, exitImage);
      StdDraw.text(pauseX +3.5, y - 1.5 , textToDisplay);

      while(true){
         StdDraw.show();
         StdDraw.pause(100);

         // if the mouse is clicked
         if (StdDraw.isMousePressed()) {
            double mouseX = StdDraw.mouseX(), mouseY = StdDraw.mouseY();
            if (mouseY > y - height && mouseY < y + height)
               if (mouseX > (pauseX - 4.5) - width/8 && mouseX < (pauseX - 4.5) + width/8)
                  break;

               // restart the game
               else if (mouseX > (pauseX - 0.5) - width / 8 && mouseX < (pauseX - 0.5) + width / 8){
                  StdDraw.pause(100);
                  restartGame();
               }

               else if(mouseX > (pauseX + 3.5) - width / 8 && mouseX < (pauseX + 3.5) + width / 8){
                  System.exit(0); // exit the game
               }
         }
      }
   }
   public static void displayGameOver(int gridHeight, int gridWidth, boolean win) throws InterruptedException {
      // colors used for the pause menu
      Color buttonColor = new Color(25, 255, 228);
      Color textColor = new Color(31, 160, 239);
      // clear the background canvas to background_color
      // the relative path of the image file
      String imgFile = "images/loseMenu_image.png";
      if (win) {
         imgFile = "images/winMenu_image.png";
      }
      gridWidth = gridWidth + gridWidth / 4;
      double imgCenterX = (gridWidth - 1) / 2.0, imgCenterY = gridHeight - 7;
      StdDraw.picture(imgCenterX, imgCenterY + 2, imgFile);
      double buttonW = gridWidth - 1.5, buttonH = 2;
      double buttonX = imgCenterX, buttonY = 5;
      StdDraw.setPenColor(buttonColor);

      // creating the new game button in the center.
      StdDraw.filledRectangle(buttonX, buttonY, buttonW / 2, buttonH / 2);
      // creating the exit game button with -2.5 Y difference.
      StdDraw.filledRectangle(buttonX, buttonY - 2.5, buttonW / 2, buttonH / 2);

      Font font = new Font("Arial", Font.PLAIN, 25);
      StdDraw.setFont(font);
      StdDraw.setPenColor(textColor);

      String textToDisplay = "New Game";
      StdDraw.text(buttonX, buttonY, textToDisplay);

      textToDisplay = "Exit Game";
      StdDraw.text(buttonX, buttonY - 2.5, textToDisplay);

      while(true){
         StdDraw.show();
         StdDraw.pause(100);

         //if the mouse is clicked
         if (StdDraw.isMousePressed()) {
            double mouseX = StdDraw.mouseX(), mouseY = StdDraw.mouseY();
            if (mouseX > buttonX - buttonW / 2 && mouseX < buttonX + buttonW / 2)
               if (mouseY > (buttonY) - buttonH / 2 && mouseY < (buttonY) + buttonH / 2){
                  StdDraw.pause(100);
                  restartGame();
               }
               else if(mouseY > (buttonY - 2.5) - buttonH / 2 && mouseY < (buttonY - 2.5) + buttonH / 2){
                  System.exit(0); // exit the game
               }
         }
      }
   }
   // restart the game
   public static void restartGame() throws InterruptedException {
      isRestarted = true;
      main(null);
   }
}
