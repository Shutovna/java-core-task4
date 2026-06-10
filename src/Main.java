import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        MyBlockingQueue<Runnable> taskQueue = new MyBlockingQueue<>(20);

        Runnable producer = () -> {
            for (int i = 0; i < 50; i++) {
                final int taskId = i;
                try {
                    taskQueue.enqueue(() -> System.out.println("Задача " + taskId + " запущена"));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };

        Runnable worker = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Runnable task = taskQueue.dequeue();
                    task.run();
                    System.out.println("Задача выполнена");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };

        try (ExecutorService executorService = Executors.newCachedThreadPool()) {
            executorService.submit(producer);
            executorService.submit(worker);
            executorService.submit(worker);
            Thread.sleep(1000L);
            executorService.shutdownNow();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}