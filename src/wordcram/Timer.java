package wordcram;

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

class Timer {
	
	private Stack<Timer.IntervalStart> starts = new Stack<Timer.IntervalStart>();
	private HashMap<String,ArrayList<Long>> durations = new HashMap<String,ArrayList<Long>>();
	private List<String> orderedMessages = new ArrayList<String>();
		
	private HashMap<String,Integer> counts = new HashMap<String,Integer>(); 
	
	public void count(String message) {
		if (!counts.containsKey(message)) {
			counts.put(message, 0);
		}
		counts.put(message, counts.get(message) + 1);
	}
	
	public void start(String message) {
		starts.push(new IntervalStart(message));
		
		if (!durations.containsKey(message)) {
			durations.put(message, new ArrayList<Long>());
			orderedMessages.add(message);
		}
	}
	public void end(String message) {
		IntervalStart start = starts.pop();
		long duration = now() - start.time;
		
		if (!message.equals(start.message)) {
			String holyCrap = message + " != " + start.message;
			int x = 42;
		}

		ArrayList<Long> timesForMessage = durations.get(start.message);
		timesForMessage.add(duration);
	}
	
	public String report() {
		return reportLogs() + "\n" + reportCounts();
	}
	
	private String reportLogs() {
		StringBuffer sb = new StringBuffer();
		
		if (!starts.empty()) {
			sb.append("Timer stack is not empty!\n");
			for (IntervalStart is : starts) {
				sb.append(is.message + ", " + new Date(is.time) + "\n");
			}
		}
		
		for (String message : orderedMessages) {
			ArrayList<Long> timings = durations.get(message);
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
	
	private class IntervalStart {
		public String message;
		public long time;
		
		public IntervalStart(String m) {
			message = m;
			time = now();
		}
	}
}
