package wmk.playstore.rumahraga.viewmodel.review;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import wmk.playstore.rumahraga.data.repository.review.ReviewRepository;
import wmk.playstore.rumahraga.model.ResponseModel;
import wmk.playstore.rumahraga.model.ReviewModel;
import wmk.playstore.rumahraga.util.constans.response.ConsResponse;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReviewViewModel extends ViewModel {
    private ReviewRepository reviewRepository;


    @Inject
    public ReviewViewModel(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public LiveData<ResponseModel<ReviewModel>> getTotalRating(String id) {
        MutableLiveData<ResponseModel<ReviewModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if (id != null & !id.isEmpty()) {
            return reviewRepository.getTotalRating(id);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));
        }

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> insertReview(int transactionId, String userId, String reviewText, int fieldId,
                                                float stars) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (transactionId != 0 && userId != null && reviewText != null && fieldId != 0 && stars != 0) {
            return reviewRepository.insertReview(transactionId, userId, reviewText, fieldId, stars);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.ERROR_MESSAGE, null));
        }

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<List<ReviewModel>>> getAllReviews(int fieldId) {
        MutableLiveData<ResponseModel<List<ReviewModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        if (fieldId != 0) {
            return reviewRepository.getReview(fieldId);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));
        }
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<ReviewModel>> getTotalReview(int fieldId) {
        MutableLiveData<ResponseModel<ReviewModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if (fieldId != 0) {
            return reviewRepository.getTotalReview(fieldId);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));
        }
        return responseModelMutableLiveData;
    }
}
