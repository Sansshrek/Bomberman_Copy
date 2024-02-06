package main;

import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioManager {
    Clip clip;
    URL soundURL[] = new URL[20];

    public AudioManager(){  // salviamo nell'array di URL il percorso dei file audio
        soundURL[0] = getClass().getResource("../sfx/World 1 Theme.wav");
        soundURL[1] = getClass().getResource("../sfx/Stage Intro.wav");
        soundURL[2] = getClass().getResource("../sfx/Stage Clear.wav");
        soundURL[3] = getClass().getResource("../sfx/Pause.wav");
        soundURL[4] = getClass().getResource("../sfx/Walking 1.wav");
        soundURL[5] = getClass().getResource("../sfx/Place Bomb.wav");
        soundURL[6] = getClass().getResource("../sfx/Bomberman Dies.wav");
        soundURL[7] = getClass().getResource("../sfx/Item get.wav");
        soundURL[8] = getClass().getResource("../sfx/Enemy Dies.wav");
        soundURL[9] = getClass().getResource("../sfx/Bomb Explodes.wav");
        soundURL[10] = getClass().getResource("../sfx/Knight Hammer.wav");
        soundURL[11] = getClass().getResource("../sfx/Title Screen Select.wav");
        soundURL[12] = getClass().getResource("../sfx/Title Screen Cursor.wav");
        soundURL[13] = getClass().getResource("../sfx/Menu Theme.wav");
        soundURL[14] = getClass().getResource("../sfx/World 2 Theme.wav");
        soundURL[15] = getClass().getResource("../sfx/Boss Theme.wav");
        soundURL[16] = getClass().getResource("../sfx/Win Theme.wav");
    }

    public void setAudio(int i){  // sceglie quale audio far partire in base all'index
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);  // prende il file audio
            clip = AudioSystem.getClip();  // crea un clip audio per eseguire l'audio
            clip.open(ais);  // apre il file audio
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(){  // fa partire l'audio
        clip.start();
    }
    public void loop(){  // imposta la clip in loop che si ripete finchè non viene stopata
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){  // ferma l'audio
        clip.stop();
    }
}
