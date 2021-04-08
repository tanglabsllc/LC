package com.dahutu.leetcode;

import java.util.Arrays;
import java.util.stream.Collectors;

public class LC498 {
  public int[] findDiagonalOrder(int[][] matrix) {
      if (matrix == null) return null;
      if (matrix.length == 0 || matrix[0].length == 0) return new int[]{};

      int rows = matrix.length;
      int cols = matrix[0].length;
      int rlen = rows * cols;
      int[] results = new int[rlen];
      boolean ur = true;
      for (int row = 0, col = 0, rid = 0; rid < rlen; rid++) {
          results[rid] = matrix[row][col];
          if (ur) {
              row--;
              col++;
              if (row < 0 || col == cols) {
                  ur = false;
                  if (col == cols) {
                    row += 2;
                    col = cols - 1;
                  } else {
                      row = 0;
                  }
              }
          } else {
              row++;
              col--;
              if (row == rows || col < 0) {
                  ur = true;
                  if (row == rows) {
                      row = rows - 1;
                      col += 2;
                  } else {
                      col = 0;
                  }
              }
          }
      }

      return results;
  }

  public static void main(String[] args) {
    int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
    LC498 s = new LC498();
    int[] results = s.findDiagonalOrder(matrix);
    dump(results);
  }

  public static void dump(int[] results) {
     System.out.println(Arrays.stream(results).boxed().collect(Collectors.toList()));
  }
}
