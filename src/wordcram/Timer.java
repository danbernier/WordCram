package wordcram;

import java.util.HashMap;

public class Timer {

    public static Timer start(String tag) {
	return new Timer(tag);
    }

    private static HashMap<String, Long> timings = new HashMap<String, Long>();

    public static void report() {
	for (String tag : timings.keySet()) {
	    System.out.println(tag + ": " + timings.get(tag));
	}
    }


    private long start;
    private String tag;

    private Timer(String tag) {
	this.tag = tag;
	start = System.currentTimeMillis();
    }

    public void stop() {
	long end = System.currentTimeMillis();
	long timing = end - start;

	if (!timings.containsKey(tag)) {
	    timings.put(tag, new Long(0));
	}
	timings.put(tag, timings.get(tag) + timing);
    }
}
