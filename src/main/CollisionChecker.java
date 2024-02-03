package main;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import entity.Enemy;
import entity.Entity;
import entity.EntityMovementBehaviour;
import entity.EntityObserver;
import entity.Node;
import entity.Player;
import entity.SearchEntity;
import entity.StupidEntity;
import objects.Block;
import objects.Bomb;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import java.util.concurrent.ConcurrentHashMap;


import java.util.concurrent.ConcurrentHashMap;

public class CollisionChecker implements EntityObserver{
    GamePanel gp;
    int boundaryX, boundaryY, boundaryWidth, boundaryHeight;
    private ConcurrentHashMap<Integer, Entity> entityMap;
    
    public CollisionChecker(GamePanel gp){
        this.entityMap = new ConcurrentHashMap<>();
        this.gp = gp;
        boundaryX = 0;
        boundaryY = 0;
        boundaryWidth = 13*gp.tileSize;
        boundaryHeight = 11*gp.tileSize;
    }

    public void updateEntity(Entity entity){
        entityMap.put(entity.uniCode, entity);
    }
    public void removeEntity(int uniCode){
        entityMap.remove(uniCode);
    }

    public void checkEntity(){
        for (Map.Entry<Integer, Entity> entry : entityMap.entrySet()) {
            Entity entity = entry.getValue();
            entity.collisionOn = false;
            // imposta il collisionOn nelle prossime funzioni
            checkTile(entity);
            Point objPoint = checkObj(entity);
            if(!(entity instanceof Player)){  // se è un nemico
                if(!entityMap.get(0).died){
                    checkPlayerCollision(entity, entityMap.get(0));  // controlla se colpisce il player
                }
            }else{  // se è il player
                entity.powerUpHandler(objPoint);  // fa il controllo del powerUp preso in quel blocco
            }
            entity.notifyObservers();
        }
    }

    public boolean canMove(Entity entity){
        if(validDirections(entity).size() > 0){  // se la lista di direzioni in cui si puo muovere non è vuota
            return true;  // allora si puo muovere
        }else
            return false;
    }
    
    public void checkTile(Entity entity){
        int tileCol = entity.getTileNumCol();
        int tileRow = entity.getTileNumRow();
        int hitboxCenterX = entity.getCenterX();  // prendiamo il centro del player
        int hitboxCenterY = entity.getCenterY();
        int hitboxWidthHalf = entity.hitboxWidth/2;  // i due valori di altezza e larghezza della hitbox dimezzati
        int hitboxHeightHalf = entity.hitboxHeight/2;
        Rectangle hitboxUpSx = new Rectangle(entity.hitbox.x, entity.hitbox.y, hitboxWidthHalf, hitboxHeightHalf);  // dividiamo in 4 sezioni la hitbox del player
        Rectangle hitboxUpDx = new Rectangle(hitboxCenterX, entity.hitbox.y, hitboxWidthHalf, hitboxHeightHalf);
        Rectangle hitboxDwSx = new Rectangle(entity.hitbox.x, hitboxCenterY, hitboxWidthHalf, hitboxHeightHalf);
        Rectangle hitboxDwDx = new Rectangle(hitboxCenterX, hitboxCenterY, hitboxWidthHalf, hitboxHeightHalf);
        Rectangle hitboxCheck1 = new Rectangle(0, 0, hitboxWidthHalf, hitboxHeightHalf);  // creiamo due hitbox temporanee che variano in base alla direzione(es. direction="down", usiamo hitboxDwSx e hitboxDwDx come base)
        Rectangle hitboxCheck2 = new Rectangle(0, 0, hitboxWidthHalf, hitboxHeightHalf);
        Rectangle wallCheck1 = null, wallCheck2 = null, wallCheckCx = null;  // impostiamo i valori di default
        switch(entity.direction){
            case "up":
                if((entity.hitbox.y - entity.speed) >= gp.gameBorderUpY){  // se non supera il bordo di gioco
                    if(tileRow > 0){  // nella prima riga non serve controllare se sopra c'è un blocco
                        hitboxCheck1.x = hitboxUpSx.x;  // imposta la hitbox delle due hitbox da controllare (in alto a sinistra/destra) nella pos in cui si deve spostare
                        hitboxCheck1.y = (int)(hitboxUpSx.y - entity.speed);
                        hitboxCheck2.x = hitboxUpDx.x;
                        hitboxCheck2.y = (int)(hitboxUpDx.y - entity.speed);
                        if(tileCol-1 >= 0)  // se la pos del blocco a sinistra non è fuori dalla mappa
                            wallCheck1 = gp.tileM.houseHitbox[tileRow-1][tileCol-1];  // carichiamo l'hitbox del blocco in alto a sinistra
                        if(tileCol+1 < 13)  // se la pos del blocco a destra non è fuori dalla mappa
                            wallCheck2 = gp.tileM.houseHitbox[tileRow-1][tileCol+1];  // carichiamo l'hitbox del blocco in alto a destra
                        wallCheckCx = gp.tileM.houseHitbox[tileRow-1][tileCol];
                    }
                }else // se supera il bordo
                    entity.collisionOn = true;
            break;
            case "down":
                if((entity.hitbox.y + entity.hitboxHeight + entity.speed) <= gp.gameBorderDownY){  // se non supera il bordo di gioco
                    if(tileRow < 10){  // nell'ultima riga non serve controllare se sopra c'è un blocco
                        hitboxCheck1.x = hitboxDwSx.x;  // imposta la hitbox delle due hitbox da controllare (in basso a sinistra/destra) nella pos in cui si deve spostare
                        hitboxCheck1.y = (int)(hitboxDwSx.y + entity.speed);
                        hitboxCheck2.x = hitboxDwDx.x;
                        hitboxCheck2.y = (int)(hitboxDwDx.y + entity.speed);
                        // prendiamo i 3 muri in alto all'entity (se per esempio siamo in tile (3,5) prende le hitbox dei muri in basso a sinistra(4,4), basso in centro (4,5) e basso a destra (4,6))
                        if(tileCol-1 >= 0)  // se la pos del blocco a sinistra non è fuori dalla mappa
                            wallCheck1 = gp.tileM.houseHitbox[tileRow+1][tileCol-1];  // carichiamo l'hitbox del blocco in basso a sinistra
                        if(tileCol+1 < 13)  // se la pos del blocco a destra non è fuori dalla mappa
                            wallCheck2 = gp.tileM.houseHitbox[tileRow+1][tileCol+1];  // carichiamo l'hitbox del blocco in alto a sinistra
                        wallCheckCx = gp.tileM.houseHitbox[tileRow+1][tileCol];
                    }
                }else
                    entity.collisionOn = true;
            break;
            case "left":
                if((entity.hitbox.x - entity.speed) >= gp.gameBorderLeftX){  // se non supera il bordo di gioco
                    if(tileCol > 0){  // nella prima colonna non serve controllare se sopra c'è un blocco
                        hitboxCheck1.x = (int)(hitboxUpSx.x - entity.speed);  // imposta la hitbox delle due hitbox da controllare (in alto/basso a sinistra) nella pos in cui si deve spostare
                        hitboxCheck1.y = hitboxUpSx.y;
                        hitboxCheck2.x = (int)(hitboxDwSx.x - entity.speed);
                        hitboxCheck2.y = hitboxDwSx.y;
                        if(tileRow-1 >= 0)  // se la pos del blocco sopra non è fuori dalla mappa
                            wallCheck1 = gp.tileM.houseHitbox[tileRow-1][tileCol-1];  // carichiamo l'hitbox del blocco in alto a sinistra
                        if(tileRow+1 < 11)  // se la pos del blocco sotto non è fuori dalla mappa
                            wallCheck2 = gp.tileM.houseHitbox[tileRow+1][tileCol-1];  // carichiamo l'hitbox del blocco in basso a sinistra
                        wallCheckCx = gp.tileM.houseHitbox[tileRow][tileCol-1];
                    }
                }else
                    entity.collisionOn = true;
            break;
            case "right":
                if((entity.hitbox.x + entity.hitboxWidth + entity.speed) <= gp.gameBorderRightX){  // se non supera il bordo di gioco
                    if(tileCol < 12){  // nell'ultima colonna non serve controllare se sopra c'è un blocco
                        hitboxCheck1.x = (int)(hitboxUpDx.x + entity.speed);  // imposta la hitbox delle due hitbox da controllare (in alto/basso a destra) nella pos in cui si deve spostare
                        hitboxCheck1.y = hitboxUpDx.y;
                        hitboxCheck2.x = (int)(hitboxDwDx.x + entity.speed);
                        hitboxCheck2.y = hitboxDwDx.y;
                        if(tileRow-1 >= 0)  // se la pos del blocco sopra non è fuori dalla mappa
                            wallCheck1 = gp.tileM.houseHitbox[tileRow-1][tileCol+1];  // carichiamo l'hitbox del blocco in alto a destra
                        if(tileRow+1 < 11)  // se la pos del blocco sotto non è fuori dalla mappa
                            wallCheck2 = gp.tileM.houseHitbox[tileRow+1][tileCol+1];  // carichiamo l'hitbox del blocco in basso a destra
                        wallCheckCx = gp.tileM.houseHitbox[tileRow][tileCol+1];
                    }
                }else
                    entity.collisionOn = true;
            break;
        }
        // controllo delle hitbox
        if(entity instanceof Player){
            if(wallCheckCx != null && (hitboxCheck1.intersects(wallCheckCx) || hitboxCheck2.intersects(wallCheckCx))){  // se entrambe le hitbox intercettano il blocco al centro
                entity.collisionOn = true;  // allora l'hitbox dell'entity colpisce interamente il blocco
            }
            else if(wallCheck1 != null && hitboxCheck1.intersects(wallCheck1)){  // se solo l'hitbox1 intercetta il blocco in wallCheck1
                if(wallCheckCx == null || !hitboxCheck2.intersects(wallCheckCx)){  // se l'hitbox2 non intercetta il blocco al centro
                    if(entity.direction == "up" || entity.direction == "down"){  // se la dir è verso l'alto/basso sposta verso destra
                        entity.imageP.x += entity.speed;
                        entity.hitbox.x += entity.speed;
                    }else{  // se la dir è verso destra/sinistra sposta verso il basso
                        entity.imageP.y += entity.speed;
                        entity.hitbox.y += entity.speed;
                    }
                }else
                    entity.collisionOn = true;  // altrimenti non passa
            }else if(wallCheck2 != null && hitboxCheck2.intersects(wallCheck2)){  // se solo l'hitbox2 intercetta il blocco in wallCheck2
                if(wallCheckCx == null || !hitboxCheck1.intersects(wallCheckCx)){  // se l'hitbox1 non intercetta il blocco al centro
                    if(entity.direction == "up" || entity.direction == "down"){  // se la dir è verso l'alto/basso sposta verso sinistra
                        entity.imageP.x -= entity.speed;
                        entity.hitbox.x -= entity.speed;
                    }else{  // se la dir è verso l'alto/basso sposta verso l'alto
                        entity.imageP.y -= entity.speed;
                        entity.hitbox.y -= entity.speed;
                    }
                }else
                    entity.collisionOn = true;
            }
        }
        if (entity instanceof Enemy){  // controllo enemy
            if(wallCheckCx != null && (hitboxCheck1.intersects(wallCheckCx) || hitboxCheck2.intersects(wallCheckCx))){  // se entrambe le hitbox intercettano il blocco al centro
                entity.collisionOn = true;  // allora l'hitbox dell'entity colpisce interamente il blocco
            }
        }
        entity.notifyObservers();
    }
    
    public void checkPlayerCollision(Entity enemy, Entity player) {
        // se il check del player è falso vuol dire che l'enemy non ha colpito il player
        // Controlla se l'hitbox del nemico interseca l'hitbox del giocatore
        if (enemy.hitbox.intersects(player.hitbox) && !player.invulnerable) {  // se colpisce l'hitbox del player e non è invulnerabile
            // Se il nemico sta andando a destra e il giocatore è alla sua destra, ferma il nemico
            enemy.collisionOn = true;
            System.out.println("Colpito player");
            player.kill();
            // appena l'enemy colpisce il player, attiva la var. bool playerCollision nell'oggetto enemy
            // cosi che la funzione non viene chiamata finche playerCollision non torna false dopo che finisce il timer in enemy
        }
    }
 
    public Point checkObj(Entity entity){
        for(int row=0; row<gp.maxGameRow; row++){
            for(int col=0; col<gp.maxGameCol; col++){
                if(gp.obj[row][col] != null){  //se non è null
                    Rectangle entityHitboxCheck = new Rectangle(entity.hitbox.x, entity.hitbox.y, entity.hitboxWidth, entity.hitboxHeight);
                    Rectangle objHitboxCheck = new Rectangle(gp.obj[row][col].hitbox.x, gp.obj[row][col].hitbox.y, gp.tileSize, gp.tileSize);

                    switch(entity.direction){
                        case "up":
                            entityHitboxCheck.y -= entity.speed;
                        break;
                        case "down":
                            entityHitboxCheck.y += entity.speed;
                        break;
                        case "left":
                            entityHitboxCheck.x -= entity.speed;
                        break;
                        case "right":
                            entityHitboxCheck.x += entity.speed;
                        break;
                    }
                    if(!(gp.obj[row][col] instanceof Bomb)){
                        if(entityHitboxCheck.intersects(objHitboxCheck)){  // controlla se l'hitbox del player interseca l'hitbox dell'oggetto
                            if(gp.obj[row][col].collision){  // se puo essere scontrato setta la collisione del player a true
                                if(entity.type != "cuppen"){
                                    entity.collisionOn = true;
                                }
                            }
                            if(entity instanceof Player){ // se è il player a toccare l'oggetto allora ritorna l'indice
                                return new Point(col, row);
                            }
                        }
                    }else{
                        checkBomb(entity);
                    }
                    objHitboxCheck = null; // viene eliminato per lasciare spazio alla memoria
                    entityHitboxCheck = null; // viene eliminato per lasciare spazio alla memoria
                }
            }
        }
        entity.notifyObservers();
        return new Point(999, 999);  // punto default
        
    }
    
    public void checkBomb(Entity entity){  // FATTO DA FRANCESCO PAGLIACCIA™
    
        Rectangle entityHitboxCheck = new Rectangle(entity.hitbox.x, entity.hitbox.y, entity.hitboxWidth, entity.hitboxHeight);
        switch(entity.direction){
            case "up":
                entityHitboxCheck.y -= entity.speed;
            break;
            case "down":
                entityHitboxCheck.y += entity.speed;
            break;
            case "left":
                entityHitboxCheck.x -= entity.speed;
            break;
            case "right":
                entityHitboxCheck.x += entity.speed;
            break;
        }
        Bomb removeBomb = null;  // check se la bomba viene mangiata dal pakupa
        for(Bomb bomb: gp.bombH.bombs){
            if(!bomb.exploded){
                if(entityHitboxCheck.intersects(bomb.hitbox)){
                    if(bomb.exitEntity.contains(entity)){  // controlla se è gia uscito l'entita
                        if(entity.type != "pakupa"){  // se non è un pakupa che mangia la bomba
                            entity.collisionOn = true;  // allora collide col blocco
                        }else{  // altrimenti lo mangia
                            if(entity.getTileNumCol() == bomb.tileCol && entity.getTileNumRow() == bomb.tileRow)  // se l'entity si trova sullo stesso tile della bomba allora la elimina
                                removeBomb = bomb;
                            // si poteva fare anche senza il controllo quindi solo quando l'entity interseca la bomba ma è piu visibilmente carino cosi che il l'entity si trova a meta sulla bomba e la elimina
                        }
                    }
                }else{
                    bomb.exitEntity.add(entity);
                }
            }else{
                entity.bombExitHitbox = false;
            }
        }
        if(removeBomb != null)  // se la bomba è stata mangiata da un pakupa
            gp.bombH.removeBomb(removeBomb);
    }

    public void checkBomb3(Entity entity){  // FATTO DA FRANCESCO PAGLIACCIA™
    
        Rectangle entityHitboxCheck = new Rectangle(entity.hitbox.x, entity.hitbox.y, entity.hitboxWidth, entity.hitboxHeight);
        switch(entity.direction){
            case "up":
                entityHitboxCheck.y -= entity.speed;
            break;
            case "down":
                entityHitboxCheck.y += entity.speed;
            break;
            case "left":
                entityHitboxCheck.x -= entity.speed;
            break;
            case "right":
                entityHitboxCheck.x += entity.speed;
            break;
        }
        for(Bomb bomb: gp.bombH.bombs){
            if(!bomb.exploded){
                for(Map.Entry<Entity, Boolean> EntityMap: bomb.hashExitEntity.entrySet()){
                    if(entityHitboxCheck.intersects(bomb.hitbox)){
                        if(EntityMap.getValue()){  // controlla se è gia uscito
                            EntityMap.getKey().collisionOn = true;
                        }
                    }else{
                        bomb.hashExitEntity.put(EntityMap.getKey(), true);
                    }
                }
            }else{
                entity.bombExitHitbox = false;
            }
        }
    }

    
    public ArrayList<Node> findPath(Entity entity, int findRow, int findCol, GamePanel gp) {
        String entityType = entity.type;
        Node startNode = new Node(entity.getTileNumRow(), entity.getTileNumCol());  // posizione di partenza (nel caso dell'enemy UFO per esempio è da dove parte l'UFO)
        Node endNode = new Node(findRow, findCol);  // posizione di arrivo (sempre nel caso di UFO è dove sta il Player)

        PriorityQueue<Node> openWay = new PriorityQueue<>();
        ArrayList<Node> closedWay = new ArrayList<>();

        openWay.add(startNode);

        while (!openWay.isEmpty()) {  // se la lista di nodi non è vuota
            Node current = openWay.poll();  // prendo il primo nodo della lista da controllare e lo tolgo dalla lista

            if (current.equals(endNode)) {  // se quel nodo è la posizione dove dobbiamo arrivare
                return reconstructPath(current);  // ricostruisci il sentiero
            }

            closedWay.add(current);  // aggiungo il nodo corrente a quelli che abbiamo gia controllato

            for (Node neighbor: getNeighbors(current, gp, entityType)) {  // per ogni nodo vicino in cui si puo muovere
                if(closedWay.contains(neighbor)) {  // se è gia nella lista di quelli controllati
                    continue;  // vai avanti
                }

                int tentativeGScore = current.getG() + 1;  // numero di passi fatti dalla partenza rispetto a quello prima

                if (!openWay.contains(neighbor) || tentativeGScore < neighbor.getG()) {  // se il nodo vicino non è nella lista di quelli da controllare o i suoi passi
                    neighbor.setParent(current);  // settiamo il nodo corrente come parente di quello vicino 
                    neighbor.setG(tentativeGScore);  // impostiamo i passi fatti dal nodo corrente
                    neighbor.setH(heuristic(neighbor, endNode));  // impostiamo la distanza in linea d'aria dal nodo vicino al nodo in cui dobbiamo arrivare

                    if (!openWay.contains(neighbor)) {  // se il nodo vicino non è nella lista di quelli da controllare
                        openWay.add(neighbor);  // lo aggiungiamo per controllarlo dopo
                    }
                }
            }
        }

        return new ArrayList<>(); // No path found
    }

    private ArrayList<Node> getNeighbors(Node node, GamePanel gp, String entityType) {
        ArrayList<Node> neighbors = new ArrayList<>();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

        for (int[] dir : directions) {  // per ogni direzione
            int newRow = node.getRow() + dir[0];  // calcoliamo dove si muove in base alla direzione
            int newCol = node.getCol() + dir[1];

            if (isValid(newRow, newCol, gp, entityType)) {  // se quella posizione sulla mappa è disponibile
                neighbors.add(new Node(newRow, newCol));  // lo aggiungiamo alla lista dei nodi vicini
            }
        }

        return neighbors;
    }

    private int heuristic(Node a, Node b) {
        // calcola la distanza in linea d'aria tra due punti
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }

    private ArrayList<Node> reconstructPath(Node node) {  // ricostruisce in base a tutti i nodi parente di ogni nodo il sentiero da fare
        ArrayList<Node> path = new ArrayList<>();
        while (node != null) {  // finche non siamo arrivati all'ultimo nodo (che sarà quello di partenza)
            path.add(node);  // aggiungiamo il nodo corrente alla lista dei nodi del sentiero
            node = node.getParent();  // imposta il nodo parente per il prossimo da iterare
        }
        Collections.reverse(path);  // inverte la lista (poiche partiamo dal nodo di arrivo fino al nodo di partenza)
        return path;
    }
    
    private boolean isValid(int row, int col, GamePanel gp, String entityType) {
        // se è dentro la mappa e non è un blocco/palazzo allora torna true
        if(entityType != "pakupa" && entityType != "cuppen" && entityType != "player"){  // se non è il pakupa/cuppen/player faccio il controllo con tutti i tipi di blocchi
            return row >= 0 && row < gp.maxGameRow && col >= 0 && col < gp.maxGameCol
                && !(gp.obj[row][col] instanceof Block) && !(gp.obj[row][col] instanceof Bomb) && gp.tileM.houseTileNum[row][col] != 3 && gp.tileM.houseTileNum[row][col] == 0;
        }else if(entityType == "pakupa") {  // se è il pakupa fa il controllo senza le bombe
            return row >= 0 && row < gp.maxGameRow && col >= 0 && col < gp.maxGameCol
                && !(gp.obj[row][col] instanceof Block) && gp.tileM.houseTileNum[row][col] != 3;
        }else if(entityType == "cuppen"){  // se è il cuppen fa il controllo senza i blocchi
            return row >= 0 && row < gp.maxGameRow && col >= 0 && col < gp.maxGameCol
                && !(gp.obj[row][col] instanceof Bomb) && gp.tileM.houseTileNum[row][col] != 3 && gp.tileM.houseTileNum[row][col] == 0;
        }else{  // se è il player con mouseBehaviour allora controlla solo che non esce dalla mappa soltanto
            return row >= 0 && row < gp.maxGameRow && col >= 0 && col < gp.maxGameCol
                && !(gp.obj[row][col] instanceof Block) && !(gp.obj[row][col] instanceof Bomb) && gp.tileM.houseTileNum[row][col] != 3 && gp.tileM.houseTileNum[row][col] == 0;
            // da rivedere, per ora si comporta solo come un enemy normale (non cuppen/pakupa)
        }
    }

    public ArrayList<String> validDirections(Entity entity){
        ArrayList<String> directions = new ArrayList<>();
        int tileCol = entity.getTileNumCol();
        int tileRow = entity.getTileNumRow();
        // per ogni posizione vicina a quella dell'entita, se è valida la aggiunge alla lista
        if(isValid(tileRow-1, tileCol, gp, entity.type)){
            directions.add("up");
        }
        if(isValid(tileRow+1, tileCol, gp, entity.type)){
            directions.add("down");
        }
        if(isValid(tileRow, tileCol-1, gp, entity.type)){
            directions.add("left");
        }
        if(isValid(tileRow, tileCol+1, gp, entity.type)){
            directions.add("right");
        }
        return directions;
    }

}
