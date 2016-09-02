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

#include <iostream>
#include "cs251_base.hpp"
#include "render.hpp"


#ifdef __APPLE__
	#include <GLUT/glut.h>
#else
	#include "GL/freeglut.h"
#endif

#include <cstring>
using namespace std;

#include "dominos.hpp"

namespace cs251
{
  /**  The is the constructor 
   * This is the documentation block for the constructor.
   */ 
  
  dominos_t::dominos_t()
  {
    //Ground
    /*! \var b1 
     * \brief pointer to the body ground 
     */ 

    b2Body* b1;  
    {

      b2EdgeShape shape; 
      shape.Set(b2Vec2(-90.0f, 0.0f), b2Vec2(90.0f, 0.0f));
      b2BodyDef bd; 
      b1 = m_world->CreateBody(&bd); 
      b1->CreateFixture(&shape, 0.0f);
    }

////////////////////////////////////////////////////////////////////////////////////
// Create the Static circular magnets here
////////////////////////////////////////////////////////////////////////////////////
    b2Body *magnet1,*magnet2,*ball1,*ball2;
    {

        // Magnet 1

        b2BodyDef mag1;
        mag1.position.Set(10,10);

        b2CircleShape circle1;
        circle1.m_p.Set(10,10);
        circle1.m_radius = 2;

        b2FixtureDef fixture1;
        fixture1.density = 10;
        fixture1.shape = &circle1;

        magnet1 = m_world->CreateBody(&mag1);
        magnet1->CreateFixture(&fixture1);

        // Magnet 2

        b2BodyDef mag2;
        mag2.position.Set(-10,10);

        b2CircleShape circle2;
        circle2.m_p.Set(-10,10);
        circle2.m_radius = 2;

        b2FixtureDef fixture2;
        fixture2.density = 10;
        fixture2.shape = &circle2;

        magnet2 = m_world->CreateBody(&mag2);
        magnet2->CreateFixture(&fixture2);

    }

////////////////////////////////////////////////////////////////////////////////////
// Create the dynamic magnetic balls here
////////////////////////////////////////////////////////////////////////////////////
    {

        // Magnet ball 1

        b2BodyDef mag1;
        mag1.position.Set(5,15);
        mag1.type = b2_dynamicBody;

        b2CircleShape circle1;
        circle1.m_p.Set(5,15);
        circle1.m_radius = 1;

        b2FixtureDef fixture1;
        fixture1.density = 5;
        fixture1.shape = &circle1;
        fixture1.restitution = 0.6;

        ball1 = m_world->CreateBody(&mag1);
        ball1->CreateFixture(&fixture1);

        // Magnet ball 2

        b2BodyDef mag2;
        mag2.position.Set(-5,15);
        mag2.type = b2_dynamicBody;

        b2CircleShape circle2;
        circle2.m_p.Set(-5,15);
        circle2.m_radius = 1;

        b2FixtureDef fixture2;
        fixture2.density = 5;
        fixture2.shape = &circle2;
        fixture2.restitution = 0.6;

        ball2 = m_world->CreateBody(&mag2);
        ball2->CreateFixture(&fixture2);

    }

    ball1->ApplyLinearImpulse( b2Vec2(-100,-50), ball1->GetWorldCenter() , true);

  }

  sim_t *sim = new sim_t("Magnets!", dominos_t::create);
}

