package object scala2_iter {
 def main(args:Array[String]){

	  	def sqrt(x:Double):Double = {
	  	  def square (x:Double):Double = x*x
	  	  def abs(x:Double) = if(x>0) x else -x
	  	 
	  	  def sqrtIter(guess:Double):Double = {
	  	    if(goodEnough(guess)) guess//Need to fix types of square and abs
	  	    else sqrtIter(improve(guess))
	  	  }
	  	  
	  	  def goodEnough(guess:Double) = abs(square(guess)-x)<.001
	  	  def improve(guess:Double) = (guess+(x/guess))/2
	  	  sqrtIter(1.0)
	  	}
	  	println("sqrt(9):")
	  	println(sqrt(9))

	  	//Arrays you access by giving an index
	  	//Lists are heads and tails
	  	
	  	def l1 = List(1, 2, 3, 8, 2, 5, 7)
	  	
	  	/*
	  	for(int i=0; i<SIZE; i++){
	  	  if(xs[i]==x)
	  	    return i
	  	}
	  	return -1
	  	* */
	  	/*
	  	 * def find_index(xs: List[Int], x:Int):Int = {//Find the index of x in xs
		  	  if(xs.isEmpty) -1
		  	  else  if(xs.head==x) 0
		  	  		else 1+findIter(xs.tail, x)
	  	}
	  	 */
	  	
	  	
	  	def find_index(xs: List[Int], x:Int):Int = {//Find the index of x in xs
	  	  def findIter(xs:List[Int], x:Int, i:Int):Int = {
		  	  if(xs.isEmpty) -1
		  	  else  if(xs.head==x) i
		  	  		else findIter(xs.tail, x, i+1)
		  	  }
		  	  findIter(xs, x, 0)
	  	}
	  	println(find_index(l1, 8))
	  	
	  	
	  	def sum(n:Int):Int = {
	  	  def sumIter(n:Int, i:Int):Int = {
	  	    if (i==n) i
	  	    else i+ sumIter(n, i+1)
	  	  }
	  	  sumIter(n, 0)
	  	}
	  	
	  	//Add numbers 1 to n
	  	def sum2(n:Int):Int = {
	  	  if(n==0) n
	  	  else n+sum2(n-1)
	  	}
	  	
	  	//Add SQUARES of numbers 1 to n
	  	def square(x:Int) = x*x
	  	def sum3(n:Int):Int = {
	  	  if(n==0) 0
	  	  else square(n)+sum3(n-1)
	  	}
	  	println(sum2(5))
	  	println(sum3(5))
	  	
	  	
	  	def sum4(n:Double, f:Double=>Double):Double = {
	  	  if(n==0) 0
	  	  else f(n)+sum4(n-1, f)
	  	}
	  	
	  	//println(sum4(5, square))
	  	println(sum4(5, sqrt))
	  	
	  	//f(xs[0])+f(xs[1])+...+f(xs[size-1])

	  	def sum5(xs:List[Int], f:Int=>Int):Int = {
	  	  if(xs.isEmpty) 0
	  	  else f(xs.head)+sum5(xs.tail, f)
	  	}
 }
}