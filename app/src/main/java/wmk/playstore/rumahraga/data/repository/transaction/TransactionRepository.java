package wmk.playstore.rumahraga.data.repository.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import wmk.playstore.rumahraga.data.remote.ApiService;
import wmk.playstore.rumahraga.model.BookedModel;
import wmk.playstore.rumahraga.model.ResponseModel;
import wmk.playstore.rumahraga.model.TransactionModel;
import wmk.playstore.rumahraga.util.constans.response.ConsResponse;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionRepository {
    private ApiService apiService;
    @Inject
    public TransactionRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel> transaction(HashMap map, MultipartBody.Part
                                               filePart) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.insertTransaction(map, filePart).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel(true, ConsResponse.SUCCESS_MESSAGE, null));

                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.SERVER_ERROR, null));

            }
        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> detailTransaction(List<BookedModel> bookedModel) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.insertDetailTransaction(bookedModel).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel(true, ConsResponse.SUCCESS_MESSAGE, null));

                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.SERVER_ERROR, null));

            }
        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<List<TransactionModel>>>  getMyTransactions(String userId) {
        MutableLiveData<ResponseModel<List<TransactionModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getMyTransaction(userId).enqueue(new Callback<ResponseModel<List<TransactionModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<TransactionModel>>> call, Response<ResponseModel<List<TransactionModel>>> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<TransactionModel>>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.SERVER_ERROR, null));

            }
        });

        return responseModelMutableLiveData;
    }
}
