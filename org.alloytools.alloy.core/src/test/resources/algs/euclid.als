    let N = { n : Int | n >= 1}
    fun divisors[ a : N ] : set N {
        { r : N | a.rem[r] = 0 }
    }
    
    fun gcd[ a, b : N ] : lone Int {
        (divisors[a] & divisors[b]).max
    }
    
    algorithm euclid[ a, b : N, var u, v : Int ] {
        u := a
        v := b
        
        while( u != 0 ) {
            if ( u < v ) {
                u := v
                v := u
            }
            assign: u := u.minus[v]
        }
    
        assert valid gcd[a,b] = v
    }
    check valid 