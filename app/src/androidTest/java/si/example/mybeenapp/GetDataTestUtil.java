package si.example.mybeenapp;

import android.util.Pair;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import si.example.mybeenapp.model.Album;
import si.example.mybeenapp.model.Photo;

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

    public static Pair<List<Album>, List<Photo>> getPairValue(final MediatorLiveData<Pair<List<Album>, List<Photo>>> liveData) throws InterruptedException {
        Object[] A = new Object[1];
        Object[] B = new Object[1];
        CountDownLatch latch = new CountDownLatch(2);
        liveData.observeForever(new Observer<Pair<List<Album>, List<Photo>>>() {
            @Override
            public void onChanged(Pair<List<Album>, List<Photo>> pair) {
                if (pair != null) {
                    if (pair.first != null) {
                        A[0] = pair.first;
                    }
                    if (pair.second != null) {
                        B[0] = pair.second;
                    }
                }

                latch.countDown(); //stop waiting
                if (latch.getCount() == 0) {
                    liveData.removeObserver(this);
                }
            }
        });
        latch.await(4, TimeUnit.SECONDS);
        return Pair.create((List<Album>) A[0], (List<Photo>) B[0]);
    }
}
