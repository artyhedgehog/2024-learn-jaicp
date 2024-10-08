require: ./patterns.sc

theme: /
    
    state: Start
        q!: *start
        q!: $hello
        random:
            a: Hello, world!
            a: Greetings!
            a: Who's there?!
        go!: /BookAWorkplace
        
    state: CatchAll || noContext = true
        event!: noMatch
        random:
            a: Forstor ikke, men veldig interessant!
            a: Hva?
            a: Come again?
            
            
    state: BookAWorkplace || modal = true
        q!: * (~book) *
        a: What would you like to book?
        buttons:
            "A workplace" -> Workplace
            "A meeting room" -> MeetingRoom
            "An auditorium" -> Auditorium
        
        state: Workplace
            a: There is a desk with a monitor and an armchair.
            go!: /Pay
            
        state: MeetingRoom
            a: We have a nice hall with a round table!
            go!: /Pay
            
        state: Auditorium
            a: Unfortunately, we don't have any available.

        state: Decline
            q!: no
            a: Oh, well...
            go!: /Bye/ByeBye
            
        state: LocalCatchAll
            event: noMatch
            a: I don't think we have that
            
            
    state: Bye
        
        state: ByeBye
            event: noMatch
            random: 
                a: Bye!
                a: See ya!
                a: Salut!
                
                
    state: Pay || modal = true
        a: How would you like to pay?
        buttons:
            "Bank card"
            "Cash"
            
        state: Any
            q: * (~card | ~cash) *
            a: You can pay in place
            
        state: CatchAllLocal
            q: noMatch
            a: Sorry, this isn't supported.
