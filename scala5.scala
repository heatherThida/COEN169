// This is basically scalad5sets.txt
package scala345

object scala5 {
def main(args:Array[String]){
	  
		def f_list(xs:List[Int], f:Int=>Int): List[Int] = {
				if(xs.isEmpty) Nil
				else f(xs.head)::f_list(xs.tail, f)
			
		}
	  
		println(f_list(List(3,5,2), (x) => x*x*x)) //anonymous function
		
	  
		//Define a set by its characteristic function
		//function that takes in an Int, return true if that number is in the set 
		//False if it isn’t
	
		def contains(s:Int => Boolean, elem:Int): Boolean = s(elem)
		//println(contains(set1,7))
	
		
		def singletonSet(elem:Int):Int  => Boolean = {
			def output(e2:Int): Boolean  = {	
				e2 == elem
			}
			output
		}
		
		//other way of writing singletonSet
		def singletonSet2(elem:Int):Int  => Boolean = { 
			
			(x:Int) => x == elem
	
		}
		def set7= singletonSet(7)
	
		println(contains(set7,7))
		println(contains(set7,8))
		println(contains(set7,1))
		println(set7(8))
		
		def negs(x:Int ): Boolean = x< 0  //it returns true if it’s negative, false if it’s positive, can do even  without if 
		
		//Union the 2 sets {set, set} and return set and check if particular no# contains or not in the set 
		def union(s1:Int=>Boolean, s2:Int=>Boolean): Int=>Boolean = {
				def output(e2:Int): Boolean = {
						//s1(e2) || s2(e2)
				  contains(s1,e2) || contains (s2,e2)
				}
				output
		}
		
		
		def union2(s1:Int=>Boolean, s2:Int=>Boolean):Int=>Boolean ={
			(e2:Int)=> contains(s1,e2) || contains(s2,e2)
		}
		
		def neg7= union2(negs,set7)
		contains(neg7,7)
		contains(neg7,-5)
		
		def intersect(s1:Int=>Boolean, s2:Int=>Boolean):Int=>Boolean ={
			(e2:Int)=> contains(s1,e2) && contains(s2,e2)
		}

		// give {A-B}
		// define the set of all positive ints
		// define the set of all negative ints AND 3,6, 12
		// define the set of all positive ints except 3,6,12
		def diff(s1:Int=>Boolean, s2:Int=>Boolean):Int=>Boolean = 
		  (e2:Int)=> contains(s1,e2) && !contains(s2,e2) 
		  //or  (e2:Int)=>s1(e2) && !s2(e2) this would do exact same thing
			
			def pos= (x:Int) => x > 0 // or def pos(x:Int): Boolean = x>0
			def nextra(x:Int): Boolean = contains(negs,x) || x==3 || x==6 || x== 12
			
			def tst= (x:Int) =>  x==3 || x==6 || x== 12 // A set
			def nextra2 = union(negs,tst)  // A test
			
			println(contains(nextra,-129))
			println(contains(nextra2,6))
			println(contains(nextra,129))
			
			def lesspos(x:Int):Boolean = contains(pos,x) && !( x==3 || x==6 || x== 12) // A set
			def lesspos2 = diff(pos,tst)  // A set
			
			println(contains(diff(pos,tst),1)) 
			println("Function 1 " + diff(pos,singletonSet(1))) // A set
			
			def aset = diff(pos, singletonSet(1))//A set
			
		/* Thida's note on pattern matching (didn't use in class yet)
	 	def l1 = List(1,2,3,4)
		println("l1: " + l1)
		def l2 = List(7,8,9,10,11,12)
		println("l2: " + l2)
		
		def alternate[A](l1:List[A], l2: List[A]): List[A] = l1 match{
		  case first :: rest => first :: alternate(l2,rest)
		  case _ => l2
		}
		println("l1 alternate l2: " + alternate(l1,l2) + "\n" ) */
		
	}
}