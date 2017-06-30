/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package context;

import static constant.GameConstants.DEFAULT_RESOURCE_PACKAGE;
import model.container.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import exception.MapUnavailableException;

/**
 *
 * @author Feng
 */
public class MapPool {
    private static final MapPool instance = new MapPool();
    private static final String MAP = "maps/";
    public static MapPool getInstance(){
        return instance;
    }

    private static HashMap<Integer, String> mDangers = new HashMap<>();
    // Static field across the experiment, 0 is not counted
    private static boolean[] mLeftKeys = new boolean[17];

    static {
//        mDangers.put(0, DEFAULT_RESOURCE_PACKAGE.concat("dangers1_big.txt"));
//        mLeftKeys[0] = true;
        for(Integer i = 1; i<mLeftKeys.length; i++){
            mLeftKeys[i] = true;
            String FileName = "dangers".concat(i.toString()).concat("_big.txt");
            mDangers.put(i, DEFAULT_RESOURCE_PACKAGE.concat(MAP).concat(FileName));
        }
    }

    public MapEntry getRandomMapEntry() {
        if (!hasAvailableMap()) {
            return null;
        }
        ArrayList<Integer> shuffledKeys = getAvailableKeys();
        Collections.shuffle(shuffledKeys);
        int index = shuffledKeys.get(0);
        mLeftKeys[index] = false;
        return new MapEntry(index, mDangers.get(index));
    }
    
    public MapEntry getOrderedMapEntry() {
        if (!hasAvailableMap()) {
            return null;
        }
        ArrayList<Integer> availKeys = getAvailableKeys();
        int index = availKeys.get(0);
        mLeftKeys[index] = false;
        return new MapEntry(index, mDangers.get(index));  
    }
    
    public boolean hasMap(int index){
        return mLeftKeys[index];
    }
    
    //TODO
    public String getMap(Integer index) throws MapUnavailableException{
        if(!hasMap(index))
            throw new MapUnavailableException(index.toString());
        mLeftKeys[index] = false;
        return mDangers.get(index);
    }

    public boolean hasAvailableMap() {
        return !getAvailableKeys().isEmpty();
    }

    public ArrayList<Integer> getAvailableKeys() {
        ArrayList<Integer> keys = new ArrayList<>();
        for (int index = 1; index < mLeftKeys.length; index++) {
            if (mLeftKeys[index]) {
                keys.add(index);
            }
        }
        return keys;
    }

    public class MapEntry extends Pair<Integer, String>{

        public MapEntry(Integer index, String map) {
            super(index, map);
        }

        public int getIndex() {
            return getA();
        }

        public String getMap() {
            return getB();
        }

    }

}
