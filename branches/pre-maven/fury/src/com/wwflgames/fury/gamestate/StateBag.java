package com.wwflgames.fury.gamestate;

import java.util.HashMap;
import java.util.Map;

// used to hold a collection of "named" bunch of state data, specified by strings,

// instead of having a bunch of instance variables to track this stuff
public class StateBag {
    Map<String, Boolean> booleanStates = new HashMap<String, Boolean>();
    Map<String, Integer> integerStates = new HashMap<String, Integer>();

    // if you ask for a state that doesn't exist, it will create it
    // and set it to false
    public boolean getBooleanState(String key) {
        Boolean state = booleanStates.get(key);
        if (state == null) {
            state = false;
            setBooleanState(key, state);
        }
        return state;
    }

    public void setBooleanState(String key, boolean flag) {
        booleanStates.put(key, flag);
    }

    public int getIntegerState(String key) {
        Integer value = integerStates.get(key);
        if (value == null) {
            value = 0;
            setIntegerState(key, value);
        }
        return value;
    }

    public void setIntegerState(String key, Integer value) {
        integerStates.put(key, value);
    }

    public void clearAll() {
        booleanStates.clear();
        integerStates.clear();
    }


}
