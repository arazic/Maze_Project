package View;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Optional;

public class SolvedController {

    public void successTosolvedMaze(){
       // String musicFile = "C:\\Users\\user\\Documents\\לימודים\\חומרי לימוד\\סמסטר ד\\נושאים מתקדמים בתכנות\\עבודות\\חלק ג\\13.6.18\\1600\\ATP_PART3\\src\\MyViewController\\WIN.mp3";     // For example
        String musicFile = "src/View/WIN.mp3";     // For example
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Maze solved");
        alert.setHeaderText("You are the champion!!");
        alert.setContentText("Congratulation! \n You did it! ");
        alert.showAndWait();
        alert.close();
    }

}
