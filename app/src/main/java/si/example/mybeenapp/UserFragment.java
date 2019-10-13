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

import si.example.mybeenapp.adapters.UserAdapter;
import si.example.mybeenapp.databinding.FragmentUserListBinding;
import si.example.mybeenapp.viewmodel.UserListViewModel;

public class UserFragment extends Fragment {
    final static String TAG = "UserFragment";

    private FragmentUserListBinding mBinding;
    private UserListViewModel mUserViewModel;
    private UserAdapter mUserAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false);

        mUserViewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
        mUserViewModel.getUsers().observe(this, users -> {
            mUserAdapter = new UserAdapter(onUserClickCallback);
            mUserAdapter.setList(users);
            mBinding.userList.setAdapter(mUserAdapter);
        });

        return mBinding.getRoot();
    }

    private UserAdapter.OnClickCallback onUserClickCallback = user -> {
        UserFragmentDirections.ActionUserToAlbum directions = UserFragmentDirections.actionUserToAlbum(user.id);
        NavHostFragment.findNavController(this).navigate(directions);
    };
}
