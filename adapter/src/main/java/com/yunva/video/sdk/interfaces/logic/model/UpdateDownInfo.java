package com.yunva.video.sdk.interfaces.logic.model;


/**
 * Created by plm on 2014年6月9日10:26:02.
 */
public class UpdateDownInfo  {
	
	private Integer appid;// 应用id
	private String phone_uuid;// 手机唯一值(注:IOS传手机唯一码,android本地生成一个uuid保存在SD卡上)
	private Byte terminal_type;// 终端类型
	private Short terminal_version;// 终端版本
	
	private Short version;// 软件版本
	private Byte cpu;// cpu类型
	private String manufacturer;// 厂商名
	private String mobile_type;// 机型
	
	private String imei;// 手机IMEI号
	private String display;// 手机分辨率
	private String module ;   //*string*/	JSON(module,"module")//更新模块(如果无模块化则为空)
	private Integer gameId;    //*uint32*/	JSON(gameId,"gameId")//游戏ID
	public String getModule()
	{
		return module;
	}
	public void setModule(String module)
	{
		this.module = module;
	}
	public Integer getGameId()
	{
		return gameId;
	}
	public void setGameId(Integer gameId)
	{
		this.gameId = gameId;
	}
	@Override
	public String toString()
	{
		return "UpdateInfo [appid=" + appid + ", phone_uuid=" + phone_uuid + ", terminal_type=" + terminal_type + ", terminal_version=" + terminal_version + ", version=" + version + ", cpu=" + cpu + ", manufacturer=" + manufacturer + ", mobile_type=" + mobile_type + ", imei=" + imei + ", display=" + display + "]";
	}
	public Integer getAppid()
	{
		return appid;
	}
	public void setAppid(Integer appid)
	{
		this.appid = appid;
	}
	public String getPhone_uuid()
	{
		return phone_uuid;
	}
	public void setPhone_uuid(String phone_uuid)
	{
		this.phone_uuid = phone_uuid;
	}
	public Byte getTerminal_type()
	{
		return terminal_type;
	}
	public void setTerminal_type(Byte terminal_type)
	{
		this.terminal_type = terminal_type;
	}
	public Short getTerminal_version()
	{
		return terminal_version;
	}
	public void setTerminal_version(Short terminal_version)
	{
		this.terminal_version = terminal_version;
	}
	public Short getVersion()
	{
		return version;
	}
	public void setVersion(Short version)
	{
		this.version = version;
	}
	public Byte getCpu()
	{
		return cpu;
	}
	public void setCpu(Byte cpu)
	{
		this.cpu = cpu;
	}
	public String getManufacturer()
	{
		return manufacturer;
	}
	public void setManufacturer(String manufacturer)
	{
		this.manufacturer = manufacturer;
	}
	public String getMobile_type()
	{
		return mobile_type;
	}
	public void setMobile_type(String mobile_type)
	{
		this.mobile_type = mobile_type;
	}
	public String getImei()
	{
		return imei;
	}
	public void setImei(String imei)
	{
		this.imei = imei;
	}
	public String getDisplay()
	{
		return display;
	}
	public void setDisplay(String display)
	{
		this.display = display;
	}
	
}
