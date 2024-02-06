package main;

import java.util.ArrayList;
import java.util.Arrays;

import entity.EnemyType;

public enum LevelType{
    Level1(EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.DENKYUN),  // priumo mondo
    Level2(EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.DENKYUN, EnemyType.PAKUPA),
    Level3(EnemyType.PUROPEN, EnemyType.DENKYUN, EnemyType.PAKUPA, EnemyType.PAKUPA, EnemyType.PAKUPA),
    Level4(EnemyType.KNIGHT),  // primo boss
    Level5(EnemyType.DENKYUN, EnemyType.DENKYUN, EnemyType.CUPPEN),  // secondo mondo
    Level6(EnemyType.DENKYUN, EnemyType.DENKYUN, EnemyType.CUPPEN, EnemyType.PAKUPA, EnemyType.UFO),
    Level7(EnemyType.DENKYUN, EnemyType.DENKYUN, EnemyType.CUPPEN, EnemyType.CUPPEN, EnemyType.UFO, EnemyType.UFO),
    Level8(EnemyType.CLOWN),  // secondo boss
    // easy
    Level1Easy(EnemyType.PUROPEN, EnemyType.PUROPEN),  // priumo mondo
    Level2Easy(EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.DENKYUN),
    Level3Easy(EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.DENKYUN, EnemyType.PAKUPA),
    Level4Easy(EnemyType.KNIGHT),  // primo boss
    Level5Easy(EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.DENKYUN),  // secondo mondo
    Level6Easy(EnemyType.PUROPEN, EnemyType.DENKYUN, EnemyType.CUPPEN, EnemyType.UFO),
    Level7Easy(EnemyType.PUROPEN, EnemyType.DENKYUN, EnemyType.CUPPEN, EnemyType.PAKUPA, EnemyType.UFO),
    Level8Easy(EnemyType.CLOWN),  // secondo boss
    // hard
    Level1Hard(EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.DENKYUN),  // priumo mondo
    Level2Hard(EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.DENKYUN, EnemyType.DENKYUN, EnemyType.DENKYUN, EnemyType.PAKUPA),
    Level3Hard(EnemyType.DENKYUN, EnemyType.DENKYUN, EnemyType.DENKYUN, EnemyType.PAKUPA, EnemyType.PAKUPA, EnemyType.PAKUPA),
    Level4Hard(EnemyType.KNIGHT),  // primo boss
    Level5Hard(EnemyType.DENKYUN, EnemyType.DENKYUN, EnemyType.DENKYUN, EnemyType.PAKUPA, EnemyType.CUPPEN),  // secondo mondo
    Level6Hard(EnemyType.DENKYUN, EnemyType.DENKYUN, EnemyType.CUPPEN, EnemyType.CUPPEN, EnemyType.PAKUPA, EnemyType.UFO, EnemyType.UFO),
    Level7Hard(EnemyType.DENKYUN, EnemyType.DENKYUN, EnemyType.CUPPEN, EnemyType.CUPPEN, EnemyType.CUPPEN, EnemyType.PAKUPA, EnemyType.PAKUPA, EnemyType.UFO, EnemyType.UFO, EnemyType.UFO),
    Level8Hard(EnemyType.CLOWN);  // secondo boss

    ArrayList<EnemyType> enemyList;

    LevelType(EnemyType... enemies) {
        enemyList = new ArrayList<>(Arrays.asList(enemies));
    }

    public ArrayList<EnemyType> getEnemyList() {
        return enemyList;
    }
}
