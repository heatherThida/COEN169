// HW8 
// Thida Aung CSCI 169 due Friday 
// Inheritance in scala SingletonSet extends Set 
// making forall for one element instead of all set
// rangeSet using start and end to check the elements in that set

object main {
  
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
	  	
	  	def sg1 = new singletonSet(4)
		def sg2 = new singletonSet(5)
		println("Should be false: " + sg1.contain(5))
		println("Should be true: " + sg1.contain(4))
		def sg3 = sg1 \/ sg2
		println("Should be true: " + sg3.contain(4))
		println("Should be true: " + sg3.contain(5))
		println("Should be false: " + sg3.contain(6))
		println("Should be false: " + sg3.forall(x=> x % 4 == 0 ))
		println("Should be true: " + sg3.exists(x=> x % 4 == 0 ))
		
		def rs1 = new rangeSet(2,10)
		def rs2 = new rangeSet(10,20)
		println("Should be true : " + rs1.contain(3))
		println("Should be true: " + rs2.contain(12))
		def rs3 = rs1 \/ rs2
		def rs4 = rs1 /\ rs2
		println("Should be true: " + rs3.contain(2))
		println("Should be true: " + rs3.contain(12))
		println("Should be false: " + rs3.contain(22))
		println("Should be true: " + rs4.contain(10))
		
		println("Should be true: " + rs3.forall(x=> x % 1 == 0 ))
		println("Should be true : " + rs3.exists(x=> x % 13 == 0 ))
	  	
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


//class singletonSet(elem1:Int, elem2:Int) extends Set((x)=> x == elem1 || x == elem2)  {
class singletonSet(elem:Int) extends Set((x)=> x == elem)  {
	override def forall(p:Int=>Boolean):Boolean=  p(elem)
	
	override def exists(p:Int=> Boolean): Boolean= !forall(x => !p(x))
	   
}
//This class creates a set whose elements are all integers between start and end, inclusive.
//Now that we have a set with a finite number of elements, we can do better with 
//forall and exists.  Implement these functions to work for ALL integers,

class rangeSet(start:Int, end:Int) extends Set(x=> x >= start && x <= end ) {
	override def forall(p:Int=>Boolean):Boolean= {
	   def forthis(x:Int):Boolean = {
		   if(x > end) true   
		   else if(this.contain(x))
	          if(p(x)) forthis(x+1)
	          	else false
	           else forthis(x+1)
	    }
	   forthis(start)
	}
	 override def exists(p:Int=> Boolean): Boolean= !forall(x => !p(x))
	 
}











