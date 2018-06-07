package object Scala6classes {
	def main(args:Array[String]){
	  def a = new Rational(1, 2)                
	  println(a.numer)
	  println(a.denom)
	  
	  def b = new Rational(3, 4) 
	  //println(a.plus(b))
	  println(a.+(b))
	   
	  //println(a mult b)
	  println(a * b)//You can leave off the . and () in scala, whether it's * or mult
	  println(1+3)
	  println(1.+(3))
	}
}


class Rational(x: Int, y: Int){
	//Monday lecture
	def this(z:Int) = this (z,1)
	def this() = this (1,1)
	
	//Monday lecture
	def gcd(a:Int, b:Int): Int= {
	  if (b==0)a else gcd(b,a%b)  
	}
	
	private val div = gcd(x,y)
	val numer = x/div  //once created object, they are immutable in functional programming, it never change. 
	val denom = y/div
	
	//def plus(that:Rational):Rational =
	def +(that:Rational):Rational = 
	  new Rational(this.numer*that.denom  + that.numer*this.denom,this.denom*that.denom)
	override def toString = numer+"/"+denom
	
	//def mult(that:Rational):Rational =
	def *(that:Rational):Rational = 
	  new Rational(this.numer*that.numer, this.denom*that.denom)
	
	def unary_- = new Rational(-this.numer, this.denom)//Note:  the space after the - is important since there are no ()
	
	//def sub(that:Rational):Rational = this.plus(that.neg)
	def - (that:Rational):Rational = this.+(-that)
}