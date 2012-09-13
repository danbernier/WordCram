package wordcram;

import java.util.HashMap;

public class Ticker {
    private static Ticker singleton = new Ticker();

    public static Ticker ticker() {
    return singleton;
    }



    private HashMap<String, Integer> ticks;

    private Ticker() {
    ticks = new HashMap<String, Integer>();
    }

    public void tick(String tickLabel) {
    if (ticks.containsKey(tickLabel)) {
        ticks.put(tickLabel, ticks.get(tickLabel) + 1);
    }
    else {
        ticks.put(tickLabel, 1);
    }
    }

    public void report() {
    for (String label : ticks.keySet()) {
        System.out.println(label + ": " + ticks.get(label));
    }
    }
}
