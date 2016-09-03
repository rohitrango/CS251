/*
* Copyright (c) 2006-2009 Erin Catto http://www.box2d.org
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

/* 
 * Base code for CS 251 Software Systems Lab 
 * Department of Computer Science and Engineering, IIT Bombay
 * 
 */
#ifndef _DOMINOS_HPP_
#define _DOMINOS_HPP_
#include <Box2D/Box2D.h>

namespace cs251
{

  struct Magnet {
      b2Body* body;
      long double k;

      Magnet(long double ktemp, b2Vec2 position, int radius, b2World *m_world) {
          k = ktemp;      //define the constant

          b2BodyDef mag1;
          mag1.position.Set(position.x,position.y);

          b2CircleShape circle1;
          circle1.m_p.Set(0,0);
          circle1.m_radius = radius;

          b2FixtureDef fixture1;
          fixture1.density = 10;
          fixture1.shape = &circle1;

          body = m_world->CreateBody(&mag1);
          body->CreateFixture(&fixture1);
      }

  };

  struct Ball {

    b2Body* body;

    Ball(b2Vec2 pos, int radius, b2World *m_world) {

        b2BodyDef mag1;
        mag1.position.Set(pos.x,pos.y);
        mag1.type = b2_dynamicBody;

        b2CircleShape circle1;
        circle1.m_p.Set(0,0);
        circle1.m_radius = radius;

        b2FixtureDef fixture1;
        fixture1.density = 5;
        fixture1.shape = &circle1;
        fixture1.restitution = 0.6;

        body = m_world->CreateBody(&mag1);
        body->CreateFixture(&fixture1);

    }

  };

  struct Slider {
    b2Body* body;
  };
  //! This is the class that sets up the Box2D simulation world
  //! Notice the public inheritance - why do we inherit the base_sim_t class?
  class dominos_t : public base_sim_t
  {

  public:

    vector<Magnet> magnets;
    vector<Ball> mgBalls;
    Slider player;
    
    dominos_t();
    ~dominos_t();
    static base_sim_t* create()
    {
      return new dominos_t;
    }

    virtual void step(settings_t* settings); 
    virtual void keyboard(unsigned char key);
    virtual void mouse_down(const b2Vec2& p);

  };


//////////////////////////////////////////////////////
  //Magnets and ball struct
}


  
#endif
