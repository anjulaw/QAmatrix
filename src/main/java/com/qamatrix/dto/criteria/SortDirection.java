package com.qamatrix.dto.criteria;

public enum SortDirection
{
	ASC( "asc" ),
	DESC( "desc" );

	private String dirction;

	SortDirection( String dirction )
	{
		this.dirction = dirction;
	}
}

