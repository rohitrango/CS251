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
#include <cstdlib>
#include <string>
#include <time.h>
#ifdef __APPLE__
	#include <GLUT/glut.h>
#else
	#include "GL/freeglut.h"
#endif

#include <cstring>
using namespace std;

#include "dominos.hpp"

int score=0;
int highscore=0;

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
    srand(time(NULL));
    b2Body* b1;
    {

      b2EdgeShape shape;
      shape.Set(b2Vec2(-67.0f, -4.0f), b2Vec2(67.0f, -4.0f));
      b2BodyDef bd;
      ground = m_world->CreateBody(&bd);
      ground->CreateFixture(&shape, 0.0f);

      shape.Set(b2Vec2(-67.0f, -4.0f), b2Vec2(-67.0f, 120.0f));
      b1 = m_world->CreateBody(&bd);
      b1->CreateFixture(&shape, 0.0f);

      shape.Set(b2Vec2(-67.0f, 120.0f), b2Vec2(67.0f, 120.0f));
      b1 = m_world->CreateBody(&bd);
      b1->CreateFixture(&shape, 0.0f);

      shape.Set(b2Vec2(67.0f, -4.0f), b2Vec2(67.0f, 120.0f));
      b1 = m_world->CreateBody(&bd);
      b1->CreateFixture(&shape, 0.0f);

    }
////////////////////////////////////////////////////////////////////////////////////
// Create the Slider here
////////////////////////////////////////////////////////////////////////////////////
      player = new Slider(m_world);

////////////////////////////////////////////////////////////////////////////////////
// Create the Static circular magnets here
////////////////////////////////////////////////////////////////////////////////////

    	magnets.push_back(Magnet(70000,b2Vec2(-53,30),3,m_world));
      magnets.push_back(Magnet(70000,b2Vec2(53,30),3,m_world));

      magnets.push_back(Magnet(70000,b2Vec2(35,70),3,m_world));
      magnets.push_back(Magnet(70000,b2Vec2(-35,70),3,m_world));

      magnets.push_back(Magnet(70000,b2Vec2(0,50),3,m_world));

////////////////////////////////////////////////////////////////////////////////////
// Create the dynamic magnetic balls here
////////////////////////////////////////////////////////////////////////////////////
    // Magnet ball 1 and 2
		mgBalls.push_back(Ball(b2Vec2(rand()%120-60,100), 1,m_world));


  }


  //// The step function overwritten, added from settings
  void dominos_t::step(settings_t* settings) {

      base_sim_t::step(settings);

      // Check if not paused and then apply forces on all the dynamic balls
      if(!settings->pause) {

            for(vector<Ball>::iterator ball = mgBalls.begin(); ball!=mgBalls.end(); ball++) {
                  for(vector<Magnet>::iterator mag = magnets.begin(); mag!=magnets.end(); mag++)  {

                      b2Vec2 dist = ball->body->GetWorldCenter() - mag->body->GetWorldCenter();
                      b2Vec2 force = mag->k/pow(dist.Length(),3)*dist;
                      ball->body->ApplyForce(force,ball->body->GetWorldCenter(),true);
                  }
            }
      }

      // Check for any collisions of the balls with the player (slider)
      for (b2ContactEdge* edge = player->body->GetContactList(); edge; edge = edge->next) {
          edge->other->SetTransform(b2Vec2(rand()%120-60,100),true);
          edge->other->SetLinearVelocity(b2Vec2(0,0));
          mgBalls.push_back(Ball(b2Vec2(rand()%120-60,100), 1,m_world));
					score++;
					highscore=max(score,highscore);
      }

      // Check if any ball collides with the ground (lower most edge)
      for (b2ContactEdge* edge = ground->GetContactList(); edge; edge = edge->next) {

          // Destroy all the balls
          for(unsigned int i=0;i<mgBalls.size();i++) {
              mgBalls[i].body->GetWorld()->DestroyBody(mgBalls[i].body);
          }

          // Clear the vector
          mgBalls.clear();

          // Push 2 brand new balls
					mgBalls.push_back(Ball(b2Vec2(rand()%120-60,100), 1,m_world));


          // Restore repulsive power of all magnets
          for(unsigned int i=0;i<magnets.size();i++) {
              magnets[i].k = abs(magnets[i].k);
          }

          // Reset the player to center
          player->body->SetTransform(b2Vec2(0,1),player->body->GetAngle());

					score = 0;

          break;
      }
			m_debug_draw.DrawString(210, 20, "SCORE = %d      HIGH SCORE = %d",score,highscore);

  }

  dominos_t::~dominos_t() {
    ;
  }

  void dominos_t::keyboard(unsigned char key) {

      switch(key) {
          case 'q':
              for (vector<Magnet>::iterator mag = magnets.begin(); mag!=magnets.end(); mag++) {
                  mag->k = -abs(mag->k);
              }
              break;

          case 't':
              for (vector<Magnet>::iterator mag = magnets.begin(); mag!=magnets.end(); mag++) {
                  mag->k = -mag->k;
              }
              break;

          case GLUT_KEY_LEFT:
							if(player->body->GetPosition().x>-60)
              player->Move(-3);
              break;

          case GLUT_KEY_RIGHT:
              player->Move(3);
              break;
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
