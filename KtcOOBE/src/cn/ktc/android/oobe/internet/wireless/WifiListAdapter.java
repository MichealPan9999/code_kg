package cn.ktc.android.oobe.internet.wireless;

import java.util.List;

import cn.ktc.android.oobe.R;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WifiListAdapter extends BaseAdapter {

	private final int[] WIFI_SIGNAL_IMG = {
			R.drawable.ic_wifi_signal_0,
			R.drawable.ic_wifi_signal_1,
			R.drawable.ic_wifi_signal_2,
			R.drawable.ic_wifi_signal_3,
			R.drawable.ic_wifi_signal_4,
			R.drawable.ic_wifi_signal_5,
    };

	private Context mContext;

	private List<ScanResult> mScanResults;

	public WifiListAdapter(Context context, List<ScanResult> list) {
		this.mContext = context;
		this.mScanResults = list;
		Log.e("wifi", "new WifiListAdapter");
		WifiUtils.refreshConnectedID(mContext);
	}

	@Override
	public int getCount() {
		return mScanResults.size();
	}

	@Override
	public Object getItem(int position) {
		return mScanResults.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater factory = LayoutInflater.from(mContext);

		ViewHolder viewHolder  = null;

		if (convertView == null || convertView.getTag() == null) {
			viewHolder = new ViewHolder();
			
			convertView = factory.inflate(R.layout.listitem_wifi, parent, false);			
			viewHolder.ivWpsLock = (ImageView) convertView.findViewById(R.id.ivWpsLock);
			viewHolder.ivWps = (ImageView) convertView.findViewById(R.id.ivWps);
			viewHolder.tvSsid = (TextView) convertView.findViewById(R.id.tvSsid);
			viewHolder.ivSingalStrength = (ImageView) convertView.findViewById(R.id.ivSignalStrenth);
			viewHolder.ivConnected = (ImageView) convertView.findViewById(R.id.ivConnected);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ScanResult result = mScanResults.get(position);

		viewHolder.tvSsid.setText(result.SSID);
		
		int security = WifiUtils.getSecurity(result);
		int level = getLevel(result.level);
		
		viewHolder.ivWps.setVisibility(
				(security != WifiUtils.SECURITY_NONE && isContainWps(result))
				? View.VISIBLE : View.GONE);
		
		if (level == Integer.MAX_VALUE) {
			viewHolder.ivSingalStrength.setImageDrawable(null);
		} else {
			viewHolder.ivWpsLock.setVisibility(
					(security != WifiUtils.SECURITY_NONE) ? View.VISIBLE : View.GONE);
			viewHolder.ivSingalStrength.setImageResource(WIFI_SIGNAL_IMG[level]);
		}

		if (WifiUtils.isConnected(result)){
			viewHolder.ivConnected.setVisibility(View.VISIBLE);
		} else {
			viewHolder.ivConnected.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}

	public static int getLevel(int level) {
		if (level == Integer.MAX_VALUE) {
			return -1;
		}
		return WifiManager.calculateSignalLevel(level, 6);
	}
	
	public static boolean isContainWps(ScanResult result) {
		if (null != result && result.capabilities.contains("WPS")) {
			return true;
		}
		return false;
	}

	private static class ViewHolder {
		ImageView ivWpsLock;
		
		ImageView ivWps;
		
		TextView tvSsid;

		ImageView ivSingalStrength;

		ImageView ivConnected;
	}

	@Override
	public void notifyDataSetChanged() {
		Log.d("wifi", "notifyDataSetChanged");
		WifiUtils.refreshConnectedID(mContext);
		super.notifyDataSetChanged();
	}
}
