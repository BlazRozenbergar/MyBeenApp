package si.example.mybeenapp;

import android.app.Application;
import android.util.Pair;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;

import si.example.mybeenapp.model.Album;
import si.example.mybeenapp.model.Photo;
import si.example.mybeenapp.model.User;
import si.example.mybeenapp.viewmodel.MyViewModel;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    MyViewModel mViewModel;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        mViewModel = new MyViewModel(Mockito.mock(Application.class));
    }

    @Test
    public void isFragmentVisible() throws InterruptedException {
        onView(withId(R.id.user_list)).check(matches(isDisplayed()));

        List<User> userlist = GetDataTestUtil.getValue(mViewModel.getUsers());

        onView(withId(R.id.user_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        onView(withId(R.id.album_list)).check(matches(isDisplayed()));

        Pair<List<Album>, List<Photo>> albumList = GetDataTestUtil.getPairValue(mViewModel.getAlbums(userlist.get(3).id));

        onView(withId(R.id.album_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.photo_list)).check(matches(isDisplayed()));

        GetDataTestUtil.getValue(mViewModel.getPhotos(albumList.first.get(1).id));

        onView(withId(R.id.photo_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withId(R.id.image_url)).check(matches(isDisplayed()));

        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.bottom_app_bar)).check(matches(isDisplayed()));

        onView(withId(R.id.image_url)).perform(click());

        Thread.sleep(1000);
        onView(withId(R.id.toolbar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.bottom_app_bar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        pressBack();

        onView(withId(R.id.photo_list)).check(matches(isDisplayed()));
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    /*@Test
    public void noInternetConnection() {
        if (!new NetworkUtil(Mockito.mock(Application.class)).isOnline()) {
            onView(withId(R.id.is_offline_text)).check(matches(isDisplayed()));
        }
    }*/
}