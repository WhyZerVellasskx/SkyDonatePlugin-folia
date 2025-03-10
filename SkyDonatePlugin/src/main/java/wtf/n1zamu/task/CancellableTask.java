package wtf.n1zamu.task;

public interface CancellableTask {
    void cancel();

    boolean isCancelled();
}