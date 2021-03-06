package com.example.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tasks.service.listener.APIListener;
import com.example.tasks.service.listener.Feedback;
import com.example.tasks.service.model.PriorityModel;
import com.example.tasks.service.model.TaskModel;
import com.example.tasks.service.repository.PriorityRepository;
import com.example.tasks.service.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private PriorityRepository mPriorityRepository;
    private TaskRepository mTaskRepository;

    private MutableLiveData<List<PriorityModel>> mListPriority = new MutableLiveData<>();
    public LiveData<List<PriorityModel>> listPriority = this.mListPriority;

    private MutableLiveData<TaskModel> mTask = new MutableLiveData<>();
    public LiveData<TaskModel> task = this.mTask;

    private MutableLiveData<Feedback> mFeedback = new MutableLiveData<>();
    public LiveData<Feedback> feedback = this.mFeedback;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        this.mPriorityRepository = new PriorityRepository(application);
        this.mTaskRepository = new TaskRepository(application);
    }

    public void getList() {
        List<PriorityModel> lst = this.mPriorityRepository.getList();
        this.mListPriority.setValue(lst);
    }

    public void save(TaskModel task) {
        APIListener<Boolean> listener = new APIListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                mFeedback.setValue(new Feedback());
            }

            @Override
            public void onFaliure(String massage) {
                mFeedback.setValue(new Feedback(massage));
            }
        };

        if (task.getId() == 0) {
            this.mTaskRepository.create(task, listener); //inser????o ou cria????o ou salvar
        } else {
                this.mTaskRepository.update(task, listener);  //atualiza cadastro
        }
    }

    public void load(int mTaskId) {
        this.mTaskRepository.load(mTaskId, new APIListener<TaskModel>() {
            @Override
            public void onSuccess(TaskModel result) {
                mTask.setValue(result);
            }

            @Override
            public void onFaliure(String massage) {
                mFeedback.setValue(new Feedback(massage));
            }
        });
    }
}