import java.util.LinkedList;
import java.util.Queue;

public class MyBlockingQueue<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final int size;

    public MyBlockingQueue(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Размер должен быть положительным");
        }
        this.size = size;
    }

    public synchronized void enqueue(T item) throws InterruptedException {
        while (queue.size() == size) {
            wait();
        }
        queue.add(item);
        notifyAll();
    }

    public synchronized T dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T item = queue.poll();
        notifyAll();
        return item;
    }

    public synchronized int size() {
        return queue.size();
    }
}
