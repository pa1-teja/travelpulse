package com.trimax.vts.view.users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trimax.vts.repository.UserRepository;

public class UserViewModel extends ViewModel {
    private static final String TAG = "UserViewModel";
    private UserRepository repository;
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();


    public UserViewModel() {
        this.repository = new UserRepository();
    }

    public LiveData<Boolean> getLoading(){
        return loading;
    }


}
