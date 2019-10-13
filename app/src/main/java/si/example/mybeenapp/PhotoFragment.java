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

import si.example.mybeenapp.adapters.PhotoAdapter;
import si.example.mybeenapp.databinding.FragmentPhotoListBinding;
import si.example.mybeenapp.viewmodel.PhotoListViewModel;

public class PhotoFragment extends Fragment {
    final static String TAG = "PhotoFragment";

    private PhotoFragmentArgs mArgs;
    private FragmentPhotoListBinding mBinding;
    private PhotoAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = PhotoFragmentArgs.fromBundle(requireArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_list, container, false);
        setHasOptionsMenu(true);

        mAdapter = new PhotoAdapter(onPhotoClick);
        mBinding.photoList.setAdapter(mAdapter);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mArgs == null) {
            return;
        }
        PhotoListViewModel.Factory factory = new PhotoListViewModel.Factory(
                requireActivity().getApplication(), mArgs.getAlbumId());

        final PhotoListViewModel viewModel = new ViewModelProvider(this, factory)
                .get(PhotoListViewModel.class);

        addToModel(viewModel);
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

    private void addToModel(final PhotoListViewModel model) {
        model.getPhotos().observe(this, photos -> mAdapter.setList(photos));
    }

    private PhotoAdapter.OnClickCallback onPhotoClick = album -> {
        //TODO: Navigate forward to single photo viewer
    };
}
