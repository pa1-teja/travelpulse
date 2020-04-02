package com.trimax.vts.view.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.trimax.vts.view.R;
import com.trimax.vts.view.databinding.ActivityUserListBinding;
import com.trimax.vts.view.login.LoginViewModel;
import com.trimax.vts.view.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UserListActivity";
    private ActivityUserListBinding userListBinding;
    private UsersAdapter adapter;
    private List<UserModel> users = new ArrayList<>();
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Authorized Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userListBinding = DataBindingUtil.setContentView(this,R.layout.activity_user_list);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        userListBinding.tvAddUser.setOnClickListener(this);
        userListBinding.rvUsers.setHasFixedSize(true);
        userListBinding.rvUsers.setItemAnimator(new DefaultItemAnimator());
        userListBinding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsersAdapter(users,this);
        userListBinding.rvUsers.setAdapter(adapter);

        viewModel.getLoading().observe(this, isLoading->{
            if (isLoading)
                userListBinding.pbProgress.setVisibility(View.VISIBLE);
            else
                userListBinding.pbProgress.setVisibility(View.GONE);

        });

        viewModel.getUsers().observe(this, users->{
            adapter.addUsers(users);
        });

        viewModel.getSecondaryUsers();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_add_user:
                startActivity(new Intent(this,AddUserActivity.class));
                break;

            case R.id.rl_container:

                break;

            case R.id.cp_active:
                UserModel user = (UserModel) view.getTag();
                if (user.getStatus().equalsIgnoreCase("Active"))
                    user.setStatus("Deactive");
                else
                    user.setStatus("Active");
                viewModel.changeSecondaryUserStatus(user);
                break;
        }
    }
}
