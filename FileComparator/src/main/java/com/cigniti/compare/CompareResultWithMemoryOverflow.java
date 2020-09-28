package com.cigniti.compare;

public class CompareResultWithMemoryOverflow extends AbstractCompareResultWithSwap {

    private long maxMemoryUsage = Math.min(Math.round(Runtime.getRuntime().maxMemory() * 0.7), Runtime.getRuntime().maxMemory() - 200 * 1024 * 1024);

    /**
     * Defaults to 70% of the available maxMemory reported by the JVM.
     */
    public CompareResultWithMemoryOverflow() {
    }

    /**
     * Stores images to disk, when the used memory is higher than the given theshold in megabytes.
     * @param approximateMaxMemoryUsageInMegaBytes the maximum memory to use in megabytes
     */
    public CompareResultWithMemoryOverflow(final long approximateMaxMemoryUsageInMegaBytes) {
        this.maxMemoryUsage = approximateMaxMemoryUsageInMegaBytes * 1024 * 1024;
    }

    @Override
    protected boolean needToSwap() {
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return usedMemory >= maxMemoryUsage;
    }

    @Override
    protected void afterSwap() {
        System.gc();
    }
}
