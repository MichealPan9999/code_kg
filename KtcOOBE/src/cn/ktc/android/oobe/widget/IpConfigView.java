package cn.ktc.android.oobe.widget;

import cn.ktc.android.oobe.R;
import cn.ktc.android.oobe.utils.Utils;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;

public class IpConfigView extends RelativeLayout {
	
	// IP地址
	private String[] ips;
	// 子网掩码
	private String[] netmasks;
	// 默认网关
	private String[] gateways;
	// 首选DNS
	private String[] firstdnss;
	// 备用DNS
	private String[] secondnss;
		
	private Switch swAutoIp;
	
	private EditText edtIpOne;
	private EditText edtIpTwo;
	private EditText edtIpThree;
	private EditText edtIpFour;
	
	private EditText edtSubnetMaskOne;
	private EditText edtSubnetMaskTwo;
	private EditText edtSubnetMaskThree;
	private EditText edtSubnetMaskFour;
	
	private EditText edtGatewayOne;
	private EditText edtGatewayTwo;
	private EditText edtGatewayThree;
	private EditText edtGatewayFour;
	
	private EditText edtFirstDnsOne;
	private EditText edtFirstDnsTwo;
	private EditText edtFirstDnsThree;
	private EditText edtFirstDnsFour;
	
	private EditText edtSecondDnsOne;
	private EditText edtSecondDnsTwo;
	private EditText edtSecondDnsThree;
	private EditText edtSecondDnsFour;

	public IpConfigView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	public IpConfigView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	
	public IpConfigView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.ip_config, this, true);
		
		init(context);
	}
	
	private void init(Context context) {
		swAutoIp = (Switch) findViewById(R.id.swAutoIp);
		
		edtIpOne = (EditText) findViewById(R.id.edtIpOne);
		edtIpTwo = (EditText) findViewById(R.id.edtIpTwo);
		edtIpThree = (EditText) findViewById(R.id.edtIpThree);
		edtIpFour = (EditText) findViewById(R.id.edtIpFour);
		
		edtSubnetMaskOne = (EditText) findViewById(R.id.edtSubnetMaskOne);
		edtSubnetMaskTwo = (EditText) findViewById(R.id.edtSubnetMaskTwo);
		edtSubnetMaskThree = (EditText) findViewById(R.id.edtSubnetMaskThree);
		edtSubnetMaskFour = (EditText) findViewById(R.id.edtSubnetMaskFour);
		
		edtGatewayOne = (EditText) findViewById(R.id.edtGatewayOne);
		edtGatewayTwo = (EditText) findViewById(R.id.edtGatewayTwo);
		edtGatewayThree = (EditText) findViewById(R.id.edtGatewayThree);
		edtGatewayFour = (EditText) findViewById(R.id.edtGatewayFour);
		
		edtFirstDnsOne = (EditText) findViewById(R.id.edtFirstDnsOne);
		edtFirstDnsTwo = (EditText) findViewById(R.id.edtFirstDnsTwo);
		edtFirstDnsThree = (EditText) findViewById(R.id.edtFirstDnsThree);
		edtFirstDnsFour = (EditText) findViewById(R.id.edtFirstDnsFour);
		
		edtSecondDnsOne = (EditText) findViewById(R.id.edtSecondDnsOne);
		edtSecondDnsTwo = (EditText) findViewById(R.id.edtSecondDnsTwo);
		edtSecondDnsThree = (EditText) findViewById(R.id.edtSecondDnsThree);
		edtSecondDnsFour = (EditText) findViewById(R.id.edtSecondDnsFour);
		
		swAutoIp.setOnCheckedChangeListener(
				new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					setIpInputEnable(false);
				} else {
					setIpInputEnable(true);
				}
			}
		});
	}
	
	public void setIp(String ip) {
		if (null != ip ) {
			ips = Utils.resolutionIP(ip);
			InputIPAddress.isForwardRightWithTextChange = false;
			edtIpOne.setText(ips[0]);
			edtIpTwo.setText(ips[1]);
			edtIpThree.setText(ips[2]);
			edtIpFour.setText(ips[3]);
			InputIPAddress.isForwardRightWithTextChange = true;
		} else {
			edtIpOne.setText("");
			edtIpTwo.setText("");
			edtIpThree.setText("");
			edtIpFour.setText("");
		}
	}
	
	public void setSubnetMask(String subnet) {
		if (null != subnet ) {
			netmasks = Utils.resolutionIP(subnet);
			InputIPAddress.isForwardRightWithTextChange = false;
			edtSubnetMaskOne.setText(netmasks[0]);
			edtSubnetMaskTwo.setText(netmasks[1]);
			edtSubnetMaskThree.setText(netmasks[2]);
			edtSubnetMaskFour.setText(netmasks[3]);
			InputIPAddress.isForwardRightWithTextChange = true;
		} else {
			edtSubnetMaskOne.setText("");
			edtSubnetMaskTwo.setText("");
			edtSubnetMaskThree.setText("");
			edtSubnetMaskFour.setText("");
		}
	}
	
	public void setGateway(String gateway) {
		if (null != gateway ) {
			gateways = Utils.resolutionIP(gateway);
			InputIPAddress.isForwardRightWithTextChange = false;
			edtGatewayOne.setText(gateways[0]);
			edtGatewayTwo.setText(gateways[1]);
			edtGatewayThree.setText(gateways[2]);
			edtGatewayFour.setText(gateways[3]);
			InputIPAddress.isForwardRightWithTextChange = true;
		} else {
			edtGatewayOne.setText("");
			edtGatewayTwo.setText("");
			edtGatewayThree.setText("");
			edtGatewayFour.setText("");
		}
	}
	
	public void setFirstDns(String firstDns) {
		if (null != firstDns) {
			firstdnss = Utils.resolutionIP(firstDns);
			InputIPAddress.isForwardRightWithTextChange = false;
			edtFirstDnsOne.setText(firstdnss[0]);
			edtFirstDnsTwo.setText(firstdnss[1]);
			edtFirstDnsThree.setText(firstdnss[2]);
			edtFirstDnsFour.setText(firstdnss[3]);
			InputIPAddress.isForwardRightWithTextChange = true;
		} else {
			edtFirstDnsOne.setText("");
			edtFirstDnsTwo.setText("");
			edtFirstDnsThree.setText("");
			edtFirstDnsFour.setText("");
		}
	}
	
	public void setSecondDns(String secondDns) {
		if (null != secondDns) {
			secondnss = Utils.resolutionIP(secondDns);
			InputIPAddress.isForwardRightWithTextChange = false;
			edtSecondDnsOne.setText(secondnss[0]);
			edtSecondDnsTwo.setText(secondnss[1]);
			edtSecondDnsThree.setText(secondnss[2]);
			edtSecondDnsFour.setText(secondnss[3]);
			InputIPAddress.isForwardRightWithTextChange = true;
		} else {
			edtSecondDnsOne.setText("");
			edtSecondDnsTwo.setText("");
			edtSecondDnsThree.setText("");
			edtSecondDnsFour.setText("");
		}
	}
	
	public void resetIPs() {
		edtIpOne.setText("");
		edtIpTwo.setText("");
		edtIpThree.setText("");
		edtIpFour.setText("");

		edtSubnetMaskOne.setText("");
		edtSubnetMaskTwo.setText("");
		edtSubnetMaskThree.setText("");
		edtSubnetMaskFour.setText("");

		edtGatewayOne.setText("");
		edtGatewayTwo.setText("");
		edtGatewayThree.setText("");
		edtGatewayFour.setText("");

		edtFirstDnsOne.setText("");
		edtFirstDnsTwo.setText("");
		edtFirstDnsThree.setText("");
		edtFirstDnsFour.setText("");

		edtSecondDnsOne.setText("");
		edtSecondDnsTwo.setText("");
		edtSecondDnsThree.setText("");
		edtSecondDnsFour.setText("");
	}
	
	public void setAutoIp(boolean autoIp) {
		swAutoIp.setChecked(autoIp);
	}

	private void setIpInputEnable(boolean isEnable){
		edtIpOne.setEnabled(isEnable);
		edtIpTwo.setEnabled(isEnable);
		edtIpThree.setEnabled(isEnable);
		edtIpFour.setEnabled(isEnable);

		edtSubnetMaskOne.setEnabled(isEnable);
		edtSubnetMaskTwo.setEnabled(isEnable);
		edtSubnetMaskThree.setEnabled(isEnable);
		edtSubnetMaskFour.setEnabled(isEnable);

		edtGatewayOne.setEnabled(isEnable);
		edtGatewayTwo.setEnabled(isEnable);
		edtGatewayThree.setEnabled(isEnable);
		edtGatewayFour.setEnabled(isEnable);

		edtFirstDnsOne.setEnabled(isEnable);
		edtFirstDnsTwo.setEnabled(isEnable);
		edtFirstDnsThree.setEnabled(isEnable);
		edtFirstDnsFour.setEnabled(isEnable);

		edtSecondDnsOne.setEnabled(isEnable);
		edtSecondDnsTwo.setEnabled(isEnable);
		edtSecondDnsThree.setEnabled(isEnable);
		edtSecondDnsFour.setEnabled(isEnable);
	}
	
	public boolean isAutoIp() {
		return swAutoIp.isChecked();
	}

	public String getIp() {
		return edtIpOne.getText().toString().trim() + "." 
				+ edtIpTwo.getText().toString().trim() + "."
				+ edtIpThree.getText().toString().trim() + "." 
				+ edtIpFour.getText().toString().trim();
	}

	public String getSubneMask() {
		return edtSubnetMaskOne.getText().toString().trim() + "." 
				+ edtSubnetMaskTwo.getText().toString().trim() + "."
				+ edtSubnetMaskThree.getText().toString().trim() + "." 
				+ edtSubnetMaskFour.getText().toString().trim();
	}

	public String getGateway() {
		return edtGatewayOne.getText().toString().trim() + "." 
				+ edtGatewayTwo.getText().toString().trim() + "."
				+ edtGatewayThree.getText().toString().trim() + "." 
				+ edtGatewayFour.getText().toString().trim();
	}

	public String getFirstDns() {
		return edtFirstDnsOne.getText().toString().trim() + "." 
				+ edtFirstDnsTwo.getText().toString().trim() + "."
				+ edtFirstDnsThree.getText().toString().trim() + "." 
				+ edtFirstDnsFour.getText().toString().trim();
	}

	public String getSecondDns() {
		return edtSecondDnsOne.getText().toString().trim() + "." 
				+ edtSecondDnsTwo.getText().toString().trim() + "." 
				+ edtSecondDnsThree.getText().toString().trim() + "."
				+ edtSecondDnsFour.getText().toString().trim();
	}

}
