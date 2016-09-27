/*!
* Copyright (c) 2006-2007 Erin Catto http://www.box2d.org
*
* This software is provided 'as-is', without any express or implied
* warranty.  In no event will the authors be held liable for any damages
* arising from the use of this software.
* Permission is granted to anyone to use this software for any purpose,
* including commercial applications, and to alter it and redistribute it
* freely, subject to the following restrictions:
* 1. The origin of this software must not be misrepresented; you must not
* claim that you wrote the original software. If you use this software
* in a product, an acknowledgment in the product documentation would be
* appreciated but is not required.
* 2. Altered source versions must be plainly marked as such, and must not be
* misrepresented as being the original software.
* 3. This notice may not be removed or altered from any source distribution.
*/

/*! 
 * Base code for CS 251 Software Systems Lab 
 * Department of Computer Science and Engineering, IIT Bombay
 * 
 */

#ifndef _RENDER_HPP_
#define _RENDER_HPP_

#include <Box2D/Box2D.h>

struct b2AABB;


//! This class implements debug drawing callbacks that are invoked
//! inside b2World::Step.
/*!
*Dear TA:

*Once you are done trying to 'check' this documentation,
*and have realized what a terrible mistake that was,
*please increment the following counter as a warning
*to the Professor:

*total_hours_wasted_here = 42
*/

class debug_draw_t : public b2Draw
{
public:
	//!DrawPolygon
	/*!
	 * Draws a Polygon
	 * @param vertices Gives vertices
	 * @param vertexCount Number of vertices
	 * @param color Color of boundary of polygon
	*/
  void DrawPolygon(const b2Vec2* vertices, int32 vertexCount, const b2Color& color);
  	
  	//!DrawSolidPolygon
	/*!
	 * Draws a Solid Polygon! Wow!
	 * @param vertices Gives vertices
	 * @param vertexCount Number of vertices
	 * @param color Color of boundary of polygon
	*/
  void DrawSolidPolygon(const b2Vec2* vertices, int32 vertexCount, const b2Color& color);
  	
  	//!DrawCircle
	/*!
	 * Draws a Circle! Round Circle!
	 * @param center Center coordinates
	 * @param radius Radius of circle
	 * @param color Color of boundary of polygon
	*/
  void DrawCircle(const b2Vec2& center, float32 radius, const b2Color& color);
  	
  	//!DrawSolidCircle
	/*!
	 * Draws a Solid Circle! Filled-in circle!
	 * @param center Center coordinates
	 * @param radius Radius of circle
	 * @param axis Axis of the circle
	 * @param color Color of boundary of polygon
	*/
  void DrawSolidCircle(const b2Vec2& center, float32 radius, const b2Vec2& axis, const b2Color& color);
  	
  	//!Draw Segment
	/*!
	 * Draws a Line Segment! Straight line!
	 * @param p1 Point 1 coordinates
	 * @param p2 Point 2 coordinates
	 * @param color Color of boundary of polygon
	*/
  void DrawSegment(const b2Vec2& p1, const b2Vec2& p2, const b2Color& color);
  	
  	//!DrawTransform
	/*!
	 * Draws a Transform! Cool!
	 * @param xf Transform
	*/
  void DrawTransform(const b2Transform& xf);
  	
  	//!DrawPoint
	/*!
	 * Draws a Point! Dot!
	 * @param p  Coordinates
	 * @param size Size of point
	 * @param color Color of boundary of polygon
	*/
  void DrawPoint(const b2Vec2& p, float32 size, const b2Color& color);
  	
  	//!DrawString
	/*!
	 * Draws a Text!
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param string Text
	*/
  void DrawString(int x, int y, const char* string, ...); 
  	
  	//!DrawAABB
	/*!
	 * Draws the AABB struct!
	 * @param aabb Object
	 * @param color Color of boundary of polygon
	*/
  void DrawAABB(b2AABB* aabb, const b2Color& color);
};


#endif


/*!

*/
