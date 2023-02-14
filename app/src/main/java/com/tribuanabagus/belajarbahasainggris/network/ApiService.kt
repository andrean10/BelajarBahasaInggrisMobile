package com.tribuanabagus.belajarbahasainggris.network

import com.tribuanabagus.belajarbahasainggris.model.auth.ResponseLogin
import com.tribuanabagus.belajarbahasainggris.model.auth.ResponseRegister
import com.tribuanabagus.belajarbahasainggris.model.hafalan.ResponseHafalan
import com.tribuanabagus.belajarbahasainggris.model.matery.MateryStudyResponse
import com.tribuanabagus.belajarbahasainggris.model.nilai.belajar.ResponseNilaiBelajar
import com.tribuanabagus.belajarbahasainggris.model.nilai.permainan.ResponseNilaiPermainan
import com.tribuanabagus.belajarbahasainggris.model.percakapan.ResponsePercakapan
import com.tribuanabagus.belajarbahasainggris.model.permainan.tebak_gambar.ResponseTebakGambar
import com.tribuanabagus.belajarbahasainggris.model.permainan.temukan_pasangan.ResponseTemukanPasangan
import com.tribuanabagus.belajarbahasainggris.model.permainan.temukan_pasangan.jawaban_pasangan.ResponseJawabanPasangan
import com.tribuanabagus.belajarbahasainggris.model.questions.PairResponse
import com.tribuanabagus.belajarbahasainggris.model.questions.QuestionPlayGuessResponse
import com.tribuanabagus.belajarbahasainggris.model.questions.QuestionPlayPairWordResponse
import com.tribuanabagus.belajarbahasainggris.model.questions.QuestionStudyResponse
import com.tribuanabagus.belajarbahasainggris.model.soal.ResponseSoal
import com.tribuanabagus.belajarbahasainggris.model.student.StudentScoreResponse
import com.tribuanabagus.belajarbahasainggris.model.student.StudentScoresResponse
import com.tribuanabagus.belajarbahasainggris.model.student.StudentsResultResponse
import com.tribuanabagus.belajarbahasainggris.model.users.ResponseUserManage
import com.tribuanabagus.belajarbahasainggris.model.users.ResponseUsers
import com.tribuanabagus.belajarbahasainggris.model.video_pembelajaran.ResponseVideoPembelajaran
import com.tribuanabagus.belajarbahasainggris.user.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    /** AUTH */
    @FormUrlEncoded
    @POST("login")
    fun login(@FieldMap params: HashMap<String, Any>): Call<ResponseLogin>

    @FormUrlEncoded
    @POST("register")
    fun register(@FieldMap params: HashMap<String, String>): Call<ResponseRegister>

    /** USER */
    @GET("users")
    fun getAllUser(@Query("tipe_role") tipeRole: String): Call<ResponseUsers>

    @GET("users/{id}")
    fun getDetailUser(@Path("id") id: Int): Call<ResponseUsers>

    /** USER - ADMIN */
    @Multipart
    @PUT("users/{id}")
    fun updateUser(
        @Path("id") id: Int,
        @PartMap params: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?,
    ): Call<ResponseUserManage>

    @FormUrlEncoded
    @PUT("users/{id}/change-password")
    fun updatePassword(
        @Path("id") id: Int,
        @Field("new_password") newPassword: String,
    ): Call<ResponseUserManage>

    /** USER - SISWA */
    @Multipart
    @POST("users")
    fun storeSiswa(@PartMap params: HashMap<String, Any?>): Call<ResponseUsers>

    @DELETE("users/{id}")
    fun deleteSiswa(@Path("id") id: Int): Call<ResponseUsers>

    /** HAFALAN */
    @GET("hafalan")
    fun getHafalan(): Call<ResponseHafalan>

    @FormUrlEncoded
    @POST("hafalan")
    fun storeHafalan(@FieldMap params: HashMap<String, String>): Call<ResponseHafalan>

    @FormUrlEncoded
    @PUT("hafalan/{id}")
    fun updateHafalan(
        @Path("id") id: Int,
        @FieldMap params: HashMap<String, String>
    ): Call<ResponseHafalan>

    @DELETE("hafalan/{id}")
    fun deleteHafalan(@Path("id") id: Int): Call<ResponseHafalan>

    /** PERCAKAPAN */
    @GET("percakapan")
    fun getPercakapan(): Call<ResponsePercakapan>

    @FormUrlEncoded
    @POST("percakapan")
    fun storePercakapan(@FieldMap params: HashMap<String, String>): Call<ResponsePercakapan>

    @FormUrlEncoded
    @PUT("percakapan/{id}")
    fun updatePercakapan(
        @Path("id") id: Int,
        @FieldMap params: HashMap<String, String>
    ): Call<ResponsePercakapan>

    @DELETE("percakapan/{id}")
    fun deletePercakapan(@Path("id") id: Int): Call<ResponsePercakapan>

    /** SOAL */
    @GET("soal")
    fun getSoal(@QueryMap queryParams: HashMap<String, Any>): Call<ResponseSoal>

    @FormUrlEncoded
    @POST("soal")
    fun storeSoal(@FieldMap params: HashMap<String, String>): Call<ResponseSoal>

    @FormUrlEncoded
    @PUT("soal/{id}")
    fun updateSoal(
        @Path("id") id: Int,
        @FieldMap queryParams: HashMap<String, String>
    ): Call<ResponseSoal>

    @DELETE("soal/{id}")
    fun deleteSoal(@Path("id") id: Int): Call<ResponseSoal>

    /** VIDEO PEMBELAJARAN */
    @GET("video-pembelajaran")
    fun getVideoPembelajaran(): Call<ResponseVideoPembelajaran>

    @Multipart
    @POST("video-pembelajaran")
    fun storeVideoPembelajaran(@PartMap params: HashMap<String, Any>): Call<ResponseVideoPembelajaran>

    @Multipart
    @PUT("video-pembelajaran/{id}")
    fun updateVideoPembelajaran(
        @Path("id") id: Int,
        @PartMap params: HashMap<String, Any>
    ): Call<ResponseVideoPembelajaran>

    @DELETE("video-pembelajaran/{id}")
    fun deleteVideoPembelajaran(@Path("id") id: Int): Call<ResponseVideoPembelajaran>

    /** PERMAINAN - TEBAK GAMBAR */
    @GET("tebak-gambar")
    fun getSoalTebakGambar(): Call<ResponseTebakGambar>

    @Multipart
    @POST("tebak-gambar")
    fun storeSoalTebakGambar(@PartMap params: HashMap<String, Any>): Call<ResponseTebakGambar>

    @Multipart
    @PUT("tebak-gambar/{id}")
    fun updateSoalTebakGambar(
        @Path("id") id: Int,
        @PartMap params: HashMap<String, Any>
    ): Call<ResponseTebakGambar>

    @DELETE("tebak-gambar/{id}")
    fun deleteSoalTebakGambar(@Path("id") id: Int): Call<ResponseTebakGambar>

    /** PERMAINAN - TEMUKAN PASANGAN */
    @GET("temukan-pasangan")
    fun getSoalTemukanPasangan(): Call<ResponseTemukanPasangan>

    @Multipart
    @POST("temukan-pasangan")
    fun storeSoalTemukanPasangan(@Part suara: MultipartBody.Part): Call<ResponseTemukanPasangan>

    @Multipart
    @PUT("temukan-pasangan/{id}")
    fun updateSoalTemukanPasangan(
        @Path("id") id: Int,
        @Part suara: MultipartBody.Part
    ): Call<ResponseTemukanPasangan>

    @DELETE("temukan-pasangan/{id}")
    fun deleteSoalTemukanPasangan(@Path("id") id: Int): Call<ResponseTemukanPasangan>

    /** PERMAINAN - TEMUKAN PASANGAN ~* JAWABAN PASANGAN */
    @GET("jawaban-pasangan")
    fun getSoalJawabanPasangan(@Query("id_temukan_pasangan") idTemukanPasangan: Int): Call<ResponseJawabanPasangan>

    @Multipart
    @POST("jawaban-pasangan")
    fun storeSoalJawabanPasangan(@PartMap params: HashMap<String, Any>): Call<ResponseJawabanPasangan>

    @Multipart
    @PUT("jawaban-pasangan/{id}")
    fun updateSoalJawabanPasangan(
        @Path("id") id: Int,
        @PartMap params: HashMap<String, Any>
    ): Call<ResponseJawabanPasangan>

    @DELETE("jawaban-pasangan/{id}")
    fun deleteSoalJawabanPasangan(@Path("id") id: Int): Call<ResponseJawabanPasangan>

    /** NILAI - BELAJAR */
    @GET("nilai-belajar")
    fun getNilaiBelajarSiswa(@QueryMap queryParams: HashMap<String, Any>): Call<ResponseNilaiBelajar>

    @FormUrlEncoded
    @POST("nilai-belajar")
    fun storeNilaiBelajarSiswa(@FieldMap params: HashMap<String, Any>): Call<ResponseNilaiBelajar>

    /** NILAI - PERMAINAN */
    @GET("nilai-permainan")
    fun getNilaiPermainanSiswa(@QueryMap queryParams: HashMap<String, Any>): Call<ResponseNilaiPermainan>

    @FormUrlEncoded
    @POST("nilai-permainan")
    fun storeNilaiPermainanSiswa(@FieldMap params: HashMap<String, Any>): Call<ResponseNilaiPermainan>


    @Multipart
    @POST("user")
    fun storeUser(
        @Part image: MultipartBody.Part? = null,
        @PartMap params: HashMap<String, RequestBody>
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/{userId}")
    fun detailUser(@Path("userId") userId: Int): Call<UserResponse>

    @GET("user/students")
    fun getStudents(): Call<StudentsResultResponse>

    @FormUrlEncoded
    @POST("user/change-password")
    fun updateUserPassword(@FieldMap params: HashMap<String, Any>): Call<UserResponse>

    //ROUTE-DATA MATERY (MATERIAL STUDY)
    @FormUrlEncoded
    @POST("matery")
    fun storeMateryStudy(@FieldMap params: HashMap<String, Any>): Call<MateryStudyResponse>

    @GET("matery/{id}")
    fun materyStudy(@Path("id") materyType: Int): Call<MateryStudyResponse>

    @DELETE("matery/{id}")
    fun deleteMateryStudy(@Path("id") idMateryStudy: Int): Call<MateryStudyResponse>


    // ROUTE-LESSONQ
    @GET("lessonq/{materyId}")
    fun getQuestionsStudy(@Path("materyId") materyId: Int): Call<QuestionStudyResponse>

    @GET("lessonq/tipe/{materyTypeId}")
    fun getQuestionsStudyByType(@Path("materyTypeId") materyId: Int): Call<QuestionStudyResponse>

    @Multipart
    @POST("lessonq")
    fun storeQuestionStudy(
        @Part audio: MultipartBody.Part?,
        @Part image: MultipartBody.Part?,
        @PartMap params: HashMap<String, RequestBody>
    ): Call<QuestionStudyResponse>

    @DELETE("lessonq/{id}")
    fun deleteQuestionStudy(@Path("id") idQuestion: Int): Call<QuestionStudyResponse>


    // ROUTE-GUESSQ
    @GET("guessq")
    fun getQuestionsGuess(): Call<QuestionPlayGuessResponse>

    @Multipart
    @POST("guessq")
    fun storeGuessQ(
        @Part audio: MultipartBody.Part?,
        @PartMap params: HashMap<String, RequestBody>
    ): Call<QuestionPlayGuessResponse>

    @DELETE("guessq/{id}")
    fun deleteQuestionGuess(@Path("id") idGuessQ: Int): Call<QuestionPlayGuessResponse>


    // ROUTE-PAIRQ
    @GET("pairwordq")
    fun getQuestionsPairWords(): Call<QuestionPlayPairWordResponse>

    @Multipart
    @POST("pairwordq")
    fun storeQuestionPairW(
        @Part("id") id: RequestBody,
        @Part sound: MultipartBody.Part?
    ): Call<QuestionPlayPairWordResponse>

    @DELETE("pairwordq/{id}")
    fun deleteQuestionPairW(@Path("id") idPairW: Int): Call<QuestionPlayPairWordResponse>

    //ROUTE-PAIR
    @Multipart
    @POST("pair")
    fun storePair(
        @Part sound: MultipartBody.Part?,
        @PartMap params: HashMap<String, RequestBody>
    ): Call<PairResponse>

    @DELETE("pair/{id}")
    fun deletePairItem(@Path("id") idPairItem: Int): Call<PairResponse>

    //ROUTE-STUDENT SCORE
    @FormUrlEncoded
    @POST("student-score")
    fun storeStudentScrore(@FieldMap params: HashMap<String, Any>): Call<StudentScoreResponse>

    @GET("student-score/{tipe_materi}/{id_siswa}")
    fun allStudentScoress(
        @Path("tipe_materi") materyType: Int,
        @Path("id_siswa") studentId: Int,
    ): Call<StudentScoresResponse>

    @GET("student-score/game/{tipe_materi}/{id_siswa}")
    fun studentGameScore(
        @Path("tipe_materi") materyType: Int,
        @Path("id_siswa") studentId: Int,
    ): Call<StudentScoreResponse>

}
