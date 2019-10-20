package si.example.mybeenapp;

import android.os.Bundle;
import android.view.LayoutInflater;
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

import si.example.mybeenapp.adapters.UserAdapter;
import si.example.mybeenapp.databinding.FragmentUserListBinding;
import si.example.mybeenapp.endpoint.NetworkUtil;
import si.example.mybeenapp.viewmodel.MyViewModel;

public class UserFragment extends Fragment {
    final static String TAG = "UserFragment";

    private FragmentUserListBinding mBinding;
    private MyViewModel mViewModel;
    private UserAdapter mUserAdapter;
    private NetworkUtil mNetworkUtil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false);
        mNetworkUtil = new NetworkUtil(requireActivity().getApplication());
        mUserAdapter = new UserAdapter(onUserClickCallback);
        mBinding.swipeRefresh.setOnRefreshListener(onRefreshListener);
        mBinding.userList.setAdapter(mUserAdapter);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        getData();
    }

    private void getData() {
        //Check for internet connection
        //Get data when online or wait for connection if currently offline
        if (mNetworkUtil.isOnline()) {
            mBinding.setIsLoading(true); //show loading progress
            mViewModel.getUsers().observe(this, users -> {
                mBinding.setIsLoading(false); //hide when data received
                mUserAdapter.setList(users);
            });
        } else {
            mUserAdapter.setList(new ArrayList<>());
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

    private UserAdapter.OnClickCallback onUserClickCallback = user -> {
        //On user click navigates forward to users albums
        UserFragmentDirections.ActionUserToAlbum directions = UserFragmentDirections.actionUserToAlbum();
        directions.setUser(user);
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
