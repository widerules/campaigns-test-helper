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

import java.util.EnumMap;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GenerateActivity extends Activity {

	TextView mUrl = null;
	Button mSend = null;
	String mFormatted = null;
	Uri mUri = null;
	String mPackageName = null;
	String mReferrer = null;
	ImageView mImage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.generate);

		mUrl = (TextView) findViewById(R.id.generated_url);
		mSend = (Button) findViewById(R.id.send);
		mImage = (ImageView) findViewById(R.id.image_view);

		Intent intent = getIntent();
		if (null != intent) {
			mUri = intent.getData();
			mReferrer = mUri.getQueryParameter("referrer");
			mPackageName = mUri.getQueryParameter("id");
			String url = intent.getDataString();
			Log.i(GenerateActivity.class.getSimpleName(), url);
			// String.format("", args)
			mUrl.setText(url);

			//
			Bitmap bitmap;
			try {
				bitmap = encodeAsBitmap(url);
				mImage.setImageBitmap(bitmap);
			} catch (WriterException e) {
				e.printStackTrace();
			}
		}

		mSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doReferrer();
			}
		});
	}

	void doReferrer() {
		Intent i = new Intent("com.android.vending.INSTALL_REFERRER");
		i.setPackage(mPackageName);

		// referrer is a composition of the parameter of the campaign
		i.putExtra("referrer", mReferrer);
		sendBroadcast(i);

	}

	Bitmap encodeAsBitmap(String contents) throws WriterException {
		String contentsToEncode = contents;
		if (contentsToEncode == null) {
			return null;
		}
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(contentsToEncode);
		if (encoding != null) {
			hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();

		WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		int widthD = display.getWidth();
		int heightD = display.getHeight();
		int dimension = widthD < heightD ? widthD : heightD;
		dimension = dimension * 7 / 8;

		BitMatrix result = writer.encode(contentsToEncode,
				BarcodeFormat.QR_CODE, dimension, dimension, hints);
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? Color.BLACK
						: Color.WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return HTTP.UTF_8;
			}
		}
		return null;
	}
}
