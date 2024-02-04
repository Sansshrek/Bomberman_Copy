package main;

import java.util.ArrayList;
import java.util.Arrays;

import entity.EnemyType;

public enum LevelType{
    LEVEL1(EnemyType.CUPPEN, EnemyType.PUROPEN, EnemyType.PUROPEN),
    LEVEL2(EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.PAKUPA, EnemyType.PAKUPA),
    LEVEL3(EnemyType.PUROPEN, EnemyType.PUROPEN, EnemyType.PAKUPA, EnemyType.PAKUPA, EnemyType.UFO, EnemyType.UFO);

    ArrayList<EnemyType> enemyList;

    LevelType(EnemyType... enemies) {
        enemyList = new ArrayList<>(Arrays.asList(enemies));
    }

    public ArrayList<EnemyType> getEnemyList(String gameDifficulty) {
        return enemyList;
    }
}
