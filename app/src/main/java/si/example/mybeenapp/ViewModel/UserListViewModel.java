package si.example.mybeenapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

import si.example.mybeenapp.endpoint.ApiRepository;
import si.example.mybeenapp.model.User;

public class UserListViewModel extends AndroidViewModel {
    private final MediatorLiveData<List<User>> mObservable;

    public UserListViewModel(@NonNull Application application) {
        super(application);

        mObservable = new MediatorLiveData<>();
        mObservable.setValue(null);

        mObservable.addSource(ApiRepository.getInstance().getUsers(), mObservable::setValue);
    }

    public MediatorLiveData<List<User>> getUsers() {
        return mObservable;
    }
}
