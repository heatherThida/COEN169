object Mondayclass {
	def main(args:Array[String]){
	  def a = new Rational2(1, 2)                
	  println(a.numer)
	  println(a.denom)
	  
	  def b = new Rational2(3, 4) 
	  //println(a.plus(b))
	  println(a.+(b))
	   
	  //println(a mult b)
	  println(a * b)//You can leave off the . and () in scala, whether it's * or mult
	  println(1+3)
	  println(1.+(3))
	  
	  def var1 = new Rational2()
	  println(var1)
	  def var2 = new Rational2(6)
	  println(var2)
	}
}


class Rational2(x: Int, y: Int){
	//Monday lecture starts
	def this(z:Int) = this (z,1)
	def this() = this (1,1)
	
	def gcd(a:Int, b:Int): Int= {
	  if (b==0)a else gcd(b,a%b)  
	}
	
	private val div = gcd(x,y)
	val numer = x/div  //once created object, they are immutable in functional programming, it never change. 
	val denom = y/div
	
	//Monday lecture end
	
	//def plus(that:Rational2):Rational2 =
	def +(that:Rational2):Rational2 = 
	  new Rational2(this.numer*that.denom  + that.numer*this.denom,this.denom*that.denom)
	override def toString = numer+"/"+denom
	//def mult(that:Rational2):Rational2 =
	def *(that:Rational2):Rational2 = 
	  new Rational2(this.numer*that.numer, this.denom*that.denom)
	
	def unary_- = new Rational2(-this.numer, this.denom)//Note:  the space after the - is important since there are no ()
	//def sub(that:Rational):Rational = this.plus(that.neg)
	def - (that:Rational2):Rational2 = this.+(-that)
}