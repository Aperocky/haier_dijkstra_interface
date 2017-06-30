/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.parse;

import model.map.Map;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Parse the txt file in resources and obtain dangerous blocks
 * @author Feng
 */
public class ParseService {
    private Scanner mScanner;
    public ParseService(){
        
    }
    
    public int[][] getDangers(String file){
        mScanner = new Scanner(this.getClass().getClassLoader().getResourceAsStream(file));
        mScanner.useDelimiter("");
        return getDangersFromScanner(mScanner);
    }
    
    private int[][] getDangersFromScanner(Scanner scanner) {
        int[][] dangers;
        ArrayList<int[]> Dangers = new ArrayList<>();
        int counter = -1;
        while (scanner.hasNext()) {
            String next = scanner.next();
            if (next.equals(".")) {
                counter++;
            } else if (next.equals("0")) {
                counter++;
                int col = counter % Map.PPR;
                int row = counter / Map.PPR;
                int[] danger = new int[]{row, col};
                Dangers.add(danger);
            }
        }
        dangers = new int[Dangers.size()][];
        dangers = Dangers.toArray(dangers);
        return dangers;
    }
}
