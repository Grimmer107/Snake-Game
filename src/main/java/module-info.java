module sample.snake_game {
    requires javafx.controls;
    requires javafx.fxml;


    opens sample.snake_game to javafx.fxml;
    exports sample.snake_game;
}