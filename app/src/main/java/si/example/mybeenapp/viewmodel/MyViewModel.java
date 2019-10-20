package si.example.mybeenapp.viewmodel;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;

import java.util.ArrayList;
import java.util.List;

import si.example.mybeenapp.endpoint.ApiRepository;
import si.example.mybeenapp.model.Album;
import si.example.mybeenapp.model.Photo;
import si.example.mybeenapp.model.User;

public class MyViewModel extends AndroidViewModel {
    private final MediatorLiveData<List<User>> mObservableUser;
    private final MediatorLiveData<Pair<List<Album>, List<Photo>>> mObservableAlbum;
    private final MediatorLiveData<List<Photo>> mObservablePhoto;

    private List<Album> mAlbums;
    private List<Photo> mPhotos;

    public MyViewModel(@NonNull Application application) {
        super(application);
        mObservableUser = new MediatorLiveData<>();
        mObservableAlbum = new MediatorLiveData<>();
        mObservablePhoto = new MediatorLiveData<>();
    }

    public MediatorLiveData<List<User>> getUsers() {
        mObservableUser.addSource(ApiRepository.getInstance().getUsers(), mObservableUser::setValue);
        return mObservableUser;
    }

    public MediatorLiveData<Pair<List<Album>, List<Photo>>> getAlbums(int userId) {
        mObservableAlbum.addSource(ApiRepository.getInstance().getAlbums(userId), albums -> {
            if (albums == null) {
                albums = new ArrayList<>();
            }
            mAlbums = albums;
            mObservableAlbum.setValue(Pair.create(mAlbums, mPhotos));
        });
        mObservableAlbum.addSource(ApiRepository.getInstance().getAllPhotos(), photos -> {
            if (photos == null) {
                photos = new ArrayList<>();
            }
            mPhotos = photos;
            mObservableAlbum.setValue(Pair.create(mAlbums, mPhotos));
        });
        return mObservableAlbum;
    }

    public MediatorLiveData<List<Photo>> getPhotos(int albumId) {
        mObservablePhoto.addSource(ApiRepository.getInstance().getPhotos(albumId), mObservablePhoto::setValue);
        return mObservablePhoto;
    }
}
