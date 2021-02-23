package vendingNotification;


import java.util.concurrent.atomic.AtomicBoolean;

public class SendNotification implements Runnable {

    private AtomicBoolean alreadySend;

    public SendNotification() {
        alreadySend = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        //if(alreadySend.compareAndSet(false,true)){
            NotificationMain.main();
        //}
    }

    public void setAlreadySend(AtomicBoolean alreadySend) {
        this.alreadySend = alreadySend;
    }
}