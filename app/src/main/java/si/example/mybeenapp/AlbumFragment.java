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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import si.example.mybeenapp.adapters.AlbumAdapter;
import si.example.mybeenapp.databinding.FragmentAlbumListBinding;
import si.example.mybeenapp.viewmodel.AlbumListViewModel;

public class AlbumFragment extends Fragment {
    final static String TAG = "AlbumFragment";

    private AlbumFragmentArgs mArgs;
    private FragmentAlbumListBinding mBinding;
    private AlbumListViewModel mViewModel;
    private AlbumAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = AlbumFragmentArgs.fromBundle(requireArguments());
    }

        @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_album_list, container, false);
        setHasOptionsMenu(true); //set has menu to true so that we can handle onBackPressed
        mAdapter = new AlbumAdapter(onAlbumClick);
        mBinding.albumList.setAdapter(mAdapter);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mArgs == null) {
            return;
        }
        AlbumListViewModel.Factory factory = new AlbumListViewModel.Factory(
                requireActivity().getApplication(), mArgs.getUserId());

        mViewModel = new ViewModelProvider(this, factory)
                .get(AlbumListViewModel.class);

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
        mViewModel.getAlbums().observe(this, albums -> mAdapter.setList(albums));
    }

    private AlbumAdapter.OnClickCallback onAlbumClick = album -> {
        AlbumFragmentDirections.ActionAlbumToPhotos directions = AlbumFragmentDirections.actionAlbumToPhotos(album.id);
        NavHostFragment.findNavController(this).navigate(directions);
    };
}
