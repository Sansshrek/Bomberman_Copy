package entity;

import java.awt.Color;
import java.awt.Rectangle;

public class PlayerDrawBehaviour implements EntityDrawBehaviour{
    // classe che implementa l'interfaccia EntityDrawBehaviour, che permette di disegnare il player
    public void draw(Entity entity){
        entity.image = null;
        
        if(!entity.died){
            
            entity.invincibleCheck();
            switch(entity.direction){  // in base alla direzione, la variabile image prende il valore dell'immagine inserita
                case "up":
                    entity.image = entity.imageList[0].get(entity.spriteNum);
                break;
                case "down":
                    entity.image = entity.imageList[1].get(entity.spriteNum);
                break;
                case "left":
                    entity.image = entity.imageList[2].get(entity.spriteNum);
                break;
                case "right":
                    entity.image = entity.imageList[3].get(entity.spriteNum);
                break;
            }
        }else{  // se il player è morto
            if(!entity.checkDeathFall){  // se ancora non ha toccato terra allora salta
                entity.image = entity.deathImage.get(0);
                if(entity.checkDeathJump){  // dopo aver toccato 2 blocchi di altezza
                    entity.imageP.y += 8;  // scende
                }
                else{
                    entity.imageP.y -= 8;  // altrimenti sale
                }
                if(entity.imageP.y == entity.startDeathY-entity.gp.tileSize-entity.gp.tileSize/2){  // se supera i 2 blocchi
                    entity.checkDeathJump = true;
                }
                if(entity.imageP.y == entity.startDeathY){
                    entity.checkDeathFall = true;
                    entity.spriteNum = 1;
                }
            }else{  // altrimenti se tocca terra er poro chicco sta a stira
                if(entity.spriteDeathNum == 5){
                    entity.extinguished = true;
                }
        
    
                entity.spriteCounter++;
                if(entity.spriteCounter > 10){  // ogni 15/60 volte al secondo 
                    if(entity.spriteNum == 6){
                        entity.spriteNum = 5;
                        entity.spriteDeathNum++;
                    }else
                        entity.spriteNum++;
                    
                    entity.spriteCounter = 0;  // e resetta il counter
                }
                entity.image = entity.deathImage.get(entity.spriteNum);
            }
        }
        entity.g2.drawImage(entity.image, entity.imageP.x, entity.imageP.y, entity.gp.player.hittableWidth, entity.gp.player.hittableHeight, null);  // disegna lo sprite del personaggio (image) nella posizione x,y di grandezza tileSize

        entity.notifyObservers();
        
        //da eliminare
        entity.g2.setColor(Color.BLUE);
        // entity.g2.drawRect(this.hitboxX+x, this.hitboxY+y, this.hitboxWidth, this.hitboxHeight);  
        entity.g2.drawRect(entity.hitbox.x, entity.hitbox.y, entity.hitboxWidth, entity.hitboxHeight);
        // System.out.println("x: "+entity.hitbox.x+ " y: "+entity.hitbox.y);
        /*
        entity.g2.setColor(Color.GREEN);
        // System.out.println("Tilex: "+getPlayerTileX()+ " TileY: "+getPlayerTileY());
        // System.out.println("NTilex: "+getTileNumCol()+ " NTileY: "+getTileNumRow());
        // System.out.println("x: "+entity.hitbox.x+ " y: "+entity.hitbox.y);
        // System.out.println("CentX: "+(getCenterX()/gp.tileSize)+" CentY: "+(getCenterY()/gp.tileSize)+" x: "+entity.hitbox.x+" y: "+entity.hitbox.y);
        entity.g2.drawRect(entity.getTileX(), entity.getTileY(), entity.tileSize, entity.tileSize);


        //da eliminare
        entity.g2.setColor(Color.YELLOW);
        entity.g2.drawRect(entity.gp.gameBorderLeftX, entity.gp.gameBorderUpY, 13*entity.gp.tileSize, 11*entity.gp.tileSize);
        // entity.g2.drawRect(gp.gameBorderLeftX, gp.gameBorderUpY, Math.abs(gp.gameBorderRightX - gp.gameBorderLeftX), Math.abs(gp.gameBorderDownY - gp.gameBorderUpY));

        entity.g2.setColor(Color.RED);
        entity.g2.drawRect(entity.getCenterX(), entity.getCenterY(), entity.hitboxWidth/2, entity.hitboxHeight/2);

        entity.g2.setColor(Color.RED); */
        // g2.drawRect(x, y, width, height);

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
        /*
        entity.g2.setColor(Color.PINK);
        entity.g2.draw(hitboxDwDx);
        entity.g2.setColor(Color.RED);
        entity.g2.draw(hitboxDwSx);
        entity.g2.setColor(Color.GREEN);
        entity.g2.draw(hitboxUpDx);
        entity.g2.setColor(Color.YELLOW);
        entity.g2.draw(hitboxUpSx);
        */
        entity.g2.setColor(Color.GREEN);
        entity.g2.draw(entity.hittableHitbox);
        
        entity.g2.setColor(Color.BLUE);
        entity.g2.draw(entity.hitbox);
    }

    
}