package com.qamatrix.backend.domain.user;

public class AbstractContent
{
	// For indicating Edited Records
	/** 20000 */
	public static final int MODIFIED = 20000;
	/** 30000 */
	public static final int NEW = 30000;
	/** 40000 */
	public static final int DELETED = 40000;
	/** 50000 */
	public static final int UNCHANGED = 50000;

	private int status;

	public int getStatus()
	{
		return status;
	}

	public void setStatus( int status )
	{
		this.status = status;
	}
}
