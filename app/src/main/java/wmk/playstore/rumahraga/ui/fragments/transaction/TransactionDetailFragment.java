package wmk.playstore.rumahraga.ui.fragments.transaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentTransactionDetailBinding;
import wmk.playstore.rumahraga.model.ResponseModel;
import wmk.playstore.rumahraga.model.TransactionDetailModel;
import wmk.playstore.rumahraga.ui.adapters.transactions.TransactionDetailAdapter;
import wmk.playstore.rumahraga.ui.fragments.ticket.TicketFragment;
import wmk.playstore.rumahraga.util.constans.other.ConsOther;
import wmk.playstore.rumahraga.util.constans.sharedpref.ConsSharedPref;
import wmk.playstore.rumahraga.viewmodel.transaction.TransactionDetailViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;


@AndroidEntryPoint
public class TransactionDetailFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private String transactionCode, reason, fieldName, fieldAddress, createdAt;
    private int status;
    private TransactionDetailViewModel transactionDetailViewModel;
    private TransactionDetailAdapter transactionDetailAdapter;
    private List<TransactionDetailModel> transactionDetailModelist;
    private BottomSheetBehavior bottomSheetReason;


    private FragmentTransactionDetailBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionDetailBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();

        binding.tvMetodePembayaran.setText(getArguments().getString("payment_name", null));
        binding.tvDate.setText(getArguments().getString("order_date", null));
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        binding.tvFinalTotalTransaction.setText("Rp. " + decimalFormat.format(getArguments().getInt("total_price", 0)));
        initBottomSheetReason();
        bottomSheetReason.setState(BottomSheetBehavior.STATE_HIDDEN);



        if (status == 1) { // transaction success
            binding.tvStatus.setText("Pembayaran berhasil divalidasi!");
            binding.fab.setText("Lihat Tiket");
            binding.fab.setVisibility(View.VISIBLE);
            // set animation
            binding.lottieAnimation.setAnimation(R.raw.success_anim);
            binding.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new TicketFragment();
                    Bundle arf = new Bundle();
                    arf.putString("transaction_code", transactionCode);
                    arf.putString("field_name", fieldName);
                    arf.putString("field_address", fieldAddress);
                    arf.putInt("total_price", getArguments().getInt("total_price", 0));
                    arf.putSerializable("transaction_detail", (Serializable) transactionDetailModelist);
                    arf.putSerializable("transaction_date", createdAt);
                    fragment.setArguments(arf);
                    fragmentTransaction(fragment);
                }
            });
        }else if (status == 2) {
            binding.tvStatus.setText("Pembayaran sedang diproses");
            binding.fab.setVisibility(View.GONE);
            // set animation
            binding.lottieAnimation.setAnimation(R.raw.progress_anim);
            binding.tvFinalTotalTransaction.setTextColor(getContext().getColor(R.color.yellow));

        }else {
            binding.tvStatus.setText("Pembayaran tidak valid!");

            if (reason != null && !reason.equals("")) {
                binding.fab.setText("Lihat alasan");
                binding.fab.setVisibility(View.VISIBLE);
                binding.etReason.setText(reason);
                binding.fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomSheetReason();
                    }
                });

                showBottomSheetReason();

            }else {
                binding.fab.setVisibility(View.GONE);
                binding.etReason.setText("Tidak ada alasan");
            }
            binding.tvFinalTotalTransaction.setTextColor(getContext().getColor(R.color.red));
            // set animation
            binding.lottieAnimation.setAnimation(R.raw.failed_animation);



        }


         binding.tvTransactionCode.setText(transactionCode);

        getDetailTransaction();





    }
    private void init() {
        sharedPreferences = getContext().getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);


        transactionCode = getArguments().getString("transaction_code", null);
        status = getArguments().getInt("status", 0);
        reason = getArguments().getString("reason", null);
        transactionDetailViewModel = new ViewModelProvider(this).get(TransactionDetailViewModel.class);


    }

    private void listener() {
        binding.btnBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
        binding.vOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideBottomSheetReason();
            }
        });
    }


    private void getDetailTransaction() {
        if (transactionCode != null) {
            binding.shimmerMain.setVisibility(View.VISIBLE);
            binding.shimmerMain.startShimmer();
            binding.lrMain.setVisibility(View.GONE);
            transactionDetailViewModel.getDetailTransaction(transactionCode).observe(getViewLifecycleOwner(),
                    new Observer<ResponseModel<List<TransactionDetailModel>>>() {
                        @Override
                        public void onChanged(ResponseModel<List<TransactionDetailModel>> listResponseModel) {
                            if (listResponseModel.isStatus() == true) {
                                transactionDetailModelist = listResponseModel.getData();
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                transactionDetailAdapter = new TransactionDetailAdapter(getContext(), transactionDetailModelist);
                                binding.rvItem.setAdapter(transactionDetailAdapter);
                                binding.rvItem.setLayoutManager(linearLayoutManager);
                                binding.rvItem.setHasFixedSize(true);

                                fieldName = listResponseModel.getData().get(0).getField_name();
                                fieldAddress = listResponseModel.getData().get(0).getField_address();
                                createdAt = listResponseModel.getData().get(0).getTransaction_date();

                                binding.tvTotalHour.setText(String.valueOf(transactionDetailModelist.size()) + " Jam");


                                // rekayasa tampilan
                                binding.shimmerMain.setVisibility(View.GONE);
                                binding.lrMain.setVisibility(View.VISIBLE);



                            }else {
                                showToast(ConsOther.TOAST_ERR, listResponseModel.getMessage());

                            }
                        }
                    });
        }
    }

    private void initBottomSheetReason() {
        bottomSheetReason = BottomSheetBehavior.from(binding.bottomSheetAlasan);
        bottomSheetReason.setHideable(true);
        bottomSheetReason.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBottomSheetReason();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void showBottomSheetReason() {
        bottomSheetReason.setState(BottomSheetBehavior.STATE_COLLAPSED);
        binding.vOverlay.setVisibility(View.VISIBLE);
    }

    private void hideBottomSheetReason() {
        bottomSheetReason.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
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



}