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
import java.util.List;

import si.example.mybeenapp.adapters.AlbumAdapter;
import si.example.mybeenapp.databinding.FragmentAlbumListBinding;
import si.example.mybeenapp.endpoint.NetworkUtil;
import si.example.mybeenapp.model.Album;
import si.example.mybeenapp.model.Photo;
import si.example.mybeenapp.model.User;
import si.example.mybeenapp.viewmodel.MyViewModel;

public class AlbumFragment extends Fragment {
    final static String TAG = "AlbumFragment";

    private User mUser;
    private FragmentAlbumListBinding mBinding;
    private MyViewModel mViewModel;
    private AlbumAdapter mAdapter;
    private NetworkUtil mNetworkUtil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlbumFragmentArgs mArgs = AlbumFragmentArgs.fromBundle(requireArguments());
        mUser = mArgs.getUser();
    }

        @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_album_list, container, false);
        setHasOptionsMenu(true); //set has menu to true so that we can handle onBackPressed
        mNetworkUtil = new NetworkUtil(requireActivity().getApplication());
        mAdapter = new AlbumAdapter(onAlbumClick);
        mBinding.swipeRefresh.setOnRefreshListener(onRefreshListener);
        mBinding.albumList.setAdapter(mAdapter);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mUser == null) {
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
            mViewModel.getAlbums(mUser.id).observe(this, pair -> {
                mBinding.setIsLoading(false); //hide when data received
                //Join together albums and photos when you receive both
                if (pair != null && pair.first != null && pair.second != null) {
                    List<Album> albums = pair.first;
                    for (Album album : albums) {
                        for (Photo photo : pair.second) {
                            if (album.id == photo.albumId) {
                                album.photo = photo;
                                break;
                            }
                        }
                    }
                    mAdapter.setList(albums);
                }
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

    private AlbumAdapter.OnClickCallback onAlbumClick = album -> {
        AlbumFragmentDirections.ActionAlbumToPhotos directions = AlbumFragmentDirections.actionAlbumToPhotos();
        directions.setAlbum(album);
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
