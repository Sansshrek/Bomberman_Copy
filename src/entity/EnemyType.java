package entity;

public enum EnemyType {

    //Tipi diversi di nemici
    KNIGHT(10, 1000, 1, new KnightBoss(), new KnightDrawBehaviour(), 1, "knight", 80, 80, 80, 152, 32, 80, 0, 24, 5, 6, false, false),
    CLOWN(10, 1000, 1, new ClownBoss(), new ClownDrawBehaviour(), 1, "clown", 110, 80, 110, 100, 48, 48, 0, 0, 5, 6, false, true),
    DENKYUN(2, 200, 1, new StupidEntity(), new EnemyDrawBehaviour(), 6, "denkyun", 16, 24, 16, 24, 0, 8, 0, 0, -1, -1, true, false),
    PUROPEN(1, 100, 1, new StupidEntity(), new EnemyDrawBehaviour(), 4, "puropen", 16, 24, 16, 24, 0, 8, 0, 0, -1, -1, false, false),
    UFO(2, 3200, 1, new SearchEntity(), new EnemyDrawBehaviour(), 4, "ufo", 16, 16, 16, 16, 0, 0, 0, 0, -1, -1, true, false),
    PAKUPA(3, 300, 1, new SearchBomb(), new EnemyDrawBehaviour(), 6, "pakupa", 16, 16, 16, 16, 0, 0, 0, 0, -1, -1, true, false),
    CUPPEN(1, 400, 1, new StupidEntityRandom(), new EnemyDrawBehaviour(), 8, "cuppen", 16, 16, 16, 16, 0, 0, 0, 0, -1, -1, false, true);

    //Creazione campi per interfacce di movimento
    EntityMovementBehaviour movementBehaviour;
    EntityDrawBehaviour drawBehaviour;
    String type; //Tipo di nemico
    int hittableWidth, hittableHeight, imageWidth, imageHeight, startingLives, startingScore, maxSpriteNum, offsetImageX, offsetImageY, offsetHittableX, offsetHittableY, startRow, startCol, speed;  //Proprieta da decidere a seconda del tipo di nemico
    boolean reverseAnimation, blockCross;  //Utilizzo un controllo per eseguire a specchio le animazioni

    EnemyType(int startingLives, int startingScore, int speed, EntityMovementBehaviour movementBehaviour, EntityDrawBehaviour drawBehaviour, int maxSpriteNum, String type, int hittableWidth, int hittableHeight, int imageWidth, int imageHeight, int offsetImageX, int offsetImageY, int offsetHittableX, int offsetHittableY, int startRow, int startCol, boolean reverseAnimation, boolean blockCross) {
        this.startingLives = startingLives;
        this.startingScore = startingScore;
        this.speed = speed;
        this.movementBehaviour = movementBehaviour;
        this.drawBehaviour = drawBehaviour;
        this.maxSpriteNum = maxSpriteNum;
        this.type = type;
        this.hittableWidth = hittableWidth;
        this.hittableHeight = hittableHeight;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.offsetImageX = offsetImageX;
        this.offsetImageY = offsetImageY;
        this.offsetHittableX = offsetHittableX;
        this.offsetHittableY = offsetHittableY;
        this.startRow = startRow;
        this.startCol = startCol;
        this.reverseAnimation = reverseAnimation;
        this.blockCross = blockCross;
    }
}