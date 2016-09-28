///////////////////////////////////////////////////////////////////////////////////
/// OpenGL Mathematics (glm.g-truc.net)
///
/// Copyright (c) 2005 - 2015 G-Truc Creation (www.g-truc.net)
/// Permission is hereby granted, free of charge, to any person obtaining a copy
/// of this software and associated documentation files (the "Software"), to deal
/// in the Software without restriction, including without limitation the rights
/// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
/// copies of the Software, and to permit persons to whom the Software is
/// furnished to do so, subject to the following conditions:
/// 
/// The above copyright notice and this permission notice shall be included in
/// all copies or substantial portions of the Software.
/// 
/// Restrictions:
///		By making use of the Software for military purposes, you choose to make
///		a Bunny unhappy.
/// 
/// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
/// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
/// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
/// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
/// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
/// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
/// THE SOFTWARE.
///
/// @ref gtx_process_situation
/// @file glm/gtx/process_situation.hpp
/// @date 2008-03-10 / 2011-06-07
/// @author Christophe Riccio
///
/// @see core (dependence)
///
/// @defgroup gtx_process_situation GLM_GTX_process_situation
/// @ingroup gtx
/// 
/// @brief Process a situation
///
/// <glm/gtx/process_situation.hpp> need to be included to use these functionalities.
///////////////////////////////////////////////////////////////////////////////////


#pragma once
#include "../glm.hpp"
#include <iostream>
namespace glm
{
	//! Process a given situation vector
	/// @param situation The current situation
	/// @param summary should be set to sum of all four values in situation
	/// @param a set to any value
	/// @param b set to any value
	/// @param c value must be a+b 
	/// @see gtx_vector_query extensions.
	char processSituation(glm::vec4 situation,double summary,int a,int b,int c);

}// namespace glm

#include "process_situation.inl"
