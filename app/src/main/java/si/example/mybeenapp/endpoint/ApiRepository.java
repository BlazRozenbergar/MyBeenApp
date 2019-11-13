package si.example.mybeenapp.endpoint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import si.example.mybeenapp.model.Album;
import si.example.mybeenapp.model.Photo;
import si.example.mybeenapp.model.User;

public class ApiRepository {
    private final String TAG = "ApiRepository";

    private static ApiRepository sInstance;
    private ApiService mApiService;
    private MutableLiveData<List<User>> mObservableUser;

    private ApiRepository() {
        mApiService = createService();
    }

    public static ApiRepository getInstance() {
        if (sInstance == null) {
            synchronized (ApiRepository.class) {
                if (sInstance == null) {
                    sInstance = new ApiRepository();
                }
            }
        }
        return sInstance;
    }

    private ApiService createService() {
        //Service gets created with repository
        //Added JSON and RxJava support
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }

    public LiveData<List<User>> getUsers() {
        MediatorLiveData<List<User>> data = new MediatorLiveData<>();
        mApiService.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                //Log.e(TAG, "onFailure: getUsers", t);
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Album>> getAlbums(int userId) {
        MediatorLiveData<List<Album>> data = new MediatorLiveData<>();
        mApiService.getAlbums(userId, "photos").enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Photo>> getPhotos(int albumId) {
        MediatorLiveData<List<Photo>> data = new MediatorLiveData<>();
        mApiService.getPhotos(albumId).enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Photo>> getAllPhotos() {
        MediatorLiveData<List<Photo>> data = new MediatorLiveData<>();
        mApiService.getAllPhotos().enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public List<Photo> getPagedPhotos(int albumId, int page, int pageLimit) {
        try {
            Response<List<Photo>> response = mApiService.getPagedPhotos(albumId, page, pageLimit).execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
