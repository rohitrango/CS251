/* 
 * Base code for CS 251 Software Systems Lab 
 * Department of Computer Science and Engineering, IIT Bombay
 * 
 */

//! These are user defined include files
//! Included in double quotes - the path to find these has to be given at compile time
#include "callbacks.hpp"

#ifndef __APPLE__
#include "GL/glui.h"
#else
#include "GL/glui.h"
#endif

//! The namespace protects the global variables and other names from
//! clashes in scope. Read about the use of named and unnamed
//! namespaces in C++ Figure out where all the datatypes used below
//! are defined
namespace cs251
{
  int32 test_index = 0;
  int32 test_selection = 0;
  int32 test_count = 0;
  cs251::sim_t* entry;
  cs251::base_sim_t* test;
  cs251::settings_t settings;
  int32 width = 640;
  int32 height = 480;
  int32 frame_period = 16;
  int32 main_window;
  float settings_hz = 60.0;
  float32 view_zoom = 1.0f;
  int tx, ty, tw, th;
  bool r_mouse_down;
  b2Vec2 lastp;

  b2Vec2 callbacks_t::convert_screen_to_world(int32 x, int32 y)
  {
    float32 u = x / static_cast<float32>(tw);
    float32 v = (th - y) / float32(th);
    
    float32 ratio = static_cast<float32>(tw) / static_cast<float32>(th);
    b2Vec2 extents(ratio * 25.0f, 25.0f);
    extents *= view_zoom;
    
    b2Vec2 lower = settings.view_center - extents;
    b2Vec2 upper = settings.view_center + extents;
  
    b2Vec2 p;
    p.x = (1.0f - u) * lower.x + u * upper.x;
    p.y = (1.0f - v) * lower.y + v * upper.y;
    return p;
  }
  
  
  void callbacks_t::resize_cb(int32 w, int32 h)
  {
    width = w;
    height = h;
    
    GLUI_Master.get_viewport_area(&tx, &ty, &tw, &th);
    glViewport(tx, ty, tw, th);
    
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    
    //! Notice the type casting 
    //! Read about explicit/implicit type casting in C++
    float32 ratio = static_cast<float32>(tw) / static_cast<float32>(th);
    
    b2Vec2 extents(ratio * 25.0f, 25.0f);
    extents *= view_zoom;
    
    b2Vec2 lower = settings.view_center - extents;
    b2Vec2 upper = settings.view_center + extents;
  
    //! L/R/B/T extents of the view frustum
    //! Find where this function is defined
    gluOrtho2D(lower.x, upper.x, lower.y, upper.y);
  }
  
  
  void callbacks_t::keyboard_cb(unsigned char key, int x, int y)
  {
    //! What are these?
    B2_NOT_USED(x);
    B2_NOT_USED(y);
    
    switch (key)
    {
    case 27:
      exit(0);
      break;
      
      //! Press 'z' to zoom out.
    case 'z':
      view_zoom = b2Min(1.1f * view_zoom, 20.0f);
      resize_cb(width, height);
      break;
      
    //! Press 'x' to zoom in.
    case 'x':
      view_zoom = b2Max(0.9f * view_zoom, 0.02f);
      resize_cb(width, height);
      break;
      
    //! Press 'r' to reset.
    case 'r':
      delete test;
      test = entry->create_fcn();
      break;
      
      //! Press 'p' to pause.
    case 'p':
      settings.pause = !settings.pause;
      break;
      
      //! The default case. Why is this needed?
    default:
      if (test)
	{
	  test->keyboard(key);
	}
    }
  }
  
  
  void callbacks_t::keyboard_special_cb(int key, int x, int y)
  {
    B2_NOT_USED(x);
    B2_NOT_USED(y);
    
    switch (key)
    {
    case GLUT_ACTIVE_SHIFT:
      
      //! Press left to pan left.
    case GLUT_KEY_LEFT:
      settings.view_center.x -= 0.5f;
      resize_cb(width, height);
      break;
      
    //! Press right to pan right.
    case GLUT_KEY_RIGHT:
      settings.view_center.x += 0.5f;
      resize_cb(width, height);
      break;
      
    //! Press down to pan down.
    case GLUT_KEY_DOWN:
      settings.view_center.y -= 0.5f;
      resize_cb(width, height);
      break;
      
    //! Press up to pan up.
    case GLUT_KEY_UP:
      settings.view_center.y += 0.5f;
      resize_cb(width, height);
      break;
      
    //! Press home to reset the view.
    case GLUT_KEY_HOME:
      view_zoom = 1.0f;
      settings.view_center.Set(0.0f, 20.0f);
      callbacks_t::resize_cb(width, height);
      break;
    }
  }

  void callbacks_t::keyboard_up_cb(unsigned char key, int x, int y)
  {
    B2_NOT_USED(x);
    B2_NOT_USED(y);
    
    if (test)
      {
	test->keyboard_up(key);
      }
  }
  
  void callbacks_t::mouse_cb(int32 button, int32 state, int32 x, int32 y)
  {
    //! Use the mouse to move things around - figure out how this works?
    if (button == GLUT_LEFT_BUTTON)
      {
	int mod = glutGetModifiers();
	b2Vec2 p = convert_screen_to_world(x, y);
	if (state == GLUT_DOWN)
	  {
	    b2Vec2 p = convert_screen_to_world(x, y);
	    if (mod == GLUT_ACTIVE_SHIFT)
	      {
		test->shift_mouse_down(p);
	      }
	    else
	      {
		test->mouse_down(p);
	      }
	  }
	
	if (state == GLUT_UP)
	  {
	    test->mouse_up(p);
	  }
      }
    else if (button == GLUT_RIGHT_BUTTON)
      {
	if (state == GLUT_DOWN)
	  {	
	    lastp = convert_screen_to_world(x, y);
	    r_mouse_down = true;
	  }
	
	if (state == GLUT_UP)
	  {
	  r_mouse_down = false;
	  }
      }
  }
  
  
  void callbacks_t::mouse_motion_cb(int32 x, int32 y)
  {
    b2Vec2 p = convert_screen_to_world(x, y);
    test->mouse_move(p);
    
    if (r_mouse_down)
      {
	b2Vec2 diff = p - lastp;
	settings.view_center.x -= diff.x;
	settings.view_center.y -= diff.y;
	resize_cb(width, height);
	lastp = convert_screen_to_world(x, y);
      }
  }
  
  void callbacks_t::timer_cb(int)
  {
    glutSetWindow(main_window);
    glutPostRedisplay();
    glutTimerFunc(frame_period, timer_cb, 0);
  }
  
  void callbacks_t::display_cb(void)
  {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    
    test->set_text_line(30);
    b2Vec2 old_center = settings.view_center;
    settings.hz = settings_hz;
    
    test->step(&settings);
    
    if (old_center.x != settings.view_center.x || old_center.y != settings.view_center.y)
      {
	resize_cb(width, height);
      }
    
    test->draw_title(5, 15, entry->name);
    
    glutSwapBuffers();
    
    if (test_selection != test_index)
      {
	test_index = test_selection;
	delete test;
	entry = cs251::sim;
	test = entry->create_fcn();
	view_zoom = 1.0f;
	settings.view_center.Set(0.0f, 20.0f);
      resize_cb(width, height);
      }
  }
  
  
  
  void callbacks_t::restart_cb(int)
  {
    delete test;
    entry = cs251::sim;
    test = entry->create_fcn();
    resize_cb(width, height);
  }
  
  void callbacks_t::pause_cb(int)
  {
    settings.pause = !settings.pause;
  }
  
  void callbacks_t::exit_cb(int code)
  {
    exit(code);
  }
  
  void callbacks_t::single_step_cb(int)
  {
    settings.pause = 1;
    settings.single_step = 1;
  }

};
