package com.dahutu.leetcode;

import java.util.*;
import java.util.stream.Collectors;

public class LC786 {
    static class FractionHolder {
        int numerator;
        int denominator;

        public FractionHolder(int a, int b) {
            numerator = a;
            denominator = b;
        }
    }

    public int[] kthSmallestPrimeFraction(int[] arr, int k) {
        Queue<FractionHolder> q = new PriorityQueue<>((a, b) -> a.numerator * b.denominator - b.numerator * a.denominator);
        for (int i = 0; i < Math.min(k, arr.length - 1); i++) {
            for (int j = i + 1; j < arr.length; j++) {
                q.add(new FractionHolder(arr[i], arr[j]));
            }
        }

        FractionHolder fh = new FractionHolder(arr[0],arr[0]);
        for (int i = 0; i < k; i++) {
            fh = q.remove();
        }

        return new int[]{fh.numerator, fh.denominator};
    }

    public static void main(String[] argv) {
        LC786 s = new LC786();
        dump(s.kthSmallestPrimeFraction(new int[]{1,2,3,5}, 3));
        dump(s.kthSmallestPrimeFraction(new int[]{1, 7}, 1));
    }

    public static void dump(int[] results) {
        System.out.println(Arrays.stream(results).boxed().collect(Collectors.toList()));
    }
}
