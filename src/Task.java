import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Task {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int treesCount = Integer.valueOf(sc.nextLine());

        ArrayList<ChristmasTree> trees = new ArrayList<>();
        ArrayList<Integer> heights = new ArrayList<>();


        for(int i = 0; i < treesCount; i++){
            String[] params = sc.nextLine().split(" ");
            ChristmasTree tree = new ChristmasTree(Integer.valueOf(params[0]), Integer.valueOf(params[1]));
            trees.add(tree);
            heights.add(tree.totalHeight);
        }

        for(ChristmasTree tree : trees)
            tree.setTreeToTable();


        int commonTableWidth = 0;

        for(int i = 0; i < treesCount - 1; i++)
            commonTableWidth += trees.get(i).getSuitableColumnIndex(trees.get(i + 1).totalHeight);

        commonTableWidth += trees.get(treesCount - 1).totalWidth;

        heights.sort(Collections.reverseOrder());

        int commonTableHeight = heights.get(0);


        char[][] commonTable = new char[commonTableHeight][commonTableWidth];

        ChristmasTree firstTree = trees.get(0);
        char[][] firstTable = firstTree.getTable();

        for(int i = 0; i < firstTree.totalHeight; i++)
            for(int j = 0; j < firstTree.totalWidth; j++)
                commonTable[i][j] = firstTable[i][j];

        int rowsUsed = 0;

        for(int index = 0; index < treesCount - 1; index++){
            ChristmasTree prevTree = trees.get(index);
            ChristmasTree currentTree = trees.get(index + 1);

            int suitableColumnIndex = prevTree.getSuitableColumnIndex(currentTree.totalHeight);
            rowsUsed += suitableColumnIndex;

            for(int i = 0; i < currentTree.totalHeight; i++)
                for(int j = 0; j < currentTree.totalWidth; j++)
                    commonTable[i][j + rowsUsed] = currentTree.getTable()[i][j];
        }

        for(int i = 0; i < commonTableHeight; i++){
            for(int j = 0; j < commonTableWidth; j ++)
                if(commonTable[i][j] != '#' && commonTable[i][j] != '.')
                    commonTable[i][j] = '.';
        }

        for(char[] chars : commonTable){
            for(char c : chars)
                System.out.print(c);
            System.out.println();
        }
    }

    private static class ChristmasTree {
        private int triangleCount, smallestTriangleHeight;

        private int totalHeight, totalWidth;

        private char[][] getTable() {
            return table;
        }

        private char[][] table;

        ChristmasTree(int count, int height){
            triangleCount = count;
            smallestTriangleHeight = height;

            totalHeight = smallestTriangleHeight;
            for(int i = 1; i < triangleCount; i++)
                totalHeight += smallestTriangleHeight + i;

            totalWidth = (smallestTriangleHeight + triangleCount - 1) * 2 - 1;

            table = new char[totalHeight][totalWidth];
        }

        void setTreeToTable(){
            int highestPointX = totalWidth / 2;

            int currentTriangleHeight = smallestTriangleHeight;


            StringBuilder sb = new StringBuilder();
            ArrayList<String> lines = new ArrayList<>();


            for(int o = 0; o < triangleCount; o++){
                for (int i = 0; i < currentTriangleHeight; i++){
                    for(int j = 0; j < highestPointX - i; j++)
                        sb.append(".");

                    for(int j = 0; j < i * 2 + 1; j++)
                        sb.append("#");

                    for(int j = highestPointX + i; j < totalWidth - 1; j++)
                        sb.append(".");

                    lines.add(sb.toString());
                    sb.delete(0, sb.length());
                }

                currentTriangleHeight++;
            }

            int size = lines.size();

            for(int i = 0; i < size; i++)
                table[i] = lines.get(i).toCharArray();
        }

        int getSuitableColumnIndex(int otherTreeTotalHeight){
            for(int j = totalWidth / 2; j < totalWidth; j++){
                for(int i = 0; i < totalHeight; i++){
                    if(table[i][j] == '#')
                        break;

                    if(i == otherTreeTotalHeight)
                        return j + 1;
                }
            }

            return totalWidth;
        }
    }
}
