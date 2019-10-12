package si.example.mybeenapp;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GetDataTestUtil {
    /**
     * Wait to get the value and return it
     */
    public static <T> T getValue(final MediatorLiveData<T> liveData) throws InterruptedException {
        Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);
        liveData.observeForever(new Observer<T>() {
            @Override
            public void onChanged(T t) {
                data[0] = t;
                latch.countDown(); //stop waiting
                liveData.removeObserver(this);
            }
        });
        latch.await(3, TimeUnit.SECONDS);
        return (T) data[0];
    }
}
