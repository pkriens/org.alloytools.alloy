sig Process {
  var reg : Int,
}
enum incrEnum {sta, Done, lda, inc}
one sig incr {
  var pc : Process -> incrEnum,
  var p : Process,
  var total : Int,
}
pred incr.sta{
     this.p -> sta in this.pc
     this.total' = this.p.reg
     this.pc' = this.pc ++ this.p -> Done
     reg' = reg
}
pred incr.Done{
     this.p -> Done in this.pc
     this.total' = this.total
     this.pc' = this.pc
     reg' = reg
}
pred incr.lda{
     this.p -> lda in this.pc
     this.p.reg' = this.total
     this.pc' = this.pc ++ this.p -> inc
     this.total' = this.total
     (Process - this.p).reg' = (Process - this.p).reg
}
pred incr.inc{
     this.p -> inc in this.pc
     this.p.reg' = this.p.reg.plus[1]
     this.pc' = this.pc ++ this.p -> sta
     this.total' = this.total
     (Process - this.p).reg' = (Process - this.p).reg
}
pred incr.incr{
     this.total = 0
     this.pc = Process -> lda
     always (
         this.sta
        or this.Done
        or this.lda
        or this.inc
     )
     eventually (
       this.pc = Process -> Done
     )
}


    run  {
        incr.incr
        and eventually (
        incr.pc = Process->Done
    and incr.total != #Process 
    )
    } for exactly 2 Process 