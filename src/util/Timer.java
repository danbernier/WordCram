package util;

/*
 Copyright 2010 Daniel Bernier

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import java.util.*;

public class Timer {
	
	private HashMap<String,ArrayList<Long>> times = new HashMap<String,ArrayList<Long>>();
	private List<String> orderedMessages = new ArrayList<String>();
	private long lastTiming;
	
	private HashMap<String,Integer> counts = new HashMap<String,Integer>(); 
	
	public Timer() {
		lastTiming = now();
	}
	
	public void log(String message) {
		if (!times.containsKey(message)) {
			times.put(message, new ArrayList<Long>());
			orderedMessages.add(message);
		}
		ArrayList<Long> timesForMessage = times.get(message);
		
		long now = now();
		timesForMessage.add(now - lastTiming);
		lastTiming = now;
	}
	
	public void count(String message) {
		if (!counts.containsKey(message)) {
			counts.put(message, 0);
		}
		counts.put(message, counts.get(message) + 1);
	}
	
	public String report() {
		return reportLogs() + reportCounts();
	}
	
	private String reportLogs() {
		StringBuffer sb = new StringBuffer();
		for (String message : orderedMessages) {
			ArrayList<Long> timings = times.get(message);
			long sum = 0;
			for (Long timing : timings) {
				sum += timing;
			}
			double avg = (double)sum / timings.size();
			avg = Math.round(avg * 100) / 100d;
			sb.append(message + ": " + sum + "ms total, averaging " + avg + "ms over " + timings.size() + " runs\n");
		}
		return sb.toString();
	}
	
	private String reportCounts() {
		StringBuffer sb = new StringBuffer();
		
		for (String message : counts.keySet()) {
			sb.append(message + ": " + counts.get(message) + " times\n");
		}
		
		return sb.toString();
	}
	
	private long now() {
		return System.currentTimeMillis();
	}
}
