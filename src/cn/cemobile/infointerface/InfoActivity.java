package cn.cemobile.infointerface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.cookie.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.cemobile.infointerface.model.Counsel;
import cn.cemobile.infointerface.model.RequestInfo;
import cn.cemobile.infointerface.util.CommonData;
import cn.cemobile.infointerface.util.HttpUtil;

public class InfoActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

	private SearchView search;
	private ListView infoList;
	private Spinner typeList;
	private Spinner areaChoose;
	private ProgressBar mProgressBar;
	private TextView emptyView;
	
	private boolean firstCaseType = true;;
	private boolean firstCity = true;
	
	private InfoListAdapter infoListAdapter = new InfoListAdapter();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.info_activity);
		
		search = (SearchView) findViewById(R.id.search);
		search.setOnQueryTextListener(this);
		search.setSubmitButtonEnabled(false);
		
		new CounselAsyncTask().execute();
		
		infoList = (ListView) findViewById(R.id.info_list);
		infoList.setAdapter(infoListAdapter);
		
		typeList = (Spinner) findViewById(R.id.type_list);
		ArrayAdapter<CharSequence> typeListAdapter = ArrayAdapter.createFromResource(this, R.array.type_list, android.R.layout.simple_spinner_item);
		typeListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeList.setAdapter(typeListAdapter);
		typeList.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (firstCaseType) {
					firstCaseType = false;
					return;
				}
				
				RequestInfo requestInfo = new RequestInfo();
				String caseType = (String) parent.getAdapter().getItem(position);
				if (!"咨询类型".equals(caseType)) {					
					requestInfo.setCasetype(caseType);
				}
				String city = (String) areaChoose.getSelectedItem();
				if (!"地区选择".equals(city)) {					
					requestInfo.setCity(city);
				}
				
				String query = search.getQuery().toString();
				requestInfo.setTitle(query);
				
				refreshList(requestInfo);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
			
		});
		
		areaChoose = (Spinner) findViewById(R.id.area_choose);
		ArrayAdapter<CharSequence> areaChooseAdapter = ArrayAdapter.createFromResource(this, R.array.area_choose, android.R.layout.simple_spinner_item);
		areaChooseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaChoose.setAdapter(areaChooseAdapter);
		areaChoose.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (firstCity) {
					firstCity = false;
					return;
				}
				
				RequestInfo requestInfo = new RequestInfo();
				String caseType = (String) typeList.getSelectedItem();
				if (!"咨询类型".equals(caseType)) {					
					requestInfo.setCasetype(caseType);
				}
				String city = (String) parent.getAdapter().getItem(position);
				if (!"地区选择".equals(city)) {					
					requestInfo.setCity(city);
				}
				
				String query = search.getQuery().toString();
				requestInfo.setTitle(query);
				
				refreshList(requestInfo);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
			
		});
		
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		
		emptyView = (TextView) findViewById(R.id.empty);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_publish) {
        	Toast.makeText(InfoActivity.this, "我要发布", Toast.LENGTH_SHORT).show();
        	return true;
        }
        if (id == R.id.btn_refresh) {
        	RequestInfo requestInfo = new RequestInfo();
        	String caseType = (String) typeList.getSelectedItem();
			if (!"咨询类型".equals(caseType)) {					
				requestInfo.setCasetype(caseType);
			}
			String city = (String) areaChoose.getSelectedItem();
			if (!"地区选择".equals(city)) {					
				requestInfo.setCity(city);
			}
			
			String query = search.getQuery().toString();
			requestInfo.setTitle(query);
			
        	refreshList(requestInfo);
            return true;
        }
        if (id == R.id.btn_about) {
        	Toast.makeText(InfoActivity.this, "关于", Toast.LENGTH_SHORT).show();
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	private class InfoListAdapter extends BaseAdapter {

		private List<Counsel> items = new ArrayList<Counsel>();
		
		public void setItems(List<Counsel> items) {
			this.items.clear();
			this.items.addAll(items);
			
			//notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			TextView textView1;
			TextView textView2;
			TextView textView3;
			TextView textView4;
			if (view == null || (view != null && view.getTag() != getItem(position))) {
				Counsel counsel = (Counsel) getItem(position);
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_list_item, null);
				textView1 = (TextView) view.findViewById(R.id.text1);
				textView1.setText(formatDate(counsel.getCreate_date()));
				
				textView2 = (TextView) view.findViewById(R.id.text2);
				textView2.setText(getStatusText(counsel.getStatus()));
				textView2.setTextColor(getStatusColor(counsel.getStatus()));
				
				textView3 = (TextView) view.findViewById(R.id.text3);
				textView3.setText(counsel.getTitle());
				
				textView4 = (TextView) view.findViewById(R.id.text4);
				textView4.setText(counsel.getContent());
				
				view.setTag(counsel);
			}
			return view;
		}
		
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		//Toast.makeText(InfoActivity.this, "onQueryTextSubmit", Toast.LENGTH_SHORT).show();
		
		RequestInfo requestInfo = new RequestInfo();
		System.out.println(search.getQuery());
		String queryStr = search.getQuery().toString();
		requestInfo.setTitle(queryStr);
		//requestInfo.setContent(queryStr);
		
		String caseType = (String) typeList.getSelectedItem();
		if (!"咨询类型".equals(caseType)) {					
			requestInfo.setCasetype(caseType);
		}
		String city = (String) areaChoose.getSelectedItem();
		if (!"地区选择".equals(city)) {					
			requestInfo.setCity(city);
		}
		
		refreshList(requestInfo);
		
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		//Toast.makeText(InfoActivity.this, "onQueryTextChange", Toast.LENGTH_SHORT).show();
		
		RequestInfo requestInfo = new RequestInfo();
		System.out.println(search.getQuery());
		String query = search.getQuery().toString();
		requestInfo.setTitle(query);
		//requestInfo.setContent(query);
		
		String caseType = (String) typeList.getSelectedItem();
		if (!"咨询类型".equals(caseType)) {					
			requestInfo.setCasetype(caseType);
		}
		String city = (String) areaChoose.getSelectedItem();
		if (!"地区选择".equals(city)) {					
			requestInfo.setCity(city);
		}
		
		refreshList(requestInfo);
		
		//infoListAdapter.notifyDataSetChanged();
		
		return false;
	}
	
	private class CounselAsyncTask extends AsyncTask<RequestInfo, Void, String> {
		
		@Override
		protected String doInBackground(RequestInfo... params) {
			RequestInfo requestInfo = new RequestInfo();
			if (params.length > 0) {
				requestInfo = params[0];
			}
			String result = null;
			HashMap<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("userid", requestInfo.getUserid());
			requestParams.put("title", requestInfo.getTitle());
			requestParams.put("content", requestInfo.getContent());
			requestParams.put("province", requestInfo.getProvince());
			requestParams.put("city", requestInfo.getCity());
			requestParams.put("casetype", requestInfo.getCasetype());
			requestParams.put("status", requestInfo.getStatus());
			requestParams.put("lawerid", requestInfo.getLawerid());
			requestParams.put("istip", requestInfo.getIstip());
			requestParams.put("pageno", requestInfo.getPageno());
			requestParams.put("pagesize", requestInfo.getPagesize());
			try {
				result = HttpUtil.httpPost(CommonData.INFO_LIST, requestParams);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.err.println("***********" + result + "***********");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			infoListAdapter.setItems(json2List(result));
			mProgressBar.setVisibility(View.GONE);
			infoList.setVisibility(View.VISIBLE);
			infoList.setEmptyView(emptyView);
			
			//infoList.invalidate();
		}
		
	}
	
	private List<Counsel> json2List(String result) {
		List<Counsel> counsels = new ArrayList<Counsel>();
		try {
			JSONObject resultJSON = new JSONObject(result);
			String resultcode = resultJSON.getString("resultcode");
			if ("0000".equals(resultcode)) {
				String resultdesc = resultJSON.getString("resultdesc");
				JSONObject data = resultJSON.getJSONObject("data");
				String pagesize = data.getString("pagesize");
				String totalrecord = data.getString("totalrecord");
				String currentpage = data.getString("currentpage");
				String totalpage = data.getString("totalpage");
				JSONArray lstdata = data.getJSONArray("lstdata");
				JSONObject obj = null;
				Counsel counsel = null;
				for (int i = 0; i < lstdata.length(); i++) {
					obj = new JSONObject();
					obj = lstdata.getJSONObject(i);
					counsel = new Counsel();
					counsel.setId(obj.getString("ID"));
					counsel.setUser_id(obj.getString("USER_ID"));
					counsel.setTitle(obj.getString("TITLE"));
					counsel.setContent(obj.getString("CONTENT"));
					counsel.setProvince(obj.getString("PROVINCE"));
					counsel.setCity(obj.getString("CITY"));
					counsel.setTip(obj.getString("TIP"));
					counsel.setCase_type(obj.getString("CASE_TYPE"));
					counsel.setAuthor(obj.getString("AUTHOR"));
					counsel.setTelephone(obj.getString("TELPHONE"));
					counsel.setEmail(obj.getString("EMAIL"));
					counsel.setStatus(obj.getInt("STATUS"));
					counsel.setLawyer_id(obj.getString("LAWER_ID"));
					counsel.setAssess(obj.getString("ASSESS"));
					counsel.setCreate_date(obj.getLong("CREATE_DATE"));
					counsel.setUpdate_date(obj.getString("UPDATE_DATE"));
					counsel.setNotes(obj.getString("NOTES"));
					counsel.setNum(obj.getString("NUM"));
					counsels.add(counsel);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return counsels;
	}
	
	private String getStatusText(int status) {
		String statusText = "未知状态";
		switch (status) {
		case 0:
			statusText = "待处理";
			break;
		case 1:
			statusText = "待处理";
			break;
		case 2:
			statusText = "已评价";
			break;
		}
		return statusText;
	}
	
	private int getStatusColor(int status) {
		int color = Color.BLACK;
		switch (status) {
		case 0:
			color = Color.RED;
			break;
		case 1:
			color = Color.BLACK;
			break;
		case 2:
			color = Color.BLUE;
			break;
		}
		return color;
	}
	
	private String formatDate(long time) {
		return DateUtils.formatDate(new Date(time), "yyyy年MM月dd日 HH:mm");
	}
	
	private void refreshList(RequestInfo... params) {
		hideInputMethod();
		
		mProgressBar.setVisibility(View.VISIBLE);
    	infoList.setVisibility(View.GONE);
    	emptyView.setVisibility(View.GONE);
    	new CounselAsyncTask().execute(params);
	}
	
	private void hideInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (imm != null) {			
			imm.hideSoftInputFromWindow(infoList.getWindowToken(), 0);
		}
	}
	
}
