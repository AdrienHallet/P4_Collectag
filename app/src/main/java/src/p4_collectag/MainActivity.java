package src.p4_collectag;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import src.connect.openlibrary.Book;
import src.connect.openlibrary.BookClient;
import src.database.Database;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ActionMode mActionMode;
    Menu context_menu;
    boolean isMultiSelect = false;
    private CollectionAdapter mAdapter;
    private final List<ListItem> displayedList = new ArrayList<>();
    private final Set<ListItem> selectedItems = new HashSet<>();
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multi_select, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    List<ListItem> remainingItems = new ArrayList<>();
                    for(ListItem listItem : displayedList) {
                        if(!selectedItems.contains(listItem)) {
                            remainingItems.add(listItem);
                        }
                    }
                    displayedList.clear();
                    displayedList.addAll(remainingItems);
                    clearSelection();
                    mAdapter.notifyDataSetChanged();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            clearSelection();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StaticEnvironment.setStaticEnvironment(this);
        Database database = new Database(getApplicationContext());

        database.addBook();

        ArrayList<String> myBooks;
        myBooks = database.getAllBooks();

        for (String string : myBooks) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
        }

        //FIXME This is a DEBUG LIST
        displayedList.clear();
        for (int i = 0; i < 2; i++) {
            displayedList.add(new ListItem() {
                @Override
                public int getDisplayText() {
                    return R.string.default_item_name;
                }

                @Override
                public int getDisplayImage() {
                    return R.drawable.ic_menu_gallery;
                }
            });
        }

        /// BEGIN
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.list);

        mRecyclerView.setHasFixedSize(false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CollectionAdapter(this, displayedList, selectedItems);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    toggleSelection(position);
                else
                    //TODO DISPLAY ITEM DATA
                    Toast.makeText(getApplicationContext(), "Details Page", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    if(selectedItems.size() > 0)
                    {
                         clearSelection();
                    }
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }
                toggleSelection(position);
            }
        }));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, (Toolbar) findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void toggleSelection(int position) {
        if (mActionMode != null) {
            ListItem item = displayedList.get(position);
            if (selectedItems.contains(item)) {
                selectedItems.remove(item);
            } else {
                selectedItems.add(item);
            }

            mActionMode.setTitle("Items selected : " + selectedItems.size());
            mAdapter.notifyItemChanged(position);
        }
    }

    public void clearSelection() {
        selectedItems.clear();
        mAdapter.notifyDataSetChanged();
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
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //TODO Here we choose if we search directly when user types or/and when he clicks on submit
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.length() < 3) {
                    return false;
                }
                return true;
            }
        });
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_scan) {
            scanNow();
        } else if (id == R.id.nav_add) {
            snackThis("Added!");//TODO
            displayedList.add(new ListItem() {
                @Override
                public int getDisplayText() {
                    return R.string.default_item_name;
                }

                @Override
                public int getDisplayImage() {
                    return R.drawable.ic_menu_gallery;
                }
            });
            mAdapter.notifyItemInserted(displayedList.size() - 1);
        } else if (id == R.id.nav_gallery) {
            snackThis("Pressed button!");//TODO
        } else if (id == R.id.nav_slideshow) {
            snackThis("Pressed button!");//TODO
        } else if (id == R.id.nav_settings) {
            snackThis("Pressed button!");//TODO
        } else if (id == R.id.nav_share) {
            snackThis("Pressed button!");//TODO
        } else if (id == R.id.nav_send) {
            snackThis("Pressed button!");//TODO
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void scanNow() {
        if (isCameraAccessible()) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
            integrator.setPrompt("Scan a barcode");
            integrator.setResultDisplayDuration(0);
            integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.initiateScan();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            // display it on screen

            Log.d("FORMAT: ", scanFormat);
            System.out.println("Content :" + scanContent);
            Log.d("CONTENT: ", scanContent);

        } else {
            snackThis("No scan data received!");
        }
    }

    private boolean isCameraAccessible() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) return true;
        else {
            System.out.println("We got no right to access camera");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1337);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            //TODO
            case 1337: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Display text in a {@link Snackbar} without action, modern alternative to the old {@link Toast}
     *
     * @param toasting Text that should be displayed.
     */
    public void snackThis(String toasting) {
        Snackbar.make(findViewById(android.R.id.content), toasting, Snackbar.LENGTH_LONG)
                .show();
    }
}
