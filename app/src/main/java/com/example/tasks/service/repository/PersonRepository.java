package com.example.tasks.service.repository;

import android.content.Context;

import com.example.tasks.R;
import com.example.tasks.service.constants.TaskConstants;
import com.example.tasks.service.listener.APIListener;
import com.example.tasks.service.model.PersonModel;
import com.example.tasks.service.repository.local.SecurityPreferences;
import com.example.tasks.service.repository.remote.BaseRepository;
import com.example.tasks.service.repository.remote.PersonService;
import com.example.tasks.service.repository.remote.RetrofitClient;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonRepository extends BaseRepository {

    private PersonService mPersonService;
    private SecurityPreferences mSercurityPreferences;

    public PersonRepository(Context context) {
        super(context);
        this.mContext = context;
        this.mSercurityPreferences = new SecurityPreferences(context);
        this.mPersonService = RetrofitClient.createService(PersonService.class);
    }

    public void create(String name, String email, String password, final APIListener<PersonModel> listener) {

        if (super.isConnectionAvailable) {
            listener.onFaliure(mContext.getString(R.string.ERROR_INTERNET_CONNECTION));
            return;
        }

        Call<PersonModel> call = this.mPersonService.create(name, email, password, true);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFaliure(handleFailure(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<PersonModel> call, Throwable t) {
                listener.onFaliure(mContext.getString(R.string.ERROR_UNEXPECTED));
            }
        });
    }

    public void login(String email, String password, final APIListener<PersonModel> listener) {

        if (super.isConnectionAvailable) {
            listener.onFaliure(mContext.getString(R.string.ERROR_INTERNET_CONNECTION));
            return;
        }

        Call<PersonModel> call = this.mPersonService.login(email, password);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {

                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFaliure(handleFailure(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<PersonModel> call, Throwable t) {
                listener.onFaliure(mContext.getString(R.string.ERROR_INTERNET_CONNECTION));
            }
        });
    }

    public void saveUserData(PersonModel model) {
        this.mSercurityPreferences.storeString(TaskConstants.SHARED.TOKEN_KEY, model.getToken());
        this.mSercurityPreferences.storeString(TaskConstants.SHARED.PERSON_KEY, model.getPersonKey());
        this.mSercurityPreferences.storeString(TaskConstants.SHARED.PERSON_NAME, model.getName());
        this.mSercurityPreferences.storeString(TaskConstants.SHARED.PERSON_EMAIL, model.getEmail());

        RetrofitClient.saveHeaders(model.getToken(), model.getPersonKey()); //adicionamos os headres na requisição
    }

    public void clearUserDate() {                 // MÉTODO LOGOUT
        this.mSercurityPreferences.remove(TaskConstants.SHARED.TOKEN_KEY);
        this.mSercurityPreferences.remove(TaskConstants.SHARED.PERSON_KEY);
        this.mSercurityPreferences.remove(TaskConstants.SHARED.PERSON_NAME);
    }

    public PersonModel getUserData() {
        PersonModel model = new PersonModel();
        model.setName(this.mSercurityPreferences.storeString(TaskConstants.SHARED.PERSON_NAME));
        model.setToken(this.mSercurityPreferences.storeString(TaskConstants.SHARED.TOKEN_KEY));
        model.setPersonKey(this.mSercurityPreferences.storeString(TaskConstants.SHARED.PERSON_KEY));
        model.setEmail(this.mSercurityPreferences.storeString(TaskConstants.SHARED.PERSON_EMAIL));

        return model;
    }
}