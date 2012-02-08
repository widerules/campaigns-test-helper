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
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button mGenerate = null;
	Button mClear = null;
	EditText mPackage_Name = null; // *
	EditText mCampaign_Source = null; // *
	EditText mCampaign_Medium = null; // *
	EditText mCampaign_Term = null;
	EditText mCampaign_Content = null;
	EditText mCampaign_Name = null; // *

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		mPackage_Name = (EditText) findViewById(R.id.Package_Name);
		mCampaign_Source = (EditText) findViewById(R.id.Campaign_Source);
		mCampaign_Medium = (EditText) findViewById(R.id.Campaign_Medium);
		mCampaign_Term = (EditText) findViewById(R.id.Campaign_Term);
		mCampaign_Content = (EditText) findViewById(R.id.Campaign_Content);
		mCampaign_Name = (EditText) findViewById(R.id.Campaign_Name);

		mGenerate = (Button) findViewById(R.id.generate);
		mGenerate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ((mPackage_Name.getText() == null || mPackage_Name.getText()
						.toString().trim().equalsIgnoreCase(""))
						|| (mCampaign_Source.getText() == null || mCampaign_Source
								.getText().toString().trim()
								.equalsIgnoreCase(""))
						|| (mCampaign_Medium.getText() == null || mCampaign_Medium
								.getText().toString().trim()
								.equalsIgnoreCase(""))
						|| (mCampaign_Name.getText() == null || mCampaign_Name
								.getText().toString().trim()
								.equalsIgnoreCase(""))) {
					Toast.makeText(getApplicationContext(),
							R.string.error_must_field, Toast.LENGTH_SHORT)
							.show();
					return;
				} else {
					String url = String.format(
							"http://market.android.com/details?id=%s",
							mPackage_Name.getText().toString().trim());
					Log.i("", url);
					Intent intent = new Intent(MainActivity.this,
							GenerateActivity.class);
					Uri uri = Uri.parse(url);
					Uri.Builder builder = uri.buildUpon();

					String content = "utm_source="
							+ mCampaign_Source.getText().toString().trim()
							+ "&utm_medium="
							+ mCampaign_Medium.getText().toString().trim();

					if (mCampaign_Term.getText() != null
							&& !mCampaign_Term.getText().toString().trim()
									.equalsIgnoreCase(""))
						content += "&utm_term="
								+ mCampaign_Term.getText().toString().trim();

					if (mCampaign_Content.getText() != null
							&& !mCampaign_Content.getText().toString().trim()
									.equalsIgnoreCase(""))
						content += "&utm_content="
								+ mCampaign_Content.getText().toString().trim();

					content += "&utm_campaign="
							+ mCampaign_Name.getText().toString().trim();

					builder.appendQueryParameter("referrer", content);
					uri = builder.build();
					intent.setData(uri);
					startActivity(intent);
				}
			}
		});

		mClear = (Button) findViewById(R.id.clear);
		mClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPackage_Name.setText("");
				mCampaign_Source.setText("");
				mCampaign_Medium.setText("");
				mCampaign_Term.setText("");
				mCampaign_Content.setText("");
				mCampaign_Name.setText("");
			}
		});
	}
}
