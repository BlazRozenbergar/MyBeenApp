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

import si.example.mybeenapp.adapters.UserAdapter;
import si.example.mybeenapp.databinding.FragmentUserListBinding;
import si.example.mybeenapp.model.User;
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
            mUserAdapter = new UserAdapter(new UserAdapter.OnClickCallback() {
                @Override
                public void onClick(User user) {
                    ((MainActivity)getActivity()).navigateToAlbum(user);
                }
            });
            mUserAdapter.setList(users);
            mBinding.userList.setAdapter(mUserAdapter);
        });

        return mBinding.getRoot();
    }
}
