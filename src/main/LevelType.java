package main;

import java.util.ArrayList;
import java.util.Arrays;

import entity.EnemyType;

public enum LevelType{
    Level1(EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.DENKYUN),
    Level2(EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.PAKUPA, EnemyType.PAKUPA),
    Level3(EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.PAKUPA, EnemyType.PAKUPA, EnemyType.UFO, EnemyType.UFO),
    // easy
    Level1Easy(EnemyType.PUROPEN, EnemyType.PUROPEN),
    Level2Easy(EnemyType.PUROPEN, EnemyType.DENKYUN, EnemyType.PAKUPA),
    Level3Easy(EnemyType.PUROPEN, EnemyType.DENKYUN, EnemyType.PAKUPA, EnemyType.UFO),
    // hard
    Level1Hard(EnemyType.DENKYUN, EnemyType.DENKYUN, EnemyType.PUROPEN, EnemyType.PUROPEN),
    Level2Hard(EnemyType.PUROPEN, EnemyType.DENKYUN, EnemyType.DENKYUN, EnemyType.PAKUPA, EnemyType.PAKUPA, EnemyType.UFO),
    Level3Hard(EnemyType.DENKYUN, EnemyType.DENKYUN, EnemyType.PAKUPA, EnemyType.PAKUPA, EnemyType.UFO, EnemyType.UFO);

    ArrayList<EnemyType> enemyList;

    LevelType(EnemyType... enemies) {
        enemyList = new ArrayList<>(Arrays.asList(enemies));
    }

    public ArrayList<EnemyType> getEnemyList(String gameDifficulty) {
        return enemyList;
    }
}
