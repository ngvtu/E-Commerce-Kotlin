import retrofit2.http.GET

interface TestApiService {
    @GET("user")
    fun getInforUser(): String



}