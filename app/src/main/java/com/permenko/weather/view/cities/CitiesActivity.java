package com.permenko.weather.view.cities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.permenko.weather.R;
import com.permenko.weather.model.City;
import com.permenko.weather.view.cities.dialog.AddCityDialog;
import com.permenko.weather.view.cities.dialog.CompareCityDialog;
import com.permenko.weather.view.cities.dialog.DeleteCityDialog;
import com.permenko.weather.view.cities.dialog.ErrorDialog;
import com.permenko.weather.view.city.CityActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

//todo: add @NonNull, @Nullable to everywhere
public class CitiesActivity extends AppCompatActivity
        implements DeleteCityDialog.ClickListener, AddCityDialog.ClickListener, CompareCityDialog.ClickListener, CitiesAdapter.OnItemClickListener, CitiesView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.refresh)
    //todo: add colors
            SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.cities_list)
    RecyclerView mCitiesRecycler;
    @BindView(R.id.progress_bar)
    View mLoadingView;

    private CitiesPresenter mCitiesPresenter;
    private CitiesAdapter mCitiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        ButterKnife.bind(this);

        initToolbar();
        //todo: move to initSomething, find a method name
        mCitiesRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRefreshLayout.setOnRefreshListener(() -> {
            mCitiesPresenter.onRefresh();
        });
        mCitiesPresenter = new CitiesPresenter(this);
        mCitiesPresenter.init(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCitiesPresenter != null) {
            mCitiesPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCitiesPresenter != null) {
            mCitiesPresenter.onStop();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cities, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_city:
                mCitiesPresenter.onMenuAddClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
    }

    @Override
    public void openCityScreen(@NonNull City city) {
        CityActivity.start(this, city);
    }

    @Override
    public void showDialog(@NonNull DialogFragment dialog) {
        dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
    }

    //todo: move logic to presenter (maybe should use small presenter for each dialog)
    @Override
    public void getWeather(@NonNull String cityName) {
        mCitiesPresenter.getWeather(cityName);
    }

    @Override
    public void addCityToAdapter(@NonNull City city) {
        mCitiesAdapter.addCity(city);
    }

    @Override
    public void deleteCityFromAdapter(int position) {
        mCitiesAdapter.deleteCity(position);
    }

    //todo: move logic to presenter (maybe should use small presenter for each dialog)
    @Override
    public void addCity(@NonNull City city) {
        mCitiesPresenter.addCity(city);
    }

    @Override
    public void showMessage(int messageId) {
        showDialog(ErrorDialog.newInstance(messageId));
    }

    @Override
    public void addCitiesToAdapter(@NonNull ArrayList<City> cities) {
        if (mRefreshLayout.isRefreshing()) mRefreshLayout.setRefreshing(false);
        //this.cities = cities;
        mCitiesAdapter = new CitiesAdapter(cities, this);
        mCitiesRecycler.setAdapter(mCitiesAdapter);
        mCitiesRecycler.invalidate();
    }

    @Override
    public void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mLoadingView.setVisibility(View.GONE);
    }

    //todo: move logic to presenter (maybe should use small presenter for each dialog)
    @Override
    public void deleteCity(int position, @NonNull City city) {
        mCitiesPresenter.deleteCity(position, city);
    }

    @Override
    public void onItemClick(@NonNull City city) {
        mCitiesPresenter.onItemClick(city);
    }

    @Override
    public void onItemLongClick(@NonNull City city, int position) {
        mCitiesPresenter.onItemLongClick(city, position);
    }
}
