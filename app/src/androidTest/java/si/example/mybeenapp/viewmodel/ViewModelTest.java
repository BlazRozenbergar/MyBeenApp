package si.example.mybeenapp.viewmodel;

import android.app.Application;
import android.util.Pair;

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
    private MyViewModel mViewModel;

    @Before
    public void setUp() throws Exception {
        mApplication = Mockito.mock(Application.class);
        mViewModel = new MyViewModel(mApplication);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getUsers() throws InterruptedException {
        List<User> userList = GetDataTestUtil.getValue(mViewModel.getUsers());
        Assert.assertNotNull(userList);
        Assert.assertTrue(userList.size() > 0);
    }

    @Test
    public void getAlbums() throws InterruptedException {
        Pair<List<Album>, List<Photo>> albumList = GetDataTestUtil.getPairValue(mViewModel.getAlbums(1));
        Assert.assertNotNull(albumList);
        Assert.assertNotNull(albumList.first);
        Assert.assertNotNull(albumList.second);
        Assert.assertTrue(albumList.first.size() > 0);
        Assert.assertTrue(albumList.second.size() > 0);
    }

    @Test
    public void getPhotos() throws InterruptedException {
        List<Photo> photosList = GetDataTestUtil.getValue(mViewModel.getPhotos(1));
        Assert.assertNotNull(photosList);
        Assert.assertTrue(photosList.size() > 0);
    }
}
