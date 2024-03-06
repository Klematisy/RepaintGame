package ru.vsu.cs.course1.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Класс, реализующий логику игры
 */
public class Game {

    private int[][] matrix;
    private final Random rnd = new Random();
    private int colorCount = 0;
    public void newGame(int rowCount, int colCount, int colorCount) {
        // создаем поле
        matrix = new int[rowCount][colCount];
        this.colorCount = colorCount;
    }
    public void matrixRandomizer() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = rnd.nextInt(6) + 1;
            }
        }
    }
    public void solution(int replaceValue) {

        int firstValue = matrix[0][0];
        matrix[0][0] = replaceValue;
        calculator(matrix, replaceValue, firstValue, 0, 0);

    }
    private void calculator(int[][] matrix, int replaceValue, int firstValue, int i, int j) {
        List<String> list;



        list = director(matrix, i, j, firstValue);

        //if (list.size() > 1) {
            for (int k = 0; k < list.size(); k++) {
                if (list.get(k) == "UP") {
                    matrix[i - 1][j] = replaceValue;
                    calculator(matrix, replaceValue, firstValue, i - 1, j);
                }
                if (list.get(k) == "DOWN") {
                    matrix[i + 1][j] = replaceValue;
                    calculator(matrix, replaceValue, firstValue, i + 1, j);
                }
                if (list.get(k) == "LEFT") {
                    matrix[i][j - 1] = replaceValue;
                    calculator(matrix, replaceValue, firstValue, i, j - 1);
                }
                if (list.get(k) == "RIGHT") {
                    matrix[i][j + 1] = replaceValue;
                    calculator(matrix, replaceValue, firstValue, i, j + 1);
                }
            }
        //}
        /*for (int k = 0; k < list.size(); k++) {
            list.set(k, null);
        }*/

    }
    private List<String> director(int[][] matrix, int i, int j, int firstValue) {


        List<String> list = new ArrayList<>();
        if (i - 1 >= 0) {
            if (matrix[i - 1][j] == firstValue) {
                list.add("UP");
            }
        }
        if (i + 1 < matrix.length) {
            if (matrix[i + 1][j] == firstValue) {
                list.add("DOWN");
            }
        }
        if (j + 1 < matrix[0].length) {
            if (matrix[i][j + 1] == firstValue) {
                list.add("RIGHT");
            }
        }
        if (j - 1 >= 0) {
            if (matrix[i][j - 1] == firstValue) {
                list.add("LEFT");
            }
        }

        return list;
    }
    public boolean exam() {
        int count = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == matrix[0][0]) {
                    count++;
                }
            }
        }
        return count == (matrix.length) * matrix[0].length;
    }
    public int getColorNum(int i, int j) {
        return matrix[i][j];
    }
}
