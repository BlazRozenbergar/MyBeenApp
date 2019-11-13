package si.example.mybeenapp.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import si.example.mybeenapp.endpoint.ApiRepository
import si.example.mybeenapp.model.Photo

class PhotoDataSource(private val albumId: Int) : PageKeyedDataSource<Int, Photo>() {
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Photo>) {
        val currentPage = 1
        callback.onResult(ApiRepository.getInstance().getPagedPhotos(albumId, currentPage, params.requestedLoadSize), currentPage, currentPage + 1)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        callback.onResult(ApiRepository.getInstance().getPagedPhotos(albumId, params.key, params.requestedLoadSize), params.key + 1)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {

    }

}

class PhotoDataSourceFactory(private val albumId: Int) : DataSource.Factory<Int, Photo>() {
    val liveData = MutableLiveData<PhotoDataSource>()

    override fun create(): DataSource<Int, Photo> {
        val dataSource = PhotoDataSource(albumId)
        liveData.postValue(dataSource)
        return dataSource
    }

}