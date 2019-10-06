package com.example.runislife;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.runislife.data.AppDatabase;

public class Run extends Application {

	public static Run instance;
	private AppDatabase database;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		Conf.init(getApplicationContext());
		database = Room.databaseBuilder(this, AppDatabase.class, "database").addCallback(new RoomDatabase.Callback() {
			@Override
			public void onCreate(@NonNull SupportSQLiteDatabase db) {
				super.onCreate(db);
				OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SeedDatabaseWorker.class).build();
				WorkManager.getInstance().enqueue(request);
			}
		}).build();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public AppDatabase getDatabase() {
		return database;
	}

}