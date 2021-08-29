package com.example.adminstatisticsscreen;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitAPI {

    @GET("/getNewKids/{period}")
    Call<HashMap<String, Integer>> getNewKids(@Path("period") int period);
    @GET("/getNewParents/{period}")
    Call<HashMap<String, Integer>> getNewParents(@Path("period") int period);
    @GET("/getKidsCountByCategory/{period}")
    Call<HashMap<String,Integer>> getKidsCountByCategory(@Path("period") int period);
    @GET("/getActivityTime/{period}")
    Call<HashMap<String,Double>> getAcitvityTime(@Path("period") int period);
    // post
//    @POST("/createUser")
    //creating a method to post our data.
//    Call<User> createUser(@Body User user);
//
//    @GET("/api/user/{id}")
//    Call<User> getUserById(@Path("id") int id);
//
//    @GET("/findUser/{email}")
//    Call<User> retrieveUserByEmail(@Path("email") String email);
//
//    @GET("/api/user")
//    Call<List<User>> getAllUsers();
//
//    @PUT("/updateUser/{oldEmail}")
//    Call<User> updateUserEmail(@Path("oldEmail") String oldEmail,@Body String newEmail);
}
