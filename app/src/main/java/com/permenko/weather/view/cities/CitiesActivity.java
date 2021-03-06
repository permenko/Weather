package com.permenko.weather.view.cities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.permenko.weather.repository.WeatherRepository;
import com.permenko.weather.util.DbHelper;
import com.permenko.weather.view.cities.dialog.AddCityDialog;
import com.permenko.weather.view.cities.dialog.CompareCityDialog;
import com.permenko.weather.view.cities.dialog.DeleteCityDialog;
import com.permenko.weather.view.cities.dialog.ErrorDialog;
import com.permenko.weather.view.city.CityActivity;
import com.permenko.weather.widget.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CitiesActivity extends AppCompatActivity implements
        CitiesView,
        SwipeRefreshLayout.OnRefreshListener,
        CitiesAdapter.OnItemClickListener,
        DeleteCityDialog.ClickListener,
        AddCityDialog.ClickListener,
        CompareCityDialog.ClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.cities_list)
    RecyclerView mCitiesRecycler;
    @BindView(R.id.progress_bar)
    View mLoadingView;

    private CitiesPresenter mCitiesPresenter;
    private CitiesAdapter mCitiesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        ButterKnife.bind(this);

        initToolbar();
        initRecycler();
        initSwipeRefresh();

        mCitiesPresenter = new CitiesPresenter(this, new WeatherRepository(new DbHelper()));
        mCitiesPresenter.init(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCitiesPresenter != null) {
            mCitiesPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCitiesPresenter != null) {
            mCitiesPresenter.release();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cities, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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

    private void initRecycler() {
        mCitiesRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCitiesRecycler.addItemDecoration(new DividerItemDecoration(this));
        mCitiesAdapter = new CitiesAdapter(this);
        mCitiesRecycler.setAdapter(mCitiesAdapter);
    }

    private void initSwipeRefresh() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void setCitiesToAdapter(@NonNull List<City> cities) {
        mCitiesAdapter.setCities(cities);
    }

    @Override
    public void addCityToAdapter(@NonNull City city) {
        mCitiesAdapter.addCity(city);
    }

    @Override
    public void onRefresh() {
        mCitiesPresenter.onRefreshClick();
    }

    @Override
    public void onItemClick(@NonNull City city) {
        mCitiesPresenter.onItemClick(city);
    }

    @Override
    public void onItemLongClick(@NonNull City city) {
        mCitiesPresenter.onItemLongClick(city);
    }

    @Override
    public void onDialogAddPositiveClick(@NonNull String cityName) {
        mCitiesPresenter.getWeather(cityName);
    }

    @Override
    public void onDialogComparePositiveClick(@NonNull City city) {
        mCitiesPresenter.onAddClick(city);
    }

    @Override
    public void onDialogDeletePositiveClick(@NonNull City city) {
        mCitiesPresenter.onDeleteClick(city);
    }

    @Override
    public void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void showRefreshing() {
        if (!mRefreshLayout.isRefreshing()) mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshing() {
        if (mRefreshLayout.isRefreshing()) mRefreshLayout.setRefreshing(false);
    }

    private void showDialog(@NonNull DialogFragment dialog) {
        dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
    }

    @Override
    public void showCompareCityDialog(@NonNull City city) {
        showDialog(CompareCityDialog.newInstance(city));
    }

    @Override
    public void showAddCityDialog() {
        showDialog(new AddCityDialog());
    }

    @Override
    public void showDeleteCityDialog(@NonNull City city) {
        showDialog(DeleteCityDialog.newInstance(city));
    }

    @Override
    public void showMessage(int messageId) {
        showDialog(ErrorDialog.newInstance(messageId));
    }

    @Override
    public void openCityScreen(@NonNull City city) {
        CityActivity.start(this, city);
    }
}
