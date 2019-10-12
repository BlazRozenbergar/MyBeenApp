package si.example.mybeenapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import si.example.mybeenapp.endpoint.ApiRepository;
import si.example.mybeenapp.model.Photo;

public class PhotoListViewModel extends AndroidViewModel {
    private final MediatorLiveData<List<Photo>> mObservable;

    public PhotoListViewModel(@NonNull Application application, final int albumId) {
        super(application);

        mObservable = new MediatorLiveData<>();
        mObservable.setValue(null);

        mObservable.addSource(ApiRepository.getInstance().getPhotos(albumId), mObservable::setValue);
    }

    public MediatorLiveData<List<Photo>> getPhotos() {
        return mObservable;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final Application mApplication;
        private final int mAlbumId;

        public Factory(@NonNull Application application, int albumId) {
            this.mApplication = application;
            this.mAlbumId = albumId;
        }

        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PhotoListViewModel(mApplication, mAlbumId);
        }
    }
}
