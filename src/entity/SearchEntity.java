package entity;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

import main.GamePanel;

public class SearchEntity  implements EntityMovementBehaviour{
    public static List<Node> findPath(Entity entity, int findRow, int findCol, GamePanel gp) {
        Node startNode = new Node(entity.getTileNumRow(), entity.getTileNumCol());
        Node endNode = new Node(findRow, findCol);

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        List<Node> closedSet = new ArrayList<>();

        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(endNode)) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            for (Node neighbor : getNeighbors(current, gp)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                int tentativeGScore = current.getG() + 1;

                if (!openSet.contains(neighbor) || tentativeGScore < neighbor.getG()) {
                    neighbor.setParent(current);
                    neighbor.setG(tentativeGScore);
                    neighbor.setH(heuristic(neighbor, endNode));

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private static List<Node> getNeighbors(Node node, GamePanel gp) {
        List<Node> neighbors = new ArrayList<>();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

        for (int[] dir : directions) {
            int newRow = node.getRow() + dir[0];
            int newCol = node.getCol() + dir[1];

            if (isValid(newRow, newCol, gp)) {
                neighbors.add(new Node(newRow, newCol));
            }
        }

        return neighbors;
    }

    private static boolean isValid(int row, int col, GamePanel gp) {
        return row >= 0 && row < gp.maxGameRow && col >= 0 && col < gp.maxGameCol
                && gp.obj[row][col] == null && gp.tileM.houseTileNum[row][col] == 0;
    }

    private static int heuristic(Node a, Node b) {
        // Implement a simple heuristic function (Manhattan distance)
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }

    private static List<Node> reconstructPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.getParent();
        }
        Collections.reverse(path);
        return path;
    }
    public void update(Entity entity){
        int findRow = entity.findP.y;
        int findCol = entity.findP.x;
        List<Node> path = findPath(entity, findRow, findCol, entity.gp);
        
    }
}
