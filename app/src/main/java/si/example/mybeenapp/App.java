package si.example.mybeenapp;

import android.app.Application;

import si.example.mybeenapp.endpoint.ApiRepository;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Add Crashlytics or other
    }

    public ApiRepository getRepository() {
        return ApiRepository.getInstance();
    }
}
