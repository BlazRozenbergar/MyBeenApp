package si.example.mybeenapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import si.example.mybeenapp.adapters.AlbumAdapter;
import si.example.mybeenapp.databinding.FragmentAlbumListBinding;
import si.example.mybeenapp.model.Album;
import si.example.mybeenapp.viewmodel.AlbumListViewModel;

public class AlbumFragment extends Fragment {
    final static String TAG = "AlbumFragment";
    private static final String KEY_USER_ID = "userid";
    private FragmentAlbumListBinding mBinding;
    //private AlbumListViewModel mViewModel;
    private AlbumAdapter mAdapter;

    static AlbumFragment newInstance(int userId) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_album_list, container, false);
        mAdapter = new AlbumAdapter(new AlbumAdapter.OnClickCallback() {
            @Override
            public void onClick(Album album) {
                //TODO: Add onClick event
            }
        });
        mBinding.albumList.setAdapter(mAdapter);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AlbumListViewModel.Factory factory = new AlbumListViewModel.Factory(
                requireActivity().getApplication(), getArguments().getInt(KEY_USER_ID));

        final AlbumListViewModel viewModel = new ViewModelProvider(this, factory)
                .get(AlbumListViewModel.class);

        addToModel(viewModel);
    }

    private void addToModel(final AlbumListViewModel model) {
        model.getAlbums().observe(this, new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                mAdapter.setList(albums);
            }
        });
    }
}
