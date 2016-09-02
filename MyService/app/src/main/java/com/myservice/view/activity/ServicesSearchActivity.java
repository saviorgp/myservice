package com.myservice.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myservice.R;
import com.myservice.model.Preferences;
import com.myservice.model.component.Advertisement;
import com.myservice.model.component.Category;
import com.myservice.model.component.FilterVO;
import com.myservice.model.component.UserVO;
import com.myservice.model.component.WebServiceWrapper;
import com.myservice.model.transaction.ITransaction;
import com.myservice.model.transaction.TransactionTask;
import com.myservice.utils.AndroidUtils;
import com.myservice.utils.Constants;
import com.myservice.view.adapter.AdvertisementAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServicesSearchActivity extends AppCompatActivity implements ITransaction,  NavigationView.OnNavigationItemSelectedListener {

    private static final int SERVICE_FILTER = 10;

    private JSONObject resultObject = null;
    private String query = "";
    private Integer current_page = 1;
    private ArrayList<Advertisement> advertisementArrayList  = null;
    private AdvertisementAdapter adapter = null;
    private Integer total = 0;
    public boolean isLoading = false;
    private FilterVO filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_services_search);

        query = getIntent().getStringExtra("QUERY");

        ((TextView)findViewById(R.id.txt_query)).setText(query);

        findViewById(R.id.bt_toolbar_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchDialog();
            }
        });

        findViewById(R.id.bt_toolbar_search_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ServicesSearchActivity.this, ServiceFilterActivity.class), SERVICE_FILTER);
            }
        });

        initializeDrawer();
        initializeListView();
    }

    private void showSearchDialog(){

        Dialog dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_search);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    private void initializeDrawer(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ((TextView)navigationView.findViewById(R.id.drawer_user_name_info)).setText((String) Preferences.getPreferences(this).getSharedPreference(Constants.USER_NAME));
        ((TextView)navigationView.findViewById(R.id.drawer_user_email_info)).setText((String) Preferences.getPreferences(this).getSharedPreference(Constants.EMAIL));
    }

    private void initializeListView(){

        advertisementArrayList  = new ArrayList<>();
        adapter = new AdvertisementAdapter(this, advertisementArrayList);

        final ListView listView = (ListView) findViewById(R.id.listService);


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int lastIndexInScreen = visibleItemCount + firstVisibleItem;

                if (lastIndexInScreen >= totalItemCount && !isLoading) {

                    if (adapter.getCount() < total) {

                        current_page++;
                        isLoading = true;
                        startTransacao(ServicesSearchActivity.this);
                    }
                }

            }
        });

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent it = new Intent(ServicesSearchActivity.this, ContactActivity.class);
                it.putExtra("ADVERTISEMENT", advertisementArrayList.get(position));

                startActivity(it);
            }
        });

        startTransacao(this);
    }

    @Override
    public void execute() throws Exception {
        resultObject = WebServiceWrapper.search(query, current_page, filter);
    }

    @Override
    public void updateView() {

        if(resultObject != null) {

            try {
                total = resultObject.getInt("total");
                current_page = resultObject.getInt("current_page");
                JSONArray data = resultObject.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {

                    UserVO userVO = new UserVO(data.getJSONObject(i).getJSONObject("user"));
                    Category category = new Category(data.getJSONObject(i).getJSONObject("category"));

                    Advertisement advertisement = new Advertisement();

                    advertisement.setId(data.getJSONObject(i).getInt("id"));
                    advertisement.setUser(userVO);
                    advertisement.setCategory(category);
                    advertisement.setTitle(data.getJSONObject(i).getString("title"));
                    advertisement.setDescription(data.getJSONObject(i).getString("description"));

                    adapter.add(advertisement);
                }

                adapter.notifyDataSetChanged();
                isLoading=false;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void startTransacao(ITransaction transacao) {

        boolean redeOk = AndroidUtils.isNetworkAvailable(this);

        if (redeOk) {
            TransactionTask task = new TransactionTask(this, transacao, R.string.wait);
            task.execute();
        } else {
            AndroidUtils.alertDialog(this, "Erro de net");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user) {
            // Handle the camera action
        } else if (id == R.id.nav_location) {

        } else if (id == R.id.nav_my_service) {

        } else if (id == R.id.nav_add_service) {
            startActivity(new Intent(this, AddServiceActivity.class));

        } else if (id == R.id.nav_sair) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SERVICE_FILTER && resultCode == RESULT_OK){

            filter = (FilterVO) data.getSerializableExtra(ServiceFilterActivity.FILTER);
            startTransacao(this);
        }
    }
}
