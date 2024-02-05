package entity;

public enum EnemyType {

    //Tipi diversi di nemici
    KNIGHT(5, 100, new KnightBoss(), new KnightDrawBehaviour(), 1, "knight", 80, 80, 80, 152, 32, 80, -1, -1, false),
    DENKYUN(2, 200, new StupidEntity(), new EnemyDrawBehaviour(), 6, "denkyun", 16, 24, 16, 24, 0, 8, -1, -1, true),
    PUROPEN(1, 100, new StupidEntity(), new EnemyDrawBehaviour(), 4, "puropen", 16, 24, 16, 24, 0, 8, -1, -1, false),
    UFO(2, 3200, new SearchEntity(), new EnemyDrawBehaviour(), 4, "ufo", 16, 16, 16, 16, 0, 0, -1, -1, true),
    PAKUPA(3, 300, new SearchBomb(), new EnemyDrawBehaviour(), 6, "pakupa", 16, 16, 16, 16, 0, 0, -1, -1, true),
    CUPPEN(1, 400, new StupidEntityRandom(), new EnemyDrawBehaviour(), 8, "cuppen", 16, 16, 16, 16, 0, 0, -1, -1, false);

    //Creazione campi per interfacce di movimento
    EntityMovementBehaviour movementBehaviour;
    EntityDrawBehaviour drawBehaviour;
    String type; //Tipo di nemico
    int width, height, imageWidth, imageHeight, startingLives, startingScore, maxSpriteNum, offsetX, offsetY, startX, startY;  //Proprieta da decidere a seconda del tipo di nemico
    boolean reverseAnimation;  //Utilizzo un controllo per eseguire a specchio le animazioni

    EnemyType(int startingLives, int startingScore, EntityMovementBehaviour movementBehaviour, EntityDrawBehaviour drawBehaviour, int maxSpriteNum, String type, int width, int height, int imageWidth, int imageHeight, int offsetX, int offsetY, int startX, int startY, boolean reverseAnimation) {
        this.startingLives = startingLives;
        this.startingScore = startingScore;
        this.movementBehaviour = movementBehaviour;
        this.drawBehaviour = drawBehaviour;
        this.maxSpriteNum = maxSpriteNum;
        this.type = type;
        this.width = width;
        this.height = height;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.startX = startX;
        this.startY = startY;
        this.reverseAnimation = reverseAnimation;
    }
}