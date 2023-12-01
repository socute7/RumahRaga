package wmk.playstore.rumahraga.viewmodel.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import wmk.playstore.rumahraga.data.repository.event.EventRepository;
import wmk.playstore.rumahraga.model.EventModel;
import wmk.playstore.rumahraga.model.ResponseModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EventViewModel extends ViewModel {

    private EventRepository eventRepository;

    @Inject
    public EventViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public LiveData<ResponseModel<List<EventModel>>> getEvent() {
        return eventRepository.getEvents();
    }
}
