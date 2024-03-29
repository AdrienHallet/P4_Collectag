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
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import src.database.Database;
import src.database.object.Book;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final List<Category> modelList = new ArrayList<>();//TODO implement ViewModel for Books, DVDs..
    final List<Category> selectedCategories = new ArrayList<>();
    final List<BaseItem> selectedItems = new ArrayList<>();
    private final Comparator<BaseItem> alphabeticalComparator = new Comparator<BaseItem>() {
        @Override
        public int compare(BaseItem a, BaseItem b) {
            return a.getDisplayText().compareTo(
                    b.getDisplayText());
        }
    };
    public ActionMode mActionMode;
    Menu context_menu;
    CollectionAdapter mAdapter;
    boolean isMultiSelect = false;
    public ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
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
                    deleteSelection();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            isMultiSelect = false;
            clearSelection();
            mActionMode = null;
        }
    };
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StaticEnvironment.setStaticEnvironment(this);
        Database database = new Database(getApplicationContext());

        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler);

        List<BaseItem> loadedList = new ArrayList<>();
        loadedList.addAll(database.getAllBooks());
        modelList.add(new Category("My selection",loadedList));

        for (int i = 0; i < 3; i++) {
            ArrayList<BaseItem> list = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                BaseItem debugItem = new Book("How to cook humans. Tome "+(j+1));
                list.add(debugItem);
            }
            modelList.add(new Category("My category", list));
        }
        mAdapter = new CollectionAdapter(this, modelList, selectedCategories, selectedItems);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, (Toolbar) findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAdapter.onRestoreInstanceState(savedInstanceState);
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
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null) {
                    return false;
                }
                filter(newText);
                mRecyclerView.scrollToPosition(0);
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                endFilter();
                mRecyclerView.scrollToPosition(0);
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
            //TODO Remove it once proper adding GUI&Back end are done.
            snackThis("Added debug item!");
            List<BaseItem> debugList = new ArrayList<>();
            debugList.add(new Book("How to debug with all the grace you can muster."));
            modelList.add(new Category("Debug category", debugList));
            mAdapter.notifyParentInserted(modelList.size() - 1);
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

        if (scanningResult != null && resultCode == RESULT_OK && scanningResult.getContents() != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            // display it on screen
            Log.d("FORMAT: ", scanFormat);
            System.out.println("Content :" + scanContent);
            Log.d("CONTENT: ", scanContent);
        } else {
            snackThis("No scan data was retrieved");
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
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
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

    /// SELECTION METHODS BEGIN

    public void notifySelectionChanged() {
        if (mActionMode != null) {
            mActionMode.setTitle("Items selected : " + getSelectedCount());
        }
        mAdapter.notifyDataSetChanged();
    }

    int getSelectedCount() {
        return selectedCategories.size() + selectedItems.size();
    }

    /**
     * Delete selection from the dataset.
     * Even an adapter with different dataset (like filtered) will delete what's selected
     * from the main one.
     */
    void deleteSelection() {
        modelList.removeAll(selectedCategories);
        mAdapter.notifyParentDataSetChanged(true);
        for(int i = 0; i < selectedItems.size(); i++){
            BaseItem itemToDelete = selectedItems.get(i);
            boolean itemFound = false;
            for(int j = 0; j < modelList.size() && !itemFound; j++) {
                List<BaseItem> group = modelList.get(j).getChildList();
                for (int k = 0; k < group.size() && !itemFound; k++) {
                    if (group.get(k) == itemToDelete) {
                        group.remove(k);
                        mAdapter.notifyChildRemoved(j, k);
                        itemFound = true;
                    }
                }
            }
        }
        clearSelection();
    }

    void clearSelection() {
        selectedCategories.clear();
        selectedItems.clear();
        notifySelectionChanged();
    }

    public void enableSelectionMode() {
        if (!isMultiSelect) {
            clearSelection();
            isMultiSelect = true;
            Toast.makeText(this, "Selection mode enabled", Toast.LENGTH_SHORT).show();//TODO DEBUG TOAST

            if (mActionMode == null) {
                mActionMode = startSupportActionMode(mActionModeCallback);
            }
        }
    }

    /// SELECTION METHODS END

    void filter(String query) {
        final String lowerCaseQuery = query.toLowerCase();
        //TODO filtering
    }

    public void endFilter() {
        //TODO filtering
    }
}
