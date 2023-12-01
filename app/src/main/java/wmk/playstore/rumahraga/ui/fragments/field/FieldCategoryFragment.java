package wmk.playstore.rumahraga.ui.fragments.field;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentFieldCategoryBinding;
import wmk.playstore.rumahraga.model.FieldModel;
import wmk.playstore.rumahraga.model.ResponseModel;
import wmk.playstore.rumahraga.model.listener.ItemClickListener;
import wmk.playstore.rumahraga.ui.adapters.fields.FieldMainAdapter;
import wmk.playstore.rumahraga.util.constans.other.ConsOther;
import wmk.playstore.rumahraga.util.constans.response.ConsResponse;
import wmk.playstore.rumahraga.viewmodel.field.FieldViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;


@AndroidEntryPoint
public class FieldCategoryFragment extends Fragment implements ItemClickListener {
    private FieldViewModel fieldViewModel;
    private String categoryId;
    private FragmentFieldCategoryBinding binding;
    private List<FieldModel> fieldModelList;
    private FieldMainAdapter fieldMainAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFieldCategoryBinding.inflate(inflater, container, false);

        init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getField();

        binding.tvCategories.setText(getArguments().getString("name", ""));


    }

    private void init() {
        fieldViewModel = new ViewModelProvider(this).get(FieldViewModel.class);
        categoryId = getArguments().getString("category_id", "");




    }

    private void listener() {
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getField();
                binding.swipeRefresh.setRefreshing(false);
            }
        });
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        binding.btnBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
    }

    private void getField(){
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        binding.rvField.setVisibility(View.GONE);
        binding.tvEmpty.setVisibility(View.GONE);

        if(categoryId != null) {
            fieldViewModel.getFieldByCategory(categoryId).observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<FieldModel>>>() {
                @Override
                public void onChanged(ResponseModel<List<FieldModel>> listResponseModel) {
                    binding.rvField.setVisibility(View.VISIBLE);
                    binding.shimmer.setVisibility(View.GONE);
                    if (listResponseModel.isStatus() == true) {
                        if (listResponseModel.getData().size() > 0) {
                            fieldModelList = listResponseModel.getData();
                            fieldMainAdapter = new FieldMainAdapter(getContext(), fieldModelList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            binding.rvField.setLayoutManager(linearLayoutManager);
                            binding.rvField.setAdapter(fieldMainAdapter);
                            binding.rvField.setHasFixedSize(true);
                            fieldMainAdapter.setItemClickListener(FieldCategoryFragment.this);
                        }else {
                            binding.tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }else {
                        binding.tvEmpty.setText("Tidak ada data");
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                        showToast(ConsOther.TOAST_ERR, listResponseModel.getMessage());
                    }
                }
            });
        }else {
            showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);

        }

    }

    private void filter(String query) {
        if (fieldModelList != null) {
            ArrayList<FieldModel> filteredList = new ArrayList<>();
            for (FieldModel item : fieldModelList) {
                if (fieldModelList != null && item.getName().toLowerCase().contains(query.toLowerCase()) || item.getCity_name().toLowerCase()
                        .contains(query.toLowerCase())){
                    filteredList.add(item);
                }

                fieldMainAdapter.filter(filteredList);
                if (!filteredList.isEmpty()) {
                    fieldMainAdapter.filter(filteredList);
                    binding.tvEmpty.setVisibility(View.GONE);
                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    binding.tvEmpty.setText("Lapangan tidak ditemukan");
                }

            }
        }

    }

    private void showToast(String type, String message) {
        if (type.equals(ConsOther.TOAST_SUCCESS)) {
            Toasty.success(getContext(), message, Toasty.LENGTH_SHORT).show();
        }else if (type.equals(ConsOther.TOAST_NORMAL)){
            Toasty.normal(getContext(), message, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(getContext(), message, Toasty.LENGTH_SHORT).show();

        }
    }

    private void fragmentTransaction(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frameMain, fragment)
                .commit();
    }

    @Override
    public void onItemClickListener(String type, int positon, Object object) {
        FieldModel fieldModel = (FieldModel) object;
        if (fieldModel.getField_id() != null) {
            Fragment fragment = new FieldDetailFragment();
            Bundle arg = new Bundle();
            arg.putString("field_id", fieldModel.getField_id());
            fragment.setArguments(arg);
            fragmentTransaction(fragment);
        }else {
            showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
        }

    }
}