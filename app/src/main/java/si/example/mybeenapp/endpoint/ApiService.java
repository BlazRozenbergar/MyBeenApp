package si.example.mybeenapp.endpoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import si.example.mybeenapp.model.Album;
import si.example.mybeenapp.model.Photo;
import si.example.mybeenapp.model.User;

public interface ApiService {
    String BASE_URL = "https://jsonplaceholder.typicode.com/";
    int PAGE_LIMIT = 10;

    @GET("users")
    Call<List<User>> getUsers();

    @GET("albums")
    Call<List<Album>> getAlbums(
            @Query("userId") int userId,
            @Query("_embed") String photos
    );

    @GET("photos")
    Call<List<Photo>> getPhotos(
            @Query("albumId") int albumId
    );

    @GET("photos")
    Call<List<Photo>> getPagedPhotos(
            @Query("albumId") int albumId,
            @Query("_page") int page,
            @Query("_limit") int limit
    );

    @GET("photos")
    Call<List<Photo>> getAllPhotos();
}
