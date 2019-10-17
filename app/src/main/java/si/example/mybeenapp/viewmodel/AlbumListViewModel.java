package si.example.mybeenapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import si.example.mybeenapp.endpoint.ApiRepository;
import si.example.mybeenapp.model.Album;

public class AlbumListViewModel extends AndroidViewModel {
    private final MediatorLiveData<List<Album>> mObservable;
    private final int mUserId;

    public AlbumListViewModel(@NonNull Application application, final int userId) {
        super(application);

        this.mUserId = userId;
        this.mObservable = new MediatorLiveData<>();
        //mObservable.setValue(null);
    }

    public MediatorLiveData<List<Album>> getAlbums() {
        mObservable.addSource(ApiRepository.getInstance().getAlbums(mUserId), mObservable::setValue);
        return mObservable;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final Application mApplication;
        private final int mUserId;

        public Factory(@NonNull Application application, int userId) {
            this.mApplication = application;
            this.mUserId = userId;
        }

        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new AlbumListViewModel(mApplication, mUserId);
        }
    }
}
