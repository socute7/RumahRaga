package wmk.playstore.rumahraga.viewmodel.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import wmk.playstore.rumahraga.data.repository.category.CategoryRepository;
import wmk.playstore.rumahraga.model.CategoryModel;
import wmk.playstore.rumahraga.model.ResponseModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CategoryViewModel extends ViewModel {
    private CategoryRepository categoryRepository;
    @Inject
    public CategoryViewModel(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public LiveData<ResponseModel<List<CategoryModel>>> getAllCategory() {
        return categoryRepository.getAllCategory();
    }
}
