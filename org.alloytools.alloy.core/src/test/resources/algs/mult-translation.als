
enum multEnum { Init, _L100, Done }
one sig mult {
    var pc: multEnum, 
        a : Int,
        b : Int,
    var n : Int,
    var r : Int
} 
pred mult.Init {
    (mult - this).r = (mult - this).r  
    this.pc = Init

    this.r = 0
    this.n = this.b
    this.r' = this.r
    this.n' = this.b
    this.pc' = _L100    
}

pred mult._L100 {
    this.pc = _L100
    this.n > 0 implies {
        this.r' = this.r.plus[this.a]
        this.n' = this.n.minus[1]

        this.pc' = _L100
    } else {
	this.n' = this.n
	this.r' = this.r
        this.pc' = Done
    }
}

pred mult.Done {
    this.pc = Done
    this.pc' = Done
    this.n' = this.n
    this.r' = this.r
}

pred mult.mult {

    this.pc   = Init

    always (this.Init or this._L100 or this.Done)

    eventually this.pc = Done 
}


run {
	mult.a > 1
	mult.b > 1
	mult 
} 
