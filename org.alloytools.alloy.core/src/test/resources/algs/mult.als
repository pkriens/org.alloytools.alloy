let N = { n : Int | n >= 0 }

algorithm mult[ a, b: N, var r, n :Int ] {
    r := 0
    n := b

    while ( n > 0) {
        r := r.plus[a]
        n := n.minus[1]
    }
    
    assert valid r = a.mul[b]
}


check  { mult.mult implies mult.r = mult.a.mul[mult.b] }