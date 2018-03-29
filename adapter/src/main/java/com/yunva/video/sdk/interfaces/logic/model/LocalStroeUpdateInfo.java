package com.yunva.video.sdk.interfaces.logic.model;

public class LocalStroeUpdateInfo
{
	private String updateUrl;
	private String verSionName;
	private Integer verSionNumber;

	
	@Override
	public String toString()
	{
		return "LocalStroeUpdateInfo [updateUrl=" + updateUrl + ", verSionName=" + verSionName + ", verSionNumber=" + verSionNumber + "]";
	}

	public String getUpdateUrl()
	{
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl)
	{
		this.updateUrl = updateUrl;
	}

	public String getVerSionName()
	{
		return verSionName;
	}

	public void setVerSionName(String verSionName)
	{
		this.verSionName = verSionName;
	}

	public Integer getVerSionNumber()
	{
		return verSionNumber;
	}

	public void setVerSionNumber(Integer verSionNumber)
	{
		this.verSionNumber = verSionNumber;
	}
}
