package com.example.numberslight.view.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.numberslight.App;
import com.example.numberslight.R;
import com.example.numberslight.di.NoNetworkConnectionInterceptor;
import com.example.numberslight.service.model.NumberDataDetails;
import com.example.numberslight.utils.Ext;
import com.example.numberslight.viewmodel.NumberDetailsViewModel;
import com.example.numberslight.viewmodel.NumberDetailsViewModelFactory;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

import static com.example.numberslight.utils.ConstantsKt.BUNDLE_NAME;
import static com.example.numberslight.utils.ConstantsKt.EMPTY;

public class NumberDetailsFragment1 extends Fragment {
    @Inject
    public NumberDetailsViewModelFactory viewModelFactory;

    @Nullable
    private Snackbar snackbar;

    private NumberDetailsViewModel viewModel;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView textView = getActivity().findViewById(R.id.textView);
        final ImageView imageView = getActivity().findViewById(R.id.imageView);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(NumberDetailsViewModel.class);
        viewModel.getDetails(getShownName());
        viewModel.getNumberDetails().observe(getViewLifecycleOwner(), new Observer<NumberDataDetails>() {


            @Override
            public void onChanged(NumberDataDetails numberDataDetails) {
                textView.setText(numberDataDetails.getText());
                Picasso.get().load(numberDataDetails.getImage()).into(imageView);
            }
        });

        viewModel.state().observe(getViewLifecycleOwner(), new Observer<NumberDetailsViewModel.State>() {
            @Override
            public void onChanged(NumberDetailsViewModel.State state) {
                handleError(state);
            }
        });
    }

    private void handleError(@Nullable NumberDetailsViewModel.State state) {
        if (state instanceof NumberDetailsViewModel.State.Success) {
            if (snackbar != null) {
                snackbar.dismiss();
            }
        } else if (state instanceof NumberDetailsViewModel.State.Error) {
            if (((NumberDetailsViewModel.State.Error) state).getError() instanceof NoNetworkConnectionInterceptor.NoNetworkConnectionException) {
                final String finalName = getShownName();
                snackbar = Ext.handleNoNetworkError(getActivity().findViewById(android.R.id.content), new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        viewModel.getDetails(finalName);
                        return Unit.INSTANCE;
                    }
                }, getResources());

            } else {
                Toast.makeText(
                        getContext(),
                        getString(R.string.unexpected_error),
                        Toast.LENGTH_LONG
                ).show();
            }

        }
    }

    public String getShownName() {
        if (getArguments() == null) {
            return EMPTY;
        } else {
            return getArguments().getString(BUNDLE_NAME, EMPTY);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        if (container == null) {
            return null;
        }

        View inflate = inflater.inflate(R.layout.number_details_view, container, false);
        ((App) getActivity().getApplication()).component().inject(this);
        return inflate;
    }


    static public NumberDetailsFragment1 newInstance(@Nullable String name) {
        NumberDetailsFragment1 fragment = new NumberDetailsFragment1();
        Bundle args = new Bundle();
        args.putString(BUNDLE_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }
}
