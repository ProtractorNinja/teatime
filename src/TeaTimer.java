/**
 * A remake of Jon Cooper's Timer class, for Tea Time. Somewhat simpler.
 *
 *
 * @author Austin Anderson
 * @version 1.00 2011/2/23
 */

import java.util.ArrayList;

public class TeaTimer {

	long nanos;
	long startTime;
	long stopTime;
	ArrayList<Long> laps;

    public TeaTimer() {
    	nanos = 0;
    	startTime = 0;
    	stopTime = 0;
    	laps = new ArrayList<Long>();
    }

    public void start() {
    	startTime = System.nanoTime();
    }

    public void stop() {
    	stopTime = System.nanoTime();
    	nanos = stopTime - startTime;
    }

    public void reset() {
    	nanos = 0;
    	laps.clear();
    }
    
    public void lap() {
    	stop();
    	laps.add(nanos);
    	nanos = 0;
    	start();
    }
    
    public long getNanos() {
    	return nanos;
    }
    
    public long getLap(int index) {
    	return laps.get(index);
    }

    public long getMillis(long ns) {
    	return ns/1000000;
    }

    public long getSeconds(long ns) {
    	return getMillis(ns)/1000;
    }
    
    public long getMinutes(long ns) {
    	return getSeconds(ns)/60;
    }
    
    public long getHours(long ns) {
    	return getMinutes(ns)/60;
    }
    
    public long getAverageNanos() {
    	if(!laps.isEmpty()) {
    		long average = 0;
    		for(int i=0; i<laps.size(); i++) {
    			average += laps.get(i);
    		}
    		average /= laps.size();
    		return average;
    	}
    	else return nanos;
    }
    
    public String getTinyTime(long nanos) {
    	return "" + getSeconds(nanos) + "s " + getMillis(nanos)%1000 + "ms " + nanos%1000000 + "ns";
    }
    
    public String getBigTime(long nanos) {
    	return "" + getHours(nanos) + "h " + getMinutes(nanos)%60 + "m " + getSeconds(nanos)%60 + "s " + getMillis(nanos)%1000 + "ms " + nanos%1000000 + "ns";
    }
    
    public String getTimerTime(long nanos) {
    	String h, m, s, ms;
    	h = "" + getHours(nanos);
    	m = "" + getMinutes(nanos)%60;
    	s = "" + getSeconds(nanos)%60;
    	ms = "" + getMillis(nanos)%1000;
    	if(m.length() < 2) m = "0" + m;
    	if(s.length() < 2) s = "0" + s;
    	
    	return h + ":" + m + ":" + s + "." + ms;
    }

    public String toString() {
    	return getTimerTime(getAverageNanos());
    }

}