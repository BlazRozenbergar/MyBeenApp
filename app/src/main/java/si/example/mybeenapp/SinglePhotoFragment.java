package si.example.mybeenapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import si.example.mybeenapp.databinding.FragmentSinglePhotoBinding;

public class SinglePhotoFragment extends Fragment {
    final static String TAG = "SinglePhotoFragment";
    private final static String FULLSCREEN_KEY = "GO_FULLSCREEN_KEY";

    private SinglePhotoFragmentArgs mArgs;
    private FragmentSinglePhotoBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = SinglePhotoFragmentArgs.fromBundle(requireArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_photo, container, false);
        setHasOptionsMenu(true); //set has menu to true so that we can handle onBackPressed
        mBinding.setPresenter(this);
        mBinding.setPhoto(mArgs.getPhoto());
        if (mArgs.getAlbum() != null) {
            mBinding.setAlbumTitle(mArgs.getAlbum().title);
        }
        if (mArgs.getUser() != null) {
            mBinding.setUserName(mArgs.getUser().name);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mArgs == null) {
            return;
        }
        if (savedInstanceState != null) {
            mBinding.setHideDetail(savedInstanceState.getBoolean(FULLSCREEN_KEY, false));
            goFullScreen(mBinding.getHideDetail());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(FULLSCREEN_KEY, mBinding.getHideDetail());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (getActivity() != null) {
            if (item.getItemId() == android.R.id.home) {
                getActivity().onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        goFullScreen(false);
        super.onDetach();
    }

    public void onClick() {
        mBinding.setHideDetail(!mBinding.getHideDetail());
        goFullScreen(mBinding.getHideDetail());
    }

    private void goFullScreen(boolean fullScreen) {
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        if (activity != null) {
            if (activity.getSupportActionBar() != null) {
                if (fullScreen) {
                    activity.getSupportActionBar().hide();
                } else {
                    activity.getSupportActionBar().show();
                }
            }
        }
    }
}
