package com.androidapp.salesupport;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewGroup.LayoutParams;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class SaleSupportActivity extends Activity {

	ImageView imgTakePhoto, imgTextFormat, imgShare, imgColors, imgInputText;
	FrameLayout flPhoto;
	TextView tvText;
	private PopupWindow mPopupMenu, mPopupInputText;

	final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	Uri imageUri = null;

	float xAxis = 0f;
	float yAxis = 0f;
	float lastXAxis = 0f;
	float lastYAxis = 0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sale_support_activity);
		initialUI();

	}

	public void initialUI() {
		imgTakePhoto = (ImageView) findViewById(R.id.imgTakePhoto);
		imgTextFormat = (ImageView) findViewById(R.id.imgTextFormat);
		imgShare = (ImageView) findViewById(R.id.imgShare);
		imgColors = (ImageView) findViewById(R.id.imgColors);
		imgInputText = (ImageView) findViewById(R.id.imgInputText);
		flPhoto = (FrameLayout) findViewById(R.id.flPhoto);
		tvText = (TextView) findViewById(R.id.tvText);

		tvText.setOnTouchListener(new ChoiceTouchListener());

	}

	/**
	 * Listener
	 * */
	public void onClickImage(View v) {
		switch (v.getId()) {
		case R.id.imgTakePhoto:
			takePhoto();
			break;
		case R.id.imgTextFormat:

			break;
		case R.id.imgShare:

			break;

		case R.id.imgColors:

			break;
		case R.id.imgInputText:
			// tvText.setText("Test");

			// if (!mPopupInputText.isShowing()) {
			initPopupMenu(v);

			// } else {
			// mPopupInputText.dismiss();
			// }

			break;

		default:
			break;
		}
	}

	/**********/
	public void initPopupMenu(View v) {
		LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View popup = layoutInflater.inflate(R.layout.popup_input_text_layout,
				null);

		mPopupInputText = new PopupWindow(popup, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		final EditText etInputText = (EditText) popup
				.findViewById(R.id.etInputText);
		ImageView imgTickOK = (ImageView) popup.findViewById(R.id.imgTickOK);

		mPopupInputText.setOutsideTouchable(true);
		mPopupInputText.setFocusable(true);
		mPopupInputText.update();

		imgTickOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!etInputText.getText().toString().isEmpty()) {
					tvText.setText(etInputText.getText().toString());
				}

				mPopupInputText.dismiss();
			}
		});

		mPopupInputText.setAnimationStyle(R.style.AnimationDeviceListPopup);
		// // mPopupMenu.update();

		// // mPopupDeviceList.showAsDropDown(v);
		mPopupInputText.showAtLocation(v, Gravity.TOP, 10, 50);

	}

	/**********/

	/**************/

	/**
	 * ChoiceTouchListener will handle touch events on draggable views
	 *
	 */
	private final class ChoiceTouchListener implements OnTouchListener {

		@SuppressLint("NewApi")
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {

			final int actionPeformed = motionEvent.getAction();
			switch (actionPeformed) {
			case MotionEvent.ACTION_DOWN: {
				final float x = motionEvent.getX();
				final float y = motionEvent.getY();
				lastXAxis = x;
				lastYAxis = y;

				break;
			}
			case MotionEvent.ACTION_MOVE: {
				final float x = motionEvent.getX();
				final float y = motionEvent.getY();
				final float dx = x - lastXAxis;
				final float dy = y - lastYAxis;
				xAxis += dx;
				yAxis += dy;

				view.setX(xAxis);
				view.setY(yAxis);

				break;
			}
			}

			return true;

		}

	}

	@SuppressLint("NewApi")
	private class ChoiceDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// no action necessary
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				// no action necessary
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				// no action necessary
				break;
			case DragEvent.ACTION_DROP:

				// handle the dragged view being dropped over a drop view
				View view = (View) event.getLocalState();
				// view dragged item is being dropped on
				TextView dropTarget = (TextView) v;
				// view being dragged and dropped
				TextView dropped = (TextView) view;
				// checking whether first character of dropTarget equals first
				// character of dropped
				if (dropTarget.getText().toString().charAt(0) == dropped
						.getText().toString().charAt(0)) {
					// stop displaying the view where it was before it was
					// dragged
					view.setVisibility(View.INVISIBLE);
					// update the text in the target view to reflect the data
					// being dropped
					dropTarget.setText(dropTarget.getText().toString()
							+ dropped.getText().toString());
					// make it bold to highlight the fact that an item has been
					// dropped
					dropTarget.setTypeface(Typeface.DEFAULT_BOLD);
					// if an item has already been dropped here, there will be a
					// tag
					Object tag = dropTarget.getTag();
					// if there is already an item here, set it back visible in
					// its original place
					if (tag != null) {
						// the tag is the view id already dropped here
						int existingID = (Integer) tag;
						// set the original view visible again
						findViewById(existingID).setVisibility(View.VISIBLE);
					}
					// set the tag in the target view being dropped on - to the
					// ID of the view being dropped
					dropTarget.setTag(dropped.getId());
					// remove setOnDragListener by setting OnDragListener to
					// null, so that no further drag & dropping on this TextView
					// can be done
					dropTarget.setOnDragListener(null);
				} else
					// displays message if first character of dropTarget is not
					// equal to first character of dropped
					// Toast.makeText(Picture_to_word_24_color.this,
					// dropTarget.getText().toString() + "is not " +
					// dropped.getText().toString(), Toast.LENGTH_LONG).show();
					break;
			case DragEvent.ACTION_DRAG_ENDED:
				// no action necessary
				break;
			default:
				break;
			}
			return true;
		}
	}

	/**************/

	public void takePhoto() {
		/*************************** Camera Intent Start ************************/

		// Define the file-name to save photo taken by Camera activity

		String fileName = "Sale_support.jpg";

		// Create parameters for Intent with filename

		ContentValues values = new ContentValues();

		values.put(MediaStore.Images.Media.TITLE, fileName);

		values.put(MediaStore.Images.Media.DESCRIPTION,
				"Image capture by camera");

		// imageUri is the current activity attribute, define and save it for
		// later usage

		imageUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		/****
		 * EXTERNAL_CONTENT_URI : style URI for the "primary" external storage
		 * volume.
		 ****/

		// Standard Intent action that can be sent to have the camera
		// application capture an image and return it.

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

		/*************************** Camera Intent End ************************/
	}

	/************ Convert Image Uri path to physical path **************/

	public static String convertImageUriToFile(Uri imageUri, Activity activity) {

		Cursor cursor = null;
		int imageID = 0;

		try {

			/*********** Which columns values want to get *******/
			String[] proj = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID,
					MediaStore.Images.Thumbnails._ID,
					MediaStore.Images.ImageColumns.ORIENTATION };

			cursor = activity.managedQuery(

			imageUri, // Get data for specific image URI
					proj, // Which columns to return
					null, // WHERE clause; which rows to return (all rows)
					null, // WHERE clause selection arguments (none)
					null // Order-by clause (ascending by name)

					);

			// Get Query Data

			int columnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
			int columnIndexThumb = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
			int file_ColumnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

			int orientation_ColumnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

			int size = cursor.getCount();

			/******* If size is 0, there are no images on the SD Card. *****/

			if (size == 0) {

				// imageDetails.setText("No Image");
			} else {

				int thumbID = 0;
				if (cursor.moveToFirst()) {

					/**************** Captured image details ************/

					/***** Used to show image on view in LoadImagesFromSDCard class ******/
					imageID = cursor.getInt(columnIndex);

					thumbID = cursor.getInt(columnIndexThumb);

					String Path = cursor.getString(file_ColumnIndex);

					String orientation = cursor
							.getString(orientation_ColumnIndex);

					// String CapturedImageDetails =
					// " CapturedImageDetails : \n\n"
					// + " ImageID :"
					// + imageID
					// + "\n"
					// + " ThumbID :"
					// + thumbID + "\n" + " Path :" + Path + "\n";

					// Show Captured Image detail on activity
					// imageDetails.setText(CapturedImageDetails);

				}
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		// Return Captured Image ImageID ( By this ImageID Image will load from
		// sdcard )

		return String.valueOf(imageID);
	}

	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	/**
	 * Async task for loading the images from the SD card.
	 *
	 * @author Android Example
	 *
	 */

	// Class with extends AsyncTask class

	public class LoadImagesFromSDCard extends AsyncTask<String, Void, Void> {

		private ProgressDialog Dialog = new ProgressDialog(
				SaleSupportActivity.this);

		Bitmap mBitmap;

		protected void onPreExecute() {
			/****** NOTE: You can call UI Element here. *****/

			// Progress Dialog
			// Dialog.setMessage(" Loading image from Sdcard..");
			Dialog.show();
		}

		// Call after onPreExecute method
		protected Void doInBackground(String... urls) {

			Bitmap bitmap = null;
			Bitmap newBitmap = null;
			Uri uri = null;

			try {

				/**
				 * Uri.withAppendedPath Method Description Parameters baseUri
				 * Uri to append path segment to pathSegment encoded path
				 * segment to append Returns a new Uri based on baseUri with the
				 * given segment appended to the path
				 */

				uri = Uri.withAppendedPath(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ""
								+ urls[0]);

				/************** Decode an input stream into a bitmap. *********/
				bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(uri));

				if (bitmap != null) {

					/********* Creates a new bitmap, scaled from an existing bitmap. ***********/

					int width = bitmap.getWidth();
					int height = bitmap.getHeight();
					// int newWidth = 100;
					// int newHeight = 100;
					//
					// // calculate the scale - in this case = 0.4f
					// float scaleWidth = ((float) newWidth) / width;
					// float scaleHeight = ((float) newHeight) / height;
					//
					// // createa matrix for the manipulation
					Matrix matrix = new Matrix();
					// resize the bit map
					// matrix.postScale(scaleWidth, scaleHeight);
					// rotate the Bitmap
					matrix.postRotate(90);
					//
					// // recreate the new Bitmap
					// Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					// width, height, matrix, true);
					//
					// // make a Drawable from Bitmap to allow to set the BitMap
					// // to the ImageView, ImageButton or what ever
					// BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);

					// newBitmap = Bitmap.createScaledBitmap(bitmap,
					// (int) convertPixelsToDp(170, CameraActivity),
					// (int) convertPixelsToDp(170, CameraActivity), true);

					newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
							height, matrix, true);

					bitmap.recycle();

					if (newBitmap != null) {

						mBitmap = newBitmap;
					}
				}
			} catch (IOException e) {
				// Error fetching image, try to recover

				/********* Cancel execution of this task. **********/
				cancel(true);
			}

			return null;
		}

		protected void onPostExecute(Void unused) {

			// NOTE: You can call UI Element here.

			// Close progress dialog
			Dialog.dismiss();

			if (mBitmap != null) {
				// Set Image to ImageView
				// imvPhotoPreview.setScaleType(ScaleType.CENTER);
				// imvPhotoPreview.setImageBitmap(mBitmap);
				flPhoto.setBackgroundDrawable(new BitmapDrawable(mBitmap));

			}

		}

	}

	/***/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

			if (resultCode == RESULT_OK) {

				/*********** Load Captured Image And Data Start ****************/

				String imageId = convertImageUriToFile(imageUri,
						SaleSupportActivity.this);

				// Create and excecute AsyncTask to load capture image

				new LoadImagesFromSDCard().execute(imageId);

				/*********** Load Captured Image And Data End ****************/

			} else if (resultCode == RESULT_CANCELED) {

				Toast.makeText(this, " Picture was not taken ",
						Toast.LENGTH_SHORT).show();
			} else {

				Toast.makeText(this, " Picture was not taken ",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
