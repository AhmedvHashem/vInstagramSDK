package com.vivantor.vinstagramsdk;

/**
 * Created by AhmedNTS on 2/22/2016.
 */
public class InstagramUser
{
	private String id;

	private String profile_picture;

	private String username;

	private String full_name;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getProfile_picture()
	{
		return profile_picture;
	}

	public void setProfile_picture(String profile_picture)
	{
		this.profile_picture = profile_picture;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getFull_name()
	{
		return full_name;
	}

	public void setFull_name(String full_name)
	{
		this.full_name = full_name;
	}

	@Override
	public String toString()
	{
		return "[id = " + id + ", profile_picture = " + profile_picture + ", username = " + username + ", full_name = " + full_name + "]";
	}
}