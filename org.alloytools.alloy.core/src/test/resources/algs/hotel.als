
enum Action { checkin, checkout, enter }
enum Status { denied, ok, opensfirst }

sig Key {
    next: Key
}
sig Guest {
    var key : lone Key
}

some sig Room {
    keys : disj set Key,
    var current : Key,
} {
    -- ensure the keys are in a ring
    all k : keys | k.^next = keys
    # keys >= 3
}

algorithm hotel[ 
    var count       : Int, 
    var db          : Room  -> Key, 
    var occupation      : Guest -> lone Room,
    var status          : Status,
    var private action  : Action,
    var private room    : Room ] : Guest {
    
    require all r : Room | r.current in r.keys
    occupation := none

    db := current
    count := 5

    while( count > 0) {
        count := count.minus[1]

        if ( action = checkout ) {
            if ( some occupation[p] ) {
                occupation := occupation - p->Room
                status := ok
            } else {
                status := denied
            }
        }
        if ( action = checkin) {
            require no occupation.room
            occupation := occupation ++ p->room
            p.key := db[room]
            db[room] := db[room].next
            status := ok
        }
        if ( action = enter) {
            if ( room.current = p.key ) {
                status := ok
            } else {
                if ( room.current.next = p.key ) {
                    status := opensfirst
                    room.current := room.current.next
                } else {
                    status := denied
                }
            }
        }
    }


}




run { hotel.hotel } for 10 but exactly 3 Room
