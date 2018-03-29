package com.yunva.video.sdk.interfaces.logic.model;


public class UpdateDownReturnInfo
{
	private Integer version; // 软件版本
	private String module; // 模块
	private String file_info;// 文件信息
	private String file_url;// 文件下载url
	private String remark;
	private Integer force;
	private Short crc;
	
	@Override
	public String toString()
	{
		return "UpdateDownReturnInfo [version=" + version + ", module=" + module + ", file_info=" + file_info + ", file_url=" + file_url + ", remark=" + remark + ", force=" + force + ", crc=" + crc + "]";
	}
	public Integer getVersion()
	{
		return version;
	}
	public void setVersion(Integer version)
	{
		this.version = version;
	}
	public String getModule()
	{
		return module;
	}
	public void setModule(String module)
	{
		this.module = module;
	}
	public String getFile_info()
	{
		return file_info;
	}
	public void setFile_info(String file_info)
	{
		this.file_info = file_info;
	}
	public String getFile_url()
	{
		return file_url;
	}
	public void setFile_url(String file_url)
	{
		this.file_url = file_url;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public Integer getForce()
	{
		return force;
	}
	public void setForce(Integer force)
	{
		this.force = force;
	}
	public Short getCrc()
	{
		return crc;
	}
	public void setCrc(Short crc)
	{
		this.crc = crc;
	}

	

}
