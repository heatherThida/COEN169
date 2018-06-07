
// This is Professor version of scala3.txt & scala4.txt

package object scala345 {
def main(args:Array[String]){
    
    def square(x:Int) = x*x
	def l1 = List(4, 1, 3, 2) 
	
    def sum_fun(xs:List[Int], f:Int=>Int):Int = {
	  	  if(xs.isEmpty) 0
	  	  else f(xs.head)+sum_fun(xs.tail, f)
	 }
	 println(sum_fun(l1, square))
	  	
	 def l2 = 5::l1//"cons" - base type on left, list on right
	 println(l2)
	 def l3 = 5::4::3::2::Nil
	 println(l3)
	  	
	 def l4 = l2 ::: l3//"append"
	 println(l4)
	  	
	 //Write a function that takes in a list of ints and 
	 //returns a list that consists of the squares of all the ints in the original list
  
	 def square_list(xs:List[Int]):List[Int] = {
	  	  if(xs.isEmpty) Nil
	  	  else square(xs.head) :: square_list(xs.tail)
	  	}
	  println(square_list(l3))
	  	
	def f_list(xs:List[Int], f:Int=>Int):List[Int] = {
	  	  if(xs.isEmpty) Nil
	  	  else f(xs.head) :: f_list(xs.tail, f)
	  	}
	//println(f_list(List(3,5,2), (x) => x*x*x)) //anonymous function
	
	def reverse(xs:List[Int]):List[Int] = {
	  if (xs.isEmpty) Nil
	  else reverse(xs.tail) ::: (xs.head::Nil)//List(xs.head)
	}
	
	def reverse2(xs:List[Int]): List[Int] = {
	  def reverseIter(in:List[Int], out:List[Int]): List[Int] = {
	  if (xs.isEmpty) out
	  else reverseIter(in.tail, in.head::out)
	  }
	  reverseIter(xs, Nil)
	}

	println(reverse(l3))
	
	def l5 = List(List(1, 2, 3), List(5, 2, 4), List(6, 3, 1))
	
	//List 
	def sumList(xs:List[List[Int]]):List[Int] = {
	  def sum(ys:List[Int]):Int = {
	    if(ys.isEmpty) 0
	    else ys.head + sum(ys.tail)
	  } 
	  if(xs.isEmpty) Nil
	  else sum(xs.head) :: sumList(xs.tail)  
	}
	//println(sumList(l1))
		  	
	def f_list2(xs:List[Int], f:Int=>Int):List[Int] = {
	  	  if(xs.isEmpty) Nil
	  	  else f(xs.head) :: f_list2(xs.tail, f)
	  	}
	
	//def cube(x:Int) = x*x*x
	println(f_list2(l3, (x)=>x*x*x))//Anonymous function
	
	
	
	
	
	
	/* Scala 4
	 * contains and sets
	 *                    This is Professor version of scala4.txt
	 */
	
	println("\nBelow is the prints from Scala4\n ")
	
	//Define a set by its characteristic function
	//Function that takes in an int, returns true if that number is in the set
	//false if it isn't
	
	def contains(s:Int=>Boolean, elem:Int):Boolean = s(elem)
	//contains(set1, 7)
	
	def singletonSet(elem:Int):Int=>Boolean = {
	  def output(e2:Int):Boolean = {
	    e2==elem
	  }
	  output 
	}
	
	def singletonSet2(elem:Int):Int=>Boolean = 
	  (x:Int) => x==elem
			
	
	
    def set7 = singletonSet(7)
    println(set7(7))
    println(contains(set7, 7))
    println(set7(8))
    println(contains(set7, 8))
    println(contains(set7, 1))
    
	
	}
}