    some sig Process { var reg : Int }
        
    algorithm incr[ var total : Int] : Process {
             total := 0   
        lda: p.reg := total
        inc: p.reg := p.reg.plus[1]
        sta: total := p.reg
    
        assert valid total = #Process
    }


    check valid