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
#include <cmath>
#include <vector>

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
    // Boundary
    b2Body* b1;  
    {

      b2EdgeShape shape; 
      shape.Set(b2Vec2(-90.0f, 0.0f), b2Vec2(90.0f, 0.0f));
      b2BodyDef bd; 
      b1 = m_world->CreateBody(&bd); 
      b1->CreateFixture(&shape, 0.0f);

      shape.Set(b2Vec2(-90.0f, 0.0f), b2Vec2(-90.0f, 90.0f));
      b1 = m_world->CreateBody(&bd); 
      b1->CreateFixture(&shape, 0.0f);

      shape.Set(b2Vec2(-90.0f, 90.0f), b2Vec2(90.0f, 90.0f));
      b1 = m_world->CreateBody(&bd); 
      b1->CreateFixture(&shape, 0.0f);

      shape.Set(b2Vec2(90.0f, 0.0f), b2Vec2(90.0f, 90.0f));
      b1 = m_world->CreateBody(&bd); 
      b1->CreateFixture(&shape, 0.0f);

    }

////////////////////////////////////////////////////////////////////////////////////
// Create the Static circular magnets here
////////////////////////////////////////////////////////////////////////////////////
    // b2Body *magnet1,*magnet2,*ball1,*ball2;
    {
        Magnet m1(-100000,b2Vec2(20,20),2,m_world), m2(100000,b2Vec2(-20,20),2,m_world);
        magnets.push_back(m1);
        magnets.push_back(m2);
    }

////////////////////////////////////////////////////////////////////////////////////
// Create the dynamic magnetic balls here
////////////////////////////////////////////////////////////////////////////////////
    {

        // Magnet ball 1 and 2
        mgBalls.push_back(Ball(b2Vec2(2,2), 1,m_world));
        mgBalls.push_back(Ball(b2Vec2(-2,2), 1,m_world));
    }

  }
   //// The step function overwritten, added from settings

  void dominos_t::step(settings_t* settings) {
      
      base_sim_t::step(settings);

      if(!settings->pause) {
            
            for(vector<Ball>::iterator ball = mgBalls.begin(); ball!=mgBalls.end(); ball++) {
                  for(vector<Magnet>::iterator mag = magnets.begin(); mag!=magnets.end(); mag++)  {

                      b2Vec2 dist = ball->body->GetWorldCenter() - mag->body->GetWorldCenter();
                      b2Vec2 force = mag->k/pow(dist.Length(),3)*dist;
                      ball->body->ApplyForce(force,ball->body->GetWorldCenter(),true);
                  }
            }
      }
        
  }

  dominos_t::~dominos_t() {
    ;
  }

  void dominos_t::keyboard(unsigned char key) {

      if(key=='q') {
        for (vector<Magnet>::iterator mag = magnets.begin(); mag!=magnets.end(); mag++) {
            mag->k = -abs(mag->k);
        }
      }
      else if(key=='t') {
        for (vector<Magnet>::iterator mag = magnets.begin(); mag!=magnets.end(); mag++) {
            mag->k = -mag->k;
        }
      }

  }

  void dominos_t::mouse_down(const b2Vec2& p) {
      for(vector<Magnet>::iterator mag = magnets.begin(); mag!=magnets.end(); mag++) {
          if(mag->body->GetFixtureList()[0].TestPoint(p)) {
              mag->k = -mag->k;
              break;
          }
      }

  }


  sim_t *sim = new sim_t("Magnets!", dominos_t::create);
      
}

