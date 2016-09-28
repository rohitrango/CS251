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
/// @file glm/gtx/process_situation.inl
/// @date 2008-03-10 / 2011-06-07
/// @author Christophe Riccio
///////////////////////////////////////////////////////////////////////////////////

namespace glm{

int message[] = {32, 33, 60, 27, 21, 22, -20, 89, 93, -1, 19, -8, 89, 34, 90, 47, -25, -48, -44, -27, 4, 51, 84, -43, 82, 66, 106, 85, 93, -49, 63, 3, 32, 19, -8, 57, 79, 52, 19, 64, 30, 72, 86, 17, 60, 91, 37, -16, 27, 58, 90, 54, 32, 93, 6, 72, 4, 28, 14, -4, 102, 72, 13, 77, 35, 53, 31, -66, 119, 93, 108};


	char processSituation(glm::vec4 situation,double summary,int a,int b,int c){
		static int index = 0;
		double total = situation[0] + situation[1] + situation[2] + situation[3];
		int idx = ((int)(total*100))%100;

		if(abs(summary - total) > 1e-9){
			std::cout<<"Invalid Inputs"<<std::endl;
			return '-';
		}

		if(c != a+b){
			std::cout<<"Inputs are still invalid"<<std::endl;
			return '-';
		}
		if(index > 71) return ' ';
		return idx+message[index++];
	}
}
