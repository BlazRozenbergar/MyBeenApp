package si.example.mybeenapp.viewmodel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import si.example.mybeenapp.GetDataTestUtil;
import si.example.mybeenapp.model.Album;
import si.example.mybeenapp.model.Photo;
import si.example.mybeenapp.model.User;

public class ViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Application mApplication;
    private UserListViewModel userViewModel;
    private AlbumListViewModel albumViewModel;
    private PhotoListViewModel photosViewModel;

    @Before
    public void setUp() throws Exception {
        mApplication = Mockito.mock(Application.class);
        userViewModel = new UserListViewModel(mApplication);
        albumViewModel = new AlbumListViewModel(mApplication, 1);
        photosViewModel = new PhotoListViewModel(mApplication, 1);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getUsers() throws InterruptedException {
        List<User> userList = GetDataTestUtil.getValue(userViewModel.getUsers());
        Assert.assertNotNull(userList);
        Assert.assertTrue(userList.size() > 0);
    }

    @Test
    public void getAlbums() throws InterruptedException {
        List<Album> albumList = GetDataTestUtil.getValue(albumViewModel.getAlbums());
        Assert.assertNotNull(albumList);
        Assert.assertTrue(albumList.size() > 0);
    }

    @Test
    public void getPhotos() throws InterruptedException {
        List<Photo> photosList = GetDataTestUtil.getValue(photosViewModel.getPhotos());
        Assert.assertNotNull(photosList);
        Assert.assertTrue(photosList.size() > 0);
    }
}
