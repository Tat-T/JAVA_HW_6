import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class DirectoryCopyTas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите путь к исходной директории: ");
        String sourcePath = scanner.nextLine();

        System.out.print("Введите путь к директории, куда копировать: ");
        String destPath = scanner.nextLine();

        File sourceDir = new File(sourcePath);
        File destDir = new File(destPath);

        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            System.out.println("Ошибка: исходная директория не существует.");
            return;
        }

        long start = System.currentTimeMillis();

        // Создаем поток копирования
        Thread copyThread = new Thread(() -> {
            try {
                int[] stats = copyDirectory(sourceDir.toPath(), destDir.toPath());
                System.out.println("\nКопирование завершено:");
                System.out.println("Всего файлов скопировано: " + stats[0]);
                System.out.println("Всего папок создано: " + stats[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        copyThread.start();

        try {
            copyThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("Время выполнения: " + (end - start) + " мс");
    }

    // Метод копирования
    public static int[] copyDirectory(Path source, Path target) throws IOException {
        final int[] stats = new int[2]; // [0] = files, [1] = dirs

        Files.walk(source).forEach(path -> {
            try {
                Path relativePath = source.relativize(path);
                Path destination = target.resolve(relativePath);

                if (Files.isDirectory(path)) {
                    Files.createDirectories(destination);
                    stats[1]++;
                } else {
                    Files.copy(path, destination, StandardCopyOption.REPLACE_EXISTING);
                    stats[0]++;
                }
            } catch (IOException e) {
                System.err.println("Ошибка при копировании: " + e.getMessage());
            }
        });

        return stats;
    }
}