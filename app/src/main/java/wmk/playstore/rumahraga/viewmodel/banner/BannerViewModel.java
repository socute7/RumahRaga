package wmk.playstore.rumahraga.viewmodel.banner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import wmk.playstore.rumahraga.data.repository.banner.BannerRepository;
import wmk.playstore.rumahraga.model.BannerModel;
import wmk.playstore.rumahraga.model.ResponseModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BannerViewModel extends ViewModel {
    private BannerRepository bannerRepository;

    @Inject
    public BannerViewModel(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    public LiveData<ResponseModel<List<BannerModel>>> getAllBanner() {
        return bannerRepository.getAllBanner();
    }
}
