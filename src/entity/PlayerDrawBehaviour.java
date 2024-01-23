package entity;

import java.awt.Color;

public class PlayerDrawBehaviour implements EntityDrawBehaviour{

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
                if(entity.spriteDeathNum == 10){
                    entity.extinguished = true;
                }
        
    
                entity.spriteCounter++;
                if(entity.spriteCounter > 10){  // ogni 15/60 volte al secondo 
                    switch(entity.spriteNum){
                        case 1:
                            entity.spriteNum++; 
                            break;
                        case 2: 
                            entity.spriteNum++;
                            break;
                        case 3: 
                            entity.spriteNum++;
                            break;
                        case 4:
                            entity.spriteNum++;
                            break;
                        case 5:
                            entity.spriteNum++;
                            entity.spriteDeathNum++;
                            break;
                        case 6:
                            entity.spriteNum = 5;  // fa un loop nelle ultime due animazioni
                            entity.spriteDeathNum++;
                            break;
                        }
                        entity.spriteCounter = 0;  // e resetta il counter
                }
                entity.image = entity.deathImage.get(entity.spriteNum);
            }
        }
        entity.g2.drawImage(entity.image, entity.imageP.x, entity.imageP.y, entity.gp.player.width, entity.gp.player.height, null);  // disegna lo sprite del personaggio (image) nella posizione x,y di grandezza tileSize

        entity.notifyObservers();
        
        //da eliminare
        entity.g2.setColor(Color.BLUE);
        // entity.g2.drawRect(this.hitboxX+x, this.hitboxY+y, this.hitboxWidth, this.hitboxHeight);  
        entity.g2.drawRect(entity.hitbox.x, entity.hitbox.y, entity.hitboxWidth, entity.hitboxHeight);
        // System.out.println("x: "+entity.hitbox.x+ " y: "+entity.hitbox.y);

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

        entity.g2.setColor(Color.RED);
        // g2.drawRect(x, y, width, height);
    }

    
}