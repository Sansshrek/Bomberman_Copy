package entity;

public class SearchEntity  implements EntityMovementBehaviour{
    int findRow, findCol;
    public int heuristic(int row, int col){
        return Math.abs(findRow - row) + Math.abs(findCol - col);
    }
    public void update(Entity entity){
        int entRow = entity.getTileNumRow();
        int entCol = entity.getTileNumCol();
        findRow = entity.findP.y;
        findCol = entity.findP.x;
        
    }
}
