package si.example.mybeenapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import si.example.mybeenapp.adapters.PhotoAdapter;
import si.example.mybeenapp.databinding.FragmentPhotoListBinding;
import si.example.mybeenapp.endpoint.NetworkUtil;
import si.example.mybeenapp.model.Album;
import si.example.mybeenapp.model.User;
import si.example.mybeenapp.viewmodel.MyViewModel;

public class PhotoFragment extends Fragment {
    final static String TAG = "PhotoFragment";

    private Album mAlbum;
    private User mUser;
    private FragmentPhotoListBinding mBinding;
    private MyViewModel mViewModel;
    private PhotoAdapter mAdapter;
    private NetworkUtil mNetworkUtil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoFragmentArgs mArgs = PhotoFragmentArgs.fromBundle(requireArguments());
        mAlbum = mArgs.getAlbum();
        mUser = mArgs.getUser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_list, container, false);
        setHasOptionsMenu(true); //set has menu to true so that we can handle onBackPressed
        mNetworkUtil = new NetworkUtil(requireActivity().getApplication());
        mAdapter = new PhotoAdapter(onPhotoClick);
        mBinding.swipeRefresh.setOnRefreshListener(onRefreshListener);
        mBinding.photoList.setAdapter(mAdapter);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mAlbum == null || mUser == null) {
            return;
        }
        mViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        getData();
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

    private void getData() {
        //Check for internet connection
        //Get data when online or wait for connection if currently offline
        if (mNetworkUtil.isOnline()) {
            mBinding.setIsLoading(true); //show loading progress
            mViewModel.getPhotos(mAlbum.id).observe(this, photos -> {
                mBinding.setIsLoading(false); //hide when data received
                mAdapter.setList(photos);
            });
        } else {
            mAdapter.setList(new ArrayList<>());
            waitForNetwork();
        }
    }

    private void waitForNetwork() {
        mBinding.setIsOffline(true); //show instructions to user to turn on network connection
        mNetworkUtil.observe(this, aBoolean -> {
            if (aBoolean) {
                //Get data when network connection received
                mBinding.setIsOffline(false);
                getData();
            }
        });
    }

    private PhotoAdapter.OnClickCallback onPhotoClick = photo -> {
        PhotoFragmentDirections.ActionPhotosToSinglePhoto directions = PhotoFragmentDirections.actionPhotosToSinglePhoto();
        directions.setPhoto(photo);
        directions.setAlbum(mAlbum);
        directions.setUser(mUser);
        NavHostFragment.findNavController(this).navigate(directions);
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mBinding.swipeRefresh.setRefreshing(false);
            getData();
        }
    };
}
