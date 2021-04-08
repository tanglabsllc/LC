package com.dahutu.robinhood;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WindowPricing {
/*
Coding: Window prices。给一个 String, 表示一堆按时间排序的 data points （代表时间和价格）: 0:10, 2:8, 5:9, 11:6, 17:7, 23:8, ...
然后让你输出每 10秒窗口 的 aggregate (max,min,last,first), output 是 {0:10,8,9,10}{1:7,6,7,6}{2:8,8,8,8} 这样。
*/
    public List<String> windowPrice(String datapoints, int interval) {
        List<String> results = new ArrayList<>();
        int id = -1, max = 0, min = 0, last = 0, first = 0;
        boolean hasVal = false;

        String[] dps = datapoints.split(",\\s+");
        for (String dp : dps) {
            String[] parts = dp.split(":");
            int ts = Integer.parseInt(parts[0]);
            int val = Integer.parseInt(parts[1]);

            int curId = ts / interval;
            if (id < curId) {   // new id
                if (id >= 0) {
                    results.add(getFormattedResult(id, max, min, last, first));
                }

                id = curId;
                max = val;
                min = val;
                last = val;
                first = val;
            } else {
                max = Math.max(max, val);
                min = Math.min(min, val);
                last = val;
            }
        }

        if (id >= 0) {
            results.add(getFormattedResult(id, max, min, last, first));
        }

        return results;
    }

    String getFormattedResult(int id, int max, int min, int last, int first) {
        return String.format("%d:%d,%d,%d,%d", id, max, min, last, first);
    }

    public static void main(String[] args) {
        WindowPricing s = new WindowPricing();
        List<String> results = s.windowPrice("0:10, 2:8, 5:9, 11:6, 17:7, 23:8", 10);
        System.out.println(results);
    }
}
