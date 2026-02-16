package kbo.today.adapter.out.ratelimit;

public class RateLimitEntry {
    private long windowStartMillis;
    private int count;

    synchronized boolean tryAcquire(long windowMillis, int maxRequests) {
        long now = System.currentTimeMillis();
        if (now - windowStartMillis >= windowMillis) {
            windowStartMillis = now;
            count = 0;
        }
        if (count >= maxRequests) {
            return false;
        }
        count++;
        return true;
    }
}
