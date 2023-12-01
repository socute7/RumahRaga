package wmk.playstore.rumahraga.viewmodel.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import wmk.playstore.rumahraga.data.repository.transaction.TransactionRepository;
import wmk.playstore.rumahraga.model.BookedModel;
import wmk.playstore.rumahraga.model.ResponseModel;
import wmk.playstore.rumahraga.model.TransactionModel;
import wmk.playstore.rumahraga.util.constans.response.ConsResponse;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MultipartBody;

@HiltViewModel
public class TransactionViewModel extends ViewModel {
    private TransactionRepository transactionRepository;

    @Inject
    public TransactionViewModel(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    public LiveData<ResponseModel> insertTransaction (HashMap map, MultipartBody.Part filePart) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (map != null && filePart != null) {
         return transactionRepository.transaction(map, filePart);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.ERROR_MESSAGE, null));
        }
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> insertDetailTransaction (List<BookedModel> bookedModel) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (bookedModel != null) {
            return transactionRepository.detailTransaction(bookedModel);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.ERROR_MESSAGE, null));
        }
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<List<TransactionModel>>> getMyTransactions(String userId) {
        MutableLiveData<ResponseModel<List<TransactionModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        if (userId != null && !userId.isEmpty()) {
            return transactionRepository.getMyTransactions(userId);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));
        }

        return responseModelMutableLiveData;
    }
}
