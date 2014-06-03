package com.tymm.hexapod;

import android.graphics.PointF;
/**
 * Minimum Polygon class for Android.
 * http://stackoverflow.com/questions/7044838/finding-points-contained-in-a-path-in-android
 */
public class Polygon
{
	// Polygon coodinates.
	private PointF[] poly;

	// Number of sides in the polygon.
	private int polySides;

	/**
	 * Default constructor.
	 * @param px Polygon y coods.
	 * @param py Polygon x coods.
	 * @param ps Polygon sides count.
	 */
	public Polygon( PointF[] p, int ps )
	{
		poly = p;
		polySides = ps;
	}

	/**
	 * Checks if the Polygon contains a point.
	 * @see "http://alienryderflex.com/polygon/"
	 * @param x Point horizontal pos.
	 * @param y Point vertical pos.
	 * @return Point is in Poly flag.
	 */
	public boolean contains( int x, int y )
	{
		boolean oddTransitions = false;
		for( int i = 0, j = polySides -1; i < polySides; j = i++ )
		{
			if( ( poly[ i ].y < y && poly[ j ].y >= y ) || ( poly[ j ].y < y && poly[ i ].y >= y ) )
			{
				if( poly[ i ].x + ( y - poly[ i ].y ) / ( poly[ j ].y - poly[ i ].y ) * ( poly[ j ].x - poly[ i ].x ) < x )
				{
					oddTransitions = !oddTransitions;
				}
			}
		}
		return oddTransitions;
	}
}
