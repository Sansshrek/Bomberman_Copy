package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.Point;
import java.awt.Rectangle;

public class ClownBoss implements EntityMovementBehaviour{
    EntityMovementBehaviour stupidBehaviour = new StupidEntity();
    boolean hitboxHitCheck = false, switchDir = false, attackCheck = true;
    
    public void updateMovement(Entity entity){
        if(!entity.died){
            entity.projectileHandler.updateProjectiles();  // aggiorna i proiettili
            
            if(entity.startAttack){  // se inizia l'attacco allora ferma l'entity
                entity.collisionOn = true;
            }
            // utilizziamo questo switch continuo tra x e y per dare l'effetto del movimento in diagonale
            if(switchDir){  // se è attiva controlla quanto dista la x dell'entity dal player e cambia le direzioni "left" o "right"
                if(entity.hitbox.y != entity.gp.player.hitbox.y)  // se non sta nella stessa y del player
                    switchDir = false;  // cambia direzione
                if(entity.hitbox.x < entity.gp.player.hitbox.x){  // se si trova a sinistra del player
                    entity.direction = "right";  // va a destra
                }else{
                    entity.direction = "left";  // altrimenti va a sinistra
                }
            }else{  // se non è attiva controlla quanto dista la y dell'entity dal player e cambia le direzioni "up" o "down"
                if(entity.hitbox.x != entity.gp.player.hitbox.x)  // se non sta nella stessa x del player
                    switchDir = true;  // cambia direzione
                if(entity.hitbox.y < entity.gp.player.hitbox.y){  // se si trova sopra il player
                    entity.direction = "down";  // va giù
                }else{
                    entity.direction = "up";  // altrimenti va su
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
            
            if(entity.canAttack){  // se puo attaccare inizia l'attacco
                entity.projectileHandler.createProjectiles(entity.hitbox.x, entity.hitbox.y);  // crea i proiettili
                entity.canAttack = false;  // e non può più attaccare per un po'
                entity.startAttack = true;  // inizia l'attacco

            }

        }
    }
}