package entity;

public enum EnemyType {
    PUROPEN(1, 100, new StupidEntity(), 4, "puropen", 16, 24, 0, 8, -1, -1, false),
    UFO(2, 3200, new SearchEntity(), 4, "ufo", 16, 16, 0, 0, -1, -1, true),
    PAKUPA(3, 300, new SearchBomb(), 6, "pakupa", 16, 16, 0, 0, -1, -1, true),
    CUPPEN(1, 400, new StupidEntityRandom(), 8, "cuppen", 16, 16, 0, 0, -1, -1, false);

    public final EntityMovementBehaviour movementBehaviour;
    String type;
    int width, height, startingLives, startingScore, maxSpriteNum, offsetX, offsetY, startX, startY;
    boolean reverseAnimation;

    EnemyType(int startingLives, int startingScore, EntityMovementBehaviour movementBehaviour, int maxSpriteNum, String type, int width, int height, int offsetX, int offsetY, int startX, int startY, boolean reverseAnimation) {
        this.startingLives = startingLives;
        this.startingScore = startingScore;
        this.movementBehaviour = movementBehaviour;
        this.maxSpriteNum = maxSpriteNum;
        this.type = type;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.startX = startX;
        this.startY = startY;
        this.reverseAnimation = reverseAnimation;
    }
}