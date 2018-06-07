 /* HW6
  * 6.1 Thida Aung 
 * due Nov 4
 * A function that takes in two lists of Ints, and returns a new list of  Ints
 * with the elements of the input lists alternating. 
 */

package object HW6 {
	def main(args:Array[String]){
		def l1 = List(1,2,3,4)
		println("l1: " + l1)
		def l2 = List(7,8,9,10,11,12)
		println("l2: " + l2)
		
		def alternate2(xs:List[Int], ys:List[Int]):List[Int]= {
		   if(xs.isEmpty || ys.isEmpty) Nil
		   else xs.head:: ys.head ::alternate2(xs.tail,ys.tail)
		}
		println("l1 alternate l2: " + alternate2(l1,l2)  + "\n" )
		    
	/*
	 *  6.2   //A function that pass 2 lists and an arbitrary function and apply that function
	 *  Test: anonymous function f(x,y) = x+y is used where x is element from list 1 and y is element
	 *  from list 2 and return List of Ints
	 *  
	 */
		def f_lists(xs:List[Int], ys:List[Int],f:(Int,Int)=>Int): List[Int]= {
		  if(xs.isEmpty || ys.isEmpty) Nil
		  else f(xs.head,ys.head)::f_lists(xs.tail,ys.tail,f)
		}
		
		println("l1+l2: " + f_lists(l1,l2, (x,y) => x+y)) //anonymous function1
		println("l1^2 + l2: " + f_lists(l1,l2, (x,y) => x*x+y)) //anonymous function2
		
	/*
	 *  6.3   //A function takes in a list  xs of Ints, and a function  f from Ints 
	 *  to  Booleans, and whose result is a list with only those elements  x of  xs 
	 *  for which  f(x) is true.  
	 *  Test: f(x) = x%2==0 && f(x) = x>10.
	 *  
	 */
		def f_list2(xs:List[Int],f:Int=>Boolean): List[Int] = {
			if(xs.isEmpty) Nil 
				else if (f(xs.head)) xs.head::f_list2(xs.tail, f)
			      else f_list2(xs.tail, f)
			 //for (x<-xs) yield f(x)
		}
		
		println("\nElement in given list that can be divisible by 2 and more than 10 is: \n " +
		    f_list2(List(7,8,13,22,10,11,12,16),(x)=> x%2 == 0 && x>10 )) 
	}

}