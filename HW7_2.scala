// Thida Aung
// HW7 no#2      Nov 9 due 
// CSCI 169 Prof. Linnell
// This is the object oriented approach of no#1 in some way applying class concept to functions below


object HW7_2 {
  
  //main will test functions in class Set 
 
  def main(args:Array[String]){
	  
    def setlist(ls: List[Int]): Int=>Boolean = {
			 def output(elem:Int):Boolean = {
					 def check(ys:List[Int],x:Int): Boolean = {
							 if(ys.isEmpty) false
							 else if(x == ys.head) true
							 else check(ys.tail, elem)
					 }
					 check(ls,elem)
			 }
			 output
	  }
		def l1 =  List(1, 6, 12, -2, 0)
		def l2 = List(43,4,2,6,7,8,20)
		def l3 = List(100,4,5,7,8, -3,0,-22,22)
		
		def set= setlist(l1)
		def set2 = setlist(l2)
		def set3 = setlist(l3) 
		
		println("list1:  " + l1)
	  	println("list2:  " + l2)
	  	println("list3:  " + l3)
	  	
	  	def one= new Set(set)
		
		def a = new Set(set2)
		println("\nDoes list2set contain 20?:  " + a.contain(20))
		println("Does list2set contain 5?:  " + a.contain(5))
		
		def b = new Set(set3)
	  	println("\nDoes list3set contain 98?:  " +b.contain(98))
		
	  	
	  	def test = b \/ a
		println("\nDoes Union of list2set and list3set include this variable?: ")
		println("4? " + test.contain(4))
		println("22? " + test.contain(-11))
		
		def test2= b /\ a
		println("\nDoes Intersect of list2set and list3set include this variable?: ")
		println("7? " + test2.contain(7))
		println("-11? " + test2.contain(-11))
		
		def test3= a - b
		println("\nDoes list2set - list3set has this variable? : " + test3.contain(1))
		println("\nDoes list2set - list3set has this variable? : " + test3.contain(43))
		
		def test4 = a.filter ((x)=> x%2 == 0)
		println("\nDoes this element in list2 is divisible by 2?:  " + test4.contain(20))
		
		println("\nDoes all element in List2set is divisible by 2?:  " + a.forall ((x)=> x%2 == 0))
		println("\nDoes all element in list2set is divisible by 2?:  " + a.forall ((x)=> x%2 == 0))
		
 
		println("\nIs there at least one element exist in set that is divisible by 10?:  " + a.exists ((x)=> x%10 == 0))
		println("\nIs there at least one element exist in set that is divisible by 9?:  " + a.exists ((x)=> x%9 == 0))
		
		def test7 = a map ((x)=> x+6)
		println("\nDoes elements in set satisfy given anonymous function?: " + test7.contain(8))
		println("\nDoes elements in set satisfy given anonymous function?: " + test7.contain(9))
		
  }
}
	  
	  
class Set(f:Int=> Boolean){
	   //true if it's in the set if not false
	   def contain(elem:Int): Boolean = f(elem)
	   
	   //Returns the union of this set and t.   
	   def \/ (t:Set):Set =  new Set((e2:Int)=>(this.contain(e2)|| t.contain(e2)) )
	    
	  //Returns the intersection of this set and t   
	   def /\ (t:Set):Set = new Set((e2:Int)=>(this.contain(e2) &&  t.contain(e2))) 
	    
	   
	   //Returns the difference of this set and t
	   def - (t:Set):Set = new Set((e2:Int)=> (this.contain(e2) && !(t.contain(e2))))
	   
   
	  //Returns a new set that consists of the elements of s that satisfy the predicate.  
	   def filter(p:Int=>Boolean):Set= new Set((e2:Int)=> this.contain(e2) && p(e2))
	   
	    	
	   //Returns true if the predicate is true for all elements of this set, and false otherwise 
	    def forall(p:Int=>Boolean):Boolean= {
			def iter(a:Int):Boolean = {
			      if(a == -1000) true
			      else if(contain(a) && (!p(a))) false
			      else iter(a-1)
			    }
			    iter(1000)
	    }
		     
	    //Returns true if there is at least one item in the set for which the predicate p is true.
	    def exists(p:Int=> Boolean): Boolean= !forall(x => !p(x))
	    
	   
	    //Returns a new set where each element of this set is mapped to the new set 
	    //by the function g. using an anonymous function.   
	    def map(g:Int=>Int): Set =  new Set ((x:Int)=> exists((s:Int)=> g(s) == x ) )
	  
 }
  
