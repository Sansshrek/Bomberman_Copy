package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class KnightBoss implements EntityMovementBehaviour{
    EntityMovementBehaviour stupidBehaviour = new StupidEntity();
    boolean hitboxHitCheck = false, attackCheck = true;
    
    public void updateMovement(Entity entity){
        if(!entity.died){

            entity.projectileHandler.updateProjectiles();  // aggiorna i proiettili

            Rectangle hammerHitbox = new Rectangle(entity.hitbox.x, entity.hitbox.y, entity.hitbox.width, entity.hitbox.height*4);  // l'hitbox del martello
            // controlla se la hitbox del martello colpisce il player
            
            if(entity.startAttack)  // se inizia l'attacco allora ferma l'entity
                entity.collisionOn = true;
            if(entity.hitbox.x == entity.getTileX() && entity.hitbox.y == entity.getTileY()){
                Point findP = new Point(entity.gp.player.getTileNumCol(), entity.gp.player.getTileNumRow());  // prendo la pos del player
                ArrayList<Node> path = entity.gp.cChecker.findPath(entity, findP.y, findP.x, entity.gp);  // cerco il sentiero per arrivare al player
                entity.pathSearch = path;
                if(path.size() > 1){  // se puo muoversi verso il player
                    if(entity.pathSearch.size() > 1){
                        Node nextPos = entity.pathSearch.get(1);  // la prima posizione è quella dove si trova l'entity, la seconda è dove deve andare dopo
                        int nextRow = nextPos.getRow() - entity.getTileNumRow();
                        int nextCol = nextPos.getCol() - entity.getTileNumCol();
                        if(nextRow == -1 && nextCol == 0)  // up
                            entity.direction = "up";
                        if(nextRow == +1 && nextCol == 0)  // down
                            entity.direction = "down";
                        if(nextRow == 0 && nextCol == -1)  // left
                            entity.direction = "left";
                        if(nextRow == 0 && nextCol == +1)  // right
                            entity.direction = "right";
                    }
                    entity.notifyObservers();
                    
                }
            }
            
            if(attackCheck && !entity.canAttack){  // se non puo attaccare
                attackCheck = false;  // 
                // in parallelo dopo  secondi dalla fine dell'attacco reimpostiamo canAttack a true cosi il boss puo attaccare
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);  // crea una nuova pool di thread di una grandezza
                Runnable task = () -> {entity.canAttack = true; attackCheck = true;};  // crea una nuova funzione eseguibile che setta canAttack a true
            
                executor.schedule(task, 5, TimeUnit.SECONDS);  // esegue la task dopo 3 secondi in parallelo col programma
                executor.shutdown();  // chiude la pool di thread
            }

            if(hammerHitbox.intersects(entity.gp.player.hitbox)){  // se l'hitbox sotto l'entity interseca l'hitbox del player
                // System.out.println("Passato sotto");
                if(entity.canAttack){  // se puo attaccare inizia l'attacco
                    entity.startAttack = true;  // inizia l'attacco
                    entity.canAttack = false;  // e non può più attaccare per un po'
                }
            }
            if(entity.finishAttack){  // se ha finito l'attacco e colpisce il player
                entity.finishAttack = false;  // disattiva il controllo della fine attacco
                entity.projectileHandler.createProjectiles(entity.hitbox.x, entity.hitbox.y);  // crea i proiettili
                if(hammerHitbox.intersects(entity.gp.player.hitbox)){  // se alla fine dell'attacco il martello interseca il player
                    entity.gp.player.kill();  // allora lo uccide
                }
            }
        }
    }
}