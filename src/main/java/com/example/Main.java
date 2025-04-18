package com.example;

import java.util.Arrays;
import java.util.Random;

public class Main {

    private static final int ARRAY_SIZE = 10;
    private static int[] numbers = new int[ARRAY_SIZE];
    private static int sum = 0;
    private static double average = 0.0;

    public static void main(String[] args) {

        Thread fillThread = new Thread(() -> {
            Random random = new Random();
            for (int i = 0; i < ARRAY_SIZE; i++) {
                numbers[i] = random.nextInt(100); // числа от 0 до 99
            }
            System.out.println("Массив заполнен: " + Arrays.toString(numbers));
        });

        Thread sumThread = new Thread(() -> {
            int localSum = 0;
            for (int number : numbers) {
                localSum += number;
            }
            sum = localSum;
        });

        Thread averageThread = new Thread(() -> {
            int localSum = 0;
            for (int number : numbers) {
                localSum += number;
            }
            average = (double) localSum / numbers.length;
        });

        try {
            // Сначала заполняем массив
            fillThread.start();
            fillThread.join();

            // После заполнения запускаем оба потока
            sumThread.start();
            averageThread.start();

            // Ждем завершения обоих потоков
            sumThread.join();
            averageThread.join();

            System.out.println("Сумма элементов: " + sum);
            System.out.println("Среднее значение: " + average);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}