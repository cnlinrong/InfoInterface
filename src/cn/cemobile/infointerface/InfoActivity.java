package cn.cemobile.infointerface;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class InfoActivity extends Activity {

	private EditText keyWord;
	private Button btnSearch;
	private ListView infoList;
	private Spinner typeList;
	private Spinner areaChoose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.info_activity);
		
		keyWord = (EditText) findViewById(R.id.key_word);
		btnSearch = (Button) findViewById(R.id.btn_search);
		infoList = (ListView) findViewById(R.id.info_list);
		infoList.setAdapter(new InfoListAdapter());
		
		typeList = (Spinner) findViewById(R.id.type_list);
		ArrayAdapter<CharSequence> typeListAdapter = ArrayAdapter.createFromResource(this, R.array.type_list, android.R.layout.simple_spinner_item);
		typeListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeList.setAdapter(typeListAdapter);
		
		areaChoose = (Spinner) findViewById(R.id.area_choose);
		ArrayAdapter<CharSequence> areaChooseAdapter = ArrayAdapter.createFromResource(this, R.array.area_choose, android.R.layout.simple_spinner_item);
		areaChooseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaChoose.setAdapter(areaChooseAdapter);
		
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	private class InfoListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_list_item, null);
			}
			return view;
		}
		
	}
	
}
