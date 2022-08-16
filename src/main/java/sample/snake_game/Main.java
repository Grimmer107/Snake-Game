package sample.snake_game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.security.SecureRandom;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Main extends Application {

    Pane pane = new Pane();
    StackPane end_pane = new StackPane();
    BorderPane borderPane = new BorderPane();
    StackPane wallpaper = new StackPane();
    HBox status_bar = new HBox(10);

    Scene scene = new Scene(borderPane, 400, 420);
    Scene scene1 = new Scene(wallpaper, 400, 420);
    Scene scene2 = new Scene(end_pane, 400, 420);

    Label score1 = new Label();
    Label score2 = new Label();
    Label score3 = new Label();

    ImageView img = new ImageView(new File("src\\main\\java\\sample\\snake_game\\snake.jpg").toURI().toString());

    enum dir {UP, DOWN, RIGHT, LEFT}
    dir direction = dir.RIGHT;
    int game_score = 0;

    ArrayList<Body> snake = new ArrayList<>(2);
    Canvas canvas = new Canvas(400, 400);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    boolean food_available = false;
    int food_position_X;
    int food_position_Y;
    SecureRandom randnum = new SecureRandom();

    Button start = new Button("PLAY");
    Button exit = new Button("EXIT");

    Stage stage = new Stage();

    @Override
    public void start(Stage primaryStage){

        img.setFitWidth(400);
        img.setFitHeight(420);
        wallpaper.getChildren().addAll(img, start);

        end_pane.getChildren().add(exit);
        end_pane.setStyle("-fx-background-color: black;");

        start.setStyle("-fx-background-color: green; -fx-border-color: black; -fx-border-width: 2; -fx-border-style: solid outside; -fx-border-radius:10;");
        start.setOnAction(e-> stage.setScene(scene));
        exit.setStyle("-fx-background-color: green; -fx-border-color: black; -fx-border-width: 2; -fx-border-style: solid outside; -fx-border-radius:10;");
        exit.setOnAction(e-> stage.close());

        AnimationTimer timer = new AnimationTimer() {
            long update = 0;
            @Override
            public void handle(long l) {
                if(update == 0){
                    move();
                    update = l;
                }
                if(l - update >= 1000000000/(45+game_score/3)){
                    move();
                    if(detect_collision()){
                        stage.setScene(scene2);
                    }
                    update = l;
                }
                if(!food_available) {
                    food_position_X = randnum.nextInt(350) + 30;
                    food_position_Y = randnum.nextInt(350) + 30;
                    game_score++;
                    score2.setText(null);
                    score2.setText("" + game_score);
                    food_available = true;
                }
                create_food(food_position_X, food_position_Y);
                eat_food();
            }
        };
        timer.start();

        initialize();
        set_Status_bar();

        pane.getChildren().add(canvas);
        borderPane.setTop(status_bar);
        borderPane.setCenter(pane);
        stage.setTitle("Hello World");
        stage.setScene(scene1);
        stage.show();
    }

    public void initialize(){
        snake.add(0, new Body(200, 200));
        snake.add(1, new Body(220, 200));
        snake.add(2, new Body(240, 200));
        snake.add(3, new Body(260, 200));
        snake.add(4, new Body(280, 200));
        snake.add(5, new Body(300, 200));
        set_Controls();
    }

    public void set_Status_bar(){
        score1.setText("Score: ");
        score2.setText("" + game_score);
        score3.setText("\t\t\t Level: 1");
        status_bar.setAlignment(Pos.CENTER);
        status_bar.getChildren().addAll(score1, score2, score3);
        status_bar.setStyle("-fx-border-color:white; -fx-border-width: 2; -fx-border-style: solid inside");
    }

    public void set_Controls(){
        scene.addEventFilter(KeyEvent.KEY_PRESSED,  e->{
            switch (e.getCode()){
                case UP:
                    direction = dir.UP;
                    break;
                case DOWN:
                    direction = dir.DOWN;
                    break;
                case RIGHT:
                    direction = dir.RIGHT;
                    break;
                case LEFT:
                    direction = dir.LEFT;
                    break;
            }
        });
    }

    public void move(){
        if(snake.get(0).getX() >= 390 && direction == dir.RIGHT){
            snake.get(0).setX(10);
        }
        if(snake.get(0).getY() >= 390 && direction == dir.DOWN){
            snake.get(0).setY(10);
        }
        if(snake.get(0).getX() <= 10 && direction == dir.LEFT){
            snake.get(0).setX(390);
        }
        if(snake.get(0).getY() <= 10 && direction == dir.UP){
            snake.get(0).setY(390);
        }
        if(direction == dir.UP) {
            snake.get(0).setY(snake.get(0).getY() - 5);
        }
        if(direction == dir.DOWN) {
            snake.get(0).setY(snake.get(0).getY() + 5);
        }
        if(direction == dir.RIGHT) {
            snake.get(0).setX(snake.get(0).getX() + 5);
        }
        if(direction == dir.LEFT) {
            snake.get(0).setX(snake.get(0).getX() - 5);
        }
        render();
    }

    public void body_shift(){
        for(int i = snake.size()-1; i > 0; i--){
            snake.get(i).setX(snake.get(i-1).getX());
            snake.get(i).setY(snake.get(i-1).getY());
        }
    }

    public void render(){
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 400, 400);
        body_shift();
        for(int i = 0; i < snake.size(); i++){
            gc.setFill(Color.GREEN);
            gc.fillOval(snake.get(i).getX(),snake.get(i).getY(), 15, 15);
        }
    }

    public void create_food(int food_position_X, int food_position_Y){
        gc.setFill(Color.RED);
        gc.fillOval(food_position_X, food_position_Y, 7, 7);
    }

    public void eat_food(){
        if(food_position_X/20 == snake.get(0).getX()/20 && food_position_Y/20 == snake.get(0).getY()/20){
            food_available = false;
            snake.add(new Body(snake.get(snake.size()-1).getX()+20, snake.get(snake.size()-1).getY()+20));
        }
    }

    public boolean detect_collision(){
        for(int i = snake.size()-1; i > 5; i--){
            if(snake.get(0).getX()/5 == snake.get(i).getX()/5 && snake.get(0).getY()/5 == snake.get(i).getY()/5){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class Body{
    private int x;
    private int y;
    public Body(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
}
