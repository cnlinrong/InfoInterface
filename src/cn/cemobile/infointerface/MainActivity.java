package cn.cemobile.infointerface;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private Button btnInfo;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnInfo = (Button) findViewById(R.id.btn_info);
        btnInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, InfoActivity.class));
			}
			
		});
        
        ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_exit) {
        	AlertDialog.Builder builder = new Builder(MainActivity.this);
        	builder.setMessage("确认退出吗？");
        	builder.setTitle("提示");
        	builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
        		
        		@Override
        		public void onClick(DialogInterface dialog, int which) {
        			dialog.dismiss();
        			MainActivity.this.finish();
        		}
        		
        	});
        	builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
        		
        		@Override
        		public void onClick(DialogInterface dialog, int which) {
        			dialog.dismiss();
        		}
        		
        	});
        	builder.create().show();
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}
