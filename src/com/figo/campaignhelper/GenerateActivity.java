/*
 *  Copyright [2012] [figofuture]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 * 
 * */

package com.figo.campaignhelper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GenerateActivity extends Activity {

	TextView mUrl = null;
	Button mSend = null;
	String mFormatted = null;
	Uri mUri = null;
	String mPackageName = null;
	String mReferrer = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.generate);
		
		mUrl = (TextView) findViewById(R.id.generated_url);
		mSend = (Button) findViewById(R.id.send);
		
		Intent intent = getIntent();
		if ( null != intent ) {
			mUri = intent.getData();
			mReferrer = mUri.getQueryParameter("referrer");
			mPackageName = mUri.getQueryParameter("id");
			String url = intent.getDataString();
			Log.i(GenerateActivity.class.getSimpleName(), url);
			//String.format("", args)
			mUrl.setText(url);
		}
		
		mSend.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				doReferrer();
			}});
	}
	
	void doReferrer() {
		Intent i = new Intent("com.android.vending.INSTALL_REFERRER");
        i.setPackage(mPackageName);

        //referrer is a composition of the parameter of the campaign
        i.putExtra("referrer", mReferrer);
        sendBroadcast(i);

	}
}
