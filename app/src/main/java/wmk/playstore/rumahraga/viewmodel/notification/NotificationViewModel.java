package wmk.playstore.rumahraga.viewmodel.notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import wmk.playstore.rumahraga.data.repository.notification.NotificationRepository;
import wmk.playstore.rumahraga.model.NotificationModel;
import wmk.playstore.rumahraga.model.ResponseModel;
import wmk.playstore.rumahraga.util.constans.response.ConsResponse;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class NotificationViewModel extends ViewModel {
    private NotificationRepository notificationRepository;

    @Inject
    public NotificationViewModel(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public LiveData<ResponseModel<List<NotificationModel>>> getAllNotification (String userId, int isRead) {
        MutableLiveData<ResponseModel<List<NotificationModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        if (userId != null) {
            return notificationRepository.getNotification(userId, isRead);
        }else{
            responseModelMutableLiveData.setValue(new ResponseModel<>(false,ConsResponse.ERROR_MESSAGE, null));

        }
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> updateNotification (String userId) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (userId != null) {
            return notificationRepository.updateNotification(userId);
        }else{
            responseModelMutableLiveData.setValue(new ResponseModel<>(false,ConsResponse.ERROR_MESSAGE, null));
        }
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> deleteNotification(String id) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (id != null) {
            return notificationRepository.deleteNotification(id);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.ERROR_MESSAGE, null));
        }
        return responseModelMutableLiveData;
    }
}
